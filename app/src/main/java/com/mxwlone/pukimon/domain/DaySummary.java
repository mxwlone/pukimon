package com.mxwlone.pukimon.domain;

/**
 * Created by maxwel on 22.09.2015.
 */
public class DaySummary {

    private String date;
    private int drinkTotal, eatTotal, sleepTotalMinutes;

    public DaySummary() {
    }

    public DaySummary(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDrinkTotal() {
        return drinkTotal;
    }

    public void setDrinkTotal(int drinkTotal) {
        this.drinkTotal = drinkTotal;
    }

    public int getEatTotal() {
        return eatTotal;
    }

    public void setEatTotal(int eatTotal) {
        this.eatTotal = eatTotal;
    }

    public int getSleepTotalMinutes() {
        return sleepTotalMinutes;
    }

    public void setSleepTotalMinutes(int sleepTotalMinutes) {
        this.sleepTotalMinutes = sleepTotalMinutes;
    }
}
