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

public class DrinkEntryFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static DrinkEntryFragment newInstance(int sectionNumber) {
        DrinkEntryFragment fragment = new DrinkEntryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public DrinkEntryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_drink_entry, container, false);

        // set current time in editTextTime
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat(getResources().getString(R.string.time_format));
        EditText editTextTime = (EditText) view.findViewById(R.id.editTextTime);
        editTextTime.setText(timeFormat.format(calendar.getTime()));

        // set current date in editTextDate
        SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format));
        EditText editTextDate = (EditText) view.findViewById(R.id.drinkEntryEditTextDate);
        editTextDate.setText(dateFormat.format(calendar.getTime()));

        editTextTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    DialogFragment dialogFragment = new TimePickerFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("view", R.id.editTextTime);
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
                    bundle.putInt("view", R.id.drinkEntryEditTextDate);
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getFragmentManager(), "datePicker");
                }
                return false;
            }
        });

        return view;
    }

}
