package com.mxwlone.pukimon.domain;

import java.util.Date;

/**
 * Created by maxwel on 9/7/2015.
 */
public abstract class Event implements Comparable<Event> {

    @Override
    public int compareTo(Event another) {

        Date thisDate, anotherDate;

        thisDate = (this instanceof DrinkEvent) ? ((DrinkEvent)this).getDate() :
                this instanceof SleepEvent ? ((SleepEvent)this).getFromDate() :
                        null;

        anotherDate = (another instanceof DrinkEvent) ? ((DrinkEvent)another).getDate() :
                (another instanceof SleepEvent) ? ((SleepEvent)another).getFromDate() :
                        null;

        if (thisDate == null || anotherDate == null)
            return 0;

//        return thisDate.compareTo(anotherDate);

        // sort the other way around to achieve the desired behaviour
        // (i.e. latest events at the top of the list view)
        return anotherDate.compareTo(thisDate);
    }
}
