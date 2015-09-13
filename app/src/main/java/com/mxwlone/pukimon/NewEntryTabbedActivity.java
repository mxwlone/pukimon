package com.mxwlone.pukimon;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mxwlone.pukimon.sql.PukimonContract;
import com.mxwlone.pukimon.sql.PukimonDbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewEntryTabbedActivity extends Activity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    final String TAG = this.getClass().getSimpleName();

    public void cancelButtonClicked(View view) {
        finish();
    }

    public void okButtonClickedFromSleepEntry(View view) {
        EditText editTextDate = (EditText) findViewById(R.id.sleepEntryEditTextDate);
        String dateString = editTextDate.getText().toString();
        EditText editTextFromTime = (EditText) findViewById(R.id.editTextFromTime);
        String fromTimeString = editTextFromTime.getText().toString();
        EditText editTextToTime = (EditText) findViewById(R.id.editTextToTime);
        String toTimeString = editTextToTime.getText().toString();

        Log.d(TAG, "dateString: " + dateString);
        Log.d(TAG, "fromTimeString: " + fromTimeString);
        Log.d(TAG, "toTimeString: " + toTimeString);

        Date fromDate = null, toDate = null;
        try {
            //TODO refactor: use DateFormat.getDateTimeInstance(DateFormat.DEFAULT, currentLocale) http://docs.oracle.com/javase/tutorial/i18n/format/dateFormat.html
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat(getResources().getString(R.string.date_time_format), getResources().getConfiguration().locale);
            fromDate = dateTimeFormat.parse(dateString + " " + fromTimeString);
            toDate = dateTimeFormat.parse(dateString + " " + toTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (fromDate == null || toDate == null) {
            Toast.makeText(getApplicationContext(), "Unparseable date", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(PukimonContract.SleepEventEntry.COLUMN_NAME_TIMESTAMP_FROM, fromDate.getTime());
        values.put(PukimonContract.SleepEventEntry.COLUMN_NAME_TIMESTAMP_TO, toDate.getTime());

        PukimonDbHelper dbHelper = new PukimonDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newRowId = db.insert(PukimonContract.SleepEventEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            Toast.makeText(getApplicationContext(), "Database insert failed", Toast.LENGTH_SHORT).show();
            return;
        }

        finish();
    }

    public void okButtonClickedFromDrinkEntry(View view) {
        EditText editTextDate = (EditText) findViewById(R.id.drinkEntryEditTextDate);
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

        ContentValues values = new ContentValues();
        values.put(PukimonContract.DrinkEventEntry.COLUMN_NAME_TIMESTAMP, date.getTime());
        values.put(PukimonContract.DrinkEventEntry.COLUMN_NAME_AMOUNT, amountString);

        PukimonDbHelper dbHelper = new PukimonDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newRowId = db.insert(PukimonContract.DrinkEventEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            Toast.makeText(getApplicationContext(), "Database insert failed", Toast.LENGTH_SHORT).show();
            return;
        }

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry_tabbed);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setIcon(mSectionsPagerAdapter.getPageIcon(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_entry_tabbed, menu);
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0)
                return DrinkEntryFragment.newInstance(position);
            else
                return SleepEntryFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }

        public Drawable getPageIcon(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getDrawable(R.drawable.bottle);
                case 1:
                    return getDrawable(R.drawable.sleep);
            }
            return null;
        }
    }
}
