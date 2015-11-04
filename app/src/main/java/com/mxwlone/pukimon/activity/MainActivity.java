package com.mxwlone.pukimon.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mxwlone.pukimon.EventAdapter;
import com.mxwlone.pukimon.ExpandableEventAdapter;
import com.mxwlone.pukimon.R;
import com.mxwlone.pukimon.domain.DrinkEvent;
import com.mxwlone.pukimon.domain.EatEvent;
import com.mxwlone.pukimon.domain.Event;
import com.mxwlone.pukimon.domain.SleepEvent;
import com.mxwlone.pukimon.sql.PukimonContract.DrinkEventEntry;
import com.mxwlone.pukimon.sql.PukimonContract.EatEventEntry;
import com.mxwlone.pukimon.sql.PukimonContract.SleepEventEntry;
import com.mxwlone.pukimon.sql.PukimonDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends FragmentActivity {

    final String TAG = this.getClass().getSimpleName();

    ExpandableListView mListView;
    Cursor mCursor = null;
    ExpandableEventAdapter mAdapter;
    List<Event> mEvents = new ArrayList<Event>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new ExpandableEventAdapter(this, mEvents);
        mListView = (ExpandableListView) findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);

//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                editEventFromList(position);
//            }
//        });
//
//        registerForContextMenu(this.mListView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewEventActivity(view);
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.context_menu_edit_item:
                editEventFromList((int) info.id);
                return true;
            case R.id.context_menu_delete_item:
                return deleteEventFromList((int) info.id);
            default:
                return super.onContextItemSelected(item);
        }
    }

    private Boolean deleteEventFromList(int position) {
        Event event = mEvents.get(position);
        Log.d(TAG, String.format("Delete %s with id %d", event.getClass().getSimpleName(), event.getId()));

        PukimonDbHelper dbHelper = new PukimonDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = "";
        String tableName = "";
        if (event instanceof DrinkEvent) {
            selection = DrinkEventEntry._ID + " LIKE ?";
            tableName = DrinkEventEntry.TABLE_NAME;
        } else if (event instanceof SleepEvent) {
            selection = SleepEventEntry._ID + " LIKE ?";
            tableName = SleepEventEntry.TABLE_NAME;
        } else if (event instanceof EatEvent) {
            selection = EatEventEntry._ID + " LIKE ?";
            tableName = EatEventEntry.TABLE_NAME;
        } else {
            Log.d(TAG, String.format("Event at position %d does not have type %s or %s or %s. " +
                    "Delete failed.", position, DrinkEvent.class.getSimpleName(),
                    SleepEvent.class.getSimpleName(), EatEvent.class.getSimpleName()));
            return false;
        }

        String[] selectionArgs = { String.valueOf(event.getId()) };
        if (db.delete(tableName, selection, selectionArgs) < 1) {
            Toast.makeText(getApplicationContext(), "Database delete failed", Toast.LENGTH_SHORT).show();
            return false;
        }

        updateList();
        return true;
    }

    private void editEventFromList(int position) {
        Event event = mEvents.get(position);
        Log.d(TAG, String.format("Edit %s with id %d", event.getClass().getSimpleName(), event.getId()));

        Intent intent = new Intent(MainActivity.this, NewEventActivity.class);
        intent.putExtra("eventType", event.getClass().toString());

        if (event instanceof DrinkEvent) {
            intent.putExtra("id", event.getId());
            intent.putExtra("date", ((DrinkEvent) event).getDate().getTime());
            intent.putExtra("amount", ((DrinkEvent) event).getAmount());
        } else if (event instanceof SleepEvent) {
            intent.putExtra("id", event.getId());
            intent.putExtra("fromDate", ((SleepEvent) event).getFromDate().getTime());
            intent.putExtra("toDate", ((SleepEvent) event).getDate().getTime());
        } else if (event instanceof EatEvent) {
            intent.putExtra("id", event.getId());
            intent.putExtra("date", ((EatEvent) event).getDate().getTime());
            intent.putExtra("amount", ((EatEvent) event).getAmount());
        }

        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.listView) {
//            ListView listView = (ListView) v;
//            AdapterView.AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) menuInfo;
//            Event event = (Event) listView.getItemAtPosition(acmi.position);

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateList();
    }

    private void updateList() {
        mEvents.clear();

        PukimonDbHelper dbHelper = new PukimonDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        queryDrinkEventEntry(db);
        querySleepEventEntry(db);
        queryEatEventEntry(db);
        db.close();

        Collections.sort(mEvents);

        // debug
        Log.d(TAG, "Order after sort:");
        for (Event event : mEvents) {
            Log.d(TAG, String.format("%s id: %d", event.getClass().getSimpleName(), event.getId()));
        }

        if (mAdapter != null) {
            mAdapter.setData(mEvents);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void queryDrinkEventEntry(SQLiteDatabase db) {
        String[] projection = {
                DrinkEventEntry._ID,
                DrinkEventEntry.COLUMN_NAME_TIMESTAMP,
                DrinkEventEntry.COLUMN_NAME_AMOUNT,
        };

        mCursor = db.query(
                DrinkEventEntry.TABLE_NAME,
                projection,
                null, null, null, null, null
        );

        if (mCursor != null) {
            while (mCursor.moveToNext()) {

                Long id = mCursor.getLong(mCursor.getColumnIndexOrThrow(DrinkEventEntry._ID));
                Long timestamp = mCursor.getLong(mCursor.getColumnIndexOrThrow(DrinkEventEntry.COLUMN_NAME_TIMESTAMP));
                int amount = mCursor.getInt(mCursor.getColumnIndexOrThrow(DrinkEventEntry.COLUMN_NAME_AMOUNT));

                DrinkEvent drinkEvent = new DrinkEvent(id);
                drinkEvent.setAmount(amount);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timestamp);
                drinkEvent.setDate(calendar.getTime());

                Log.d(TAG, String.format("DrinkEventEntry id: %d date: %s amount: %s" +
                                System.getProperty("line.separator"),
                        id, drinkEvent.getDate().toString(), drinkEvent.getAmount()));
                mEvents.add(drinkEvent);

            }
        }
    }

    private void queryEatEventEntry(SQLiteDatabase db) {
        String[] projection = {
                EatEventEntry._ID,
                EatEventEntry.COLUMN_NAME_TIMESTAMP,
                EatEventEntry.COLUMN_NAME_AMOUNT,
        };

        mCursor = db.query(
                EatEventEntry.TABLE_NAME,
                projection,
                null, null, null, null, null
        );

        if (mCursor != null) {
            while (mCursor.moveToNext()) {

                Long id = mCursor.getLong(mCursor.getColumnIndexOrThrow(EatEventEntry._ID));
                Long timestamp = mCursor.getLong(mCursor.getColumnIndexOrThrow(EatEventEntry.COLUMN_NAME_TIMESTAMP));
                int amount = mCursor.getInt(mCursor.getColumnIndexOrThrow(EatEventEntry.COLUMN_NAME_AMOUNT));

                EatEvent eatEvent = new EatEvent(id);
                eatEvent.setAmount(amount);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timestamp);
                eatEvent.setDate(calendar.getTime());

                Log.d(TAG, String.format("EatEventEntry id: %d date: %s amount: %s",
                        id, eatEvent.getDate().toString(), eatEvent.getAmount()));
                mEvents.add(eatEvent);

            }
        }
    }

    private void querySleepEventEntry(SQLiteDatabase db) {
        String[] projection = {
                SleepEventEntry._ID,
                SleepEventEntry.COLUMN_NAME_TIMESTAMP_FROM,
                SleepEventEntry.COLUMN_NAME_TIMESTAMP_TO,
        };

        mCursor = db.query(
                SleepEventEntry.TABLE_NAME,
                projection,
                null, null, null, null, null
        );

        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                Long id = mCursor.getLong(mCursor.getColumnIndexOrThrow(SleepEventEntry._ID));
                Long fromTimestamp = mCursor.getLong(mCursor.getColumnIndexOrThrow(SleepEventEntry.COLUMN_NAME_TIMESTAMP_FROM));
                Long toTimestamp = mCursor.getLong(mCursor.getColumnIndexOrThrow(SleepEventEntry.COLUMN_NAME_TIMESTAMP_TO));

                SleepEvent sleepEvent = new SleepEvent(id);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(fromTimestamp);
                sleepEvent.setFromDate(calendar.getTime());
                calendar.setTimeInMillis(toTimestamp);
                sleepEvent.setDate(calendar.getTime());

                Log.d(TAG, String.format("SleepEventEntry id: %d fromDate: %s toDate: %s" +
                                System.getProperty("line.separator"),
                        id, sleepEvent.getFromDate().toString(), sleepEvent.getDate().toString()));
                mEvents.add(sleepEvent);
            }
        }
    }

    public void startNewEventActivity(View view) {
        Intent intent = new Intent(this, NewEventActivity.class);
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
