package com.mxwlone.pukimon;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.mxwlone.pukimon.sql.PukimonContract.DrinkEventEntry;
import com.mxwlone.pukimon.sql.PukimonDbHelper;

public class MainActivity extends Activity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    final String TAG = this.getClass().getSimpleName().toString();

    ListView mListView;
    SimpleCursorAdapter mAdapter;
    static final String[] PROJECTION = new String[] {
            DrinkEventEntry._ID,
            DrinkEventEntry.COLUMN_NAME_TIMESTAMP,
            DrinkEventEntry.COLUMN_NAME_AMOUNT,
    };
    static final String SORT_ORDER = DrinkEventEntry.COLUMN_NAME_TIMESTAMP + " DESC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                R.layout.list_item, null,
                fromColumns, toViews, 0);
        mListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        debugDatabaseOutput();
    }

    private void debugDatabaseOutput() {
        PukimonDbHelper dbHelper = new PukimonDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DrinkEventEntry._ID,
                DrinkEventEntry.COLUMN_NAME_TIMESTAMP,
                DrinkEventEntry.COLUMN_NAME_AMOUNT,
        };
        String sortOrder = DrinkEventEntry.COLUMN_NAME_TIMESTAMP + " DESC";

        Cursor cursor = db.query(
                DrinkEventEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

//        cursor.moveToFirst();
        while(cursor.moveToNext()) {
            Long id = cursor.getLong(cursor.getColumnIndexOrThrow(DrinkEventEntry._ID));
            Long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(DrinkEventEntry.COLUMN_NAME_TIMESTAMP));
            Long amount = cursor.getLong(cursor.getColumnIndexOrThrow(DrinkEventEntry.COLUMN_NAME_AMOUNT));

            Log.d(TAG, String.format("id: %d\ntimestamp: %d\namount: %d\n\n", id, timestamp, amount));
        }


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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, null, PROJECTION, null, null, SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
