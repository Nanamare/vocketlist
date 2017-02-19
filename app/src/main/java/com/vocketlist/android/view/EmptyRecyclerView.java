package com.vocketlist.android.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;


/**
 * 뷰 : 리사이클 : Empty View 추가
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class EmptyRecyclerView extends RecyclerView {
    private static final String TAG = EmptyRecyclerView.class.getSimpleName();

    private View emptyView;

    private AdapterDataObserver adapterDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            Adapter<?> adapter =  getAdapter();
            if(adapter != null && emptyView != null) {
                if(adapter.getItemCount() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    EmptyRecyclerView.this.setVisibility(View.GONE);
                }
                else {
                    emptyView.setVisibility(View.GONE);
                    EmptyRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }
        }
    };


    /**
     * 생성자
     *
     * @param context
     */
    public EmptyRecyclerView(Context context) {
        super(context);
    }

    /**
     * 생성자
     *
     * @param context
     * @param attrs
     */
    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 생성자
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if(adapter != null) adapter.registerAdapterDataObserver(adapterDataObserver);
        adapterDataObserver.onChanged();
    }

    /**
     *
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }
}