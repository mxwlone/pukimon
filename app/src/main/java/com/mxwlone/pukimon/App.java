package com.mxwlone.pukimon;

import android.app.Application;
import android.content.Context;

/**
 * This class enables to get the context object from outside of activities,
 * to access resources for instance.
 */
public class App extends Application{

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}