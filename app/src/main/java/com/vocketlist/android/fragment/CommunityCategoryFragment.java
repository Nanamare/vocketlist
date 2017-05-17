package com.vocketlist.android.fragment;

import android.app.Activity;
import android.content.Intent;
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
import com.vocketlist.android.activity.PostCUActivity;
import com.vocketlist.android.activity.PostCommentActivity;
import com.vocketlist.android.adapter.PostAdapter;
import com.vocketlist.android.api.Link;
import com.vocketlist.android.api.community.CommunityServiceManager;
import com.vocketlist.android.api.community.model.CommunityLike;
import com.vocketlist.android.api.community.model.CommunityList;
import com.vocketlist.android.api.user.LoginModel;
import com.vocketlist.android.api.user.UserServiceManager;
import com.vocketlist.android.defined.Args;
import com.vocketlist.android.defined.CommunityCategory;
import com.vocketlist.android.defined.Extras;
import com.vocketlist.android.defined.RequestCode;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;
import com.vocketlist.android.manager.ToastManager;
import com.vocketlist.android.presenter.IView.ICommunityView;
import com.vocketlist.android.roboguice.log.Ln;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindColor;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 커뮤니티 : 카테고리
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 5. 13.
 */
public class CommunityCategoryFragment extends RecyclerFragment implements
		ICommunityView,
		RecyclerViewItemClickListener
{
	@BindColor(R.color.point_E47B75) int pointE47B75;

	private PostAdapter adapter;
	private Link links;

	private CommunityCategory category = CommunityCategory.All;
	private String searchKeyword;

	private int page = 1;
	private int pageTotal = 1;

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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			// 글 수정 후
			case RequestCode.POST_MODIFY:
			// 댓글 보기 or 작성 후
			case RequestCode.POST_COMMENT: {
				if(Activity.RESULT_OK == resultCode) {
					if(data != null && data.hasExtra(Extras.DATA)) {
						Serializable s = data.getExtras().getSerializable(Extras.DATA);
						if(s != null && s instanceof CommunityList.CommunityData) {
							CommunityList.CommunityData post = (CommunityList.CommunityData) s;
							reqPost(post);
						}
					}
				}
			}
			break;

			default: super.onActivityResult(requestCode, resultCode, data); break;
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

		if(page < pageTotal) {
			reqList(++page, searchKeyword);
			return;
		}

		recyclerView.hideMoreProgress();
	}

	@Override
	public void onItemClick(View v, int position) {
        // 비로그인
        if(!UserServiceManager.isLogin()) {
            ToastManager.show(R.string.login);
            return;
        }

        //
		switch (v.getId()) {
			// 더보기
			case R.id.btnMore: {
				Object o = v.getTag();
				if (o != null && o instanceof CommunityList.CommunityData) {
					CommunityList.CommunityData data = (CommunityList.CommunityData) o;
					data.position = position;

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
							case R.id.action_police: doPolice(data); break;
							case R.id.action_modify: doModify(data); break;
							case R.id.action_delete: doDelete(data); break;
						}
						return true;
					});
					popup.show();
				}
			} break;

			// 하트뿅뿅
			case R.id.llLike: {
				Object o = v.getTag();
				if (o != null && o instanceof CommunityList.CommunityData) {
					CommunityList.CommunityData data = (CommunityList.CommunityData) o;
					//
					doLike(data, position);
				}
			} break;

			// 댓글쓰기
			case R.id.llCommentWrite:
			// 댓글보기
			case R.id.llComments:
			// 댓글
			case R.id.tvCommentMore: {
				Object d = v.getTag(R.id.TAG_DATA);
				Object b = v.getTag(R.id.TAG_IS_WRITE);

				if (d != null && d instanceof CommunityList.CommunityData && b != null && b instanceof Boolean) {
					CommunityList.CommunityData data = (CommunityList.CommunityData) d;
					data.position = position;
					boolean isWrite = (Boolean) b;
					doComment(data, isWrite);
				}
			} break;
		}
	}

	private void resetSearch() {
		searchKeyword = null;
	}

	/**
	 * 신고
	 *
	 * @param data
	 */
	private void doPolice(CommunityList.CommunityData data) {
		// TODO 신고처리를 정말로 할지
		ToastManager.show(R.string.toast_police);
	}

	/**
	 * 수정
	 *
	 * @param data
	 */
	private void doModify(CommunityList.CommunityData data) {
		// TODO 포스트 액티비티 호출
		goToPostUpdate(data);
	}

	/**
	 * 삭제
	 *
	 * @param data
	 */
	private void doDelete(CommunityList.CommunityData data) {
		dialogDelete(data);
	}

	/**
	 * 하트뿅뿅
	 *
	 * @param data
	 * @param position
	 */
	private void doLike(CommunityList.CommunityData data, int position) {
		reqLike(data, position);
	}

	/**
	 * 댓글
	 *
	 * @param data
	 * @param isWrite
	 */
	private void doComment(CommunityList.CommunityData data, boolean isWrite) {
		goToComment(data, isWrite);
	}

	/**
	 * 다이얼로그 : 삭제확인
	 *
	 * @param data
	 */
	private void dialogDelete(CommunityList.CommunityData data) {
		new MaterialDialog.Builder(getActivity())
				.content(R.string.delete_communityList)
				.negativeText(R.string.cancel)
				.positiveText(R.string.delete).positiveColor(pointE47B75).onPositive((dialog, which) -> {
					reqDelete(data.mId, data.position);
                })
				.show();
	}

	/**
	 * 글수정
	 */
	private void goToPostUpdate(CommunityList.CommunityData data) {
		Intent intent = new Intent(getActivity(), PostCUActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.putExtra(Extras.DATA, data);
		startActivityForResult(intent, RequestCode.POST_MODIFY);
	}

	/**
	 * 댓글 보기 or 작성
	 *
	 * @param  data
	 * @param isWrite
	 */
	private void goToComment(CommunityList.CommunityData data, boolean isWrite) {
		Intent intent = new Intent(getActivity(), PostCommentActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.putExtra(Extras.DATA, data);
		intent.putExtra(Extras.IS_WRITE, isWrite);
		startActivityForResult(intent, RequestCode.POST_COMMENT);
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
			pageTotal = response.mResult.mPageCnt;

			if(page == 1) {
				adapter.setList(response.mResult.mData);

			} else {
				adapter.addAll(response.mResult.mData);
			}
		}
	}

	/**
	 * 요청 : 상세
	 *
	 * @param data
	 */
	private void reqPost(CommunityList.CommunityData data) {
		CommunityServiceManager.detail(data.mId)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Response<BaseResponse<CommunityList.CommunityData>>>() {
					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
						Ln.e(e, "onError");
					}

					@Override
					public void onNext(Response<BaseResponse<CommunityList.CommunityData>> baseResponseResponse) {
						resPost(baseResponseResponse.body(), data.position);
					}
				});
	}

	/**
	 * 응답 : 상세
	 *
	 * @param response
	 * @param position
	 */
	private void resPost(BaseResponse<CommunityList.CommunityData> response, int position) {
		if(response != null && response.mResult != null) {
			adapter.change(position, response.mResult);
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
}
