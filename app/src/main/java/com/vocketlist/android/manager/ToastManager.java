package com.vocketlist.android.manager;

import android.support.annotation.StringRes;
import android.widget.Toast;

import com.gonbro.memovie.App;

/**
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 15.
 */
public class ToastManager {
    public static final void show(@StringRes int id) {
        Toast.makeText(App.getInstance(), id, Toast.LENGTH_SHORT).show();
    }
    public static final void show(CharSequence text) {
        Toast.makeText(App.getInstance(), text, Toast.LENGTH_SHORT).show();
    }
}
