package com.mobileagro.reborn.saprodi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputSaprodi extends AppCompatActivity  implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    Spinner spinner1;
    ImageView imv;
    private SupportMapFragment fragment;
    // EditText nama_preusahaan_form;
    // EditText alamat_form;
    EditText no_telp_form;
    EditText nama_saprodi_form;
    EditText harga_saprodi_form;
    EditText stock_saprodi_form;
    EditText latSaprodi;
    EditText lngSaprodi;
    private ImageButton backButton;
    private static final int CAMERA_REQUEST = 1888;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private static GoogleMap map;
    Marker now;
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
        setContentView(R.layout.activity_input_saprodi);
        latSaprodi = (EditText) findViewById(R.id.lat_lokasi);
        lngSaprodi = (EditText) findViewById(R.id.lng_lokasi);
        nama_saprodi_form = (EditText) findViewById(R.id.nama_saprodi);
        harga_saprodi_form = (EditText) findViewById(R.id.harga_saprodi);
        stock_saprodi_form = (EditText) findViewById(R.id.stock_saprodi);
        imv = (ImageView) findViewById(R.id.img_view_saprodi);

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
        spinner1 = (Spinner) findViewById(R.id.komoditasSpinner);

        List<String> list = new ArrayList<String>();
        list.add("Alsintan");
        list.add("Benih");
        list.add("Pupuk");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);

        FragmentManager fm = getSupportFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_container, fragment).commit();
        }
        fragment.getMapAsync(this);
    }

    public void pickCamera(View view) {

        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, "shoot.jpg");

            // String path = "cam/test.jpg";
            // file = new File(path);

        Uri outputFileUri = Uri.fromFile(file);

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        System.out.println("Saving Photo File....  " + outputFileUri.toString());
        startActivityForResult(intent, 1);
    }
    public void pickCard(View view) {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 2);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            imv.setImageBitmap(bitmap);
        }

        else {
            switch (resultCode) {
                case 0:
                    Log.i("STATUS ___", "User cancelled");
                    break;
                case -1:
                    onPhotoTaken();
                    break;
            }
        }
    }

    public void submitData(View view) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String kodeKomoditas =  String.valueOf(spinner1.getSelectedItemPosition());
        final String NamaSaprodi = nama_saprodi_form.getText().toString();
        final String HargaSaprodi = harga_saprodi_form.getText().toString();
        final String StockSaprodi = stock_saprodi_form.getText().toString();
        final String Latitude = latSaprodi.getText().toString();
        final String Longitude = lngSaprodi.getText().toString();
        final String userId = MyApplication.getInstance().getPrefManager().getUser().getId();
        String addSaprodiUrl = EndPoints.ADD_SAPRODI;
        StringRequest strRequest = new StringRequest(Request.Method.POST, addSaprodiUrl, new Response.Listener<String>() {
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


                params.put("user_id", userId);
                params.put("kode_komoditas", kodeKomoditas);
                params.put("nama", NamaSaprodi);
                params.put("harga", HargaSaprodi);
                params.put("stock", StockSaprodi);
                params.put("latitude", Latitude);
                params.put("longitude", Longitude);

                return params;
            }
        };
        queue.add(strRequest);
    }

    protected void onPhotoTaken() {
        // Log message
        Log.i("STATUS ___", "onPhotoTaken");
        File sdcard = Environment.getExternalStorageDirectory();
        String path =  sdcard + "/shoot.jpg";
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        imv.setImageBitmap(bitmap);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000); //10 seconds
        mLocationRequest.setFastestInterval(5000); //5 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        markerOptions.position(latLng).isDraggable();
        map.addMarker(markerOptions).setTitle("Lokasi Saprodi");
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));

        latSaprodi.setText(String.valueOf(mLastLocation.getLatitude()));
        lngSaprodi.setText(String.valueOf(mLastLocation.getLongitude()));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        /*
        if(now != null){
            now.remove();
        }
        TextView tvLat = (TextView) findViewById(R.id.lat_lokasi);
        double newLat = location.getLatitude();
        double newLng = location.getBearing();
        LatLng latLng = new LatLng(newLat, newLng);

        now = map.addMarker(new MarkerOptions().position(latLng));
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
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
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("MAP IS READY ----------");
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
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                map.clear();

                Log.d("System out", "MAP CLICKED...");
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                map.addMarker(markerOptions);
                double latDouble = latLng.latitude;
                double lngDouble = latLng.longitude;
                latSaprodi.setText(String.valueOf(latDouble));
                lngSaprodi.setText(String.valueOf(lngDouble));
            }
        });

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
                System.out.println("MARKER START DRAG....");
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {

                Log.d("System out", "onMarkerDragEnd...");
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(arg0.getPosition());
                map.addMarker(markerOptions);
                map.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));

            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                System.out.println("MARKER IS DRAG....");
            }
        });
    }
}
