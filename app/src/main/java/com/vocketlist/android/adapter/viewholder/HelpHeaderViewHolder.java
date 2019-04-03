package com.vocketlist.android.adapter.viewholder;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.vocketlist.android.R;
import com.vocketlist.android.util.ColorUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 뷰홀더 : 도움말 : 헤더
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 4. 8.
 */
public class HelpHeaderViewHolder extends ParentViewHolder<ListItemHelp, String> {
    @BindView(R.id.tvLabel)
    AppCompatTextView tvLabel;
    @BindView(R.id.ivExpandIcon)
    AppCompatImageView ivExpandIcon;

    /**
     * Default constructor.
     *
     * @param itemView The {@link View} being hosted in this ViewHolder
     */
    public HelpHeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);

        // 확장
        if(expanded){
            animateTextColor(tvLabel, tvLabel.getTextColors().getDefaultColor(), Color.BLACK);
            ivExpandIcon.animate().rotation(0).alpha(.3F);
            ColorUtil.setTint(ivExpandIcon.getDrawable(), Color.parseColor("#212121"));
        }
        // 축소
        else {
            animateTextColor(tvLabel, tvLabel.getTextColors().getDefaultColor(), Color.parseColor("#52C9E9"));
            ivExpandIcon.animate().rotation(180).alpha(1F);
            ColorUtil.setTint(ivExpandIcon.getDrawable(), Color.parseColor("#52C9E9"));
        }
    }

    /**
     * 데이터 바인딩
     *
     * @param help
     */
    public void bind(ListItemHelp help) {
        tvLabel.setText(help.getTitle());
    }

    private void animateTextColor(TextView tv, int fromColor, int toColor) {
        ObjectAnimator colorAnim = ObjectAnimator.ofInt(tv, "textColor", fromColor, toColor);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.start();
    }
}
