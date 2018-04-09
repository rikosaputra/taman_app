package com.mobileagro.reborn.bantuan;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.mana_livechatv2.R;
import com.example.user.mana_livechatv2.app.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BantuanTentang extends AppCompatActivity {
    ImageButton backButton;
    String aboutUrl = "http://202.56.170.37/mobile-agro/new/tentang.php";
    TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan_tentang);
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
        // tv1 = (TextView) findViewById(R.id.textView10);
        // fetchText();
    }
    private void fetchText() {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                aboutUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("Benih Detail Response: " + response);
                    JSONArray jsonarray = new JSONArray(response);
                    if (response != null) {
                        String deskripsi = "";
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonObj = jsonarray.getJSONObject(i);
                            deskripsi = jsonObj.getString("text_content");
                        }
                        tv1.setText(deskripsi);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                System.out.println("NETWROK ERROR" + networkResponse);
                Toast.makeText(getApplicationContext(), "Internet Anda bermasalah, silahkan coba lagi", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
}
