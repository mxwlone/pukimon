package com.mxwlone.pukimon;

import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewEntryFragment extends Fragment {

    public NewEntryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_entry, container, false);

        // set current time in editTextTime
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat(getResources().getString(R.string.time_format));
        EditText editTextTime = (EditText) view.findViewById(R.id.editTextTime);
        editTextTime.setText(timeFormat.format(calendar.getTime()));

        // set current date in editTextDate
        SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format));
        EditText editTextDate = (EditText) view.findViewById(R.id.editTextDate);
        editTextDate.setText(dateFormat.format(calendar.getTime()));

        editTextTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    DialogFragment dialogFragment = new TimePickerFragment();
                    dialogFragment.show(getFragmentManager(), "timePicker");
                }
                return false;
            }
        });

        editTextDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    DialogFragment dialogFragment = new DatePickerFragment();
                    dialogFragment.show(getFragmentManager(), "datePicker");
                }
                return false;
            }
        });

        return view;
    }

}
