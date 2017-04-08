package com.vocketlist.android.adapter.viewholder;

import android.support.annotation.NonNull;
import android.view.View;
import android.support.v7.widget.AppCompatTextView;
import com.vocketlist.android.R;

import java.io.Serializable;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 뷰홀더 : 봉사활동
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class CommentViewHolder extends BaseViewHolder {

    @BindView(R.id.civPhoto)
    CircleImageView civPhoto;

    @BindView(R.id.tvName)
    AppCompatTextView tvName;

    /**
     * 생성자
     * @param itemView
     */
    public CommentViewHolder(View itemView) {
        super(itemView);
    }

    @NonNull
    @Override
    public <T extends Serializable> void bind(T data) {
        tvName.setText("test");
    }
}
