package com.mxwlone.pukimon.picker;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = null;
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("date"))
                date = new Date(bundle.getLong("date"));
        }

        Calendar c = Calendar.getInstance();
        if (date != null) c.setTime(date);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                android.text.format.DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // set current time in editTextTime
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        Locale locale = getResources().getConfiguration().locale;
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, locale);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.containsKey("view")) {
                EditText editTextTime = (EditText) getActivity().findViewById(bundle.getInt("view"));
                editTextTime.setText(timeFormat.format(calendar.getTime()));
            }
        }
    }
}