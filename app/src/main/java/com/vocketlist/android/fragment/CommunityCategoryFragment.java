package com.vocketlist.android.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.vocketlist.android.R;
import com.vocketlist.android.adapter.PostAdapter;
import com.vocketlist.android.api.Link;
import com.vocketlist.android.api.community.CommunityServiceManager;
import com.vocketlist.android.api.community.model.CommunityLike;
import com.vocketlist.android.api.community.model.CommunityList;
import com.vocketlist.android.defined.Args;
import com.vocketlist.android.defined.CommunityCategory;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;
import com.vocketlist.android.preference.FacebookPreperence;
import com.vocketlist.android.presenter.IView.ICommunityView;
import com.vocketlist.android.roboguice.log.Ln;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by kinamare on 2017-02-20.
 */

/**
 * 커뮤니티 : 카테고리
 */
public class CommunityCategoryFragment extends RecyclerFragment implements ICommunityView, RecyclerViewItemClickListener {

	private PostAdapter adapter;
	private Link links;
	private int communityListPgCnt = 1;

	private String searchKeyword;
	private int page = 1;


	private  BaseResponse<CommunityList> communityList;
	private BaseResponse<CommunityLike> communityLike;
	private int communityPosition;
	private CommunityCategory category = CommunityCategory.All;

	private ArrayAdapter<String> menuList;
	private int pageCount;

	/**
	 * 인스턴스
	 *
	 * @param catetory
	 * @return
	 */
	public static CommunityCategoryFragment newInstance(CommunityCategory catetory) {
		Bundle args = new Bundle();
		args.putSerializable(Args.CATEGORY, catetory);

		CommunityCategoryFragment fragment = new CommunityCategoryFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if(view == null) {
			return;
		}

		Bundle args = getArguments();
		Serializable c = args.getSerializable(Args.CATEGORY);

		resetSearch();

		if (c != null && c instanceof CommunityCategory) {
			this.category = (CommunityCategory) c;
			recyclerView.setAdapter(adapter = new PostAdapter(new ArrayList<>(),listener));

			requestCommunityList(communityListPgCnt++, null);
		}
	}

	@Override
	public void onStart() {
		super.onStart();


	}

	private void resetSearch() {
		searchKeyword = null;
	}

