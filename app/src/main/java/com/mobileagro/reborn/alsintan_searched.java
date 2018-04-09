package com.mobileagro.reborn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.mana_livechatv2.R;
import com.mobileagro.adapter.listAlsintanAdapter;
import com.mobileagro.reborn.fetch_data.alsintan_fetch_data;

import java.util.ArrayList;

public class alsintan_searched extends AppCompatActivity implements com.mobileagro.reborn.fetch_data.alsintan_fetch_data.fetching.AsyncResponse {
    alsintan_fetch_data.fetching asyncTask;
    ListView listView;
    TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alsintan_searched);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            String keyword = b.getString("KEYWORD");
            String kabId = b.getString("KAB_ID");
            asyncTask = new alsintan_fetch_data.fetching(this);
            asyncTask.execute(keyword, kabId);
            tv1 = (TextView) findViewById(R.id.textView9);
            tv1.setText(keyword);
        }
    }

    @Override
    public void processFinish() {
        ArrayList<String> ids = alsintan_fetch_data.alsintanIds;
        ArrayList<String> titles = alsintan_fetch_data.alsintanTitles;

        listView = (ListView) findViewById(R.id.lvAlsintan);
        listView.setAdapter(null);

        ListAdapter la = new listAlsintanAdapter(this, ids, titles);
        listView.setAdapter(la);
    }
}
