package com.vocketlist.android.manager;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * 관리자 : 스낵바
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 15.
 */
public class SnackbarManager {
    public static final void show(@NonNull View v, @StringRes int id) {
        SnackbarManager.show(v, id, Snackbar.LENGTH_SHORT);
    }
    public static final void show(@NonNull View v, CharSequence text) {
        Snackbar.make(v, text, Snackbar.LENGTH_SHORT).show();
    }

    public static final void show(@NonNull View v, @StringRes int id, int duration) {
        Snackbar.make(v, id, duration).show();
    }
}
