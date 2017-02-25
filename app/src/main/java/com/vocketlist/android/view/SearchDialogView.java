package com.vocketlist.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.vocketlist.android.R;

import butterknife.ButterKnife;


/**
 * 뷰 : 검색 다이얼로그
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 25.
 */
public class SearchDialogView extends LinearLayout {
    /**
     * 생성자
     * @param context
     */
    public SearchDialogView(Context context) {
        super(context);
    }

    /**
     * 생성자
     * @param context
     * @param attrs
     */
    public SearchDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 생성자
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public SearchDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.dialog_search, this, true);
        ButterKnife.bind(this);
    }
}
