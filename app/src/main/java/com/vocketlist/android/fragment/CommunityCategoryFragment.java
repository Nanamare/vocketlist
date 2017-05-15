package com.vocketlist.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.vocketlist.android.R;
import com.vocketlist.android.adapter.PostAdapter;
import com.vocketlist.android.api.Link;
import com.vocketlist.android.api.community.CommunityServiceManager;
import com.vocketlist.android.api.community.model.CommunityLike;
import com.vocketlist.android.api.community.model.CommunityList;
import com.vocketlist.android.api.user.LoginModel;
import com.vocketlist.android.api.user.UserServiceManager;
import com.vocketlist.android.defined.Args;
import com.vocketlist.android.defined.CommunityCategory;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;
import com.vocketlist.android.manager.ToastManager;
import com.vocketlist.android.presenter.IView.ICommunityView;
import com.vocketlist.android.roboguice.log.Ln;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2017-02-20.
 */

/**
 * 커뮤니티 : 카테고리
 */
public class CommunityCategoryFragment extends RecyclerFragment implements
		ICommunityView,
		RecyclerViewItemClickListener
{

	private PostAdapter adapter;
	private Link links;

	private CommunityCategory category = CommunityCategory.All;
	private String searchKeyword;

	private int page = 1;
	private int pageCount = 1;

//	private BaseResponse<CommunityList> communityList;
//	private BaseResponse<CommunityLike> communityLike;
//	private int communityPosition;


//	private ArrayAdapter<String> menuList;


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
		if(view == null) return;

		Bundle args = getArguments();
		Serializable c = args.getSerializable(Args.CATEGORY);

		resetSearch();

		if (c != null && c instanceof CommunityCategory) {
			category = (CommunityCategory) c;
			recyclerView.setAdapter(adapter = new PostAdapter(new ArrayList<>(), this));

			//
			reqList(page, searchKeyword);
		}
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		if (TextUtils.isEmpty(query)) {
			Toast.makeText(getContext(), R.string.hint_search, Toast.LENGTH_SHORT).show();
			searchKeyword = null;
			return false;
		}

		searchKeyword = query;
		page = 1;

		reqList(page, searchKeyword);
		return true;
	}

	@Override
	public void closeSearchView() {
		super.closeSearchView();

		searchKeyword = null;
		page = 1;

		reqList(page, searchKeyword);

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
		reqList(page = 1, searchKeyword);
	}

	@Override
	public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
		super.onMoreAsked(overallItemsCount, itemsBeforeMore, maxLastVisiblePosition);

		if (links == null
				|| links.mNextId == page
				|| links.mNextId >= pageCount) {
			 recyclerView.hideMoreProgress();
			return;
		}

		reqList(links.mNextId, searchKeyword);
	}

	@Override
	public void onItemClick(View v, int position) {
        // 비로그인
        if(!UserServiceManager.isLogin()) {
            ToastManager.show(R.string.login);
            return;
        }

		Object o = v.getTag();

		switch (v.getId()) {
			// 더보기
			case R.id.btnMore: {
				if (o != null && o instanceof CommunityList.CommunityData) {
					CommunityList.CommunityData data = (CommunityList.CommunityData) o;

					int menuId = R.menu.police;

                    // 나
                    if(UserServiceManager.getLoginInfo() != null && UserServiceManager.getLoginInfo().mUserInfo.mUserId  == data.mUser.mId){
                        menuId = R.menu.police_modify_delete;
                    }

					PopupMenu popup = new PopupMenu(v.getContext(), v, GravityCompat.END, R.attr.actionOverflowMenuStyle, 0);
					popup.inflate(menuId);
					popup.setOnMenuItemClickListener(item -> {
						int id = item.getItemId();
						switch (id) {
							case R.id.action_police: doPolice(data, position); break;
							case R.id.action_modify: doModify(data, position); break;
							case R.id.action_delete: doDelete(data, position); break;
						}
						return true;
					});
					popup.show();
				}
			} break;

			// 하트뿅뿅
			case R.id.llLike: {
				if (o != null && o instanceof CommunityList.CommunityData) {
					CommunityList.CommunityData data = (CommunityList.CommunityData) o;
					//
					doLike(data, position);
				}
			} break;

			// 댓글쓰기
			case R.id.llCommentWrite: {

			} break;

			// 댓글보기
			case R.id.llComments: {

			} break;

			// 댓글
			case R.id.tvCommentMore: {


			} break;
		}
	}

	private void resetSearch() {
		searchKeyword = null;
	}

	/**
	 * 신고
	 * @param data
	 */
	private void doPolice(CommunityList.CommunityData data, int position) {
		// TODO 신고처리를 정말로 할지
		ToastManager.show(R.string.toast_police);
	}

	/**
	 * 수정
	 * @param data
	 */
	private void doModify(CommunityList.CommunityData data, int position) {

	}

	/**
	 * 삭제
	 * @param data
	 */
	private void doDelete(CommunityList.CommunityData data, int position) {
		dialogDelete(data, position);
	}

	private void doLike(CommunityList.CommunityData data, int position) {
		reqLike(data, position);
	}

	private void doComment() {

	}

	private void dialogDelete(CommunityList.CommunityData data, int position) {
		new MaterialDialog.Builder(getActivity())
				.content(R.string.delete_communityList)
				.negativeText(R.string.cancel)
				.positiveText(R.string.delete).onPositive((dialog, which) -> {
					reqDelete(data.mId, position);
                })
				.show();
	}

	/**
	 * 요청 : 목록
	 *
	 * @param pageNum
	 * @param searchKeyword
	 */
	private void reqList(int pageNum, String searchKeyword) {
		LoginModel loginModel = UserServiceManager.getLoginInfo();
		String userId = null;

		if (category == CommunityCategory.MyPost && loginModel != null) {
			userId = Integer.toString(loginModel.mUserInfo.mUserId);
		}

		// todo 명언 보기의 경우 어떻게 api를 호출해서 사용해야 하는지 정리가 필요하다.
//		if (category == CommunityCategory.Wisdom) {
//			return;
//		}

		CommunityServiceManager.search(pageNum, userId, searchKeyword)
				.observeOn(AndroidSchedulers.mainThread())
				.doOnTerminate(() -> {
					recyclerView.setRefreshing(false);
					recyclerView.hideMoreProgress();
				})
				.subscribe(new Subscriber<Response<BaseResponse<CommunityList>>>() {
					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
						Ln.e(e, "onError");
					}

					@Override
					public void onNext(Response<BaseResponse<CommunityList>> baseResponseResponse) {
						resList(baseResponseResponse.body());
					}
				});
	}

	/**
	 * 응답 : 목록
	 *
	 * @param response
	 */
	private void resList(BaseResponse<CommunityList> response) {
		if (response != null) {
			page = response.mResult.mPageCurrentCnt;
			pageCount = response.mResult.mPageCnt;
			links = response.mResult.mLinks;

			if(page == 1) adapter.setList(response.mResult.mData);
			else adapter.addAll(response.mResult.mData);
		}
	}

	/**
	 * 요청 : 하트뿅뿅
	 *
	 * @param data
	 * @param position
	 */
	private void reqLike(CommunityList.CommunityData data, int position) {
		CommunityServiceManager.like(data.mId)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new Subscriber<Response<BaseResponse<CommunityLike>>>() {
				@Override
				public void onCompleted() {
				}

				@Override
				public void onError(Throwable e) {
					Ln.e(e, "onError");
				}

				@Override
				public void onNext(Response<BaseResponse<CommunityLike>> baseResponseResponse) {
					resLike(baseResponseResponse.body(), data, position);
				}
			});
	}

	/**
	 * 응답 : 하트뿅뿅
	 *
	 * @param response
	 * @param data
	 * @param position
	 */
	private void resLike(BaseResponse<CommunityLike> response, CommunityList.CommunityData data, int position) {
		if(response != null) {
			boolean isLike = response.mResult.mIsLike;
			data.mIsLike = isLike;
			if(isLike) data.mLikeCount += 1;
			else data.mLikeCount -= 1;

			adapter.change(position, data);
		}
	}

	/**
	 * 요청 : 삭제
	 *
	 * @param id
	 * @param position
	 */
	private void reqDelete(int id, int position) {
		CommunityServiceManager.delete(id)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new Subscriber<Response<BaseResponse<Void>>>() {
				@Override
				public void onCompleted() {
				}

				@Override
				public void onError(Throwable e) {
					Ln.e(e, "onError");
				}

				@Override
				public void onNext(Response<BaseResponse<Void>> baseResponseResponse) {
					resDelete(position);
				}
			});
	}

	/**
	 * 응답 : 삭제
	 *
	 * @param position
	 */
	private void resDelete(int position) {
		adapter.remove(position);
	}

