package com.mxwlone.pukimon;

import java.util.Date;

/**
 * Created by maxwel on 9/7/2015.
 */
public abstract class Event {

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
