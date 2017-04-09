package com.vocketlist.android.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vocketlist.android.R;
import com.vocketlist.android.activity.PostCommentActivity;
import com.vocketlist.android.api.community.model.CommunityList;
import com.vocketlist.android.dto.Comment;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;
import com.vocketlist.android.preference.FacebookPreperence;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 뷰홀더 : 커뮤니티 : 글
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class CommunityViewHolder extends BaseViewHolder implements View.OnClickListener {

    @BindView(R.id.civPhoto) CircleImageView civPhoto;
    @BindView(R.id.tvName) AppCompatTextView tvName;
    @BindView(R.id.tvVolunteer) AppCompatTextView tvVolunteer;
    @BindView(R.id.btnMore) AppCompatImageButton btnMore;
    @BindView(R.id.ivPhoto) AppCompatImageView ivPhoto;
    @BindView(R.id.btnFavorite) AppCompatImageButton btnFavorite;
    @BindView(R.id.btnComment) AppCompatImageButton btnComment;
    @BindView(R.id.btnFacebook) AppCompatImageButton btnFacebook;
    @BindView(R.id.btnKakaolink) AppCompatImageButton btnKakaolink;
    @BindView(R.id.ivCountIcon) AppCompatImageView ivCountIcon;
    @BindView(R.id.tvCount) AppCompatTextView tvCount;
		@BindView(R.id.CommentUserNm) AppCompatTextView tvCommentUserNm;
		@BindView(R.id.tvComment) AppCompatTextView tvComment;
		@BindView(R.id.CommentUserNm2) AppCompatTextView tvCommentUserNm2;
		@BindView(R.id.tvComment2) AppCompatTextView tvComment2;
    @BindView(R.id.tvContents) AppCompatTextView tvContents;
    @BindView(R.id.tvCommentMore) AppCompatTextView tvCommentMore;
    @BindView(R.id.tvCreated) AppCompatTextView tvCreated;


		private CommunityList.CommunityData communityData;
    private RecyclerViewItemClickListener mListener;
		private AlertDialog dialog;

    /**
     * 생성자
     * @param itemView
     */
    public CommunityViewHolder(View itemView, RecyclerViewItemClickListener listener) {
        super(itemView);
        mListener = listener;
        itemView.setOnClickListener(this);
    }

	@NonNull
	@Override
	public <T extends Serializable> void bind(T data) {
		if (data instanceof CommunityList.CommunityData) {
			communityData = (CommunityList.CommunityData) data;
			if (!TextUtils.isEmpty(FacebookPreperence.getInstance().getUserImageUrl())) {
				// 작성자 : 프로필 : 사진
				Glide.with(context)
						.load("")
						.centerCrop()
						.placeholder(new ColorDrawable(context.getResources().getColor(R.color.black_7)))
						.crossFade()
						.into(civPhoto);
			}
			tvName.setText(communityData.mUser.mName);
			//tvVolunteer.setText(communityData.);
			if (!TextUtils.isEmpty(communityData.mImageUrl)) {
				Glide.with(context)
						.load(communityData.mImageUrl)
						.into(ivPhoto);
			}

			ivPhoto.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					createCommunityDetailDialog();
				}
			});

			if(communityData.mIsLike){
				//좋아요 상태

			} else {
				//좋아요 취소 상태

			}

			btnFavorite.setOnClickListener(this);

			btnComment.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

				}
			});

			btnFacebook.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

				}
			});

			btnKakaolink.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

				}
			});

			tvCount.setText("좋아요 " + communityData.mLikeCount);

			tvContents.setText(communityData.mContent);

			//댓글 보여주기
			if(communityData.mComment != null) {
				if (communityData.mComment.size() == 1) {
					tvCommentUserNm.setText(communityData.mComment.get(1).mUserInfo.mName);
					tvComment.setText(communityData.mComment.get(1).mContent);
				} else if (communityData.mComment.size() >= 2) {
					tvCommentUserNm.setText(communityData.mComment.get(1).mUserInfo.mName);
					tvCommentUserNm2.setText(communityData.mComment.get(2).mUserInfo.mName);
					tvComment.setText(communityData.mComment.get(1).mContent);
					tvComment2.setText(communityData.mComment.get(2).mContent);
				}
			} else {
				tvComment.setVisibility(View.GONE);
				tvComment2.setVisibility(View.GONE);
				tvCommentUserNm.setVisibility(View.GONE);
				tvCommentUserNm2.setVisibility(View.GONE);
			}

			tvCommentMore.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent goToCommentActivity = new Intent(context, PostCommentActivity.class);
					if(communityData.mComment != null) {
							goToCommentActivity.putExtra("commentList",communityData.mComment);
							goToCommentActivity.putExtra("CommunityRoomId",communityData.mId);
					}
					context.startActivity(goToCommentActivity);
				}
			});

			String createdDate = communityData.mCreateDate;
			DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			try {
				Date time = format.parse(createdDate);
				tvCreated.setText(calculateTime(time));
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
	}

	private void createCommunityDetailDialog() {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View innerView = inflater.inflate(R.layout.dialog_community_detail, null);
		ImageView image = (ImageView) innerView.findViewById(R.id.dialog_iv);
		ImageButton cancleBtn = (ImageButton) innerView.findViewById(R.id.dialog_cancleBtn);
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setView(innerView);
		dialog = alert.create();
		dialog.show();

		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.MATCH_PARENT;
		dialog.getWindow().setAttributes(params);
		FrameLayout.LayoutParams imageParams =
				new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		image.setLayoutParams(imageParams);
		image.setImageResource(R.drawable.dummy_profile);

		cancleBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
	}

	private static class TIME_MAXIMUM {
		public static final int SEC = 60;
		public static final int MIN = 60;
		public static final int HOUR = 24;
		public static final int DAY = 30;
		public static final int MONTH = 12;
	}

	public String calculateTime(Date date) {

		long curTime = System.currentTimeMillis();
		long regTime = date.getTime();
		long diffTime = (curTime - regTime) / 1000;

		String msg = null;

		if (diffTime < TIME_MAXIMUM.SEC) {
			// sec
			msg = diffTime + "초전";
		} else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
			// min
			System.out.println(diffTime);

			msg = diffTime + "분전";
		} else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
			// hour
			msg = (diffTime) + "시간전";
		} else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
			// day
			msg = (diffTime) + "일전";
		} else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
			// day
			msg = (diffTime) + "달전";
		} else {
			msg = (diffTime) + "년전";
		}

		return msg;
	}

	@Override
	public void onClick(View v) {
		if(mListener != null) mListener.onItemClick(v, getAdapterPosition());
	}

}
