package com.mxwlone.pukimon;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mxwlone.pukimon.sql.PukimonContract.DrinkEventEntry;
import com.mxwlone.pukimon.sql.PukimonDbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewEntryActivity extends Activity {

    final String TAG = this.getClass().getSimpleName().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
    }

    public void okButtonClicked(View view) {
        EditText editTextDate = (EditText) findViewById(R.id.editTextDate);
        String dateString = editTextDate.getText().toString();
        EditText editTextTime = (EditText) findViewById(R.id.editTextTime);
        String timeString = editTextTime.getText().toString();
        EditText editTextAmount = (EditText) findViewById(R.id.editTextAmount);
        String amountString = editTextAmount.getText().toString();

        Log.d(TAG, "dateString: " + dateString);
        Log.d(TAG, "timeString: " + timeString);
        Log.d(TAG, "amountString: " + amountString);

        Date date = null;
        try {
            //TODO refactor: use DateFormat.getDateTimeInstance(DateFormat.DEFAULT, currentLocale) http://docs.oracle.com/javase/tutorial/i18n/format/dateFormat.html
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat(getResources().getString(R.string.date_time_format), getResources().getConfiguration().locale);
            date = dateTimeFormat.parse(dateString + " " + timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) {
            Toast.makeText(getApplicationContext(), "Unparseable date", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Date: " + date.getTime());

//        DrinkEvent drinkEvent = new DrinkEvent();
//        drinkEvent.setAmount(Integer.parseInt(amountString));
//        drinkEvent.setDate(date);

        ContentValues values = new ContentValues();
        values.put(DrinkEventEntry.COLUMN_NAME_TIMESTAMP, date.getTime());
        values.put(DrinkEventEntry.COLUMN_NAME_AMOUNT, amountString);

        PukimonDbHelper dbHelper = new PukimonDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newRowId = db.insert(DrinkEventEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            Toast.makeText(getApplicationContext(), "Database insert failed", Toast.LENGTH_SHORT).show();
            return;
        }

        finish();
    }

    public void cancelButtonClicked(View view) {
        finish();
    }

    public void showTimePickerDialog(View view) {
        DialogFragment dialogFragment = new TimePickerFragment();
        dialogFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View view) {
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
