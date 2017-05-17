package com.vocketlist.android.adapter.viewholder;

import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;

import com.binaryfork.spanny.Spanny;
import com.bumptech.glide.Glide;
import com.vocketlist.android.R;
import com.vocketlist.android.api.comment.model.CommentListModel;
import com.vocketlist.android.api.community.model.User;
import com.vocketlist.android.api.user.UserServiceManager;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;
import com.vocketlist.android.util.DateUtil;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 뷰홀더 : 봉사활동
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class CommentViewHolder extends BaseViewHolder<CommentListModel.Comment> implements View.OnClickListener{

    @BindView(R.id.civPhoto) CircleImageView civPhoto;
    @BindView(R.id.tvNameNContent) AppCompatTextView tvNameNContent;
    @BindView(R.id.tvTime) AppCompatTextView tvTime;
    @BindView(R.id.btnComment) AppCompatTextView btnComment;
    @BindView(R.id.btnEdit) AppCompatTextView btnEdit;
    @BindView(R.id.btnDelete) AppCompatTextView btnDelete;

    private RecyclerViewItemClickListener mListener;

    /**
     * 생성자
     * @param itemView
     */
    public CommentViewHolder(View itemView, RecyclerViewItemClickListener mListener) {
        super(itemView);
        this.mListener = mListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void bind(CommentListModel.Comment data) {
        User user = data.mUserInfo;
        if(user == null) user = data.mUser;

        // 이름 + 내용
        Spanny msg = new Spanny();
        civPhoto.setImageDrawable(null);
        if(user != null) {
            // 작성자 : 프로필 : 사진
            if (!TextUtils.isEmpty(user.mImageUrl)) {
                Glide.with(context)
                        .load(user.mImageUrl)
                        .error(R.drawable.ci_inset)
                        .centerCrop()
                        .into(civPhoto);
            }

            // 이름
            msg.append(user.mName, new StyleSpan(Typeface.BOLD));
        }
        // 이름
        else msg.append(user.mName, new StyleSpan(Typeface.BOLD));

        // 이름 + 내용
        tvNameNContent.setText(msg.append("  ").append(data.mContent));

        // 시간
        tvTime.setText(DateUtil.convertForComment(data.mTimestamp));

        // 답글달기
        btnComment.setTag(data);
        btnComment.setOnClickListener(this);

        // 내 댓글만 수정/삭제 가능
        if(UserServiceManager.getLoginInfo() != null && UserServiceManager.getLoginInfo().mUserInfo.mUserId == user.mId) {
            // 수정
            btnEdit.setVisibility(View.VISIBLE);
            btnEdit.setTag(data);
            btnEdit.setOnClickListener(this);
            // 삭제
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setTag(data);
            btnDelete.setOnClickListener(this);
        }
        else {
            // 수정
            btnEdit.setVisibility(View.GONE);
            // 삭제
            btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if(mListener != null) mListener.onItemClick(view, getAdapterPosition());
    }
}
