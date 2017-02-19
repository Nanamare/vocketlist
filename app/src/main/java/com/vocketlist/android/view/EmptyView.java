package com.vocketlist.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.vocketlist.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 뷰 : 빈페이지
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class EmptyView extends LinearLayout {

    //
    @BindView(R.id.ivIcon)
    AppCompatImageView ivIcon;
    @BindView(R.id.tvLabel)
    AppCompatTextView tvLabel;

    //
    String text;
    int icon;

    /**
     * 생성자
     *
     * @param context
     */
    public EmptyView(Context context) {
        this(context, null);
    }

    /**
     * 생성자
     *
     * @param context
     * @param attrs
     */
    public EmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 생성자
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.empty_community, this, true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EmptyView, defStyleAttr, 0);

        icon = a.getResourceId(R.styleable.EmptyView_android_icon, R.drawable.ic_all_inclusive);
        text = a.getString(R.styleable.EmptyView_android_text);

        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        setIcon(icon);
        setText(text);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        tvLabel.setText(text);
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
        ivIcon.setImageResource(icon);
    }
}
