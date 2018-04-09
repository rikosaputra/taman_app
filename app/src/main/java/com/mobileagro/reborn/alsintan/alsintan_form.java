package com.mobileagro.reborn.alsintan;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class alsintan_form extends AppCompatActivity {
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alsintan_form);
        ll = (LinearLayout) findViewById(R.id.linearLayout);
        requestData();
    }
    public void sendData(View view) {
        for(int i = 0; i < ll.getChildCount(); i++) {
            View child = ll.getChildAt(i);
            if (child instanceof CheckBox) {
                CheckBox cb = (CheckBox) child;
                int checked = cb.isChecked() ? 1 : 0;

            }
        }
    }
    private void requestData() {
        StringRequest strReq = new StringRequest(Request.Method.POST,
            "http://202.56.170.37/mobile-agro/new/komoditas.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonarray = new JSONArray(response);
                    if (response!=null) {
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonObj = jsonarray.getJSONObject(i);
                            String id = jsonObj.getString("komoditas_id");
                            final String KomoditasName = jsonObj.getString("nama_komoditas");
                            System.out.println(KomoditasName);
                            CheckBox cb = new CheckBox(getApplicationContext());
                            cb.setText(KomoditasName);
                            cb.setId(Integer.parseInt(id));
                            cb.setTextColor(Color.BLACK);
                            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    System.out.println(KomoditasName);
                                }
                            });
                            ll.addView(cb);
                        }
                        EditText editText = new EditText(getBaseContext());
                        editText.setText("Nama Alsintan");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
            new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Toast.makeText(getApplicationContext(), "Internet Anda bermasalah, silahkan coba lagi", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
    private void createForm() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayout);
        for (int i=0; i<10; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText("Nama Apa");
            cb.setTextColor(Color.BLACK);
            cb.setId(i);
            ll.addView(cb);
        }
    }
}
