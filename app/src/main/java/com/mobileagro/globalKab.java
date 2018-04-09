package com.mobileagro;


import android.util.Log;

/**
 * Created by ThinkPad T440s VPro on 17/07/2016.
 */
public class globalKab {
    private static globalKab instance;
    private int data;
    private String name;
    private globalKab(){}
    public void setData(int d) {
        this.data = d;
    }
    public int getData() {
        return this.data;
    }
    public void setName(String n) {
        Log.d("GLoBAL SET NAME","GLoBAL SET NAME: " + n);
        this.name = n;
    }
    public String getName() {
        return this.name;
    }
    public static synchronized globalKab getInstance() {
        if (instance==null) {
            instance=new globalKab();
        }
        return instance;
    }
}
