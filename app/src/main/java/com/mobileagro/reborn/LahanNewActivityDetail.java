package com.mobileagro.reborn;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.mobileagro.VarietasDetail;
import com.mobileagro.reborn.adapter.listVarietasMHAdapter;
import com.mobileagro.reborn.adapter.listVarietasMKAdapter;
import com.mobileagro.reborn.adapter.listVarietasPrefAdapter;
import com.mobileagro.reborn.adapter.listViewTekno;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LahanNewActivityDetail extends AppCompatActivity  {
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private static GoogleMap map;
    private static TextView tva;
    private static TextView tvb;
    private static TextView tv1;
    private static TextView KomoditasText;
    private String selectedKabId = "0";
    public static String kabId;
    // private Context mContext;
    private static String latStr;
    private static String lngStr;
    private ImageButton backButton;
    String KabId;
    String KodeKom;
    private AutoCompleteTextView text;
    TableLayout tbl;
    TableRow tr;
    ArrayList<String> Kecamatans = new ArrayList<>();
    ArrayList<String> Keslahs = new ArrayList<>();
    String KeslahDetailURL = "http://202.56.170.37/mobile-agro/new/keslah_detail.php";
    String KesVarURL = "http://202.56.170.37/mobile-agro/new/kes_varietas.php";
    String teknikURL = "http://202.56.170.37/mobile-agro/new/tekno.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lahan_new_detail);
        tv1 = (TextView) findViewById(R.id.currLocNew);
        KomoditasText = (TextView)findViewById(R.id.komoditasTitle);
        tbl = (TableLayout) findViewById(R.id.tbl_01);
        tbl.setColumnStretchable(0, true);
        tbl.setColumnStretchable(1, true);
        ImageView imgV = (ImageView) findViewById(R.id.imageView2);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            System.out.println("Bundle Not NULL");
            KabId = b.getString("KAB_ID");
            KodeKom = b.getString("KODE_KOM");
            tv1.setText(b.getString("LOC_TEXT"));
            String komText = "";
            switch (KodeKom) {
                case "SI":
                    komText = "Sawah Irigasi";
                    imgV.setImageResource(R.drawable.padi_sawah_image);
                    break;
                case "SP":
                    komText = "Sawah Pasang Surut";
                    imgV.setImageResource(R.drawable.padi_sawah_image);
                    break;
                case "SL":
                    komText = "Sawah Lebak";
                    imgV.setImageResource(R.drawable.padi_sawah_image);
                    break;
                case "SH":
                    komText = "Sawah Tadah Hujan";
                    imgV.setImageResource(R.drawable.padi_sawah_image);
                    break;
                case "PG":
                    komText = "Padi Gogo";
                    imgV.setImageResource(R.drawable.padi_sawah_image);
                    break;
                case "KD":
                    komText = "Kedelai";
                    imgV.setImageResource(R.drawable.kedelai_image);
                    break;
                case "JG":
                    komText = "Jagung";
                    imgV.setImageResource(R.drawable.jagung_image);
                    break;
            }
            KomoditasText.setText(komText);
            fetchData();
            fetchTeknik();
        }
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
    }
    private void fetchData() {
        System.out.println("Fetch Data Running ----- ---- ----- ");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                KeslahDetailURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response!=null) {
                        JSONArray jsonarray = new JSONArray(response);
                        System.out.println("Activity DETAIL Response :: " + response);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonObj = jsonarray.getJSONObject(i);
                            String Kecamatan = jsonObj.getString("kecamatan");
                            String Keslah = jsonObj.getString("keslah");

                            tr = new TableRow(getBaseContext());
                            tr.setPadding(6, 6, 6, 6);
                            if (i%2==0) {
                                tr.setBackgroundColor(Color.rgb(204,255,51));
                            } else {
                                tr.setBackgroundColor(Color.WHITE);
                            }
                            tva = new TextView(getBaseContext());
                            tvb = new TextView(getBaseContext());
                            tva.setText(Kecamatan);
                            tvb.setText(Keslah);
                            tva.setTextSize(14);
                            tvb.setTextSize(14);
                            tva.setTextColor(Color.BLACK);
                            tvb.setTextColor(Color.BLACK);
                            tva.setGravity(Gravity.LEFT);
                            tvb.setGravity(Gravity.CENTER);
                            tr.addView(tva);
                            tr.addView(tvb);
                            tbl.addView(tr);
                        }
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
                    System.out.println("NETWROK ERROR" +  networkResponse);
                    Toast.makeText(getApplicationContext(), "Internet Anda bermasalah, silahkan coba lagi", Toast.LENGTH_SHORT).show();
                }
            }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                System.out.println("KabId = " + KabId);
                System.out.println("Kode Kom = " + KodeKom);
                params.put("kab_id", KabId);
                params.put("kode_kom", KodeKom);
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
        fetchVarietas();
    }
    private void fetchVarietas() {
        System.out.println("Fetch Varietas Running ----- ---- ----- ");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                KesVarURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response!=null) {
                        JSONArray jsonarray = new JSONArray(response);
                        System.out.println("Kes Varietas Response :: " + response);
                        ArrayList<String> listMK = new ArrayList<>();
                        ArrayList<String> listMH = new ArrayList<>();
                        ArrayList<String> listPref = new ArrayList<>();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonObj = jsonarray.getJSONObject(i);
                            String kabupatenId = jsonObj.getString("id_kab");
                            String kabupaten = jsonObj.getString("kabupaten");
                            String var_mks = jsonObj.getString("var_mk");
                            String var_mhs = jsonObj.getString("var_mh");
                            String var_prefs = jsonObj.getString("var_pref");
                            String[] varMks = var_mks.split(",");
                            for (int ix =0; ix< varMks.length; ix++) {
                                String varMk = varMks[ix].trim();
                                listMK.add(varMk);
                            }
                            String[] varMHs = var_mhs.split(",");
                            for (int iy=0; iy< varMHs.length; iy++) {
                                String varMh = varMHs[iy].trim();
                                listMH.add(varMh);
                            }
                            String[] varPrefs = var_prefs.split(",");
                            for (int iz=0; iz< varPrefs.length; iz++) {
                                String varPref = varPrefs[iz].trim();
                                listPref.add(varPref);
                            }
                        }
                        ListView lvMK = (ListView) findViewById(R.id.listViewVarMK);
                        lvMK.setAdapter(new listVarietasMKAdapter(getBaseContext(),listMK));
                        UIUtils.setListViewHeightBasedOnItems(lvMK);
                        lvMK.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String item = ((TextView) view.findViewById(R.id.title)).getText().toString();
                                Intent intent = new Intent(getBaseContext(), VarietasDetail.class);
                                Bundle b = new Bundle();
                                b.putString("name", item);
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        });

                        ListView lvMH = (ListView) findViewById(R.id.listViewVarMH);
                        lvMH.setAdapter(new listVarietasMHAdapter(getBaseContext(), listMH));
                        UIUtils.setListViewHeightBasedOnItems(lvMH);
                        lvMH.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String item = ((TextView) view.findViewById(R.id.title)).getText().toString();
                                Intent intent = new Intent(getBaseContext(), VarietasDetail.class);
                                Bundle b = new Bundle();
                                b.putString("name", item);
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        });

                        ListView lvPref = (ListView) findViewById(R.id.listViewVarPref);
                        lvPref.setAdapter(new listVarietasPrefAdapter(getBaseContext(), listPref));
                        UIUtils.setListViewHeightBasedOnItems(lvPref);
                        lvPref.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String item = ((TextView) view.findViewById(R.id.title)).getText().toString();
                                Intent intent = new Intent(getBaseContext(), VarietasDetail.class);
                                Bundle b = new Bundle();
                                b.putString("name", item);
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        });
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
                        System.out.println("NETWROK ERROR" +  networkResponse);
                        Toast.makeText(getApplicationContext(), "Internet Anda bermasalah, silahkan coba lagi", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                System.out.println("KabId = " + KabId);
                System.out.println("Kode Kom = " + KodeKom);
                params.put("kab_id", KabId);
                params.put("kode_kom", KodeKom);
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
    private void fetchTeknik() {
        System.out.println("Fetch Teknik Running ----- ---- ----- ");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                teknikURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response!=null) {
                        JSONArray jsonarray = new JSONArray(response);
                        System.out.println("Fetch Teknik Response :: " + response);

                        ArrayList<String> TeknikTani = new ArrayList<>();
                        ArrayList<String> TeknikDesc = new ArrayList<>();
                        final ArrayList<String> TeknikDoc = new ArrayList<>();
                        for (int i = 0; i < jsonarray.length(); i++) {

                            JSONObject jsonObj = jsonarray.getJSONObject(i);
                            String teknikTani = jsonObj.getString("teknik_tani");
                            String teknikDesc = jsonObj.getString("teknik_desc");
                            String teknikDoc = jsonObj.getString("teknik_doc");

                            TeknikTani.add(teknikTani);
                            TeknikDoc.add(teknikDoc);
                        }
                        final ListView lvTek = (ListView) findViewById(R.id.listViewTekno);
                        lvTek.setAdapter(new listViewTekno(getBaseContext(), TeknikTani, TeknikDoc));
                        UIUtils.setListViewHeightBasedOnItems(lvTek);
                        lvTek.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String item = ((TextView) view.findViewById(R.id.title)).getText().toString();
                                String Doc = TeknikDoc.get(position);
                                System.out.println("DOKUMEN TEKNIK ::: " + Doc);

                                Intent intent = new Intent(getBaseContext(), viewTekno.class);
                                Bundle b = new Bundle();
                                b.putString("TEKNO", item);
                                b.putString("TEKNO_DOC", Doc);
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        });
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
                        System.out.println("NETWROK ERROR" +  networkResponse);
                        Toast.makeText(getApplicationContext(), "Internet Anda bermasalah, silahkan coba lagi", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("kab_id", KabId);
                params.put("kode_kom", KodeKom);
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
    public static class UIUtils {

        /**
         * Sets ListView height dynamically based on the height of the items.
         *
         * @param listView to be resized
         * @return true if the listView is successfully resized, false otherwise
         */
        public static boolean setListViewHeightBasedOnItems(ListView listView) {

            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter != null) {

                int numberOfItems = listAdapter.getCount();

                // Get total height of all items.
                int totalItemsHeight = 0;
                for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                    View item = listAdapter.getView(itemPos, null, listView);
                    item.measure(0, 0);
                    totalItemsHeight += item.getMeasuredHeight();
                }

                // Get total height of all item dividers.
                int totalDividersHeight = listView.getDividerHeight() *
                        (numberOfItems - 1);

                // Set list height.
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = totalItemsHeight + totalDividersHeight;
                listView.setLayoutParams(params);
                listView.requestLayout();

                return true;

            } else {
                return false;
            }

        }
    }
}