	private void requestCommunityList(int pageNum, String searchKeyword) {
		String name = (category.getResId() == R.string.com_myWriting) ? FacebookPreperence.getInstance().getUserName() : null;

		if (category.getResId() == R.string.com_wisdom) {
			// todo 명언 보기의 경우 어떻게 api를 호출해서 사용해야 하는지 정리가 필요하다.
			return;
		}

		CommunityServiceManager.search(pageNum, name, searchKeyword)
				.observeOn(AndroidSchedulers.mainThread())
				.doOnTerminate(new Action0() {
					@Override
					public void call() {
						hideRefreshView();
					}
				})
				.subscribe(new Subscriber<Response<BaseResponse<CommunityList>>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Ln.e(e, "getCommunityList : " + e.toString());
					}

					@Override
					public void onNext(Response<BaseResponse<CommunityList>> baseResponseResponse) {
						setCommunityList(baseResponseResponse.body());
					}
				});

	}

	private void hideRefreshView() {
		recyclerView.setRefreshing(false);
		recyclerView.hideMoreProgress();
	}

	private void setCommunityList(BaseResponse<CommunityList> communityList) {
		adapter.clear();
		this.communityList = communityList;
		adapter.addAll(communityList.mResult.mData);
		page = communityList.mResult.mPageCurrentCnt;
		pageCount = communityList.mResult.mPageCnt;
		links = communityList.mResult.mLinks;
		adapter.notifyDataSetChanged();
	}


	@Override
	protected int getLayoutId() {
		return R.layout.fragment_community_category;
	}

	@Override
	protected RecyclerView.LayoutManager getLayoutManager() {
		return new LinearLayoutManager(getContext());
	}

	@Override
	public void onRefresh() {
		super.onRefresh();

		// TODO 리프레시
//		adapter.addAll(new ArrayList<Volunteer>());
		recyclerView.setRefreshing(false);
	}

	@Override
	public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
		super.onMoreAsked(overallItemsCount, itemsBeforeMore, maxLastVisiblePosition);

		if (links == null
				|| links.mNextId == page
				|| links.mNextId >= pageCount) {
			return;
		}

		requestCommunityList(links.mNextId, searchKeyword);
	}

	RecyclerViewItemClickListener listener = new RecyclerViewItemClickListener() {
		@Override
		public void onItemClick(View v, int position) {
			switch (v.getId()){
				case R.id.btnFavorite : {
					communityPosition = position;
					requestFavorite();
					break;
				}
				case R.id.btnMore : {
					setSpinner(v, position);
					break;
				}
				case R.id.btnKakaolink : {
					shareKakaoLink(position);
					break;
				}
				case R.id.btnFacebook : {
					shareToFacebook(position);
					break;
				}
			}
		}
	};

	private void shareKakaoLink(int position) {
		try{
			final KakaoLink kakaoLink = KakaoLink.getKakaoLink(getContext());
			final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

        /*메시지 추가*/
			kakaoBuilder.addText("카카오링크 테스트입니다.");

        /*이미지 가로/세로 사이즈는 80px 보다 커야하며, 이미지 용량은 500kb 이하로 제한된다.*/
			String url = "https://lh3.googleusercontent.com/4FMghyiNYU73ECn5bHOKG0X1Nv_A5J7z2eRjHGIGxtQtK7L-fyNVuqcvyq6C1vIUxgPP=w300-rw";
			kakaoBuilder.addImage(url, 160, 160);

        /*앱 실행버튼 추가*/
			kakaoBuilder.addAppButton("앱 실행 혹은 다운로드");

        /*메시지 발송*/
			kakaoLink.sendMessage(String.valueOf(kakaoBuilder), getContext());

		}catch (Exception e)
		{
			e.printStackTrace();
		}


	}

	private void setSpinner(View btnMore, int position) {

		LayoutInflater layoutInflater
				= LayoutInflater.from(getContext());
		View popupView = layoutInflater.inflate(R.layout.popup_community, null);
		final PopupWindow popupWindow = new PopupWindow(popupView,
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, true);

		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.showAsDropDown(btnMore, 0, 20);

		LinearLayout llDelete = (LinearLayout) popupView.findViewById(R.id.popup_delete);
		llDelete.setOnClickListener(v1 -> {
			android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(getContext())
					.setPositiveButton(android.R.string.ok, (dialog, which) -> {

						//삭제하고 데이터 제거후 업데이트

						deleteCommunityRoom(position);
						adapter.remove(position);

						dialog.dismiss();

						popupWindow.dismiss();

					}).setNegativeButton(android.R.string.cancel, (dialog, which) -> {
						dialog.dismiss();     //닫기
						popupWindow.dismiss();

					}).setMessage(R.string.delete_communityList);

			alert.show();
		});

		LinearLayout llModify = (LinearLayout) popupView.findViewById(R.id.popup_modify);

		llModify.setOnClickListener(v1 -> {

			popupWindow.dismiss();
			Toast.makeText(getContext() ,"수정" , Toast.LENGTH_SHORT)
					.show();
		});

	}

	private void deleteCommunityRoom(int position) {
		CommunityServiceManager.delete(communityList.mResult.mData.get(position).mId)
				.observeOn(AndroidSchedulers.mainThread())
				.doOnTerminate(new Action0() {
					@Override
					public void call() {

					}
				})
				.subscribe(new Subscriber<Response<BaseResponse<Void>>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
						adapter.notifyDataSetChanged();
					}

					@Override
					public void onNext(Response<BaseResponse<Void>> baseResponseResponse) {
						adapter.notifyDataSetChanged();
					}
				});
	}


	private void requestFavorite() {
		CommunityServiceManager.like(communityList.mResult.mData.get(communityPosition).mId)
				.observeOn(AndroidSchedulers.mainThread())
				.doOnTerminate(new Action0() {
					@Override
					public void call() {

					}
				})
				.subscribe(new Subscriber<Response<BaseResponse<CommunityLike>>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
            e.printStackTrace();
					}

					@Override
					public void onNext(Response<BaseResponse<CommunityLike>> baseResponseResponse) {
						communityLike = baseResponseResponse.body();
						refreshFavoriteBtn();
					}
				});
	}

	private void refreshFavoriteBtn() {
		if(communityLike.mResult.mIsLike){
			//좋아요 이미지
			Toast.makeText(getContext(),"좋아요" , Toast.LENGTH_SHORT).show();
		} else {
			//좋아요 취소 이미지
			Toast.makeText(getContext(),"좋아요 취소" , Toast.LENGTH_SHORT).show();
		}
		//좋아요 갯수 업데이트
//		communityLike.mResult.mLikeCnt


	}

	@Override
	public void onItemClick(View v, int position) {

	}

	private void shareToFacebook(int position) {
		String mImageUrl = "";
		if(!TextUtils.isEmpty(communityList.mResult.mData.get(position).mImageUrl)){
			mImageUrl = communityList.mResult.mData.get(position).mImageUrl;
		} else {
			mImageUrl = "https://i.vimeocdn.com/video/620559869_1280.jpg";
		}
		ShareLinkContent content = new ShareLinkContent.Builder()
				//링크의 콘텐츠 제목
				.setContentTitle(communityList.mResult.mData.get(position).mUser.mName+" 님이 작성하신 글입니다.")
				//게시물에 표시될 썸네일 이미지의 URL
				.setImageUrl(Uri.parse(mImageUrl))
				//공유될 링크 (봉사 활동에 대한 내용)
				.setContentUrl(Uri.parse("http://52.78.106.73:8080/kozy/"))
				//게일반적으로 2~4개의 문장으로 구성된 콘텐츠 설명
				.setContentDescription(communityList.mResult.mData.get(position).mContent)
				.build();

		ShareDialog shareDialog = new ShareDialog(CommunityCategoryFragment.this);
		shareDialog.show(content, ShareDialog.Mode.FEED);
	}

//	@Override
//	public void onResume(){
//		super.onResume();
//		communityListPgCnt = 0;
//		requestCommunityList(communityListPgCnt++, null);
//	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		if (TextUtils.isEmpty(query)) {
			Toast.makeText(getContext(), R.string.hint_search, Toast.LENGTH_SHORT).show();
			searchKeyword = null;
			return false;
		}

		searchKeyword = query;
		page = 1;

		requestCommunityList(page, searchKeyword);
		return true;
	}

	/**
	private void refreshFavoriteCount(int page, int postId) {
		CommunityServiceManager.list(page)
				.map(new Func1<Response<BaseResponse<CommunityList>>, Integer>() {
					@Override
					public Integer call(Response<BaseResponse<CommunityList>> baseResponseResponse) {
						return baseResponseResponse.body().mResult.mData.get(postId).mLikeCount;
					}
				})
				.observeOn(AndroidSchedulers.mainThread())
				.doOnTerminate(new Action0() {
					@Override
					public void call() {

					}
				})
				.subscribe(new Subscriber<Integer>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(Integer integer) {
						Ln.i("좋아요 갯수", integer);
					}
				});
	}
	**/
}
