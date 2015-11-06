package com.mxwlone.pukimon.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mxwlone.pukimon.R;
import com.mxwlone.pukimon.picker.DatePickerFragment;
import com.mxwlone.pukimon.picker.TimePickerFragment;
import com.mxwlone.pukimon.sql.PukimonContract.SleepEventEntry;
import com.mxwlone.pukimon.sql.PukimonDbHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SleepEventFragment extends Fragment {

    final String TAG = this.getClass().getSimpleName();

    Long mId = 0L;
    EditText mEditTextDate;
    EditText mEditTextFromTime;
    EditText mEditTextToTime;

    Locale LOCALE;
    DateFormat TIME_FORMAT, DATE_FORMAT, DATE_TIME_FORMAT;

    public static SleepEventFragment newInstance() {
        return new SleepEventFragment();
    }

    public SleepEventFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LOCALE = getResources().getConfiguration().locale;
        TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.SHORT, LOCALE);
        DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT, LOCALE);
        DATE_TIME_FORMAT = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT, LOCALE);

        mEditTextDate = (EditText) getActivity().findViewById(R.id.sleepEventEditTextDate);
        mEditTextFromTime = (EditText) getActivity().findViewById(R.id.editTextFromTime);
        mEditTextToTime = (EditText) getActivity().findViewById(R.id.editTextToTime);

        // try to get arguments
        Date fromDate = null, toDate = null;

        Bundle extras = getArguments();
        if (extras != null) {
            if (extras.containsKey("id"))
                mId = extras.getLong("id");
            if (extras.containsKey("fromDate"))
                fromDate = new Date(extras.getLong("fromDate"));
            if (extras.containsKey("toDate"))
                toDate = new Date(extras.getLong("toDate"));
        }

        Calendar calendar = Calendar.getInstance();

        if (fromDate == null || toDate == null) {
            Date date;
            int interval = TimePickerFragment.getInterval();
            int minute = calendar.get(Calendar.MINUTE);

            // round minute to match the interval
            if ((minute % interval) < (interval - minute % interval)) {
                minute = minute - (minute % interval);
            } else {
                minute = minute + (interval - minute % interval);
                calendar.add(Calendar.HOUR_OF_DAY, 1);
            }

            calendar.set(Calendar.MINUTE, minute);
            date = calendar.getTime();

            if (fromDate == null)
                fromDate = date;
            if (toDate == null)
                toDate = date;
        }

        Log.d(TAG, "current values:");
        Log.d(TAG, "id: " + mId);
        Log.d(TAG, "fromDate: " + fromDate.toString());
        Log.d(TAG, "toDate: " + toDate.toString());

        mEditTextDate.setText(DATE_FORMAT.format(toDate));
        mEditTextFromTime.setText(TIME_FORMAT.format(fromDate));
        mEditTextToTime.setText(TIME_FORMAT.format(toDate));

        mEditTextDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    DialogFragment dialogFragment = new DatePickerFragment();
                    Bundle bundle = new Bundle();
                    try {
                        Date currentDate = DATE_FORMAT.parse(mEditTextDate.getText().toString());
                        bundle.putLong("date", currentDate.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    bundle.putInt("view", R.id.sleepEventEditTextDate);
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getActivity().getFragmentManager(), "datePicker");
                }
                return false;
            }
        });

        mEditTextFromTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    DialogFragment dialogFragment = new TimePickerFragment();
                    Bundle bundle = new Bundle();
                    try {
                        Date currentDate = TIME_FORMAT.parse(mEditTextFromTime.getText().toString());
                        bundle.putLong("date", currentDate.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    bundle.putInt("view", R.id.editTextFromTime);
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getActivity().getFragmentManager(), "timePicker");
                }
                return false;
            }
        });

        mEditTextToTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    DialogFragment dialogFragment = new TimePickerFragment();
                    Bundle bundle = new Bundle();
                    try {
                        Date currentDate = TIME_FORMAT.parse(mEditTextToTime.getText().toString());
                        bundle.putLong("date", currentDate.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    bundle.putInt("view", R.id.editTextToTime);
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getActivity().getFragmentManager(), "timePicker");
                }
                return false;
            }
        });

        Button okButton = (Button) getActivity().findViewById(R.id.sleepEventOkButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                okButtonClicked();
            }
        });
    }

    private void okButtonClicked() {
        Activity activity = getActivity();
        String dateString = mEditTextDate.getText().toString();
        String fromTimeString = mEditTextFromTime.getText().toString();
        String toTimeString = mEditTextToTime.getText().toString();

        Log.d(TAG, "new values:");
        Log.d(TAG, "dateString: " + dateString);
        Log.d(TAG, "fromTimeString: " + fromTimeString);
        Log.d(TAG, "toTimeString: " + toTimeString);

        Date fromDate = null, toDate = null;
        try {
            fromDate = DATE_TIME_FORMAT.parse(dateString + " " + fromTimeString);
            toDate = DATE_TIME_FORMAT.parse(dateString + " " + toTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (fromDate == null || toDate == null) {
            Toast.makeText(activity.getApplicationContext(), "Unparseable date", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(SleepEventEntry.COLUMN_NAME_TIMESTAMP_FROM, fromDate.getTime());
        values.put(SleepEventEntry.COLUMN_NAME_TIMESTAMP_TO, toDate.getTime());

        if (mId != 0L) {
            updateDrinkEvent(values);
        } else {
            saveSleepEvent(values);
        }
    }

    private void saveSleepEvent(ContentValues values) {
        PukimonDbHelper dbHelper = new PukimonDbHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newRowId = db.insert(SleepEventEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            Toast.makeText(getActivity().getApplicationContext(), "Database insert failed", Toast.LENGTH_SHORT).show();
            return;
        }

        getActivity().finish();
    }

    private void updateDrinkEvent(ContentValues values) {
        PukimonDbHelper dbHelper = new PukimonDbHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = SleepEventEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(mId) };

        int count = db.update(
                SleepEventEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        if (count > 0)
            Log.d(TAG, String.format("%d entry successfully updated.", count));
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Database update failed", Toast.LENGTH_SHORT).show();
            return;
        }

        getActivity().finish();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sleep_event, container, false);
    }
}
