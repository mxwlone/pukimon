package com.mxwlone.pukimon.picker;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private static int interval = 5;

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
        return new DurationTimePickDialog(getActivity(), this, hour, minute,
                android.text.format.DateFormat.is24HourFormat(getActivity()), interval);
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

    public static int getInterval() {
        return interval;
    }

    private class DurationTimePickDialog extends TimePickerDialog
    {
        final OnTimeSetListener mCallback;
        TimePicker mTimePicker;
        final int increment;

        public DurationTimePickDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView, int increment)
        {
            super(context, callBack, hourOfDay, minute/increment, is24HourView);
            this.mCallback = callBack;
            this.increment = increment;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (mCallback != null && mTimePicker!=null) {
                mTimePicker.clearFocus();
                mCallback.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                        mTimePicker.getCurrentMinute()*increment);
            }
        }

        @Override
        protected void onStop()
        {
            // override and do nothing
        }

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            try
            {
                Class<?> rClass = Class.forName("com.android.internal.R$id");
                Field timePicker = rClass.getField("timePicker");
                this.mTimePicker = (TimePicker)findViewById(timePicker.getInt(null));
                Field m = rClass.getField("minute");

                NumberPicker mMinuteSpinner = (NumberPicker)mTimePicker.findViewById(m.getInt(null));
                mMinuteSpinner.setMinValue(0);
                mMinuteSpinner.setMaxValue((60/increment)-1);
                List<String> displayedValues = new ArrayList<String>();
                for(int i=0;i<60;i+=increment)
                {
                    displayedValues.add(String.format("%02d", i));
                }
                mMinuteSpinner.setDisplayedValues(displayedValues.toArray(new String[0]));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
