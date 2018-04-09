package com.mobileagro.reborn;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.mana_livechatv2.R;
import com.example.user.mana_livechatv2.app.MyApplication;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mobileagro.adapter.listSentraProdAdapter;
import com.mobileagro.demo1.fetchKabupaten;
import com.mobileagro.globalKab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mobileagro.demo1.setList.getXAxisValues;

public class SentraNewActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, fetchKabupaten.getKabs.AsyncResponse, fetchDataSentraProdNew.getSentraProds.AsyncResponse {
    private AutoCompleteTextView text;
    private String selectedKabId = "0";
    private ImageButton backButton;
    private GoogleApiClient mGoogleApiClient;
    private static TextView tvCurrent;
    TextView tvLast;
    private static String latStr;
    private static String lngStr;
    private BarChart chart;
    public static String kabId;
    public static String CurrentKabId;
    fetchDataSentraProdNew.getSentraProds asynTask;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    String SentraURL = "http://202.56.170.37/mobile-agro/new/sentra_prod.php";
    static int[] redSeq = new int[] {255,0,255,0,153,51,51,51,51,51,153,255,255,160 };
    static int[] greenSeq = new int[] {51,155,255,97,255,255,255,255,153,51,51,51,51,160 };
    static int[] blueSeq = new int[] {51,80,102,255,51,51,153,255,255,255,255,255,153,160 };
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentra_new);
        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tvCurrent = (TextView) findViewById(R.id.currLocNewSentra);
        tvLast = (TextView) findViewById(R.id.lastYear);
        fetchKabupaten.getKabs asyncRequestObject = new fetchKabupaten.getKabs(this);
        asyncRequestObject.execute();

        // asynTask = new fetchDataSentraProdNew.getSentraProds(this);
        // asynTask.execute("3276");

        buildGoogleApiClient();
        mGoogleApiClient.connect();


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
    private void fetchData(final String kab_id) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                SentraURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response!=null) {
                        JSONArray jsonarray = new JSONArray(response);
                        System.out.println("Sentra Produksi Response :: " + response);
                        ArrayList<BarDataSet> dataSets = new ArrayList<>();
                        String[] Koms = new String[4];
                        String[] LastProds = new String[4];
                        // String[][] prodArray = new String[7][11];
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonObj = jsonarray.getJSONObject(i);

                            /*
                            Koms[i] = jsonObj.getString("kode_komoditas");
                            prodArray[i][0] = jsonObj.getString("p_2005");
                            prodArray[i][1] = jsonObj.getString("p_2006");
                            prodArray[i][2] = jsonObj.getString("p_2007");
                            prodArray[i][3] = jsonObj.getString("p_2008");
                            prodArray[i][4] = jsonObj.getString("p_2009");
                            prodArray[i][5] = jsonObj.getString("p_2010");
                            */
                            ArrayList<BarEntry> valueSet1 = new ArrayList<>();
                            BarEntry v1e1 = new BarEntry(Float.valueOf(jsonObj.getString("p_2005")), 0);
                            valueSet1.add(v1e1);
                            BarEntry v1e2 = new BarEntry(Float.valueOf(jsonObj.getString("p_2006")), 1);
                            valueSet1.add(v1e2);
                            BarEntry v1e3 = new BarEntry(Float.valueOf(jsonObj.getString("p_2007")), 2);
                            valueSet1.add(v1e3);
                            BarEntry v1e4 = new BarEntry(Float.valueOf(jsonObj.getString("p_2008")), 3);
                            valueSet1.add(v1e4);
                            BarEntry v1e5 = new BarEntry(Float.valueOf(jsonObj.getString("p_2009")), 4);
                            valueSet1.add(v1e5);
                            BarEntry v1e6 = new BarEntry(Float.valueOf(jsonObj.getString("p_2010")), 5);
                            valueSet1.add(v1e6);
                            BarEntry v1e7 = new BarEntry(Float.valueOf(jsonObj.getString("p_2011")), 6);
                            valueSet1.add(v1e7);
                            BarEntry v1e8 = new BarEntry(Float.valueOf(jsonObj.getString("p_2012")), 7);
                            valueSet1.add(v1e8);
                            BarEntry v1e9 = new BarEntry(Float.valueOf(jsonObj.getString("p_2013")), 8);
                            valueSet1.add(v1e9);
                            BarEntry v1e10 = new BarEntry(Float.valueOf(jsonObj.getString("p_2014")), 9);
                            valueSet1.add(v1e10);
                            BarEntry v1e11 = new BarEntry(Float.valueOf(jsonObj.getString("p_2015")), 10);
                            valueSet1.add(v1e11);
                            String kodeKom = jsonObj.getString("kode_komoditas");
                            String Komoditas = "";
                            switch (kodeKom) {
                                case "JG":
                                    Komoditas = "Jagung";
                                    break;
                                case "KD":
                                    Komoditas = "Kedelai";
                                    break;
                                case "PG":
                                    Komoditas = "Padi Gogo";
                                    break;
                                case "SI":
                                    Komoditas = "Padi Sawah";
                                    break;
                                case "SL":
                                    Komoditas = "Padi Sawah Lebak";
                                    break;
                                case "SP":
                                    Komoditas = "Padi Sawah Pasang Surut";
                                    break;
                                default:
                                    Komoditas = "";
                            }
                            BarDataSet barDataSet1 = new BarDataSet(valueSet1, Komoditas);
                            barDataSet1.setColor(Color.rgb(redSeq[i], greenSeq[i], blueSeq[i]));
                            System.out.println("Komoditas:: " + Komoditas);
                            Koms[i] = Komoditas;
                            LastProds[i] = jsonObj.getString("p_2015");
                            System.out.println("LAST PROD ::: " + LastProds[i]);
                            dataSets.add(barDataSet1);
                        }

                        /*
                        for (int x = 0; x<=3; x++) {
                            ArrayList<BarEntry> valueSet1 = new ArrayList<>();
                            BarEntry v1e1 = new BarEntry(
                        }
                        */
                        chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(getXAxisValues(), dataSets);
                        if (dataSets!=null) {
                            chart.setData(data);
                            // chart.setDescription("Data Sentra Produksi");
                            chart.animateXY(3000, 3000);
                            chart.invalidate();
                            tvLast.setText("Produksi Tahun 2015");
                            loadList(Koms, LastProds);

                        } else {
                            Toast.makeText(getApplicationContext(), "Data tidak ditemukan", Toast.LENGTH_LONG).show();
                            tvLast.setText("Data tidak ditemukan");
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
                globalKab gKab = globalKab.getInstance();
                System.out.println("KabId = " + kab_id);
                params.put("kab_id", kab_id);
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
    /*
    @Override
    public void processFinish() {
        ArrayList<String> Months = fetchDataSentraProdNew.months;
        ArrayList<String> Komoditas = fetchDataSentraProdNew.komoditases;
        ArrayList<String> KomoditasId = fetchDataSentraProdNew.id_komoditases;
        ArrayList<String> Productions = fetchDataSentraProdNew.prods;
        chart = (BarChart) findViewById(R.id.chart);
        BarData data = new BarData(getXAxisValues(Months), getDataSet(Months, Komoditas, KomoditasId, Productions));

        chart.setData(data);
        chart.setDescription("Data Sentra Produksi");
        chart.animateXY(3000, 3000);
        chart.invalidate();
        loadList();
    }
    */
    private void loadList(String[] Koms, String[] Prods) {

        ListView lv = (ListView) findViewById(R.id.listViewSentra);
        lv.setAdapter(new listSentraProdAdapter(getBaseContext(), Koms, Prods));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getAdapter().getItem(position).toString().toUpperCase();
                String LocationTitle = tvCurrent.getText().toString();
                /*
                Intent intent = new Intent(getBaseContext(), KomoditasNewActivity.class);
                intent.putExtra("KOMODITAS_TITLE", selectedItem);
                intent.putExtra("LOCATION", LocationTitle);
                // globalKab gKab = globalKab.getInstance();
                // kabId = String.valueOf(gKab.getData());
                intent.putExtra("KAB_ID", kabId);
                startActivity(intent);
                */
                String KodeKom = "";
                switch (selectedItem) {
                    case "JAGUNG":
                        KodeKom = "JG";
                        break;
                    case "KEDELAI":
                        KodeKom = "KD";
                        break;
                    case "PADI GOGO":
                        KodeKom = "PG";
                        break;
                    case "SAWAH IRIGASI":
                        KodeKom = "SI";
                        break;
                    case "SAWAH LEBAK":
                        KodeKom = "SL";
                        break;
                    case "SAWAH PASANG SURUT":
                        KodeKom = "SP";
                        break;
                    case "PADI SAWAH":
                        KodeKom = "SI";
                        break;
                    default:
                        KodeKom = "";
                }
                Intent intent = new Intent(getBaseContext(), LahanNewActivityDetail.class);
                intent.putExtra("KAB_ID", kabId);
                intent.putExtra("KODE_KOM", KodeKom);
                intent.putExtra("LOC_TEXT", LocationTitle);
                System.out.println("KODE KOM : " + KodeKom);
                System.out.println("KODE KAB ID : " + kabId);
                startActivity(intent);
            }
        });

    }

    public void showCurrLoc(View v) {
        text = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
        text.setText(null);
        fetchDataSentraProdNew.setTv1(tvCurrent);
        fetchDataSentraProdNew.getSentraProds asyncRequestObject = new fetchDataSentraProdNew.getSentraProds(this);
        asyncRequestObject.execute(latStr, lngStr);
        fetchData(kabId);
        // asynTask = new fetchDataSentraProdNew.getSentraProds(this);
        // asynTask.execute();
        /*
        BarData data = new BarData(getXAxisValues(), getDataSet());

        chart.setData(data);
        chart.setDescription("Data Sentra Produksi");
        chart.animateXY(3000, 3000);
        chart.invalidate();
        *****/
    }

    public void searchLoc(View v) {
        fetchLocationId.getPositions asyncRequestId = new fetchLocationId.getPositions();
        fetchLocationId.setTv1(tvCurrent);
        asyncRequestId.execute(selectedKabId);
        kabId = selectedKabId;
        fetchData(selectedKabId);
        /*
        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("Data Sentra Produksi");
        chart.animateXY(3000, 3000);
        chart.invalidate();
        */
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {

            fetchDataSentraProdNew.setTv1(tvCurrent);
            fetchDataSentraProdNew.getSentraProds asyncRequestObject = new fetchDataSentraProdNew.getSentraProds(this);

            latStr = String.valueOf(mLastLocation.getLatitude());
            lngStr = String.valueOf(mLastLocation.getLongitude());
            asyncRequestObject.execute(latStr, lngStr);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void processFinish(final ArrayList<String> output, final ArrayList<String> outputIds) {
        // Toast.makeText(this, "- Load Kabupated Data -", Toast.LENGTH_LONG).show();
        text = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,output);
        text.setAdapter(adapter);
        text.setThreshold(1);
        text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
                String[] idArray = new String[outputIds.size()];
                idArray = outputIds.toArray(idArray);
                String data = (String) parent.getItemAtPosition(position);
                int realPos = output.indexOf(data);
                selectedKabId = idArray[realPos];
            }
        });
    }

    @Override
    public void processSentraFinish() {
        fetchData(kabId);
    }
}
