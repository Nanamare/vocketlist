package com.vocketlist.android.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by kinamare on 2017-02-25.
 */


public class TimePickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	private static TextView txt_time;
	public static TimePickerDialog newInstance(View view){
		txt_time=(TextView)view;
		return(new TimePickerDialog());
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// Use the current time as the default time in the dialog
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		//Create a new instance of TimePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month,dayOfMonth);

	}


	@Override
	public void onDateSet(DatePicker datePicker, int year, int mothOfYear, int dayOfMonth) {
		txt_time.setText(year+"-"+(mothOfYear+1)+"-"+dayOfMonth);
	}

}