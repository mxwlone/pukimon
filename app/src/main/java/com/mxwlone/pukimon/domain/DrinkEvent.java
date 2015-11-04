package com.mxwlone.pukimon.domain;

/**
 * Created by maxwel on 9/7/2015.
 */
public class DrinkEvent extends Event {

    private int amount;

    public DrinkEvent(long id) {
        super(id);
    }

    public int getAmount() { return amount; }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
