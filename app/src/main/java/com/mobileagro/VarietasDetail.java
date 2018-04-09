package com.mobileagro;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.mana_livechatv2.R;
import com.mobileagro.demo1.fetchVarietasDetail;

public class VarietasDetail extends AppCompatActivity {
    fetchVarietasDetail.getVarietas  asyncTask;
    TextView tvVarietas, tvKomoditas, tvDesc;
    private ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varietas_detail);
        tvVarietas = (TextView) findViewById(R.id.varietasTitle);
        tvKomoditas = (TextView) findViewById(R.id.komoditasTitle);
        tvDesc = (TextView) findViewById(R.id.textDesc);
        /*
        ImageButton BackButton = (ImageButton) findViewById(R.id.backButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                /*
                this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
                 //*
            }
        });
        */
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

        Bundle b = getIntent().getExtras();
        String Name = "";
        if (b!=null) {
            Name = b.getString("name");
            asyncTask = new fetchVarietasDetail.getVarietas(new fetchVarietasDetail.getVarietas.AsyncResponse() {
                @Override
                public void processFinish() {
                    tvVarietas.setText(fetchVarietasDetail.NamaVarietas);
                    tvKomoditas.setText(fetchVarietasDetail.Komditas);
                    tvDesc.setText(fetchVarietasDetail.Keterangan);
                    ImageView imgv = (ImageView) findViewById(R.id.imageView2);
                    String varietasId = fetchVarietasDetail.VarietasId;
                    // String URl = "http://202.56.170.37/mobile-agro/varietas/img/"+fetchVarietasDetail.ImgId+".png";

                    // String URl = "http://new.litbang.pertanian.go.id/varietas/"+varietasId+"/foto";
                    String URl = "http://www.litbang.pertanian.go.id/varietas/one/"+varietasId+"/image";
                    System.out.println("URL : " + URl);
                    Glide.with(VarietasDetail.this).load(URl).into(imgv);
                }
            });
            asyncTask.execute(Name);
        }
    }
}
