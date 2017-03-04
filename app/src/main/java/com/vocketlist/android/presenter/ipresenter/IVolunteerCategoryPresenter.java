package com.vocketlist.android.presenter.ipresenter;

import com.vocketlist.android.defined.Category;

/**
 * Created by kinamare on 2017-02-22.
 */

public interface IVolunteerCategoryPresenter {
	void getVocketList(int page);
	void getVocketDetail(int vocketIdx);
	void getVocketCategoryList(Category category, int page);
}
