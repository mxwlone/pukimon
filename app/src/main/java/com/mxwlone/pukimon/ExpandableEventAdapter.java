package com.mxwlone.pukimon;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.mxwlone.pukimon.domain.DaySummary;
import com.mxwlone.pukimon.domain.DrinkEvent;
import com.mxwlone.pukimon.domain.EatEvent;
import com.mxwlone.pukimon.domain.Event;
import com.mxwlone.pukimon.domain.SleepEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by maxwel on 22.09.2015.
 */
public class ExpandableEventAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<DaySummary> mGroupItems = new ArrayList<>();
    private HashMap<DaySummary, List<Event>> mChildItems = new HashMap<>();

    public ExpandableEventAdapter(Context mContext, List<DaySummary> groupItems, HashMap<DaySummary, List<Event>> childItems) {
        this.mContext = mContext;
        this.mGroupItems = groupItems;
        this.mChildItems = childItems;
    }

    public ExpandableEventAdapter(Context mContext, List<Event> events) {
        this.mContext = mContext;

        List<String> datesList = new ArrayList<>(); // array which contains all distinct dates
        for (Event event : events) {
            String dateString = "";
            dateString = Util.getDateString(event.getDate());

            if (!dateString.equals(""))
                if (datesList.indexOf(dateString) == -1)
                    datesList.add(dateString);
        }

        for (String dateString : datesList) {
            DaySummary daySummary = new DaySummary(dateString);
            List<Event> dayEvents = new ArrayList<>();
            int drinkTotal = 0, eatTotal = 0, sleepTotalMinutes = 0;

            for (Event event : events) {
                if (!Util.getDateString(event.getDate()).equals(dateString))
                    continue;

                dayEvents.add(event);
                if (event instanceof DrinkEvent)
                    drinkTotal += ((DrinkEvent) event).getAmount();
                else if (event instanceof EatEvent)
                    eatTotal += ((EatEvent) event).getAmount();
                else if (event instanceof SleepEvent) {
                    SleepEvent sleepEvent = (SleepEvent) event;
                    sleepTotalMinutes += Util.getDifferenceInMinutes(sleepEvent.getFromDate(),
                            sleepEvent.getDate());
                }
            }

            daySummary.setDrinkTotal(drinkTotal);
            daySummary.setEatTotal(eatTotal);
            daySummary.setSleepTotalMinutes(sleepTotalMinutes);
            mGroupItems.add(daySummary);
            mChildItems.put(daySummary, dayEvents);
        }

    }

    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
