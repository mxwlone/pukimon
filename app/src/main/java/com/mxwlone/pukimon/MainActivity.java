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

import com.mxwlone.pukimon.domain.Event;
import com.mxwlone.pukimon.sql.PukimonContract.DrinkEventEntry;
import com.mxwlone.pukimon.sql.PukimonContract.SleepEventEntry;
import com.mxwlone.pukimon.sql.PukimonDbHelper;

import java.util.ArrayList;
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

//        String[] projection = {
//                DrinkEventEntry._ID,
//                DrinkEventEntry.COLUMN_NAME_TIMESTAMP,
//                DrinkEventEntry.COLUMN_NAME_AMOUNT,
//        };
//        String sortOrder = DrinkEventEntry.COLUMN_NAME_TIMESTAMP + " DESC";
//
//        mCursor = db.query(
//                DrinkEventEntry.TABLE_NAME,
//                projection,
//                null, null, null, null,
//                sortOrder
//        );

//        String[] projection = {
//                SleepEventEntry._ID,
//                SleepEventEntry.COLUMN_NAME_TIMESTAMP_FROM,
//                SleepEventEntry.COLUMN_NAME_TIMESTAMP_TO,
//        };
//        String sortOrder = SleepEventEntry.COLUMN_NAME_TIMESTAMP_FROM + " DESC";
//
//        mCursor = db.query(
//                SleepEventEntry.TABLE_NAME,
//                projection,
//                null, null, null, null,
//                sortOrder
//        );

        final String COMMA_SEP = ",";
        String sql = "SELECT * FROM " + DrinkEventEntry.TABLE_NAME + COMMA_SEP + SleepEventEntry.TABLE_NAME;
        mCursor = db.rawQuery(sql, null);

        mEvents.clear();

        // add test data
//        SleepEvent sleepEvent = new SleepEvent();
//        Calendar cal = Calendar.getInstance();
//        sleepEvent.setFromDate(cal.getTime());
//        cal.add(Calendar.HOUR_OF_DAY, 2);
//        cal.add(Calendar.MINUTE, 37);
//        sleepEvent.setToDate(cal.getTime());
//        mEvents.add(sleepEvent);

        Log.d(TAG, "Cursor content:");

        while(mCursor.moveToNext()) {
            String[] columns = mCursor.getColumnNames();

            for (String column : columns) {
                Log.d(TAG, String.format("Column %s: %s", column, mCursor.getString(mCursor.getColumnIndexOrThrow(column))));
            }

//            Long id = mCursor.getLong(mCursor.getColumnIndexOrThrow(DrinkEventEntry._ID));
//            Long timestamp = mCursor.getLong(mCursor.getColumnIndexOrThrow(DrinkEventEntry.COLUMN_NAME_TIMESTAMP));
//            int amount = mCursor.getInt(mCursor.getColumnIndexOrThrow(DrinkEventEntry.COLUMN_NAME_AMOUNT));
//
//            DrinkEvent drinkEvent = new DrinkEvent();
//            drinkEvent.setAmount(amount);
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTimeInMillis(timestamp);
//            drinkEvent.setDate(calendar.getTime());

//            Long id = mCursor.getLong(mCursor.getColumnIndexOrThrow(SleepEventEntry._ID));
//            Long fromTimestamp = mCursor.getLong(mCursor.getColumnIndexOrThrow(SleepEventEntry.COLUMN_NAME_TIMESTAMP_FROM));
//            Long toTimestamp = mCursor.getLong(mCursor.getColumnIndexOrThrow(SleepEventEntry.COLUMN_NAME_TIMESTAMP_TO));
//
//            SleepEvent sleepEvent = new SleepEvent();
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTimeInMillis(fromTimestamp);
//            sleepEvent.setFromDate(calendar.getTime());
//            calendar.setTimeInMillis(toTimestamp);
//            sleepEvent.setToDate(calendar.getTime());
//
//            mEvents.add(sleepEvent);
//
//            Log.d(TAG, String.format("id: %d\nfromDate: %s\ntoDate: %s\n\n", id, sleepEvent.getFromDate().toString(), sleepEvent.getToDate().toString()));
        }

        mAdapter.notifyDataSetChanged();
    }

    public void showNewEntryActivity(View view) {
        Intent intent = new Intent(this, NewEntryActivity.class);
        startActivity(intent);
    }

    public void showNewEntryTabbedActivity(View view) {
        Intent intent = new Intent(this, NewEntryTabbedActivity.class);
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
