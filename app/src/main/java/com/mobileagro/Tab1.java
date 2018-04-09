package com.mobileagro;

/**
 * Created by riko on 14/06/2016.
 */
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.github.mikephil.charting.charts.BarChart;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.example.user.mana_livechatv2.R;



import java.util.ArrayList;


/**
 * Created by hp1 on 21-01-2015.
 */
public class Tab1 extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private SupportMapFragment fragment;
    private GoogleMap map;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_1, container, false);
        TextView tv = (TextView) v.findViewById(R.id.currLoc);

        globalKab gKab = globalKab.getInstance();
        // int data = gKab.getData();
        String kabName = gKab.getName();
        tv.setText(kabName);
        String hasil = "Hasil ===> " + kabName;
        Log.d(hasil, hasil );
        BarChart chart = (BarChart) v.findViewById(R.id.chart);
        ArrayList<String> abc = new ArrayList<>();

        /* BarData data = new BarData(getXAxisValues(), getDataSet());

        chart.setData(data);
        chart.setDescription("Data Sentra Produksi");
        chart.animateXY(2000, 2000);
        chart.invalidate();
        */
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_container, fragment).commit();
        }
        */
    }
    @Override
    public void onResume() {
        super.onResume();
        /*
        if (map == null) {
            map = fragment.getMap();
            map.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
        }
        */
    }

    @Override
    public void onConnected(Bundle bundle) {

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

    }
    @Override
    public void onPause() {
        super.onPause();
        if(map!=null)
            map=null;
    }


}