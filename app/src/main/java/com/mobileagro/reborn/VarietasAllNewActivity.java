package com.mobileagro.reborn;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mobileagro.VarietasDetail;
import com.mobileagro.demo1.fetchKabupaten;
import com.mobileagro.reborn.adapter.listVarietasMHAdapter;
import com.mobileagro.reborn.adapter.listVarietasMKAdapter;
import com.mobileagro.reborn.adapter.listVarietasPrefAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VarietasAllNewActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, fetchKabupaten.getKabs.AsyncResponse, fetchDataSentraProdNew.getSentraProds.AsyncResponse {
    private GoogleApiClient mGoogleApiClient;
    private AutoCompleteTextView text;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    String allVarURL = "http://202.56.170.37/mobile-agro/new/varietas_all.php";
    public static String KabId;
    String currKabId;
    String currText;
    private static TextView tvCurrent;
    private static String latStr;
    private static String lngStr;
    private String selectedKabId = "0";
    private ImageButton backButton;

    ListView lvMK;
    ListView lvMH;
    ListView lvPref;
    ListView lvMKJ;
    ListView lvMHJ;
    ListView lvPrefJ;
    ListView lvMKK;
    ListView lvMHK;
    ListView lvPrefK;
    TextView tv11;
    TextView tv12;
    TextView tv13;

    TextView tv21;
    TextView tv22;
    TextView tv23;

    TextView tv31;
    TextView tv32;
    TextView tv33;
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
        setContentView(R.layout.activity_varietas_all_new);
        tvCurrent = (TextView) findViewById(R.id.currLocNewVar);
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        fetchKabupaten.getKabs asyncRequestObject = new fetchKabupaten.getKabs(this);
        asyncRequestObject.execute();

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

        lvMK = (ListView) findViewById(R.id.listViewPDMK);
        lvMH = (ListView) findViewById(R.id.listViewPDMH);
        lvPref = (ListView) findViewById(R.id.listViewPDPref);
        lvMKJ = (ListView) findViewById(R.id.listViewJGMK);
        lvMHJ = (ListView) findViewById(R.id.listViewJGMH);
        lvPrefJ = (ListView) findViewById(R.id.listViewJGPref);
        lvMKK = (ListView) findViewById(R.id.listViewKDMK);
        lvMHK = (ListView) findViewById(R.id.listViewKDMH);
        lvPrefK = (ListView) findViewById(R.id.listViewKDPref);

        tv11 = (TextView) findViewById(R.id.tv_11);
        tv12 = (TextView) findViewById(R.id.tv_12);
        tv13 = (TextView) findViewById(R.id.tv_13);

        tv21 = (TextView) findViewById(R.id.tv_21);
        tv22 = (TextView) findViewById(R.id.tv_22);
        tv23 = (TextView) findViewById(R.id.tv_23);

        tv31 = (TextView) findViewById(R.id.tv_31);
        tv32 = (TextView) findViewById(R.id.tv_32);
        tv33 = (TextView) findViewById(R.id.tv_33);
    }
    private void fetchAllVar() {
        System.out.println("Fetch ALL arietas Running ----- ---- ----- ");
        lvMK.setAdapter(null);
        lvMH.setAdapter(null);
        lvPref.setAdapter(null);
        lvMKJ.setAdapter(null);
        lvMHJ.setAdapter(null);
        lvPrefJ.setAdapter(null);
        lvMKK.setAdapter(null);
        lvMHK.setAdapter(null);
        lvPrefK.setAdapter(null);
        tv11.setText("");
        tv12.setText("");
        tv13.setText("");

        tv21.setText("");
        tv22.setText("");
        tv23.setText("");

        tv31.setText("");
        tv32.setText("");
        tv33.setText("");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                allVarURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response!=null) {
                        JSONArray jsonarray = new JSONArray(response);
                        System.out.println("ALL Varietas Response :: " + response);

                        for (int i = 0; i < jsonarray.length(); i++) {
                            ArrayList<String> listMK = new ArrayList<>();
                            ArrayList<String> listMH = new ArrayList<>();
                            ArrayList<String> listPref = new ArrayList<>();

                            JSONObject jsonObj = jsonarray.getJSONObject(i);
                            String kabupatenId = jsonObj.getString("id_kab");
                            String var_mks = jsonObj.getString("var_mk");
                            String var_mhs = jsonObj.getString("var_mh");
                            String var_prefs = jsonObj.getString("var_pref");
                            String kode_kom = jsonObj.getString("kode_kom");

                            String[] varMks = var_mks.split(",");
                            for (int ix =0; ix< varMks.length; ix++) {
                                String varMk = varMks[ix].trim();
                                if (!varMk.equals("null"))
                                    listMK.add(varMk);
                            }
                            String[] varMHs = var_mhs.split(",");
                            for (int iy=0; iy< varMHs.length; iy++) {
                                String varMh = varMHs[iy].trim();
                                if (!varMh.equals("null"))
                                    listMH.add(varMh);
                            }
                            String[] varPrefs = var_prefs.split(",");
                            for (int iz=0; iz< varPrefs.length; iz++) {
                                String varPref = varPrefs[iz].trim();
                                if (!varPref.equals("null"))
                                    listPref.add(varPref);
                            }
                            if (kode_kom.equals("PD"))
                                fillPadi(listMK, listMH, listPref);
                            else if (kode_kom.equals("KD"))
                                fillKedelai(listMK, listMH, listPref);
                            else if (kode_kom.equals("JG"))
                                fillJagung(listMK, listMH, listPref);
                            else
                                Toast.makeText(getApplicationContext(), "Maaf, data tidak tersedia", Toast.LENGTH_LONG).show();
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
                params.put("kab_id", KabId);
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
    public void showCurrLocAllVar(View v) {
        text = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
        text.setText(null);
        KabId = currKabId;
        tvCurrent.setText(currText);
        fetchAllVar();
    }

    public void searchLocAllVar(View v) {
        fetchLocationId.getPositions asyncRequestId = new fetchLocationId.getPositions();
        fetchLocationId.setTv1(tvCurrent);
        asyncRequestId.execute(selectedKabId);
        KabId = selectedKabId;
        fetchAllVar();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    private void fillPadi(ArrayList<String> dataMK, ArrayList<String> dataMH, ArrayList<String> dataPref) {
        if (dataMK.size()>0) {
            tv11.setText("Musim Kering");
            lvMK.setAdapter(new listVarietasMKAdapter(getBaseContext(), dataMK));
            LahanNewActivityDetail.UIUtils.setListViewHeightBasedOnItems(lvMK);
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
        } else{
            tv11.setText("");
            lvMK.setAdapter(null);
        }
        if (dataMH.size()>0) {
            tv12.setText("Musim Hujan");

            lvMH.setAdapter(new listVarietasMHAdapter(getBaseContext(), dataMH));
            LahanNewActivityDetail.UIUtils.setListViewHeightBasedOnItems(lvMH);
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
        } else {
            lvMH.setAdapter(null);
            tv12.setText("");
        }
        if (dataPref.size()>0) {
            tv13.setText("Preferensi Petani");

            lvPref.setAdapter(new listVarietasPrefAdapter(getBaseContext(), dataPref));
            LahanNewActivityDetail.UIUtils.setListViewHeightBasedOnItems(lvPref);
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
        } else {
            tv13.setText("");
            lvPref.setAdapter(null);
        }
    }

    private void fillJagung(ArrayList<String> dataMK, ArrayList<String> dataMH, ArrayList<String> dataPref) {


        if (dataMK.size()>0) {
            tv21.setText("Musim Kering");

            lvMKJ.setAdapter(new listVarietasMKAdapter(getBaseContext(), dataMK));
            LahanNewActivityDetail.UIUtils.setListViewHeightBasedOnItems(lvMKJ);
            lvMKJ.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        } else {
            tv21.setText("");
            lvMKJ.setAdapter(null);
        }

        if (dataMH.size()>0) {
            tv22.setText("Musim Hujan");

            lvMHJ.setAdapter(new listVarietasMHAdapter(getBaseContext(), dataMH));
            LahanNewActivityDetail.UIUtils.setListViewHeightBasedOnItems(lvMHJ);
            lvMHJ.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        } else {
            tv22.setText("");
            lvMHJ.setAdapter(null);
        }

        if (dataPref.size()>0) {
            tv23.setText("Preferensi Petani");

            lvPrefJ.setAdapter(new listVarietasPrefAdapter(getBaseContext(), dataPref));
            LahanNewActivityDetail.UIUtils.setListViewHeightBasedOnItems(lvPrefJ);
            lvPrefJ.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        } else {
            tv23.setText("");
            lvPrefJ.setAdapter(null);
        }
    }

    private void fillKedelai(ArrayList<String> dataMK, ArrayList<String> dataMH, ArrayList<String> dataPref) {


        if (dataMK.size()>0) {
            tv31.setText("Musim Kering");

            lvMKK.setAdapter(new listVarietasMKAdapter(getBaseContext(), dataMK));
            LahanNewActivityDetail.UIUtils.setListViewHeightBasedOnItems(lvMKK);
            lvMKK.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        } else {
            tv31.setText("");
            lvMKK.setAdapter(null);
        }
        if (dataMH.size()>0) {
            tv32.setText("Musim Hujan");

            lvMHK.setAdapter(new listVarietasMHAdapter(getBaseContext(), dataMH));
            LahanNewActivityDetail.UIUtils.setListViewHeightBasedOnItems(lvMHK);
            lvMHK.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        } else {
            tv32.setText("");
            lvMHK.setAdapter(null);
        }
        if (dataPref.size()>0) {
            tv33.setText("Preferensi Petani");

            lvPrefK.setAdapter(new listVarietasPrefAdapter(getBaseContext(), dataPref));
            LahanNewActivityDetail.UIUtils.setListViewHeightBasedOnItems(lvPrefK);
            lvPrefK.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        } else {
            tv33.setText("");
            lvPrefK.setAdapter(null);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        latStr = String.valueOf(mLastLocation.getLatitude());
        lngStr = String.valueOf(mLastLocation.getLongitude());

        fetchDataSentraProdNew.setTv1(tvCurrent);
        fetchDataSentraProdNew.getSentraProds asyncRequestObject = new fetchDataSentraProdNew.getSentraProds(this);
        asyncRequestObject.execute(latStr, lngStr);
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
        currKabId = KabId;
        currText = tvCurrent.getText().toString();
        fetchAllVar();
    }
}
