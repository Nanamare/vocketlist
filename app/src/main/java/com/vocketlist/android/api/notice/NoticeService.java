package com.vocketlist.android.api.notice;

import com.vocketlist.android.dto.BaseResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by SeungTaek.Lim on 2017. 4. 8..
 */

public interface NoticeService {
    // notice는 임의로 정한 api
    // todo 차후 서버와 협의하여 api명 정한대로 수정 필요
    @GET("notice")
    Observable<Response<BaseResponse<NoticeModel>>> notice();
}
