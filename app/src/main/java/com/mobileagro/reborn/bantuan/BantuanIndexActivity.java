package com.mobileagro.reborn.bantuan;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.user.mana_livechatv2.R;

public class BantuanIndexActivity extends AppCompatActivity {
    private ImageButton backButton;
    TextView tv1, tv2, tv3, tv4, tv5, tv6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan_index);
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
        tv1 = (TextView) findViewById(R.id.textView10);
        tv2 = (TextView) findViewById(R.id.textView11);
        tv3 = (TextView) findViewById(R.id.textView12);
        tv4 = (TextView) findViewById(R.id.textView13);
        tv5 = (TextView) findViewById(R.id.textView14);
        tv6 = (TextView) findViewById(R.id.textView15);

        tv1.setPaintFlags(tv1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv2.setPaintFlags(tv2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv3.setPaintFlags(tv3.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv4.setPaintFlags(tv4.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv5.setPaintFlags(tv5.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv6.setPaintFlags(tv6.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }
    public void bantuanSentraClick(View v) {
        Intent intent = new Intent(BantuanIndexActivity.this, BantuanSentraProduksi.class);
        startActivity(intent);
    }
    public void bantuanKonsulClick(View v) {
        Intent intent = new Intent(BantuanIndexActivity.this, BantuanKonsultasi.class);
        startActivity(intent);
    }
    public void bantuanProfilClick(View v) {
        Intent intent = new Intent(BantuanIndexActivity.this, BantuanProfil.class);
        startActivity(intent);
    }
    public void bantuanTentang(View v) {
        Intent intent = new Intent(BantuanIndexActivity.this, BantuanTentang.class);
        startActivity(intent);
    }
    public void bantuanLahanClick(View v) {
        Intent intent = new Intent(BantuanIndexActivity.this, BantuanLahan.class);
        startActivity(intent);
    }
    public void bantuanVarietasClick(View v) {
        Intent intent = new Intent(BantuanIndexActivity.this, BantuanVarietas.class);
        startActivity(intent);
    }
}
