package com.mobileagro;

/**
 * Created by riko on 14/06/2016.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mobileagro.adapter.dataKomoditasVarietasAdapter;
import com.example.user.mana_livechatv2.R;
import com.mobileagro.demo1.fetchDataKomoditas;


import java.util.ArrayList;

public class Tab3 extends Fragment implements fetchDataKomoditas.getKomoditas.AsyncResponse {
    public static String komoditas;
    public static String kodeKab;
    public static String KabupatenText;
    fetchDataKomoditas.getKomoditas asynTask;
    TextView Desk,Temp, Curah, Kelembab, Drainas, Tekst, Bahan, Kedalam;
    ListView listView;
    View layer1;
    static View v;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab_3,container,false);
        layer1 = v.findViewById(R.id.included);
        listView = (ListView) layer1.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = ((TextView) view.findViewById(R.id.title)).getText().toString();
                // Toast.makeText(getActivity().getBaseContext(), item, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), VarietasDetail.class);
                Bundle b = new Bundle();
                b.putString("name", item);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        return v;
    }
    public void setParam(String KabText, String KodeKab, String NamaKomoditas) {
        kodeKab = KodeKab;
        komoditas = NamaKomoditas;
        TextView tv1 = (TextView) v.findViewById(R.id.currLoc);
        TextView tv2 = (TextView) v.findViewById(R.id.detailTitle);
        ImageView imgV = (ImageView) v.findViewById(R.id.incImg);

        Desk = (TextView) v.findViewById(R.id.descDetail);
        Temp = (TextView) v.findViewById(R.id._Temperatur);
        Curah = (TextView) v.findViewById(R.id._CurahHujan);
        Kelembab = (TextView) v.findViewById(R.id._Kelembaban);
        Drainas = (TextView) v.findViewById(R.id._Drainase);
        Tekst = (TextView) v.findViewById(R.id._Tekstur);
        Bahan = (TextView) v.findViewById(R.id._BahanKasar);
        Kedalam = (TextView) v.findViewById(R.id._KedalamanTanah);

        String kodeKom = "0";
        // Log.d("Tab 3 Kabupaten --- ", KabText);
        tv1.setText(KabText);
        tv2.setText(komoditas);
        if (komoditas.equals("JAGUNG")) {
            imgV.setImageResource(R.drawable.jagung_image);
            kodeKom = "163";
        } else if (komoditas.equals("PADI SAWAH")) {
            imgV.setImageResource(R.drawable.padi_sawah_image);
            kodeKom = "302";
        } else if (komoditas.equals("KEDELAI")) {
            imgV.setImageResource(R.drawable.kedelai_image);
            kodeKom = "257";
        } else {
            
        }
        asynTask = new fetchDataKomoditas.getKomoditas(this);
        asynTask.execute(kodeKom, kodeKab, komoditas);
    }
    @Override
    public void processFinish() {
        String id = fetchDataKomoditas.Id;
        String desk = fetchDataKomoditas.Deskripsi;
        String temperature = fetchDataKomoditas.Temperature;
        String curah_hujan = fetchDataKomoditas.CurahHujan;
        String kelembaban = fetchDataKomoditas.Kelembaban;
        String drainase = fetchDataKomoditas.Drainase;
        String tekstur = fetchDataKomoditas.Tekstur;
        String bahan_kasar = fetchDataKomoditas.BahanKasar;
        String kedalaman = fetchDataKomoditas.Kedalaman;

        Desk.setText(desk);
        Temp.setText(temperature);
        Curah.setText(curah_hujan);
        Kelembab.setText(kelembaban);
        Drainas.setText(drainase);
        Tekst.setText(tekstur);
        Bahan.setText(bahan_kasar);
        Kedalam.setText(kedalaman);

        ArrayList<String> Varietases = fetchDataKomoditas.listVarietasRekom;

        layer1 = v.findViewById(R.id.included);
        listView = (ListView) layer1.findViewById(R.id.listView);
        listView.setAdapter(null);

        ListAdapter la = new dataKomoditasVarietasAdapter(layer1.getContext(), Varietases);
        int items = la.getCount();
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = items * 40;
        listView.setLayoutParams(params);
        listView.requestLayout();

        listView.setAdapter(la);
        }
    }
