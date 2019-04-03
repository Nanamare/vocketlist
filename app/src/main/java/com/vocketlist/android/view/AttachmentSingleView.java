package com.vocketlist.android.view;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.vocketlist.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 뷰 : 첨부파일 : 단일
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 24.
 */
public class AttachmentSingleView extends RelativeLayout {
    //
    @BindView(R.id.ivThumb) AppCompatImageView ivThumb;

    //
    private String thumb;

    //
    @OnClick(R.id.btnDelete)
    void onDeleteClick() {
        if (mListener != null) mListener.onClick(this);
    }

    private OnClickListener mListener;

    public AttachmentSingleView(Context context) {
        this(context, null);
    }

    public AttachmentSingleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AttachmentSingleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View v = LayoutInflater.from(context).inflate(R.layout.attachment_single, this, true);
        ButterKnife.bind(this, v);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }


    /**
     * 이미지 설정
     *
     * @param thumb
     */
    public void setThumb(String thumb) {
        this.thumb = thumb;
        Glide.with(getContext())
                .load(thumb)
                .centerCrop()
                .crossFade()
                .into(ivThumb);
    }

    /**
     * 이벤트 수신자
     *
     * @param listener
     */
    public void setOnDeleteListener(OnClickListener listener) {
        mListener = listener;
    }
}
