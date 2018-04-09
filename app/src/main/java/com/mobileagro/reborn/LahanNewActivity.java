package com.mobileagro.reborn;

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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
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
import com.mobileagro.adapter.listLahanAdapter;
import com.mobileagro.demo1.fetchDataLahanIdNew;
import com.mobileagro.demo1.fetchDataLahanNew;
import com.mobileagro.demo1.fetchKabupaten;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LahanNewActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, fetchKabupaten.getKabs.AsyncResponse, fetchDataLahanNew.getPositions.AsyncFetchData, fetchDataLahanIdNew.getPositions.AsyncFetchData {
    GoogleMap googleMap;
    private SupportMapFragment fragment;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private static GoogleMap map;
    Marker currLocationMarker;
    LatLng latLng;
    Button btn1;
    private static TextView tv1;
    View v;
    public static String kabId;
    // private Context mContext;
    private static String latStr;
    private static String lngStr;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    public static String CurrentKabId;
    private ImageButton backButton;
    private String selectedKabId = "0";
    private AutoCompleteTextView text;
    String KeslahURL = "http://202.56.170.37/mobile-agro/new/keslah.php";

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
        setContentView(R.layout.activity_lahan_new);
        tv1 = (TextView) findViewById(R.id.currLocNew);
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
        FragmentManager fm = getSupportFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_container, fragment).commit();
        }
        fragment.getMapAsync(this);
    }

    private void loadList() {
        final ListView listView = (ListView) findViewById(R.id.listViewLahan);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                KeslahURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<String> KeslahData = new ArrayList<>();
                ArrayList<String> KodeKomoditi = new ArrayList<>();
                try {
                    JSONArray jsonarray = new JSONArray(response);
                    if (response != null) {
                        Double[] latAdd = {0.020, 0.010, 0.020, 0.020, 0.010, 0.000, 0.010};
                        Double[] lngAdd = {0.020, 0.020, 0.010, 0.000, 0.000, 0.020, 0.040};
                        System.out.println("Response LAHAN NEW ACTIVITY : " + response);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonObj = jsonarray.getJSONObject(i);
                            String KabId = jsonObj.getString("id_kabupaten");
                            String KodeKomoditas = jsonObj.getString("kode_komoditas");
                            String Keslah = jsonObj.getString("keslah");
                            String latStr = jsonObj.getString("lat");
                            String lngStr = jsonObj.getString("long");
                            KeslahData.add(Keslah);
                            KodeKomoditi.add(KodeKomoditas);

                            // SET MARKER
                            latLng = new LatLng(Double.parseDouble(latStr) + latAdd[i], Double.parseDouble(lngStr) + lngAdd[i]);
                            MarkerOptions markerOptions = new MarkerOptions();
                            String Komoditas = "";
                            switch (KodeKomoditas) {
                                case "JG":
                                    System.out.println("---- JAGUNG MARKER ----- ");
                                    Komoditas = "Jagung";
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.jagung_marker));
                                    break;
                                case "KD":
                                    System.out.println("---- Kedelai MARKER ----- ");
                                    Komoditas = "Kedelai";
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.kedelai_marker));
                                    break;
                                case "PG":
                                    System.out.println("---- Padi Gogo MARKER ----- ");
                                    Komoditas = "Padi Gogo";
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.padi_marker));
                                    break;
                                case "SI":
                                    System.out.println("---- Padi Sawah Irigasi MARKER ----- ");
                                    Komoditas = "Padi Sawah Irigasi";
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.padi_marker));
                                    break;
                                case "SL":
                                    System.out.println("---- Padi Sawah Lebak MARKER ----- ");
                                    Komoditas = "Padi Sawah Lebak";
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.padi_marker));
                                    break;
                                case "SP":
                                    System.out.println("---- Padi Sawah Pasang Surut MARKER ----- ");
                                    Komoditas = "Padi Sawah Pasang Surut";
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.padi_marker));
                                    break;
                                case "SH":
                                    System.out.println("---- Sawah Tadah Hujan MARKER ----- ");
                                    Komoditas = "Padi Sawah Tadah Hujan";
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.padi_marker));
                                    break;
                                default:
                                    Komoditas = "";
                            }
                            markerOptions.position(latLng);
                            markerOptions.title(Komoditas);
                            map.addMarker(markerOptions);
                        }
                        if (KodeKomoditi.size() > 0) {
                            String[] komArray = new String[KodeKomoditi.size()];
                            komArray = KodeKomoditi.toArray(komArray);
                            String[] kelshArray = new String[KeslahData.size()];
                            kelshArray = KeslahData.toArray(kelshArray);
                            listView.setAdapter(new listLahanAdapter(getBaseContext(), komArray, kelshArray));
                        } else {
                            listView.setAdapter(null);
                            Toast toast = Toast.makeText(getApplicationContext(), "Data lahan tidak ditemukan", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
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
                        Toast.makeText(getApplicationContext(), "Internet Anda bermasalah, silahkan coba lagi", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("kab_id", kabId);
                System.out.println("Send POST KabId = " + kabId);
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getAdapter().getItem(position).toString();
                // Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_SHORT).show();
                String LocationTitle = tv1.getText().toString();
            /*
            Intent intent = new Intent(getBaseContext(), KomoditasNewActivity.class);
            intent.putExtra("KOMODITAS_TITLE", selectedItem);
            intent.putExtra("LOCATION", LocationTitle);
            if (kabId == null ) {
                System.out.print("KAB ID NULL             -------------");
                intent.putExtra("KAB_ID", CurrentKabId);
            } else {
                System.out.print("KAB ID NOT NULL             -------------");
                intent.putExtra("KAB_ID", kabId);
            }
            */

                Intent intent = new Intent(getBaseContext(), LahanNewActivityDetail.class);
                intent.putExtra("KAB_ID", kabId);
                intent.putExtra("KODE_KOM", selectedItem);
                intent.putExtra("LOC_TEXT", tv1.getText());
                System.out.println("Selected Item = " + selectedItem);
                startActivity(intent);
            }
        });
    }

    public void showCurrLocLahan(View v) {
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
    }

    public void searchLocLahan(View v) {
        map.clear();
        kabId = selectedKabId;

        fetchDataLahanIdNew.setGoogleMap(map);
        fetchDataLahanIdNew.setTv1(tv1);
        fetchDataLahanIdNew.getPositions asyncRequestId = new fetchDataLahanIdNew.getPositions(this);
        asyncRequestId.execute(selectedKabId);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void initCamera(Location location) {
        // if (kabId == null) {
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

        /* } else {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
                    buildGoogleApiClient();
                    mGoogleApiClient.connect();
                    map.setMyLocationEnabled(true);
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other permissions this app might request
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Toast.makeText(this, "- Map Connected -", Toast.LENGTH_LONG).show();
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

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    String title = marker.getTitle();

                    String LocationTitle = tv1.getText().toString();
                    String KodeKom = "";
                    switch (title) {
                        case "Jagung":
                            KodeKom = "JG";
                            break;
                        case "Kedelai":
                            KodeKom = "KD";
                            break;
                        case "Padi Gogo":
                            KodeKom = "PG";
                            break;
                        case "Padi Sawah Irigasi":
                            KodeKom = "SI";
                            break;
                        case "Padi Sawah Lebak":
                            KodeKom = "SL";
                            break;
                        case "Padi Sawah Tadah Hujan":
                            KodeKom = "SH";
                            break;
                        case "Padi Sawah Pasang Surut":
                            KodeKom = "SP";
                            break;
                        default:
                            KodeKom = "";
                    }
                    Intent intent = new Intent(getBaseContext(), LahanNewActivityDetail.class);

                    intent.putExtra("KODE_KOM", KodeKom);
                    intent.putExtra("LOC_TEXT", LocationTitle);

                    if (kabId == null ) {
                        intent.putExtra("KAB_ID", CurrentKabId);
                    } else {
                        intent.putExtra("KAB_ID", kabId);
                    }
                    startActivity(intent);
                    return false;
                }
            });
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000); //10 seconds
        mLocationRequest.setFastestInterval(5000); //5 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        if (mLastLocation != null) {
            fetchLocationData.setTv1(tv1);
            fetchLocationData.getPositions asyncRequestObject = new fetchLocationData.getPositions();

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
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Toast.makeText(this, "- Map Ready -", Toast.LENGTH_LONG).show();
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
    public void processFinish(final ArrayList<String> output,final ArrayList<String> outputIds) {
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

    @Override
    public void fetchLahanFinish() {
        loadList();
    }

    @Override
    public void fetchLahanIdFinish() {loadList(); }
}
