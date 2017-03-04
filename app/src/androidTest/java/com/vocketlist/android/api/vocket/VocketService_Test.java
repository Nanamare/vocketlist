package com.vocketlist.android.api.vocket;

import android.support.test.runner.AndroidJUnit4;

import com.vocketlist.android.api.ServiceManager;

import org.junit.Before;
import org.junit.runner.RunWith;

import rx.Scheduler;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

/**
 * Created by SeungTaek.Lim on 2017. 3. 4..
 */
@RunWith(AndroidJUnit4.class)
public class VocketService_Test {
    @Before
    public void setup() {
        ServiceManager.mockInterceptor.setResponse(null);

        RxJavaPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getComputationScheduler() {
                return Schedulers.immediate();
            }
        });
    }
}
