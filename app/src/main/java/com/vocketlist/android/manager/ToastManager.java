package com.vocketlist.android.manager;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatTextView;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.binaryfork.spanny.Spanny;
import com.vocketlist.android.AppApplication;
import com.vocketlist.android.R;

import butterknife.ButterKnife;

/**
 * 관리자 : 토스트
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 15.
 */
public class ToastManager {
    /**
     * 리소스 아이디
     *
     * @param id
     */
    public static void show(@StringRes int id) {
        Toast.makeText(AppApplication.getInstance(), id, Toast.LENGTH_SHORT).show();
    }

    /**
     * 문자열
     *
     * @param text
     */
    public static void show(CharSequence text) {
        Toast.makeText(AppApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 내부 봉사활동 스케줄 추가
     *
     * ex) ToastManager.showAddSchedule(this, "글로벌지식교류 NGO편집 및 디자인 작업(보킷)");
     *
     * @param context
     * @param volunteerName
     */
    public static void showAddSchedule(Context context, CharSequence volunteerName) {
        View v = LayoutInflater.from(context).inflate(R.layout.toast_add_schedule, null);
        AppCompatTextView tvLabel = ButterKnife.findById(v, R.id.tvLabel);

        // 레이블 설정
        tvLabel.setText(new Spanny(volunteerName, new UnderlineSpan())
                            .append("\n")
                            .append(context.getString(R.string.toast_add_schedule))
        );

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP|Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(v);
        toast.show();
    }
}
