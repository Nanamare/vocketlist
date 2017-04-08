package com.vocketlist.android.api.notice;

import android.support.test.runner.AndroidJUnit4;

import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.roboguice.log.Ln;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import retrofit2.Response;
import rx.Scheduler;
import rx.Subscriber;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by SeungTaek.Lim on 2017. 4. 8..
 */
@RunWith(AndroidJUnit4.class)
public class Notice_InstrumentedTest {
    private BaseResponse<NoticeModel> mNotice;

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

    @Test
    public void 공지사항_리스트_가져오기_Test() {
        String mockData = "{\n" +
                "  \"success\": true,\n" +
                "  \"result\": {\n" +
                "  \t\"data\": [\n" +
                "\t  \t{\n" +
                "\t\t  \t\"id\": 1,\n" +
                "\t\t  \t\"title\": \"첫번째 타이틀\",\n" +
                "\t\t  \t\"content\": \"내용\",\n" +
                "\t\t  \t\"link\": \"http://www.naver.com\",\n" +
                "\t\t  \t\"timestamp\": \"2017.04.01\"\n" +
                "\t  \t},\n" +
                "\n" +
                "\t  \t{\n" +
                "\t\t  \t\"id\": 2,\n" +
                "\t\t  \t\"title\": \"첫번째 타이틀\",\n" +
                "\t\t  \t\"content\": \"내용\",\n" +
                "\t\t  \t\"link\": \"http://www.naver.com\",\n" +
                "\t\t  \t\"timestamp\": \"2017.04.01\"\n" +
                "\t  \t}\n" +
                "  \t]\n" +
                "  },\n" +
                "  \"message\": \"success\"\n" +
                "}";

        ServiceDefine.mockInterceptor.setResponse(mockData);
        NoticeServiceManager.getNotice()
                .subscribe(new Subscriber<Response<BaseResponse<NoticeModel>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Ln.e(e, "onError");
                    }

                    @Override
                    public void onNext(Response<BaseResponse<NoticeModel>> baseResponseResponse) {
                        mNotice = baseResponseResponse.body();
                    }
                });

        assertNotNull(mNotice);
    }
}