//	RecyclerViewItemClickListener listener = new RecyclerViewItemClickListener() {
//		@Override
//		public void onItemClick(View v, int position) {
//			switch (v.getId()){
//				case R.id.llLike : {
//					communityPosition = position;
//					requestFavorite();
//					break;
//				}
//				case R.id.btnMore : {
//					setSpinner(v, position);
//					break;
//				}
//				case R.id.btnKakaolink : {
//					shareKakaoLink(position);
//					break;
//				}
//				case R.id.btnFacebook : {
//					shareToFacebook(position);
//					break;
//				}
//			}
//		}
//	};
//
//	private void shareKakaoLink(int position) {
//		try{
//			final KakaoLink kakaoLink = KakaoLink.getKakaoLink(getContext());
//			final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
//
//        /*메시지 추가*/
//			kakaoBuilder.addText("카카오링크 테스트입니다.");
//
//        /*이미지 가로/세로 사이즈는 80px 보다 커야하며, 이미지 용량은 500kb 이하로 제한된다.*/
//			String url = "https://lh3.googleusercontent.com/4FMghyiNYU73ECn5bHOKG0X1Nv_A5J7z2eRjHGIGxtQtK7L-fyNVuqcvyq6C1vIUxgPP=w300-rw";
//			kakaoBuilder.addImage(url, 160, 160);
//
//        /*앱 실행버튼 추가*/
//			kakaoBuilder.addAppButton("앱 실행 혹은 다운로드");
//
//        /*메시지 발송*/
//			kakaoLink.sendMessage(String.valueOf(kakaoBuilder), getContext());
//
//		}catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//
//
//	}
//
//	private void setSpinner(View btnMore, int position) {
//
//		LayoutInflater layoutInflater
//				= LayoutInflater.from(getContext());
//		View popupView = layoutInflater.inflate(R.layout.popup_community, null);
//		final PopupWindow popupWindow = new PopupWindow(popupView,
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT, true);
//
//		popupWindow.setBackgroundDrawable(new BitmapDrawable());
//		popupWindow.setOutsideTouchable(true);
//		popupWindow.showAsDropDown(btnMore, 0, 20);
//
//		LinearLayout llDelete = (LinearLayout) popupView.findViewById(R.id.popup_delete);
//		llDelete.setOnClickListener(v1 -> {
//			android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(getContext())
//					.setPositiveButton(android.R.string.ok, (dialog, which) -> {
//
//						//삭제하고 데이터 제거후 업데이트
//
//						deleteCommunityRoom(position);
//						adapter.remove(position);
//
//						dialog.dismiss();
//
//						popupWindow.dismiss();
//
//					}).setNegativeButton(android.R.string.cancel, (dialog, which) -> {
//						dialog.dismiss();     //닫기
//						popupWindow.dismiss();
//
//					}).setMessage(R.string.delete_communityList);
//
//			alert.show();
//		});
//
//		LinearLayout llModify = (LinearLayout) popupView.findViewById(R.id.popup_modify);
//
//		llModify.setOnClickListener(v1 -> {
//
//			popupWindow.dismiss();
//			Toast.makeText(getContext() ,"수정" , Toast.LENGTH_SHORT)
//					.show();
//		});
//
//	}
//
//	private void deleteCommunityRoom(int position) {
//		CommunityServiceManager.delete(communityList.mResult.mData.get(position).mId)
//				.observeOn(AndroidSchedulers.mainThread())
//				.doOnTerminate(new Action0() {
//					@Override
//					public void call() {
//
//					}
//				})
//				.subscribe(new Subscriber<Response<BaseResponse<Void>>>() {
//					@Override
//					public void onCompleted() {
//
//					}
//
//					@Override
//					public void onError(Throwable e) {
//						e.printStackTrace();
//						adapter.notifyDataSetChanged();
//					}
//
//					@Override
//					public void onNext(Response<BaseResponse<Void>> baseResponseResponse) {
//						adapter.notifyDataSetChanged();
//					}
//				});
//	}
//
//
//	private void requestFavorite() {
//		CommunityServiceManager.like(communityList.mResult.mData.get(communityPosition).mId)
//				.observeOn(AndroidSchedulers.mainThread())
//				.doOnTerminate(new Action0() {
//					@Override
//					public void call() {
//
//					}
//				})
//				.subscribe(new Subscriber<Response<BaseResponse<CommunityLike>>>() {
//					@Override
//					public void onCompleted() {
//
//					}
//
//					@Override
//					public void onError(Throwable e) {
//            e.printStackTrace();
//					}
//
//					@Override
//					public void onNext(Response<BaseResponse<CommunityLike>> baseResponseResponse) {
//						communityLike = baseResponseResponse.body();
//						refreshFavoriteBtn();
//					}
//				});
//	}
//
//	private void refreshFavoriteBtn() {
//		if(communityLike.mResult.mIsLike){
//			//좋아요 이미지
//			Toast.makeText(getContext(),"좋아요" , Toast.LENGTH_SHORT).show();
//		} else {
//			//좋아요 취소 이미지
//			Toast.makeText(getContext(),"좋아요 취소" , Toast.LENGTH_SHORT).show();
//		}
//		//좋아요 갯수 업데이트
////		communityLike.mResult.mLikeCnt
//
//
//	}



