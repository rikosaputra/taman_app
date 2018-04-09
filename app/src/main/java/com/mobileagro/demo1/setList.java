package com.mobileagro.demo1;

import android.graphics.Color;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import com.mobileagro.reborn.*;
/**
 * Created by ThinkPad T440s VPro on 21/09/2016.
 */

public class setList {

    static int[] redSeq = new int[] {255,178,255,255,153,51,51,51,51,51,153,255,255,160 };
    static int[] greenSeq = new int[] {51,255,255,255,255,255,255,255,153,51,51,51,51,160 };
    static int[] blueSeq = new int[] {51,102,102,51,51,51,153,255,255,255,255,255,153,160 };
    public static int distinctVal = 0;
    public static ArrayList<BarDataSet> getDataSet(ArrayList<String> Months, ArrayList<String> Komoditas,  ArrayList<String> KomoditasId, ArrayList<String> Sums) {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
        valueSet1.add(v1e6);

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
        valueSet2.add(v2e5);
        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
        valueSet2.add(v2e6);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Padi Sawah");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Kedelai");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet2.setColor(Color.rgb(255, 82, 82));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        /*
        int dataLen = Months.size();
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        String[] ProdArray = new String[dataLen];
        ProdArray = Sums.toArray(ProdArray);

        String[] KomoditasIdArray = new String[dataLen];
        KomoditasIdArray = KomoditasId.toArray(KomoditasIdArray);

        String[] KomoditasArray = new String[dataLen];
        KomoditasArray = Komoditas.toArray(KomoditasArray);

        int graphIndex = 0;
        BarDataSet barDataSet1 = null;
        for (int b=0; b<Months.size(); b++) {
            float f = Float.parseFloat(ProdArray[b]);
            valueSet1.add(new BarEntry(f, graphIndex));
            if (b>0) {
                if (!KomoditasIdArray[b].equals(KomoditasIdArray[b-1])||(b==Months.size()-1)) {
                    if (!KomoditasIdArray[b].equals(KomoditasIdArray[b-1]))
                        valueSet1.remove(valueSet1.size()-1);
                    System.out.println(KomoditasIdArray[b] + " - " + KomoditasIdArray[b-1]);
                    System.out.println("New Value Set  ---------------- ");
                    barDataSet1 = new BarDataSet(valueSet1, KomoditasArray[b-1]);
                    barDataSet1.setColor(Color.rgb(redSeq[b], greenSeq[b], blueSeq[b]));
                    dataSets.add(barDataSet1);
                    valueSet1 = new ArrayList<>();

                    graphIndex = 0;
                    valueSet1.add(new BarEntry(f, graphIndex));
                    graphIndex++;
                } else{
                    graphIndex++;
                }
            } else {
                graphIndex++;
            }
            // barDataSet1.setColor(Color.rgb(redSeq[b], greenSeq[b], blueSeq[b]));

        }
        */
        return dataSets;
    }
    public static ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();

        xAxis.add("2005");
        xAxis.add("2006");
        xAxis.add("2007");
        xAxis.add("2008");
        xAxis.add("2009");
        xAxis.add("2010");
        xAxis.add("2011");
        xAxis.add("2012");
        xAxis.add("2013");
        xAxis.add("2014");
        xAxis.add("2015");
        return xAxis;
    }
}
