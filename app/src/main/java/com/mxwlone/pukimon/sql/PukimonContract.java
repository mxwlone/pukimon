package com.mxwlone.pukimon.sql;

import android.provider.BaseColumns;

/**
 * Created by maxwel on 9/7/2015.
 */
public final class PukimonContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public PukimonContract() {}

    public static abstract class DrinkEventEntry implements BaseColumns {
        public static final String TABLE_NAME = "drink_event_entry";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_AMOUNT = "amount";
    }

    public static abstract class SleepEventEntry implements BaseColumns {
        public static final String TABLE_NAME = "sleep_event_entry";
        public static final String COLUMN_NAME_TIMESTAMP_FROM = "timestamp_from";
        public static final String COLUMN_NAME_TIMESTAMP_TO = "timestamp_until";
    }

    public static abstract class EatEventEntry implements BaseColumns {
        public static final String TABLE_NAME = "eat_event_entry";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_AMOUNT = "amount";
    }

}