//	private void shareToFacebook(int position) {
//		String mImageUrl = "";
//		if(!TextUtils.isEmpty(communityList.mResult.mData.get(position).mImageUrl)){
//			mImageUrl = communityList.mResult.mData.get(position).mImageUrl;
//		} else {
//			mImageUrl = "https://i.vimeocdn.com/video/620559869_1280.jpg";
//		}
//		ShareLinkContent content = new ShareLinkContent.Builder()
//				//링크의 콘텐츠 제목
//				.setContentTitle(communityList.mResult.mData.get(position).mUser.mName+" 님이 작성하신 글입니다.")
//				//게시물에 표시될 썸네일 이미지의 URL
//				.setImageUrl(Uri.parse(mImageUrl))
//				//공유될 링크 (봉사 활동에 대한 내용)
//				.setContentUrl(Uri.parse("http://52.78.106.73:8080/kozy/"))
//				//게일반적으로 2~4개의 문장으로 구성된 콘텐츠 설명
//				.setContentDescription(communityList.mResult.mData.get(position).mContent)
//				.build();
//
//		ShareDialog shareDialog = new ShareDialog(CommunityCategoryFragment.this);
//		shareDialog.show(content, ShareDialog.Mode.FEED);
//	}

//	@Override
//	public void onResume(){
//		super.onResume();
//		communityListPgCnt = 0;
//		reqList(communityListPgCnt++, null);
//	}

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
