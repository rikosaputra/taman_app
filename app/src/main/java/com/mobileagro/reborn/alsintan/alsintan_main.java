package com.mobileagro.reborn.alsintan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.mana_livechatv2.LoginActivity;
import com.example.user.mana_livechatv2.R;

public class alsintan_main extends AppCompatActivity {
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alsintan_main);
        // if (MyApplication.getInstance().getPrefManager().getUser() == null) {
            launchLoginActivity();
        // }
        /*
        pDialog=new ProgressDialog(this);
        pDialog.setMessage("Mengambil data...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showDialog();
        */
    }
    private void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(alsintan_main.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle b = new Bundle();
        b.putString("REFERER", "ALSINTAN");
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }
}
