package com.mxwlone.pukimon;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxwlone.pukimon.domain.DaySummary;
import com.mxwlone.pukimon.domain.DrinkEvent;
import com.mxwlone.pukimon.domain.EatEvent;
import com.mxwlone.pukimon.domain.Event;
import com.mxwlone.pukimon.domain.SleepEvent;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by maxwel on 22.09.2015.
 */
public class ExpandableEventAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<DaySummary> mGroupItems = new ArrayList<>();
    private HashMap<DaySummary, List<Event>> mChildItems = new HashMap<>();
    private LayoutInflater mInflater;

    Locale LOCALE;
    DateFormat TIME_FORMAT, DATE_FORMAT, DATE_TIME_FORMAT;

    public ExpandableEventAdapter(Context mContext, List<DaySummary> groupItems, HashMap<DaySummary, List<Event>> childItems) {
        this.mContext = mContext;
        this.mGroupItems = groupItems;
        this.mChildItems = childItems;
    }

    public ExpandableEventAdapter(Context context, List<Event> events) {
        super();
        mInflater = LayoutInflater.from(context);
        this.mContext = context;

        LOCALE = context.getResources().getConfiguration().locale;
        TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.SHORT, LOCALE);
        DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT, LOCALE);
        DATE_TIME_FORMAT = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, LOCALE);

        setData(events);
    }

    public void setData(List<Event> events) {
        mGroupItems.clear();

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
            daySummary.setSleepMinutesTotal(sleepTotalMinutes);
            mGroupItems.add(daySummary);
            mChildItems.put(daySummary, dayEvents);
        }
    }

    @Override
    public int getGroupCount() {
        return this.mGroupItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mChildItems.get(this.mGroupItems.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mGroupItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mChildItems.get(this.mGroupItems.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        DaySummary groupView = (DaySummary) getGroup(groupPosition);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
        }

        TextView textView = (TextView) convertView
                .findViewById(R.id.list_item_amount_text);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(groupView.toString());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        Resources resources = App.getContext().getResources();
        if(convertView == null) {
            view = mInflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.eventIcon = (ImageView)view.findViewById(R.id.list_item_event_icon);
            holder.amountText = (TextView)view.findViewById(R.id.list_item_amount_text);
            holder.timeText = (TextView)view.findViewById(R.id.list_item_time_text);
            holder.dateText = (TextView)view.findViewById(R.id.list_item_date_text);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        final Event event = (Event) getChild(groupPosition, childPosition);

        String amountString = "";
        String timeString = "";
        String dateString = "";
        int iconResource = 0;

        if (event instanceof DrinkEvent) {
            DrinkEvent drinkEvent = (DrinkEvent) event;
            int amount = drinkEvent.getAmount();
            amountString = String.valueOf(amount) + resources.getString(R.string.format_milliliters);
            timeString = TIME_FORMAT.format(drinkEvent.getDate());
            dateString = DATE_FORMAT.format(drinkEvent.getDate());
            iconResource = R.drawable.bottle;
        } else if (event instanceof EatEvent) {
            EatEvent eatEvent = (EatEvent) event;
            int amount = eatEvent.getAmount();
            amountString = String.valueOf(amount) + resources.getString(R.string.format_gram);
            timeString = TIME_FORMAT.format(eatEvent.getDate());
            dateString = DATE_FORMAT.format(eatEvent.getDate());
            iconResource = R.drawable.eat;
        } else if (event instanceof SleepEvent) {
            SleepEvent sleepEvent = (SleepEvent) event;
            long diff = sleepEvent.getDate().getTime() - sleepEvent.getFromDate().getTime();
            long hours = TimeUnit.MILLISECONDS.toHours(diff);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60;

            String hoursString = hours != 0 ?
                    hours == 1 ? String.format(resources.getString(R.string.format_hour) + " ", hours) :
                            String.format(resources.getString(R.string.format_hours) + " ", hours) :
                    "";
            String minutesString = minutes != 0 ?
                    minutes == 1 ? String.format(resources.getString(R.string.format_minute), minutes) :
                            String.format(resources.getString(R.string.format_minutes), minutes)
                    : hours != 0 ? "" : String.format(resources.getString(R.string.format_minutes), minutes);
            amountString = hoursString + minutesString;
            timeString = TIME_FORMAT.format(sleepEvent.getFromDate()) + " - " +
                    TIME_FORMAT.format(sleepEvent.getDate());
            dateString = DATE_FORMAT.format(sleepEvent.getDate());
            iconResource = R.drawable.sleep;
        }

        holder.amountText.setText(amountString);
        holder.timeText.setText(timeString);
        holder.dateText.setText(dateString);
        if (iconResource != 0) holder.eventIcon.setImageResource(iconResource);

        return view;

//        final Event childView = (Event) getChild(groupPosition, childPosition);
//
//        if (convertView == null) {
//            LayoutInflater inflater = (LayoutInflater) this.mContext
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.list_item, null);
//        }
//
//        TextView textView = (TextView) convertView
//                .findViewById(R.id.list_item_time_text);
//
//        textView.setText(childView.toString());
//        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    private class ViewHolder {
        ImageView eventIcon;
        TextView amountText, timeText, dateText;
    }
}
