package com.vocketlist.android.presenter.IView;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.api.vocket.Volunteer;

/**
 * Created by kinamare on 2017-02-22.
 */

public interface IVolunteerCategoryView {
	void getVocketList(BaseResponse<Volunteer> volunteerList);
	void destorySwipeView();

}
