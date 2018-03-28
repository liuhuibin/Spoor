package com.spoor.example;

import android.app.Application;

import com.spoor.Spoor;

/**
 * Desc:
 * Author:Created by huibin
 * Date: Created on 18/3/28 11:01
 */
public class SpoorApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Spoor.init(getApplicationContext());
    }

}
