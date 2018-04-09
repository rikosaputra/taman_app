package com.mobileagro.reborn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.mana_livechatv2.R;
import com.github.mikephil.charting.charts.BarChart;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mobileagro.adapter.listSentraProdAdapter;
import com.mobileagro.reborn.adapter.listTeknoAdapter;

import java.util.ArrayList;

public class TeknologiPertanian extends AppCompatActivity {
    ArrayList<String> teknos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teknologi_pertanian);
        setList();
    }
    private void setList() {
        teknos.add("Jarwo Super");
        teknos.add("Teknik Ubian");
        teknos.add("Padi Tanam Benih Langsung");
        teknos.add("Padi Salibu");
        teknos.add("Padi Sri");
        teknos.add("Padi Sawah Irigasi");
        teknos.add("Padi Rawa Lebak");

        loadList(teknos);
    }
    private void loadList(ArrayList<String> Teks) {
        ListView lv = (ListView) findViewById(R.id.listViewTekno);
        lv.setAdapter(new listTeknoAdapter(getBaseContext(), Teks));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getAdapter().getItem(position).toString().toUpperCase();
            }
        });
    }
}
