package com.vocketlist.android.adapter.viewholder;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.vocketlist.android.R;
import com.vocketlist.android.dto.Post;

import java.io.Serializable;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 뷰홀더 : 커뮤니티 : 글
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class PostViewHolder extends BaseViewHolder {

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
    @BindView(R.id.tvComments) AppCompatTextView tvComments;
    @BindView(R.id.tvCommentMore) AppCompatTextView tvCommentMore;
    @BindView(R.id.tvCreated) AppCompatTextView tvCreated;

    /**
     * 생성자
     * @param itemView
     */
    public PostViewHolder(View itemView) {
        super(itemView);
    }

    @NonNull
    @Override
    public <T extends Serializable> void bind(T data) {
        if (data instanceof Post) {
            Post post = (Post) data;

            // 작성자 : 프로필 : 사진
            Glide.with(context)
                    .load("")
                    .centerCrop()
                    .placeholder(new ColorDrawable(context.getResources().getColor(R.color.black_7)))
                    .crossFade()
                    .into(civPhoto);
        }
    }
}
