package com.mxwlone.pukimon;

import java.util.Date;

@Deprecated
public class MilkEvent {

    private Date date;
    private double amount;

    public MilkEvent(Date date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

