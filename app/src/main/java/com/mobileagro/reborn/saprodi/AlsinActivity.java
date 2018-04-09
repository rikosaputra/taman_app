package com.mobileagro.reborn.saprodi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobileagro.demo1.fetchDataLahanNew;
import com.mobileagro.demo1.fetchKabupaten;
import com.mobileagro.reborn.getKomFromKode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AlsinActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, fetchDataLahanNew.getPositions.AsyncFetchData, GoogleApiClient.OnConnectionFailedListener, LocationListener, fetchKabupaten.getKabs.AsyncResponse {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private SupportMapFragment fragment;
    private static GoogleMap map;
    GoogleApiClient mGoogleApiClient;
    AutoCompleteTextView text;
    private String selectedKabId = "0";
    private static String latStr;
    private static String lngStr;
    TextView tv1;
    TextView tvKom;
    LatLng latLng;
    Marker currLocationMarker;
    String AlsinURL = "http://202.56.170.37/mobile-agro/new/alsin.php";
    String kodeKom;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    public static String AlsinKabId;
    private ImageButton backButton;

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
        setContentView(R.layout.activity_alsin);

        Bundle b = getIntent().getExtras();
        kodeKom = b.getString("KODE_KOM");
        System.out.print("Kode Komoditas ===>  " + kodeKom);
        getKomFromKode getKom = new getKomFromKode();
        String namaKomoditas = getKom.komoditasName(kodeKom);

        tvKom = (TextView) findViewById(R.id.textKomoditas);
        tvKom.setText(namaKomoditas);
        tv1 = (TextView) findViewById(R.id.currLocNew);

        FragmentManager fm = getSupportFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_container, fragment).commit();
        }
        fragment.getMapAsync(this);

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

        fetchKabupaten.getKabs asyncRequestObject = new fetchKabupaten.getKabs(this);
        asyncRequestObject.execute();
    }
    private void fetchDataAlsin() {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AlsinURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("Alsin Response: " + response);
                    JSONArray jsonarray = new JSONArray(response);
                    if (response != null) {
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonObj = jsonarray.getJSONObject(i);
                            int Id = jsonObj.getInt("id");
                            String nama = jsonObj.getString("nama");
                            Double Latitude = jsonObj.getDouble("latitude");
                            Double Longitude = jsonObj.getDouble("longitude");
                            latLng = new LatLng(Latitude, Longitude);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.benih_marker));
                            markerOptions.position(latLng);
                            markerOptions.title(nama);
                            Marker marker = map.addMarker(markerOptions);
                            mHashMap.put(marker, Id);
                        }
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
                System.out.println("Kab-ID" + AlsinKabId);
                System.out.println("Kode-KOM" + kodeKom);
                params.put("kab_id", AlsinKabId);
                params.put("kode_kom", kodeKom);
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
    public void showCurrLocAlsin(View v) {
        text = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1Lahan);
        text.setText(null);
        try {
            map.clear();
        } catch (Exception ep) {

        }
        fetchDataLahanNew.setTv1(tv1);
        fetchDataLahanNew.setGoogleMap(map);
        fetchDataLahanNew.getPositions asyncRequestObject = new fetchDataLahanNew.getPositions(this);
        asyncRequestObject.execute(latStr, lngStr);
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            initCamera(mLastLocation);
        }
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
            map.clear();

            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            currLocationMarker = map.addMarker(markerOptions);
            initCamera(mLastLocation);
        }
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // String title = marker.getTitle();
                int id = mHashMap.get(marker);
                // Toast.makeText(getBaseContext(), "SHOW " + title + " ID: " + id, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(), AlsinDetail.class);
                intent.putExtra("KODE_KOM", kodeKom);
                intent.putExtra("ALSIN_ID", id);
                startActivity(intent);
                return false;
            }
        });
    }
    private void initCamera(Location location) {
        fetchDataLahanNew.getPositions asyncRequestObject = new fetchDataLahanNew.getPositions(this);
        fetchDataLahanNew.setGoogleMap(map);
        fetchDataLahanNew.setTv1(tv1);

        Double lat = location.getLatitude();
        latStr = lat.toString();
        Double lng = location.getLongitude();
        lngStr = lng.toString();

        asyncRequestObject.execute(latStr, lngStr);
        latLng = new LatLng(lat, lng);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    public void searchLocAlsin(View v) {
        map.clear();
        AlsinKabId = selectedKabId;
        getKabPos(AlsinKabId);
        fetchDataAlsin();
    }
    private void getKabPos(final String kabId) {
        String kabPosURL = "http://202.56.170.37/mobile-agro/new/get_kab_pos.php";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                kabPosURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("Benih Response: " + response);
                    JSONArray jsonarray = new JSONArray(response);
                    if (response != null) {
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonObj = jsonarray.getJSONObject(i);

                            Double Latitude = jsonObj.getDouble("lat");
                            Double Longitude = jsonObj.getDouble("long");
                            String Lokasi = jsonObj.getString("lokasi");
                            latLng = new LatLng(Latitude, Longitude);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            map.addMarker(markerOptions);
                            tv1.setText(Lokasi);
                        }
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14).build();
                        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
                params.put("kab_id", kabId);
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
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
            map.setMyLocationEnabled(true);
        }
        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    @Override
    public void fetchLahanFinish() {
        fetchDataAlsin();
    }

    @Override
    public void processFinish(final ArrayList<String> output, final ArrayList<String> outputIds) {
        text = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1Lahan);
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
}
