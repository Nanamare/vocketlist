package com.vocketlist.android.presenter.IView;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.VolunteerDetail;

/**
 * Created by kinamare on 2017-02-23.
 */

public interface IVolunteerReadView {
	void bindVoketDetailData(BaseResponse<VolunteerDetail> volunteerDetails);
}
