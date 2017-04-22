package com.vocketlist.android.adapter.viewholder;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.vocketlist.android.R;
import com.vocketlist.android.api.my.MyList;
import com.vocketlist.android.api.my.MyListModel;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;

import java.io.Serializable;

import butterknife.BindView;

/**
 * 뷰홀더 : 마이리스트
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 4. 22.
 */
public class MyListViewHolder extends BaseViewHolder<MyListModel.MyList> implements View.OnClickListener {

    @BindView(R.id.cbDone) AppCompatCheckBox cbDone;
    @BindView(R.id.tvContent) AppCompatTextView tvContent;
    @BindView(R.id.ibMore) AppCompatImageButton ibMore;

    private RecyclerViewItemClickListener mListener;

    /**
     * 생성자
     *
     * @param itemView
     */
    public MyListViewHolder(View itemView, RecyclerViewItemClickListener listener) {
        super(itemView);
        itemView.setOnClickListener(this);
        mListener = listener;
    }

    @NonNull
    @Override
    public void bind(MyListModel.MyList data) {
        super.bind(data);

        tvContent.setText(String.valueOf(data.mContent));
        setDone(data.mIsDone);

        cbDone.setOnClickListener(this);
        ibMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(mListener != null) mListener.onItemClick(v, getAdapterPosition());
    }

    /**
     * 완료
     * @param isDone
     */
    public void setDone(boolean isDone) {
        // 완료
        if(isDone) {
            cbDone.setChecked(true);
            tvContent.setPaintFlags(tvContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvContent.setEnabled(true);
        }
        else {
            cbDone.setChecked(false);
            tvContent.setPaintFlags(tvContent.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
            tvContent.setEnabled(false);
        }
    }
}