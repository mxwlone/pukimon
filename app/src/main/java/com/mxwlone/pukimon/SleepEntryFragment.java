package com.mxwlone.pukimon;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SleepEntryFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static SleepEntryFragment newInstance(int sectionNumber) {
        SleepEntryFragment fragment = new SleepEntryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SleepEntryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sleep_entry, container, false);

        // set current time in editTextFromTime and editTextToTime
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat(getResources().getString(R.string.time_format));
        EditText editTextFromTime = (EditText) view.findViewById(R.id.editTextFromTime);
        EditText editTextToTime = (EditText) view.findViewById(R.id.editTextToTime);
        editTextFromTime.setText(timeFormat.format(calendar.getTime()));
        editTextToTime.setText(timeFormat.format(calendar.getTime()));

        // set current date in editTextDate
        SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format));
        EditText editTextDate = (EditText) view.findViewById(R.id.sleepEntryEditTextDate);
        editTextDate.setText(dateFormat.format(calendar.getTime()));

        editTextFromTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    DialogFragment dialogFragment = new TimePickerFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("view", R.id.editTextFromTime);
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getFragmentManager(), "timePicker");
                }
                return false;
            }
        });

        editTextToTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    DialogFragment dialogFragment = new TimePickerFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("view", R.id.editTextToTime);
                    dialogFragment.setArguments(bundle);
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
                    Bundle bundle = new Bundle();
                    bundle.putInt("view", R.id.sleepEntryEditTextDate);
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getFragmentManager(), "datePicker");
                }
                return false;
            }
        });

        return view;
    }

}
