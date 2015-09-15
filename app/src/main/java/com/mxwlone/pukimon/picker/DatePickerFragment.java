package com.mxwlone.pukimon.picker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

//TODO when date was changed and user wants to change it again, set the picker to the previously set date

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // set current date in editTextDate
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        Locale locale = getResources().getConfiguration().locale;
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            EditText editTextDate = (EditText) getActivity().findViewById(bundle.getInt("view"));
            editTextDate.setText(dateFormat.format(calendar.getTime()));
        }
    }
}