package com.vocketlist.android.network.service;

import rx.Subscriber;

/**
 * Created by SeungTaek.Lim on 2017. 1. 5..
 */

public final class EmptySubscriber<Result> extends Subscriber<Result> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Result result) {

    }
}
