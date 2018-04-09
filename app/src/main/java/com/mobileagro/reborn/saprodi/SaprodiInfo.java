package com.mobileagro.reborn.saprodi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.mana_livechatv2.R;
import com.example.user.mana_livechatv2.app.EndPoints;
import com.example.user.mana_livechatv2.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SaprodiInfo extends AppCompatActivity {
    private ImageButton backButton;
    String UserId = "";
    EditText NamaKontak, NamaPerusahaan, Alamat, NoTelp, Email;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            UserId = extras.getString("USERID");
        }
        setContentView(R.layout.activity_saprodi_info);
        pDialog=new ProgressDialog(this);
        pDialog.setMessage("Harap tunggu...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

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

        NamaKontak = (EditText) findViewById(R.id.nama_kontak);
        NamaPerusahaan = (EditText) findViewById(R.id.perusahaan);
        Alamat = (EditText) findViewById(R.id.alamat_form);
        NoTelp = (EditText) findViewById(R.id.contacn_form);
        Email = (EditText) findViewById(R.id.email_form);
        showDialog();
        fetchData();
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
    private void lauchSaprodiAdd() {
        Intent intent = new Intent(getBaseContext(), InputSaprodi.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void fetchData() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String getSaprodiInfo = EndPoints.GET_SAPRO;
        StringRequest strRequest = new StringRequest(Request.Method.POST, getSaprodiInfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Reponse = " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    System.out.println("Response::: " + response);
                    if (obj.getBoolean("error") == false) {
                        String NamaPT = obj.getString("nama");
                        String namaKontak = obj.getString("nama_kontak");
                        String Telp = obj.getString("telp");
                        String email = obj.getString("email");
                        String alamat = obj.getString("alamat");

                        NamaPerusahaan.setText(NamaPT);
                        NamaKontak.setText(namaKontak);
                        NoTelp.setText(Telp);
                        Email.setText(email);
                        Alamat.setText(alamat);
                        hideDialog();
                        // Toast.makeText(getApplicationContext(), "Data anda berhasil di simpan", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Masalah pada koneksi", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Masalah pada response", Toast.LENGTH_SHORT).show();
                }
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        NetworkResponse networkResponse = error.networkResponse;

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                System.out.println("SEND POST ::: Userid = " + UserId);
                params.put("userid", UserId);
                return params;
            }
        };
        queue.add(strRequest);
    }
    public void inputSaprodiClick (View view) {
        lauchSaprodiAdd();
    }
    public void saveData(View view) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String namaKontak = NamaKontak.getText().toString();
        final String namaPerusahaan = NamaPerusahaan.getText().toString();
        final String alamat = Alamat.getText().toString();
        final String email = Email.getText().toString();
        final String noTel = NoTelp.getText().toString();
        String saveSaprodiUrl = EndPoints.SAVE_SAPRO;
        StringRequest strRequest = new StringRequest(Request.Method.POST, saveSaprodiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Reponse = " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("error") == false) {
                        Toast.makeText(getApplicationContext(), "Data anda berhasil di simpan", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Masalah pada koneksi", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Masalah pada response", Toast.LENGTH_SHORT).show();
                }
            }

        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                NetworkResponse networkResponse = error.networkResponse;

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                System.out.println("SEND POST ::: Userid = " + UserId);
                params.put("userid", UserId);
                params.put("email", email);
                params.put("kontak", namaKontak);
                params.put("perusahaan", namaPerusahaan);
                params.put("alamat", alamat);
                params.put("telp", noTel);
                params.put("latitude", "0");
                params.put("longitude", "0");
                return params;
            }
        };
        queue.add(strRequest);
    }
}