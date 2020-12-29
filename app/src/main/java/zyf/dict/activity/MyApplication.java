package zyf.dict.activity;

import java.util.Map;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import zyf.dict.domain.User;


public class MyApplication extends Application {

    private static MyApplication INSTANCE = null;


    public static boolean messageFlag = false;

    private User _user;

    @Override
    public void onCreate() {

        super.onCreate();

        INSTANCE = this;

    }
    public User getUser() {
        return _user;
    }

    public void setUser(User user) {
        _user = user;
    }

    public static synchronized MyApplication getInstance() {
        return INSTANCE;
    }

}
