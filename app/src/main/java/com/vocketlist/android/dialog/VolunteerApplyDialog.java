package com.vocketlist.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vocketlist.android.R;
import com.vocketlist.android.api.vocket.Participate;
import com.vocketlist.android.api.vocket.VocketServiceManager;
import com.vocketlist.android.api.vocket.VolunteerDetail;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.network.error.ExceptionHelper;
import com.vocketlist.android.network.service.EmptySubscriber;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by SeungTaek.Lim on 2017. 5. 6..
 */

public class VolunteerApplyDialog extends Dialog {
    @BindView(R.id.dialog_apply_name_layer) protected RelativeLayout mNameLayer;
    @BindView(R.id.dialog_apply_email_layer) protected RelativeLayout mEmailLayer;
    @BindView(R.id.dialog_apply_phone_layer) protected RelativeLayout mPhoneLayer;
    @BindView(R.id.dialog_apply_name_edt) protected EditText mNameView;
    @BindView(R.id.dialog_apply_email_edt) protected EditText mEmailView;
    @BindView(R.id.dialog_apply_phone_edt) protected EditText mPhoneView;
    @BindView(R.id.dialog_apply_date_time) protected TextView mDateTimeView;
    @BindView(R.id.dialog_apply_title) protected TextView mTitleView;

    private final boolean mIsInternal;
    private final VolunteerDetail mVolunteerDetail;
    private VolunteerApplyListener mVolunteerApplyListener = null;

    public VolunteerApplyDialog(@NonNull Context context, boolean isInternal, VolunteerDetail volunteerDetail) {
        super(context);

        mIsInternal = isInternal;
        mVolunteerDetail = volunteerDetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_vocket_apply);

        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        mTitleView.setText(mVolunteerDetail.mTitle);
        mDateTimeView.setText(getContext().getString(R.string.volunteer_apply_dialog_date_time_format,
                                                        mVolunteerDetail.mStartDate,
                                                        mVolunteerDetail.mEndDate,
                                                        mVolunteerDetail.mStartTime,
                                                        mVolunteerDetail.mEndTime));

        if (mIsInternal == false) {
            mNameLayer.setVisibility(View.GONE);
            mEmailLayer.setVisibility(View.GONE);
            mPhoneLayer.setVisibility(View.GONE);
        } else {
            mNameLayer.setVisibility(View.VISIBLE);
            mEmailLayer.setVisibility(View.VISIBLE);
            mPhoneLayer.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.dialog_apply_done_btn)
    protected void onClickApplyDoneBtn(View view) {
        if (isValid(mNameView.getText().toString(),
                mEmailView.getText().toString(),
                mPhoneView.getText().toString()) == false) {
            Toast.makeText(getContext(), R.string.toast_volunteer_apply_dialog_check_data, Toast.LENGTH_SHORT).show();
            return;
        }

        VocketServiceManager.applyVolunteer(mVolunteerDetail.mId,
                                            true,
                                            TextUtils.isEmpty(mNameView.getText()) ? null : mNameView.getText().toString(),
                                            TextUtils.isEmpty(mPhoneView.getText()) ? null : mPhoneView.getText().toString(),
                                            TextUtils.isEmpty(mEmailView.getText()) ? null : mEmailView.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new EmptySubscriber<Response<BaseResponse<Participate>>>() {
                    @Override
                    public void onError(Throwable e) {
                        if (ExceptionHelper.isNetworkError(e)) {
                            Toast.makeText(getContext(), R.string.error_message_network, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    @Override
                    public void onNext(Response<BaseResponse<Participate>> baseResponseResponse) {
                        Toast.makeText(getContext(), R.string.toast_volunteer_apply_dialog_success_message, Toast.LENGTH_SHORT).show();

                        if (mVolunteerApplyListener != null) {
                            mVolunteerApplyListener.onVolunteerApply();
                        }

                        dismiss();
                    }
                });
    }

    private boolean isValid(String name, String email, String phoneNumber) {
        if (mIsInternal == false) {
            return true;
        }

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "이름을 적어주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (name.matches("[^가-힣A-Za-z ]")) {
            Toast.makeText(getApplicationContext(), "이름 형식을 맞춰주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isEmailValid(email)) {
            Toast.makeText(getApplicationContext(), "이메일 형식을 맞춰주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isPhoneValid(phoneNumber)) {
            Toast.makeText(getApplicationContext(), "전화번호 형식을 맞춰주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isPhoneValid(String phoneNumber) {
        boolean isValid = false;

        String expression = "(01[016789])(\\d{3,4})(\\d{4})";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phoneNumber);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;

    }


    private boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    @OnClick(R.id.dialog_apply_cancel_btn)
    protected void onClickApplyCancelBtn(View view) {
        if (mVolunteerApplyListener != null) {
            mVolunteerApplyListener.onCancel();
        }

        cancel();
    }

    public void setListener(VolunteerApplyListener listener) {
        mVolunteerApplyListener = listener;
    }

    public interface VolunteerApplyListener {
        void onVolunteerApply();
        void onCancel();
    }
}
