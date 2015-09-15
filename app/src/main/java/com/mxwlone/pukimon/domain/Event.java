package com.mxwlone.pukimon.domain;

import java.util.Date;

/**
 * Created by maxwel on 9/7/2015.
 */
public abstract class Event implements Comparable<Event> {

    Long id;

    public Event(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public int compareTo(Event another) {

        Date thisDate, anotherDate;

        thisDate = (this instanceof DrinkEvent) ? ((DrinkEvent)this).getDate() :
                this instanceof SleepEvent ? ((SleepEvent)this).getToDate() :
                        null;

        anotherDate = (another instanceof DrinkEvent) ? ((DrinkEvent)another).getDate() :
                (another instanceof SleepEvent) ? ((SleepEvent)another).getToDate() :
                        null;

        if (thisDate == null || anotherDate == null)
            return 0;

//        return thisDate.compareTo(anotherDate);

        // sort the other way around to achieve the desired behaviour
        // (i.e. latest events at the top of the list view)
        return anotherDate.compareTo(thisDate);
    }
}
