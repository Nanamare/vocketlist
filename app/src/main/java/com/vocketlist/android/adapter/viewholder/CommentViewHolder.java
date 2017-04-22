package com.vocketlist.android.adapter.viewholder;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.vocketlist.android.R;
import com.vocketlist.android.api.comment.model.CommentListModel;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;

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
    @BindView(R.id.tvName) AppCompatTextView tvName;

    private CommentListModel.Comment commentList;
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
        if(data instanceof CommentListModel.Comment){
            commentList = (CommentListModel.Comment) data;

            // 작성자 : 프로필 : 사진
            Glide.with(context)
                .load(R.drawable.dummy_profile)
                .centerCrop()
                .placeholder(new ColorDrawable(context.getResources().getColor(R.color.black_7)))
                .crossFade()
                .into(civPhoto);
            civPhoto.setOnClickListener(this);

            //이름 + 내용
            tvName.setText(commentList.mUserInfo.mName+" - "+commentList.mContent);
            tvName.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View view) {
        if(mListener != null) mListener.onItemClick(view, getAdapterPosition());
    }
}
