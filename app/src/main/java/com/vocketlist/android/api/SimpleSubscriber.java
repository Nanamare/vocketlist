package com.vocketlist.android.api;

import com.vocketlist.android.network.error.ExceptionHelper;
import com.vocketlist.android.roboguice.log.Ln;

import rx.Subscriber;

/**
 * Created by SeungTaek.Lim on 2017. 3. 4..
 */

public class SimpleSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Ln.e(e, "onError : " + ExceptionHelper.getFirstErrorMessage(e));
    }

    @Override
    public void onNext(T t) {

    }
}
