package com.mobileagro.reborn;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.user.mana_livechatv2.R;

public class viewTekno extends AppCompatActivity {
    ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tekno);

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
        if (b != null) {
            String teknoTitle = b.getString("TEKNO");
            String teknoDoc = b.getString("TEKNO_DOC");
            TextView tv = (TextView) findViewById(R.id.textTitle);
            tv.setText(teknoTitle);
            WebView wv01 = (WebView) findViewById(R.id.webView01);
            wv01.getSettings().setJavaScriptEnabled(true);
            // wv01.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + teknoDoc);
            wv01.loadUrl(teknoDoc);
        }
    }
}
