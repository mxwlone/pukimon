package com.mxwlone.pukimon.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mxwlone.pukimon.sql.PukimonContract.DrinkEventEntry;
import com.mxwlone.pukimon.sql.PukimonContract.EatEventEntry;
import com.mxwlone.pukimon.sql.PukimonContract.SleepEventEntry;

/**
 * Created by maxwel on 9/7/2015.
 */
public class PukimonDbHelper extends SQLiteOpenHelper {

    final String TAG = this.getClass().getSimpleName();

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "Pukimon.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String CREATE_TABLE_DRINK_EVENT_ENTRY =
            "CREATE TABLE " + DrinkEventEntry.TABLE_NAME + " (" +
                    DrinkEventEntry._ID + " INTEGER PRIMARY KEY," +
                    DrinkEventEntry.COLUMN_NAME_TIMESTAMP + INTEGER_TYPE + COMMA_SEP +
                    DrinkEventEntry.COLUMN_NAME_AMOUNT + INTEGER_TYPE +
                    " );";
    private static final String CREATE_TABLE_SLEEP_EVENT_ENTRY =
            "CREATE TABLE " + SleepEventEntry.TABLE_NAME + " (" +
                    SleepEventEntry._ID + " INTEGER PRIMARY KEY," +
                    SleepEventEntry.COLUMN_NAME_TIMESTAMP_FROM + INTEGER_TYPE + COMMA_SEP +
                    SleepEventEntry.COLUMN_NAME_TIMESTAMP_TO + INTEGER_TYPE +
                    " );";
    private static final String CREATE_TABLE_EAT_EVENT_ENTRY =
            "CREATE TABLE " + EatEventEntry.TABLE_NAME + " (" +
                    EatEventEntry._ID + " INTEGER PRIMARY KEY," +
                    EatEventEntry.COLUMN_NAME_TIMESTAMP + INTEGER_TYPE + COMMA_SEP +
                    EatEventEntry.COLUMN_NAME_AMOUNT + INTEGER_TYPE +
                    " );";

    private static final String DROP_TABLE_DRINK_EVENT_ENTRY =
            "DROP TABLE IF EXISTS " + DrinkEventEntry.TABLE_NAME + "; ";
    private static final String DROP_TABLE_SLEEP_EVENT_ENTRY =
            "DROP TABLE IF EXISTS " + SleepEventEntry.TABLE_NAME + "; ";
    private static final String DROP_TABLE_EAT_EVENT_ENTRY =
            "DROP TABLE IF EXISTS " + EatEventEntry.TABLE_NAME + "; ";

    public PukimonDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    private void createTables(SQLiteDatabase db) {
        Log.d(TAG, CREATE_TABLE_DRINK_EVENT_ENTRY);
        db.execSQL(CREATE_TABLE_DRINK_EVENT_ENTRY);

        Log.d(TAG, CREATE_TABLE_SLEEP_EVENT_ENTRY);
        db.execSQL(CREATE_TABLE_SLEEP_EVENT_ENTRY);

        Log.d(TAG, CREATE_TABLE_EAT_EVENT_ENTRY);
        db.execSQL(CREATE_TABLE_EAT_EVENT_ENTRY);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        dropTables(db);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void clearDatabase(SQLiteDatabase db) {
        dropTables(db);
        onCreate(db);
    }

    private void dropTables(SQLiteDatabase db) {
        Log.d(TAG, DROP_TABLE_DRINK_EVENT_ENTRY);
        db.execSQL(DROP_TABLE_DRINK_EVENT_ENTRY);

        Log.d(TAG, DROP_TABLE_SLEEP_EVENT_ENTRY);
        db.execSQL(DROP_TABLE_SLEEP_EVENT_ENTRY);

        Log.d(TAG, DROP_TABLE_EAT_EVENT_ENTRY);
        db.execSQL(DROP_TABLE_EAT_EVENT_ENTRY);
    }
}