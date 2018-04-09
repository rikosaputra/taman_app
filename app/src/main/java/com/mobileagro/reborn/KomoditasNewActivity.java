package com.mobileagro.reborn;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.mana_livechatv2.R;
import com.mobileagro.VarietasDetail;
import com.mobileagro.adapter.dataKomoditasVarietasAdapter;
import com.mobileagro.demo1.fetchDataKomoditas;

import java.util.ArrayList;

public class KomoditasNewActivity extends AppCompatActivity implements fetchDataKomoditas.getKomoditas.AsyncResponse {
    private static String komoditas;
    private static String kodeKab;
    private ImageButton backButton;
    fetchDataKomoditas.getKomoditas asynTask;
    TextView Desk,Temp, Curah, Kelembab, Drainas, Tekst, Bahan, Kedalam;
    ListView listView;
    View layer1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komoditas_new);
        layer1 = findViewById(R.id.included);
        listView = (ListView) layer1.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = ((TextView) view.findViewById(R.id.title)).getText().toString();
                // Toast.makeText(getActivity().getBaseContext(), item, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(), VarietasDetail.class);
                Bundle b = new Bundle();
                b.putString("name", item);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        backButton = (ImageButton) findViewById(R.id.back_button);
        backButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    backButton.setBackgroundColor(Color.GRAY);
                } else {
                    backButton.setBackgroundColor(Color.WHITE);
                }
                return false;
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Bundle Extras = getIntent().getExtras();
            String KomoditasTitle = Extras.getString("KOMODITAS_TITLE");
            String LocationText = Extras.getString("LOCATION");
            String KabId = Extras.getString("KAB_ID");
            setParam(LocationText, KabId, KomoditasTitle);
        }
    }
    public void setParam(String KabText, String KodeKab, String NamaKomoditas) {
        kodeKab = KodeKab;
        komoditas = NamaKomoditas;
        System.out.println("------------ Nama Komoditas: " + komoditas);
        TextView tv1 = (TextView) findViewById(R.id.currLocKomoditas);
        TextView tv2 = (TextView) findViewById(R.id.detailTitle);
        ImageView imgV = (ImageView) findViewById(R.id.incImg);

        Desk = (TextView) findViewById(R.id.descDetail);
        Temp = (TextView) findViewById(R.id._Temperatur);
        Curah = (TextView) findViewById(R.id._CurahHujan);
        Kelembab = (TextView) findViewById(R.id._Kelembaban);
        Drainas = (TextView) findViewById(R.id._Drainase);
        Tekst = (TextView) findViewById(R.id._Tekstur);
        Bahan = (TextView) findViewById(R.id._BahanKasar);
        Kedalam = (TextView) findViewById(R.id._KedalamanTanah);

        String kodeKom = "0";
        // Log.d("Tab 3 Kabupaten --- ", KabText);
        tv1.setText(KabText);
        tv2.setText(komoditas);
        if (komoditas.toUpperCase().equals("JAGUNG")) {
            imgV.setImageResource(R.drawable.jagung_image);
            kodeKom = "163";
        } else if (komoditas.toUpperCase().equals("PADI SAWAH")) {
            imgV.setImageResource(R.drawable.padi_sawah_image);
            kodeKom = "302";
        } else if (komoditas.toUpperCase().equals("KEDELAI")) {
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
