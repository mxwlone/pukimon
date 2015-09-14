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
import com.mxwlone.pukimon.domain.Event;
import com.mxwlone.pukimon.domain.SleepEvent;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by maxwel on 9/12/2015.
 */
public class EventAdapter extends BaseAdapter {

    final String TAG = this.getClass().getSimpleName();

    private LayoutInflater mInflater;
    private List<Event> mEvents;

    public EventAdapter(Context context, List<Event> events) {
        mInflater = LayoutInflater.from(context);
        mEvents = events;
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
        Resources resources = parent.getContext().getResources();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(parent.getContext().getResources().getString(R.string.date_time_format));
        SimpleDateFormat dateFormat = new SimpleDateFormat(parent.getContext().getResources().getString(R.string.date_format));
        SimpleDateFormat timeFormat = new SimpleDateFormat(parent.getContext().getResources().getString(R.string.time_format));

        String amountString = "";
        String timeString = "";
        String dateString = "";
        int iconResource = 0;

        if (event instanceof DrinkEvent) {
            DrinkEvent drinkEvent = (DrinkEvent) event;
            int amount = drinkEvent.getAmount();
            amountString = String.valueOf(amount) + " ml";
            timeString = timeFormat.format(drinkEvent.getDate()) + " " + "Uhr";
            dateString = dateFormat.format(drinkEvent.getDate());
            iconResource = R.drawable.bottle;
        } else if (event instanceof SleepEvent) {
            SleepEvent sleepEvent = (SleepEvent) event;

            long diff = sleepEvent.getToDate().getTime() - sleepEvent.getFromDate().getTime();
            long hours = TimeUnit.MILLISECONDS.toHours(diff);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60;

            String hoursString = hours != 0 ?
                    hours == 1 ? String.format("%d Stunde" + " ", hours) :
                            String.format("%d Stunden" + " ", hours) :
                    "";
            String minutesString = minutes != 0 ?
                    minutes == 1 ? String.format("%d Minute", minutes) :
                            String.format("%d Minuten", minutes)
                    : hours != 0 ? "" : String.format("%d Minuten", minutes);
            amountString = hoursString + minutesString;

            timeString = timeFormat.format(sleepEvent.getFromDate()) + " - " + timeFormat.format(sleepEvent.getToDate()) + " " + "Uhr";
            dateString = dateFormat.format(sleepEvent.getFromDate());
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
