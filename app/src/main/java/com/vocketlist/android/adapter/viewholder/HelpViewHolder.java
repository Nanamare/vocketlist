package com.vocketlist.android.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.vocketlist.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 뷰홀더 : 도움말
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 4. 8.
 */
public class HelpViewHolder extends ChildViewHolder<String> {

    @BindView(R.id.tvContent)
    AppCompatTextView tvContent;

    /**
     * Default constructor.
     *
     * @param itemView The {@link View} being hosted in this ViewHolder
     */
    public HelpViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    /**
     * 데이터 바인딩
     *
     * @param content
     */
    public void bind(String content) {
        // 내용
        if (!TextUtils.isEmpty(content)) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(content);
        } else tvContent.setVisibility(View.GONE);
    }
}
