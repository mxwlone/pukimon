package com.mxwlone.pukimon;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.mxwlone.pukimon.domain.DrinkEvent;
import com.mxwlone.pukimon.domain.Event;
import com.mxwlone.pukimon.domain.SleepEvent;
import com.mxwlone.pukimon.sql.PukimonContract.DrinkEventEntry;
import com.mxwlone.pukimon.sql.PukimonDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends Activity {

    final String TAG = this.getClass().getSimpleName();

    ListView mListView;
    Cursor mCursor = null;
    EventAdapter mAdapter;
    List<Event> mEvents = new ArrayList<Event>();
//    SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new EventAdapter(this, mEvents);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);

        updateList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateList();
    }

    private void updateList() {
        PukimonDbHelper dbHelper = new PukimonDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DrinkEventEntry._ID,
                DrinkEventEntry.COLUMN_NAME_TIMESTAMP,
                DrinkEventEntry.COLUMN_NAME_AMOUNT,
        };
        String sortOrder = DrinkEventEntry.COLUMN_NAME_TIMESTAMP + " DESC";

        mCursor = db.query(
                DrinkEventEntry.TABLE_NAME,
                projection,
                null, null, null, null,
                sortOrder
        );

        mEvents.clear();

        // add test data
        SleepEvent sleepEvent = new SleepEvent();
        Calendar cal = Calendar.getInstance();
        sleepEvent.setFromDate(cal.getTime());
        cal.add(Calendar.HOUR_OF_DAY, 1);
        cal.add(Calendar.MINUTE, 10);
        sleepEvent.setToDate(cal.getTime());
        mEvents.add(sleepEvent);

        while(mCursor.moveToNext()) {
            Long id = mCursor.getLong(mCursor.getColumnIndexOrThrow(DrinkEventEntry._ID));
            Long timestamp = mCursor.getLong(mCursor.getColumnIndexOrThrow(DrinkEventEntry.COLUMN_NAME_TIMESTAMP));
            int amount = mCursor.getInt(mCursor.getColumnIndexOrThrow(DrinkEventEntry.COLUMN_NAME_AMOUNT));

            DrinkEvent drinkEvent = new DrinkEvent();
            drinkEvent.setAmount(amount);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            drinkEvent.setDate(calendar.getTime());

            mEvents.add(drinkEvent);

            Log.d(TAG, String.format("id: %d\nDate: %s\namount: %d\n\n", id, drinkEvent.getDate().toString(), drinkEvent.getAmount()));
        }

        mAdapter.notifyDataSetChanged();
    }

    public void showNewEntryActivity(View view) {
        Intent intent = new Intent(this, NewEntryActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        } else if (id == R.id.clear_database) {
            PukimonDbHelper dbHelper = new PukimonDbHelper(getApplicationContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            dbHelper.clearDatabase(db);
            updateList();
        }

        return super.onOptionsItemSelected(item);
    }

}
