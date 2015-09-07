package com.mxwlone.pukimon.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mxwlone.pukimon.sql.PukimonContract.DrinkEventEntry;

/**
 * Created by maxwel on 9/7/2015.
 */
public class PukimonDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Pukimon.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DrinkEventEntry.TABLE_NAME + " (" +
                    DrinkEventEntry._ID + " INTEGER PRIMARY KEY," +
//                        DrinkEventEntry.COLUMN_NAME_ID + TEXT_TYPE + COMMA_SEP +
                    DrinkEventEntry.COLUMN_NAME_TIMESTAMP + INTEGER_TYPE + COMMA_SEP +
                    DrinkEventEntry.COLUMN_NAME_AMOUNT + INTEGER_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DrinkEventEntry.TABLE_NAME;

    public PukimonDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void clearDatabase(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}