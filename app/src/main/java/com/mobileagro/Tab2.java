package com.mobileagro;

/**
 * Created by riko on 14/06/2016.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

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

import com.example.user.mana_livechatv2.R;
import com.mobileagro.demo1.*;

public class Tab2 extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
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
    private Context mContext;
    private static String latStr;
    private static String lngStr;
    View incl;
    TextView tvi;
    ScrollView sv;
    ImageView imgV;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    public static String CurrentKabId;
    /*
    private boolean shouldRefreshOnResume = false;

    public static Tab2 newInstance() {
        Tab2 fragment = new Tab2();
        return fragment;
    }
    public Tab2() {

    }
    */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_COARSE_LOCATION);
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
        Bundle arguments = getArguments();
        if (arguments !=null) {
            Log.d("----------- INPUT", "------------ INPUT");
            kabId = getArguments().getString("KabId");
        }
        Log.d("----------- INPUT", "------------ INPUT:: "+ this.getTag());
        v = inflater.inflate(R.layout.tab_2,container,false);
        tv1 = (TextView) v.findViewById(R.id.currLoc);
        btn1 = (Button) v.findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Tab2List tl2 = new Tab2List();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.tab2_mainLayout, tl2, "tablist2");
                transaction.addToBackStack(null);
                transaction.commit();
                /*
                Intent intent = new Intent(getActivity(), com.tanam.user.mana_livechatv2.LoginActivity.class);
                startActivity(intent);
                */
            }
        });

        /* <-- include page -->
        incl = v.findViewById(R.id.included);
        tvi = (TextView) incl.findViewById(R.id.detailTitle);
        sv = (ScrollView) v.findViewById(R.id.sv1);
        imgV = (ImageView) incl.findViewById(R.id.incImg);
        </-- include page --> */

        // incl.setVisibility(View.GONE);
        // incl.setVisibility(View.INVISIBLE);

        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_container, fragment).commit();
        }
        fragment.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onStop() {
        super.onStop();

    }
    @Override
    public void onConnected(Bundle bundle) {
        Log.d("On Connected", "ON CONNECTED RUNNING");
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
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
                    TextView TvX = (TextView) v.findViewById(R.id.currLoc);
                    // Log.d("Text View XXX ---", TvX.getText().toString());
                    String KabTitle = TvX.getText().toString();
                    String KabID;
                    if (kabId == null)
                        KabID = CurrentKabId;
                    else
                        KabID = kabId;
                    Tab3 tb3 = new Tab3();
                    tb3.setParam(KabTitle, KabID, title);
                    ((MainActivity)getActivity()).getViewPager().setCurrentItem(2);
                    // incl.setVisibility(View.VISIBLE);
                    // tvi.setText(title);
                    /*
                    if (title.equals("JAGUNG")) {
                        // imgV.setImageResource(R.drawable.jagung_image);
                        Tab3.setParam(KabTitle, KabID, title);
                        ((MainActivity)getActivity()).getViewPager().setCurrentItem(2);
                    } else if (title.equals("PADI SAWAH")) {
                        // imgV.setImageResource(R.drawable.padi_sawah_image);
                    } else if (title.equals("KEDELAI")) {
                        // imgV.setImageResource(R.drawable.kedelai_image);
                    } else {

                    }
                    */
                    // sv.scrollTo(100, 1000);

                    // ((MainActivity) getActivity()).getViewPager().setCurrentItem(2); // Set To Page

                    // Toast.makeText(getActivity().getBaseContext(), marker.getTitle(), Toast.LENGTH_LONG).show();
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
    }

    public void reInitMarker(String selected) {
        map.clear();
        fetchDataLahanId.getPositions asyncRequestObject = new fetchDataLahanId.getPositions();
        fetchDataLahanId.setGoogleMap(map);
        fetchDataLahanId.setTv1(tv1);
        asyncRequestObject.execute(selected);
    }
    public void reInitCurrentLoc() {
        try {
            map.clear();
        }
        catch (Exception ep) {

        }
        fetchDataLahan.getPositions asyncRequestObject = new fetchDataLahan.getPositions();
        fetchDataLahanId.setGoogleMap(map);
        fetchDataLahanId.setTv1(tv1);
        asyncRequestObject.execute(latStr,lngStr);
    }

    private void initCamera( Location location ) {
        //zoom to current position:
        // latLng = new LatLng(location.getLatitude(), location.getLongitude());
        // CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14).build();
        // map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        if (kabId == null) {
            fetchDataLahan.getPositions asyncRequestObject = new fetchDataLahan.getPositions();
            fetchDataLahan.setGoogleMap(map);
            fetchDataLahan.setTv1(tv1);
            Double lat = location.getLatitude();
            latStr = lat.toString();
            Double lng = location.getLongitude();
            lngStr = lng.toString();
            asyncRequestObject.execute(latStr, lngStr);
        } else {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("On Ready", "MAP READY");
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            map.setMyLocationEnabled(true);
        }
        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    public void onPause() {
        super.onPause();
        if(map!=null)
            map=null;
    }
}
