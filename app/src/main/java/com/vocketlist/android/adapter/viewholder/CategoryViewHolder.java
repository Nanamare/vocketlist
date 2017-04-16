package com.vocketlist.android.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;

import com.vocketlist.android.R;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;

import butterknife.BindView;

/**
 * 뷰홀더 : 관심분야
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 4. 16.
 */
public class CategoryViewHolder extends BaseViewHolder implements View.OnClickListener {
    private RecyclerViewItemClickListener mListener;

    @BindView(R.id.cbLabel) AppCompatCheckBox cbLabel;

    /**
     * 생성자
     *
     * @param itemView
     */
    public CategoryViewHolder(View itemView, RecyclerViewItemClickListener listener) {
        super(itemView);

        mListener = listener;
        itemView.setOnClickListener(this);
    }

    @NonNull
    public void bind(String data) {
        super.bind(data);
        cbLabel.setText(data);
    }

    @Override
    public void onClick(View v) {
        mListener.onItemClick(v, getAdapterPosition());
    }

    public void setChecked(boolean checked) {
        cbLabel.setChecked(checked);
    }
}
