package com.mobileagro.reborn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.user.mana_livechatv2.R;
import com.mobileagro.globalKab;

public class alsintan_new extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alsintan_new);
    }
    public void daftarClicked(View v) {
        Intent intent = new Intent(getBaseContext(), com.mobileagro.reborn.alsintan.alsintan_main.class);
        startActivity(intent);
    }
    public void cariClicked(View v) {
        String keyword = ((TextView) findViewById(R.id.input_name)).getText().toString();
        globalKab gKab = globalKab.getInstance();
        String kabId = String.valueOf(gKab.getData());
        Intent intent = new Intent(getBaseContext(), alsintan_searched.class);

        Bundle b = new Bundle();
        b.putString("KEYWORD", keyword);
        b.putString("KAB_ID", kabId);
        intent.putExtras(b);
        startActivity(intent);
    }
}
