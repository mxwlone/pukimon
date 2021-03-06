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
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.mxwlone.pukimon.ExpandableEventAdapter;
import com.mxwlone.pukimon.R;
import com.mxwlone.pukimon.model.DaySummary;
import com.mxwlone.pukimon.model.DrinkEvent;
import com.mxwlone.pukimon.model.EatEvent;
import com.mxwlone.pukimon.model.Event;
import com.mxwlone.pukimon.model.SleepEvent;
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

        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Event event = (Event) mAdapter.getChild(groupPosition, childPosition);
                editEventFromList(event);
                return true;
            }
        });

        registerForContextMenu(this.mListView);

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
        ExpandableListView.ExpandableListContextMenuInfo info =
                (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        int groupPosition = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        int childPosition = ExpandableListView.getPackedPositionChild(info.packedPosition);

        if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            // do something with parent

        } else if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            Event event = (Event) mAdapter.getChild(groupPosition, childPosition);

            switch (item.getItemId()) {
                case R.id.context_menu_edit_item:
                    editEventFromList(event);
                    return true;
                case R.id.context_menu_delete_item:
                    return deleteEventFromList(event);
                default:
                    return super.onContextItemSelected(item);
            }
        }

        return super.onContextItemSelected(item);
    }

    private Boolean deleteEventFromList(Event event) {
//        Event event = mEvents.get(position);
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
            Log.d(TAG, String.format("Event %d does not have type %s or %s or %s. " +
                    "Delete failed.", DrinkEvent.class.getSimpleName(),
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

    private void editEventFromList(Event event) {
//        Event event = mEvents.get(position);
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
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        int groupPosition = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        int childPosition = ExpandableListView.getPackedPositionChild(info.packedPosition);

        MenuInflater inflater = getMenuInflater();

        if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            DaySummary daySummary = (DaySummary) mAdapter.getGroup(groupPosition);
            String title = daySummary.getDate();
            menu.setHeaderTitle(title);
            inflater.inflate(R.menu.context_menu_group, menu);

        } else if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            Event event = (Event) mAdapter.getChild(groupPosition, childPosition);
            menu.setHeaderTitle(event.toString());
            inflater.inflate(R.menu.context_menu_child, menu);
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
