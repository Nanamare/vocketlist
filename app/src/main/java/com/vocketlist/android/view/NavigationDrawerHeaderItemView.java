package com.vocketlist.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.vocketlist.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 뷰 : 헤데 : 관리
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class NavigationDrawerHeaderItemView extends RelativeLayout {

    @BindView(R.id.tvLabel) AppCompatTextView tvLabel;
    @BindView(R.id.ivNew) AppCompatImageView ivNew;

    private String text;
    private boolean isNew;

    /**
     * 생성자
     * @param context
     */
    public NavigationDrawerHeaderItemView(Context context) {
        this(context, null);
    }

    /**
     * 생성자
     * @param context
     * @param attrs
     */
    public NavigationDrawerHeaderItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 생성자
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public NavigationDrawerHeaderItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.navigation_drawer_header_item, this, true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NavigationDrawerHeaderItemView, defStyleAttr, 0);

        text = a.getString(R.styleable.NavigationDrawerHeaderItemView_android_text);

        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        setText(text);
    }

    /**
     * 레이블
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * 레이블
     * @param text
     */
    public void setText(String text) {
        this.text = text;
        tvLabel.setText(text);
    }

    /**
     * 업데이트 표시
     * @return
     */
    public boolean isNew() {
        return isNew;
    }

    /**
     * 업데이트 표시
     * @param aNew
     */
    public void setNew(boolean aNew) {
        isNew = aNew;
        ivNew.setVisibility(isNew ? VISIBLE : GONE);
    }
}
