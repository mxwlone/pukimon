package com.mxwlone.pukimon.domain;

import java.util.Date;

/**
 * Created by maxwel on 9/7/2015.
 */
public class SleepEvent extends Event {

    private Date fromDate, toDate;

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
}
