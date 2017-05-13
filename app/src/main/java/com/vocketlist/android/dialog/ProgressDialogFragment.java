package com.vocketlist.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.vocketlist.android.AppApplication;
import com.vocketlist.android.R;

public class ProgressDialogFragment extends DialogFragment {

	public static final String TAG = ProgressDialogFragment.class.getSimpleName();

	private Dialog dialog = null;

	private boolean touchable;

	private boolean cancelable;

	private OnCancelListener cancelListener;

	/**
	 *
	 * @return
	 */
	public static ProgressDialogFragment newInstance() {
		ProgressDialogFragment dialog = new ProgressDialogFragment();

		return dialog;
	}

	/**
	 *
	 * @param savedInstanceState
	 * @return
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if (touchable) {
			dialog = new Dialog(getActivity(), R.style.TransparentProgressDialog);
			dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		} else {
			dialog = new Dialog(getActivity(), R.style.DimmedProgressDialog);
		}

        ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setIndeterminateDrawable(getActivity().getResources().getDrawable(R.drawable.progress_loading_ani));

//		dialog.addContentView(progressBar, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        dialog.addContentView(progressBar, new LayoutParams(dp2px(32f), dp2px(32f)));
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(false);

		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (cancelListener != null) {
						cancelListener.onCancel(dialog);
					}
					return true;
				}

				return false;
			}
		});

		return dialog;
	}

	/**
	 *
	 * @param touchable
	 */
	public void setTouchable(boolean touchable) {
		this.touchable = touchable;
	}

	/**
	 *
	 * @param cancelable
	 * @see android.support.v4.app.DialogFragment#setCancelable(boolean)
	 */
	@Override
	public void setCancelable(boolean cancelable) {
		super.setCancelable(cancelable);

		this.cancelable = cancelable;
	}

	/**
	 *
	 * @param cancelListener
	 */
	public void setOnCancelListener(OnCancelListener cancelListener) {
		this.cancelListener = cancelListener;
	}

	/**
	 *
	 *
	 * @see android.support.v4.app.DialogFragment#dismiss()
	 */
	@Override
	public void dismiss() {
		super.dismiss();

		if (dialog != null) {
			dialog.dismiss();
		}
	}

	private int dp2px(float dp) {
		Context baseContext = AppApplication.getInstance().getApplicationContext();
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, baseContext.getResources().getDisplayMetrics());
		return px;
	}
}
