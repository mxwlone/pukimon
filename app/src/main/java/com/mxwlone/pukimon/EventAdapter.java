package com.mxwlone.pukimon;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxwlone.pukimon.domain.DrinkEvent;
import com.mxwlone.pukimon.domain.EatEvent;
import com.mxwlone.pukimon.domain.Event;
import com.mxwlone.pukimon.domain.SleepEvent;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by maxwel on 9/12/2015.
 */
public class EventAdapter extends BaseAdapter {

    final String TAG = this.getClass().getSimpleName();

    private LayoutInflater mInflater;
    private List<Event> mEvents;

    Locale LOCALE;
    DateFormat TIME_FORMAT, DATE_FORMAT, DATE_TIME_FORMAT;

    public EventAdapter(Context context, List<Event> events) {
        mInflater = LayoutInflater.from(context);
        mEvents = events;

        LOCALE = context.getResources().getConfiguration().locale;
        TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.SHORT, LOCALE);
        DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT, LOCALE);
        DATE_TIME_FORMAT = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, LOCALE);
    }

    @Override
    public int getCount() {
        return mEvents.size();
    }

    @Override
    public Object getItem(int position) {
        return mEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

        Event event = mEvents.get(position);

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
    }

    private class ViewHolder {
        ImageView eventIcon;
        TextView amountText, timeText, dateText;
    }
}
