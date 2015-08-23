package vn.minhtdh.demo;

import android.app.Application;

import vn.minhtdh.demo.db.DbHelper;

/**
 * Created by minhtdh on 8/23/15.
 */
public class ConferenceApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DbHelper.initialIns(this);
    }
}
