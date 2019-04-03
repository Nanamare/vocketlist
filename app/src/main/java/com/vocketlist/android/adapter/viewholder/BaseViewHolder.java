package com.vocketlist.android.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;


/**
 * 뷰홀더 : 기본
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    protected Context context;
    protected T data;

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
    public void bind(T data){
        this.data = data;
    }
}
