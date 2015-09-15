package com.mxwlone.pukimon.domain;

import java.util.Date;

/**
 * Created by maxwel on 9/7/2015.
 */
public class DrinkEvent extends Event {

    private Date date;
    private int amount;

    public DrinkEvent(long id) {
        super(id);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
