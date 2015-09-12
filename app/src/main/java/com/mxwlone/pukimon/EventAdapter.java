package com.mxwlone.pukimon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by maxwel on 9/12/2015.
 */
public class EventAdapter extends BaseAdapter {

    final String TAG = this.getClass().getSimpleName().toString();

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
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        Event event = mEvents.get(position);

        if (event instanceof DrinkEvent) {
            SimpleDateFormat timeFormat = new SimpleDateFormat(parent.getContext().getResources().getString(R.string.date_time_format));
            String timeString = timeFormat.format(event.getDate());

            holder.timeText.setText(timeString);
            int amount = ((DrinkEvent) event).getAmount();
            holder.amountText.setText(String.valueOf(amount) + " ml");
            holder.eventIcon.setImageResource(R.drawable.bottle);
        }

        return view;
    }

    private class ViewHolder {
        ImageView eventIcon;
        TextView amountText, timeText;
    }
}
