package com.vocketlist.android.presenter.ipresenter;

/**
 * Created by kinamare on 2017-02-22.
 */

public interface IVolunteerCategoryPresenter {
	void getVoketList(int page);
	void getVoketDetail(int voketIdx);
	void getVocketCategoryList(String category, int page);
}
