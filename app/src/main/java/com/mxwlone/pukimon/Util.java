package com.mxwlone.pukimon;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.mxwlone.pukimon.sql.PukimonContract;
import com.mxwlone.pukimon.sql.PukimonDbHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by maxwel on 01.09.2015.
 */
public class Util {

    public static void saveSleepEvent(Activity activity, final String TAG) {
        EditText editTextDate = (EditText) activity.findViewById(R.id.sleepEventEditTextDate);
        String dateString = editTextDate.getText().toString();
        EditText editTextFromTime = (EditText) activity.findViewById(R.id.editTextFromTime);
        String fromTimeString = editTextFromTime.getText().toString();
        EditText editTextToTime = (EditText) activity.findViewById(R.id.editTextToTime);
        String toTimeString = editTextToTime.getText().toString();

        Log.d(TAG, "dateString: " + dateString);
        Log.d(TAG, "fromTimeString: " + fromTimeString);
        Log.d(TAG, "toTimeString: " + toTimeString);

        Date fromDate = null, toDate = null;
        try {
            Locale locale = activity.getResources().getConfiguration().locale;
            DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(
                    DateFormat.SHORT, DateFormat.SHORT, locale);
            fromDate = dateTimeFormat.parse(dateString + " " + fromTimeString);
            toDate = dateTimeFormat.parse(dateString + " " + toTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (fromDate == null || toDate == null) {
            Toast.makeText(activity.getApplicationContext(), "Unparseable date",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (toDate.compareTo(fromDate) < 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(toDate);
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
            toDate = calendar.getTime();
        }

        ContentValues values = new ContentValues();
        values.put(PukimonContract.SleepEventEntry.COLUMN_NAME_TIMESTAMP_FROM, fromDate.getTime());
        values.put(PukimonContract.SleepEventEntry.COLUMN_NAME_TIMESTAMP_TO, toDate.getTime());

        PukimonDbHelper dbHelper = new PukimonDbHelper(activity.getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newRowId = db.insert(PukimonContract.SleepEventEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            Toast.makeText(activity.getApplicationContext(), "Database insert failed",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        activity.finish();
    }

    public static void saveDrinkEvent(Activity activity, final String TAG) {
        EditText editTextDate = (EditText) activity.findViewById(R.id.drinkEventEditTextDate);
        String dateString = editTextDate.getText().toString();
        EditText editTextTime = (EditText) activity.findViewById(R.id.editTextTime);
        String timeString = editTextTime.getText().toString();
        EditText editTextAmount = (EditText) activity.findViewById(R.id.editTextAmount);
        String amountString = editTextAmount.getText().toString();

        Log.d(TAG, "dateString: " + dateString);
        Log.d(TAG, "timeString: " + timeString);
        Log.d(TAG, "amountString: " + amountString);

        Date date = null;
        try {
            Locale locale = activity.getResources().getConfiguration().locale;
            DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(
                    DateFormat.SHORT, DateFormat.SHORT, locale);
            date = dateTimeFormat.parse(dateString + " " + timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) {
            Toast.makeText(activity.getApplicationContext(), "Unparseable date", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(PukimonContract.DrinkEventEntry.COLUMN_NAME_TIMESTAMP, date.getTime());
        values.put(PukimonContract.DrinkEventEntry.COLUMN_NAME_AMOUNT, amountString);

        PukimonDbHelper dbHelper = new PukimonDbHelper(activity.getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newRowId = db.insert(PukimonContract.DrinkEventEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            Toast.makeText(activity.getApplicationContext(), "Database insert failed", Toast.LENGTH_SHORT).show();
            return;
        }

        activity.finish();
    }

}
