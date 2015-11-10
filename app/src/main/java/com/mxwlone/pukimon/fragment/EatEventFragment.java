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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mxwlone.pukimon.R;
import com.mxwlone.pukimon.Util;
import com.mxwlone.pukimon.picker.DatePickerFragment;
import com.mxwlone.pukimon.picker.TimePickerFragment;
import com.mxwlone.pukimon.sql.PukimonContract.EatEventEntry;
import com.mxwlone.pukimon.sql.PukimonDbHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EatEventFragment extends Fragment {

    final String TAG = this.getClass().getSimpleName();

    Long mId = 0L;
    EditText mEditTextDate;
    EditText mEditTextTime;
    EditText mEditTextAmount;

    Locale LOCALE;
    DateFormat TIME_FORMAT, DATE_FORMAT, DATE_TIME_FORMAT;

    public static EatEventFragment newInstance() {
        return new EatEventFragment();
    }

    public EatEventFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LOCALE = getResources().getConfiguration().locale;
        TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.SHORT, LOCALE);
        DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT, LOCALE);
        DATE_TIME_FORMAT = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT, LOCALE);

        mEditTextDate = (EditText) getActivity().findViewById(R.id.eatEventEditTextDate);
        mEditTextTime = (EditText) getActivity().findViewById(R.id.eatEventEditTextTime);
        mEditTextAmount = (EditText) getActivity().findViewById(R.id.eatEventEditTextAmount);

        // try to get arguments
        int amount = 0;
        Date date = null;

        Bundle extras = getArguments();
        if (extras != null) {
            if (extras.containsKey("id"))
                mId = extras.getLong("id");
            if (extras.containsKey("amount"))
                amount = extras.getInt("amount");
            if (extras.containsKey("date"))
                date = new Date(extras.getLong("date"));
        } else {
            // hide date, if not editing an existing event
            ((LinearLayout) getActivity().findViewById(R.id.eatEventLayoutDate)).setVisibility(View.GONE);
        }

        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            date = Util.getIntervalRoundedDate(calendar, TimePickerFragment.getInterval());
        }

        Log.d(TAG, "current values:");
        Log.d(TAG, "id: " + mId);
        Log.d(TAG, "date: " + date.toString());
        Log.d(TAG, "amount: " + amount);

        // fill input fields
        mEditTextDate.setText(DATE_FORMAT.format(date));
        mEditTextTime.setText(TIME_FORMAT.format(date));
        if (amount > 0) {
            mEditTextAmount.setText("" + amount);
        }

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
                    bundle.putInt("view", R.id.eatEventEditTextDate);
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getActivity().getFragmentManager(), "datePicker");
                }
                return false;
            }
        });

        mEditTextTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    DialogFragment dialogFragment = new TimePickerFragment();
                    Bundle bundle = new Bundle();
                    try {
                        Date selectedDate = TIME_FORMAT.parse(mEditTextTime.getText().toString());
                        bundle.putLong("selectedDate", selectedDate.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    bundle.putLong("currentDate", new Date().getTime());
                    bundle.putInt("view", R.id.eatEventEditTextTime);
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getActivity().getFragmentManager(), "timePicker");
                }
                return false;
            }
        });

        Button okButton = (Button) getActivity().findViewById(R.id.eatEventOkButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                okButtonClicked();
            }
        });

        Button add50Button = (Button) getActivity().findViewById(R.id.eatEventAdd50Button);
        add50Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount;
                if (!Util.isNumeric(mEditTextAmount.getText().toString()))
                    amount = 0;
                else
                    amount = Integer.parseInt(mEditTextAmount.getText().toString());

                mEditTextAmount.setText(String.valueOf(amount + 50));
            }
        });

        Button add100Button = (Button) getActivity().findViewById(R.id.eatEventAdd100Button);
        add100Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount;
                if (!Util.isNumeric(mEditTextAmount.getText().toString()))
                    amount = 0;
                else
                    amount = Integer.parseInt(mEditTextAmount.getText().toString());

                mEditTextAmount.setText(String.valueOf(amount + 100));
            }
        });
}

    private void okButtonClicked() {
        Activity activity = getActivity();
        String dateString = mEditTextDate.getText().toString();
        String timeString = mEditTextTime.getText().toString();
        String amountString = mEditTextAmount.getText().toString();

        Log.d(TAG, "new values:");
        Log.d(TAG, "dateString: " + dateString);
        Log.d(TAG, "timeString: " + timeString);
        Log.d(TAG, "amountString: " + amountString);

        Date date = null;
        try {
            date = DATE_TIME_FORMAT.parse(dateString + " " + timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) {
            Toast.makeText(activity.getApplicationContext(), "Unparseable date", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(EatEventEntry.COLUMN_NAME_TIMESTAMP, date.getTime());
        values.put(EatEventEntry.COLUMN_NAME_AMOUNT, amountString);

        if (mId != 0L) {
            updateEatEvent(values);
        } else {
            saveEatEvent(values);
        }
    }

    private void saveEatEvent(ContentValues values) {
        PukimonDbHelper dbHelper = new PukimonDbHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newRowId = db.insert(EatEventEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            Toast.makeText(getActivity().getApplicationContext(), "Database insert failed", Toast.LENGTH_SHORT).show();
            return;
        }

        getActivity().finish();
    }

    private void updateEatEvent(ContentValues values) {
        PukimonDbHelper dbHelper = new PukimonDbHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = EatEventEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(mId) };

        int count = db.update(
                EatEventEntry.TABLE_NAME,
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
        return inflater.inflate(R.layout.fragment_eat_event, container, false);
    }

}
