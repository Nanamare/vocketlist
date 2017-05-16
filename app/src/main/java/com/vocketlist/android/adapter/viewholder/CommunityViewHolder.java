package com.vocketlist.android.adapter.viewholder;

import android.graphics.Typeface;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;

import com.binaryfork.spanny.Spanny;
import com.bumptech.glide.Glide;
import com.vocketlist.android.R;
import com.vocketlist.android.api.comment.model.CommentListModel;
import com.vocketlist.android.api.community.model.CommunityList;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;

import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 뷰홀더 : 커뮤니티 : 글
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class CommunityViewHolder extends BaseViewHolder<CommunityList.CommunityData> implements View.OnClickListener {

    @BindView(R.id.civPhoto)
    CircleImageView civPhoto;
    @BindView(R.id.tvName)
    AppCompatTextView tvName;
    @BindView(R.id.btnMore)
    AppCompatImageButton btnMore;

    @BindView(R.id.ivPhoto)
    AppCompatImageView ivPhoto;
//	@BindView(R.id.btnFacebook) AppCompatImageButton btnFacebook;
//	@BindView(R.id.btnKakaolink) AppCompatImageButton btnKakaolink;

    @BindView(R.id.tvVolunteer)
    AppCompatTextView tvVolunteer;
    @BindView(R.id.tvContents)
    AppCompatTextView tvContents;

    @BindView(R.id.llLike)
    LinearLayout llLike;
    @BindView(R.id.ivLike)
    AppCompatImageView ivLike;
    @BindView(R.id.tvLikeCount)
    AppCompatTextView tvLikeCount;

    @BindView(R.id.llCommentWrite)
    LinearLayout llCommentWrite;

    @BindView(R.id.tvCommentMore)
    AppCompatTextView tvCommentMore;

    @BindView(R.id.llComments)
    LinearLayout llComments;
    @BindView(R.id.tvComment_1)
    AppCompatTextView tvComment_1;
    @BindView(R.id.tvComment_2)
    AppCompatTextView tvComment_2;

//    @BindView(R.id.tvCreated) AppCompatTextView tvCreated;

    @BindString(R.string.community_volunteer)
    String title;
    @BindString(R.string.vocket_base_url)
    String BASE_URL;
    @BindString(R.string.community_comment_more)
    String commentMore;

    private RecyclerViewItemClickListener mListener;

    /**
     * 생성자
     *
     * @param itemView
     */
    public CommunityViewHolder(View itemView, RecyclerViewItemClickListener listener) {
        super(itemView);
        mListener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void bind(CommunityList.CommunityData data) {
        // 작성자 : 프로필 : 사진
        if (!TextUtils.isEmpty(data.mUser.mImageUrl)) {
            Glide.with(context)
                    .load(data.mUser.mImageUrl)
                    .centerCrop()
                    .crossFade()
                    .into(civPhoto);
        }
        // 작성자 : 프로필 : 이름
        tvName.setText(data.mUser.mName);

        //수정, 삭제 스피너
        btnMore.setTag(data);
        btnMore.setOnClickListener(this);

        // 이미지
        if (!TextUtils.isEmpty(data.mImageUrl)) {
            Glide.with(context)
                    .load(BASE_URL + data.mImageUrl)
                    .centerCrop()
                    .crossFade()
                    .into(ivPhoto);
        }
        // 공유
//		btnFacebook.setOnClickListener(this);
//		btnKakaolink.setOnClickListener(this);

        // 봉사활동
        tvVolunteer.setVisibility(View.GONE);
        if (data.mService != null && data.mService.mTitle != null) {
            tvVolunteer.setVisibility(View.VISIBLE);
            tvVolunteer.setText(String.format(Locale.getDefault(), title, data.mService.mTitle));
        }
        // 내용
        tvContents.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(data.mContent)){
            tvContents.setVisibility(View.VISIBLE);
            tvContents.setText(data.mContent);
        }

        // 좋아요
        ivLike.setSelected(data.mIsLike);
        tvLikeCount.setText(String.valueOf(data.mLikeCount));
        llLike.setTag(data);
        llLike.setOnClickListener(this);

        // 댓글쓰기
        llCommentWrite.setTag(R.id.TAG_DATA, data);
        llCommentWrite.setTag(R.id.TAG_IS_WRITE, true);
        llCommentWrite.setOnClickListener(this);

        // 댓글
        tvCommentMore.setTag(R.id.TAG_DATA, data);
        tvCommentMore.setTag(R.id.TAG_IS_WRITE, true);
        tvCommentMore.setText(R.string.community_comment);
        tvCommentMore.setOnClickListener(this);

        llComments.setTag(R.id.TAG_DATA, data);
        llComments.setTag(R.id.TAG_IS_WRITE, false);
        llComments.setOnClickListener(this);
        tvComment_1.setVisibility(View.GONE);
        tvComment_2.setVisibility(View.GONE);

        // 댓글 : 있음
        if (data.mComment != null && data.mComment.size() > 0) {
            tvCommentMore.setTag(R.id.TAG_IS_WRITE, false);
            int commentSize = data.mComment.size();

            // 1개 이상
            if (commentSize >= 1) {
                tvCommentMore.setText(R.string.community_comment_one);

                CommentListModel.Comment comment = data.mComment.get(0);
                String userName = (comment.mUserInfo != null) ? comment.mUserInfo.mName : "";

                tvComment_1.setText(new Spanny()
                        .append(userName, new StyleSpan(Typeface.BOLD))
                        .append("   ")
                        .append(comment.mContent)
                );

                tvComment_1.setVisibility(View.VISIBLE);
            }

            // 2개 이상
            if (commentSize >= 2) {
                tvCommentMore.setText(String.format(Locale.getDefault(), commentMore, commentSize));

                CommentListModel.Comment comment = data.mComment.get(1);
                String userName = (comment.mUserInfo != null) ? comment.mUserInfo.mName : "";

                tvComment_2.setText(new Spanny()
                        .append(userName, new StyleSpan(Typeface.BOLD))
                        .append("   ")
                        .append(comment.mContent)
                );

                tvComment_2.setVisibility(View.VISIBLE);
            }
        }

//		tvCommentMore.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				goToCommentActivity(view);
//			}
//		});

        // 시간
//		String createdDate = communityData.mCreateDate;
//		DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//		try {
//			Date time = format.parse(createdDate);
//			tvCreated.setText(calculateTime(time));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) mListener.onItemClick(v, getAdapterPosition());
    }

//	@OnClick({R.id.community_list_item_comment_layer, R.id.tvCommentMore})
//	protected void goToCommentActivity(View view) {
//		Intent goToCommentActivity = new Intent(context, PostCommentActivity.class);
//		if(communityData.mComment != null) {
//			goToCommentActivity.putExtra("commentList",communityData.mComment);
//		}
//
//		goToCommentActivity.putExtra("CommunityRoomId",communityData.mId);
//		goToCommentActivity.putExtra("viewId",view.getId());
//
//		context.startActivity(goToCommentActivity);
//	}


//	private void createCommunityDetailDialog() {
//		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View innerView = inflater.inflate(R.layout.dialog_community_detail, null);
//		ImageView image = (ImageView) innerView.findViewById(R.id.dialog_iv);
//		ImageButton cancleBtn = (ImageButton) innerView.findViewById(R.id.dialog_cancleBtn);
//		AlertDialog.Builder alert = new AlertDialog.Builder(context);
//		alert.setView(innerView);
//		dialog = alert.create();
//		dialog.show();
//
//		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//		params.width = WindowManager.LayoutParams.MATCH_PARENT;
//		params.height = WindowManager.LayoutParams.MATCH_PARENT;
//		dialog.getWindow().setAttributes(params);
//		FrameLayout.LayoutParams imageParams =
//				new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//		image.setLayoutParams(imageParams);
//		image.setImageResource(R.drawable.dummy_profile);
//
//		cancleBtn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				dialog.dismiss();
//			}
//		});
//	}

//	private static class TIME_MAXIMUM {
//		public static final int SEC = 60;
//		public static final int MIN = 60;
//		public static final int HOUR = 24;
//		public static final int DAY = 30;
//		public static final int MONTH = 12;
//	}
//
//	public String calculateTime(Date date) {
//
//		long curTime = System.currentTimeMillis();
//		long regTime = date.getTime();
//		long diffTime = (curTime - regTime) / 1000;
//
//		String msg = null;
//
//		if (diffTime < TIME_MAXIMUM.SEC) {
//			// sec
//			msg = diffTime + "초전";
//		} else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
//			// min
//			System.out.println(diffTime);
//
//			msg = diffTime + "분전";
//		} else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
//			// hour
//			msg = (diffTime) + "시간전";
//		} else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
//			// day
//			msg = (diffTime) + "일전";
//		} else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
//			// day
//			msg = (diffTime) + "달전";
//		} else {
//			msg = (diffTime) + "년전";
//		}
//
//		return msg;
//	}
}
