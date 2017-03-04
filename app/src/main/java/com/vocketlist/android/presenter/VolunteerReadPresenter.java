package com.vocketlist.android.presenter;

import com.vocketlist.android.api.ServiceManager;
import com.vocketlist.android.api.basepresenter.BasePresenter;
import com.vocketlist.android.presenter.IView.IVolunteerReadView;
import com.vocketlist.android.presenter.ipresenter.IVolunteerReadPresenter;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2017-02-26.
 */

public class VolunteerReadPresenter extends BasePresenter implements IVolunteerReadPresenter {

	private ServiceManager serviceManager;
	private IVolunteerReadView view;

	public VolunteerReadPresenter(IVolunteerReadView view) {
		serviceManager = new ServiceManager();
		this.view = view;
	}


	@Override
	public void applyVolunteer(String name, String phone,int service_id) {
		serviceManager.applyVolunteer(name, phone,service_id)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Response<ResponseBody>>() {
					@Override
					public void onCompleted() {
						view.showCompleteDialog();
					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
					}

					@Override
					public void onNext(Response<ResponseBody> responseBodyResponse) {
						onCompleted();
					}
				});
	}
}
