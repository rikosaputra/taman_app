package com.mobileagro.demo1;

import java.util.ArrayList;

/**
 * Created by ThinkPad T440s VPro on 18/07/2016.
 */
public class globalLahan {
    private static globalLahan instance;
    private ArrayList<String> dataKomoditas, persenKena, avgLuas;
    private globalLahan(){}
    public void setKom(ArrayList<String> d) {
        System.out.println("SET GLOBAL VALUE ========= ");
        this.dataKomoditas = d;
    }
    public ArrayList<String> getKom() {
        return this.dataKomoditas;
    }

    public void setPersen(ArrayList<String> d) {
        this.persenKena = d;
    }
    public ArrayList<String> getPersen() {
        return this.persenKena;
    }

    public void setAvg(ArrayList<String> d) {
        this.avgLuas = d;
    }
    public ArrayList<String> getAvg() {
        return this.avgLuas;
    }
    public static synchronized globalLahan getInstance() {
        if (instance==null) {
            instance=new globalLahan();
        }
        return instance;
    }
}
