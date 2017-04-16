package com.vocketlist.android.adapter.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.Serializable;

import butterknife.ButterKnife;


/**
 * 뷰홀더 : 기본
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    protected Context context;

    /**
     * 생성자
     * @param itemView
     */
    public BaseViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();

        ButterKnife.bind(this, itemView);
    }

    /**
     * 데이터 바인딩
     * @param data
     */
    @NonNull
    public <T extends Serializable> void bind(T data){
        // Do nothing
    }
}
