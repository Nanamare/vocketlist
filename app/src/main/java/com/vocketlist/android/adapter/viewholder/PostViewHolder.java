package com.vocketlist.android.adapter.viewholder;

import android.support.annotation.NonNull;
import android.view.View;

import java.io.Serializable;

/**
 * 뷰홀더 : 커뮤니티 : 글
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class PostViewHolder extends BaseViewHolder {
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
    }
}
