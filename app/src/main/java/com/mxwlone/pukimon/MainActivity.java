package com.mxwlone.pukimon;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.mxwlone.pukimon.sql.PukimonContract.DrinkEventEntry;
import com.mxwlone.pukimon.sql.PukimonDbHelper;

public class MainActivity extends Activity {

    final String TAG = this.getClass().getSimpleName().toString();

    ListView mListView;
    Cursor mCursor = null;
    SimpleCursorAdapter mAdapter;
    String[] PROJECTION = {
            DrinkEventEntry._ID,
            DrinkEventEntry.COLUMN_NAME_TIMESTAMP,
            DrinkEventEntry.COLUMN_NAME_AMOUNT,
    };
    String SORT_ORDER = DrinkEventEntry.COLUMN_NAME_TIMESTAMP + " DESC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateCursor();

        mListView = (ListView) findViewById(R.id.listView);
        String[] fromColumns = {
                DrinkEventEntry.COLUMN_NAME_TIMESTAMP,
                DrinkEventEntry.COLUMN_NAME_AMOUNT,
        };
        int[] toViews = {
                R.id.list_item_timestamp,
                R.id.list_item_amount,
        };

        mAdapter = new SimpleCursorAdapter(this,
                R.layout.list_item, mCursor,
                fromColumns, toViews, 0);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateCursor();
        mAdapter.changeCursor(mCursor);
    }

    private void updateCursor() {
        PukimonDbHelper dbHelper = new PukimonDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        mCursor = db.query(
                DrinkEventEntry.TABLE_NAME,
                PROJECTION,
                null, null, null, null,
                SORT_ORDER
        );

//        while(mCursor.moveToNext()) {
//            Long id = mCursor.getLong(mCursor.getColumnIndexOrThrow(DrinkEventEntry._ID));
//            Long timestamp = mCursor.getLong(mCursor.getColumnIndexOrThrow(DrinkEventEntry.COLUMN_NAME_TIMESTAMP));
//            Long amount = mCursor.getLong(mCursor.getColumnIndexOrThrow(DrinkEventEntry.COLUMN_NAME_AMOUNT));
//
//            Log.d(TAG, String.format("id: %d\ntimestamp: %d\namount: %d\n\n", id, timestamp, amount));
//        }
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
        }

        return super.onOptionsItemSelected(item);
    }

}
