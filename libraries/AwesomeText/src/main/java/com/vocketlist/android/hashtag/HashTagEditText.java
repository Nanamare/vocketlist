package com.vocketlist.android.hashtag;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.jmpergar.awesometext.AwesomeTextHandler;
import com.jmpergar.awesometext.R;

public class HashTagEditText extends EditText {
    private static final char KEY_SPACE = 32;

    private ViewGroup bubbleRootView;
    private TextView bubbleTextView;
    private Drawable mBubbleBackground;
    private float mBubbleTextSize;
    private int mBubbleTextColor;
    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    private int mHorizontalPadding;
    private int mVerticalPadding;

    private AwesomeTextHandler mAwesomeTextHandler = new AwesomeTextHandler();

    public HashTagEditText(Context context) {
        super(context);
        if (!isInEditMode()) {
            initAttributeSet(null);
            initView();
        }
    }

    public HashTagEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            initAttributeSet(attrs);
            initView();
        }
    }

    public HashTagEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            initAttributeSet(attrs);
            initView();
        }
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        if (selStart < length()) {
            setSelection(length());
        }
    }

    private void initAttributeSet(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray style = getContext().obtainStyledAttributes(attrs, R.styleable.HashTagEditText, 0, 0);
            mBubbleTextSize = style.getDimension(R.styleable.HashTagEditText_bubbleTextSize, getTextSize());
            mBubbleTextColor = style.getColor(R.styleable.HashTagEditText_bubbleTextColor, getCurrentTextColor());
            mHorizontalSpacing = style.getDimensionPixelSize(R.styleable.HashTagEditText_horizontalSpacing, 0);
            mVerticalSpacing = style.getDimensionPixelSize(R.styleable.HashTagEditText_verticalSpacing, 0);
            mHorizontalPadding = style.getDimensionPixelSize(R.styleable.HashTagEditText_horizontalPadding, 0);
            mVerticalPadding = style.getDimensionPixelSize(R.styleable.HashTagEditText_verticalPadding, 0);
            mBubbleBackground = style.getDrawable(R.styleable.HashTagEditText_bubbleBackground);
            if (mBubbleBackground == null) {
                mBubbleBackground = getContext().getResources().getDrawable(R.drawable.bg_default_bubble);
            }
            style.recycle();
        } else {
            mBubbleTextSize = getTextSize();
            mBubbleTextColor = getCurrentTextColor();
            mHorizontalSpacing = 0;
            mVerticalSpacing = 0;
            mHorizontalPadding = 0;
            mVerticalPadding = 0;
            mBubbleBackground = getContext().getResources().getDrawable(R.drawable.bg_default_bubble);
        }
    }

    private void initView() {
        mAwesomeTextHandler.setView(this);

        bubbleRootView = (ViewGroup) View.inflate(getContext(), R.layout.default_bubble_item, null);
        bubbleRootView.setPadding(mHorizontalSpacing, mVerticalSpacing, mHorizontalSpacing, mVerticalSpacing);

        bubbleTextView = (TextView) bubbleRootView.findViewById(R.id.hashtag_text_item);
        bubbleTextView.setTextColor(mBubbleTextColor);
        bubbleTextView.setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding, mVerticalPadding);
        bubbleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mBubbleTextSize);
        bubbleTextView.setBackgroundDrawable(mBubbleBackground);

        setSingleLine(false);
        setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        addTextChangedListener(new HashTagWatcher());
    }

    public void add(String pattern, AwesomeTextHandler.ViewSpanRenderer spanRenderer) {
        mAwesomeTextHandler.addViewSpanRenderer(pattern, spanRenderer);
    }

    private class HashTagWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public synchronized void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            removeTextChangedListener(this);

            if (charSequence.length() >= 1) {
                if (KEY_SPACE == charSequence.charAt(charSequence.length() - 1)) {
                    mAwesomeTextHandler.renderer(getText(), getText());
                }
            }

            addTextChangedListener(this);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
