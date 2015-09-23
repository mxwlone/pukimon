package com.mxwlone.pukimon.domain;

import java.util.Date;

/**
 * Created by maxwel on 9/7/2015.
 */
public abstract class Event implements Comparable<Event> {

    private Long id;
    private Date date;

    public Event(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(Event another) {

        if (this.getDate() == null || another.getDate() == null)
            return 0;

        return another.getDate().compareTo(this.getDate());
    }
}
