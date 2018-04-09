package com.mobileagro.demo1;

import java.util.ArrayList;

/**
 * Created by ThinkPad T440s VPro on 17/10/2016.
 */

public class globalSentra {
    private static globalSentra instance;
    private ArrayList<String> komoditas, prodTahunan;
    private globalSentra(){}
    public void setKomSentra(ArrayList<String> d) { this.komoditas = d; }
    public ArrayList<String> getKomSentra() {
        return this.komoditas;
    }

    public void setProd(ArrayList<String> d) {
        this.prodTahunan = d;
    }
    public ArrayList<String> getProd() {
        return this.prodTahunan;
    }

    public static synchronized globalSentra getInstance() {
        if (instance==null) {
            instance=new globalSentra();
        }
        return instance;
    }
}