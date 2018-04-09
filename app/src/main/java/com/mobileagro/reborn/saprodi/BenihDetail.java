package com.mobileagro.reborn.saprodi;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.mana_livechatv2.R;
import com.example.user.mana_livechatv2.app.MyApplication;
import com.mobileagro.reborn.getKomFromKode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BenihDetail extends AppCompatActivity {
    String kodeKom;
    int benihId;
    TextView NamaTv, DeskTv, AlamatTv, KontakTv, TelpTv, EmailTv;
    TextView NamaTitle;
    String BenihDetailURL = "http://202.56.170.37/mobile-agro/new/benih_detail.php";
    TableLayout tbl;
    TableRow tr;
    private static TextView tva;
    private static TextView tvb;
    private static TextView tvc;
    private ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benih_detail);

        Bundle b = getIntent().getExtras();
        kodeKom = b.getString("KODE_KOM");
        benihId = b.getInt("BENIH_ID");
        getKomFromKode gfk = new getKomFromKode();
        String NamaKomoditas = gfk.komoditasName(kodeKom);
        NamaTitle = (TextView) findViewById(R.id.namaKomoditas);
        NamaTitle.setText(NamaKomoditas);

        NamaTv = (TextView) findViewById(R.id.namaTv);
        AlamatTv = (TextView) findViewById(R.id.alamatTv);
        TelpTv = (TextView) findViewById(R.id.telpTv);
        KontakTv = (TextView) findViewById(R.id.kontakTv);
        tbl = (TableLayout) findViewById(R.id.tbl_01);
        tbl.setColumnStretchable(0, true);
        tbl.setColumnStretchable(1, true);
        tbl.setColumnStretchable(2, true);

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
        fetchBenihDetail();
    }
    private void fetchBenihDetail() {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                BenihDetailURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("Benih Detail Response: " + response);
                    JSONArray jsonarray = new JSONArray(response);
                    if (response != null) {
                        String nama = "";
                        String deskripsi = "";
                        String alamat = "";
                        String nama_kontak = "";
                        String telp  = "";
                        String email = "";

                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonObj = jsonarray.getJSONObject(i);
                            nama = jsonObj.getString("nama");
                            deskripsi = jsonObj.getString("deskripsi");
                            alamat = jsonObj.getString("alamat");
                            nama_kontak = jsonObj.getString("nama_kontak");
                            telp = jsonObj.getString("telp");
                            email = jsonObj.getString("email");
                            String NamaBenih = jsonObj.getString("nama_benih");
                            String StatusBenih = jsonObj.getString("status");
                            String imgUrl = jsonObj.getString("image");

                            tr = new TableRow(getBaseContext());
                            tr.setPadding(0, 6, 0, 6);
                            if (i%2==0) {
                                tr.setBackgroundColor(Color.rgb(204,255,51));
                            } else {
                                tr.setBackgroundColor(Color.WHITE);
                            }
                            tva = new TextView(getBaseContext());
                            tvb = new TextView(getBaseContext());
                            tvc = new TextView(getBaseContext());
                            tva.setText("");
                            tvb.setText(NamaBenih);
                            tvc.setText(StatusBenih);
                            tva.setTextSize(14);
                            tvb.setTextSize(14);
                            tvc.setTextSize(14);
                            tva.setTextColor(Color.BLACK);
                            tvb.setTextColor(Color.BLACK);
                            tvc.setTextColor(Color.BLACK);
                            tr.addView(tva);
                            tr.addView(tvb);
                            tr.addView(tvc);
                            tbl.addView(tr);
                        }
                        NamaTv.setText(nama);
                        AlamatTv.setText(alamat);
                        KontakTv.setText(nama_kontak);
                        TelpTv.setText(telp);
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
                params.put("kode_kom", kodeKom);
                params.put("id_benih", String.valueOf(benihId));
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
}
