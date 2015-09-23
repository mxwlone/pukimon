package com.mxwlone.pukimon.activity;

import android.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;

import com.mxwlone.pukimon.R;
import com.mxwlone.pukimon.domain.DrinkEvent;
import com.mxwlone.pukimon.domain.EatEvent;
import com.mxwlone.pukimon.domain.SleepEvent;
import com.mxwlone.pukimon.fragment.DrinkEventFragment;
import com.mxwlone.pukimon.fragment.EatEventFragment;
import com.mxwlone.pukimon.fragment.SleepEventFragment;

import java.util.Locale;

public class NewEventActivity extends FragmentActivity implements ActionBar.TabListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("eventType")) {
                getActionBar().setTitle(R.string.title_update_entry);

                if (extras.getString("eventType", null).equals(DrinkEvent.class.toString())) {
                    DrinkEventFragment fragment = new DrinkEventFragment();
                    fragment.setArguments(extras);

                    setContentView(R.layout.fragment_placeholder_event_update);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_placeholder, fragment);
                    ft.commit();
                    return;
                } else if (extras.getString("eventType", null).equals(SleepEvent.class.toString())) {
                    SleepEventFragment fragment = new SleepEventFragment();
                    fragment.setArguments(extras);

                    setContentView(R.layout.fragment_placeholder_event_update);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_placeholder, fragment);
                    ft.commit();
                    return;
                } else if (extras.getString("eventType", null).equals(EatEvent.class.toString())) {
                    EatEventFragment fragment = new EatEventFragment();
                    fragment.setArguments(extras);

                    setContentView(R.layout.fragment_placeholder_event_update);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_placeholder, fragment);
                    ft.commit();
                    return;
                }
            }
        }

        setContentView(R.layout.activity_new_event);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

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

    public void cancelButtonClicked(View view) {
        finish();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_new_event, menu);
//        return true;
//    }

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
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

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
                return DrinkEventFragment.newInstance();
            else if (position == 1)
                return EatEventFragment.newInstance();
            else
                return SleepEventFragment.newInstance();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_update_entry).toUpperCase(l);
                case 1:
                    return getString(R.string.title_update_entry).toUpperCase(l);
                case 2:
                    return getString(R.string.title_update_entry).toUpperCase(l);
            }
            return null;
        }

        public Drawable getPageIcon(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getResources().getDrawable(R.drawable.bottle);
                case 1:
                    return getResources().getDrawable(R.drawable.eat);
                case 2:
                    return getResources().getDrawable(R.drawable.sleep);
            }
            return null;
        }
    }
}
