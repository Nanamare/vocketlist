package com.vocketlist.android.api.comment;

import android.support.test.runner.AndroidJUnit4;

import com.vocketlist.android.api.ServiceDefine;

import org.junit.Before;
import org.junit.runner.RunWith;

import rx.Scheduler;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

/**
 * Created by SeungTaek.Lim on 2017. 4. 8..
 */
@RunWith(AndroidJUnit4.class)
public class Comment_InstrumentedTest {
    @Before
    public void setup() {
        ServiceDefine.mockInterceptor.setResponse(null);

        RxJavaPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getComputationScheduler() {
                return Schedulers.immediate();
            }
        });
    }
}
