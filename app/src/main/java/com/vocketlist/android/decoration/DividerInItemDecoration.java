package com.vocketlist.android.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * 데코레이션 : 리스트 구분선 : 중간만
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 3. 4.
 */
public class DividerInItemDecoration extends DividerItemDecoration {
    /**
     * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a {@link android.support.v7.widget.LinearLayoutManager}.
     *
     *
     * @param context     Current context, it will be used to access resources.
     * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    public DividerInItemDecoration(Context context, int orientation) {
        super(context, orientation);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        if (position == parent.getAdapter().getItemCount() - 1) {
            outRect.setEmpty();
        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }
}
