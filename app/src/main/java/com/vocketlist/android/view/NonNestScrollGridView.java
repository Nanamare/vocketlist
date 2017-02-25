package com.vocketlist.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * 뷰 : 그리드 : 스크롤 X
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class NonNestScrollGridView extends GridView {

    private boolean isNonScroll = false;

    /**
     * 생성자
     * @param context
     */
    public NonNestScrollGridView(Context context) {
        super(context);
    }

    /**
     * 생성자
     * @param context
     * @param attrs
     */
    public NonNestScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 생성자
     * @param context
     * @param attrs
     * @param defStyle
     */
    public NonNestScrollGridView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isNonScroll()) {
            int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        }
        else super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     *
     * @return
     */
    public boolean isNonScroll() {
        return isNonScroll;
    }

    /**
     *
     * @param nonScroll
     */
    public void setNonScroll(boolean nonScroll) {
        this.isNonScroll = nonScroll;
    }
}