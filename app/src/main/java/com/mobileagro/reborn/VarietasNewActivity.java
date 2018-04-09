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
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.user.mana_livechatv2.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mobileagro.VarietasDetail;
import com.mobileagro.demo1.fetchKabupaten;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VarietasNewActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, fetchKabupaten.getKabs.AsyncResponse {
    private AutoCompleteTextView text;
    private TextView tvCurrent;
    private static String latStr;
    private static String lngStr;
    private GoogleApiClient mGoogleApiClient;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private ImageButton backButton;
    private String selectedKabId = "0";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
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
        setContentView(R.layout.activity_varietas_new);
        tvCurrent = (TextView) findViewById(R.id.currLocNewVar);
        fetchKabupaten.getKabs asyncRequestObject = new fetchKabupaten.getKabs(this);
        asyncRequestObject.execute();
        buildGoogleApiClient();
        mGoogleApiClient.connect();

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        loadData();
    }
    private void loadData() {
        // preparing list data
        prepareListData();
        listAdapter = new expandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
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
        int groupCount = listAdapter.getGroupCount();
        for (int i =0 ;i< groupCount; i++) {
            expListView.expandGroup(i);
        }

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
            expListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
        } else {
            expListView.setIndicatorBoundsRelative(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
            expListView.setIndicatorBoundsRelative(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
        }
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String childText = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                // Toast.makeText(getApplicationContext(), childText, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), VarietasDetail.class);
                Bundle b = new Bundle();
                b.putString("name", childText);
                intent.putExtras(b);
                startActivity(intent);
                return false;
            }
        });
    }

    public void showCurrLocVar(View v) {
        text = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
        text.setText(null);

        fetchLocationData.setTv1(tvCurrent);
        fetchLocationData.getPositions asyncRequestObject = new fetchLocationData.getPositions();
        asyncRequestObject.execute(latStr, lngStr);

        loadData();
    }

    public void searchLocVar(View v) {
        fetchLocationId.getPositions asyncRequestId = new fetchLocationId.getPositions();
        fetchLocationId.setTv1(tvCurrent);
        asyncRequestId.execute(selectedKabId);
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Padi Sawah");
        listDataHeader.add("Kedelai");
        listDataHeader.add("Jagung");

        // Adding child data
        List<String> list = new ArrayList<String>();
        list.add("INPARI 11");
        list.add("INPARI 12");
        list.add("INPARI 13");
        list.add("INPARI 17");
        list.add("INPARI 21");
        list.add("INPARI 22");
        list.add("INPARI 23");
        list.add("INPARI 24");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("BURANGRANG");
        nowShowing.add("ANJASMORO");
        nowShowing.add("ARGOMULYO");
        nowShowing.add("GROBOGAN");
        nowShowing.add("SINABUNG");
        nowShowing.add("WILIS");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("PALAKKA");
        comingSoon.add("PROVIT-A1");
        comingSoon.add("PROVIT-A2");
        comingSoon.add("SHS-1");
        comingSoon.add("SHS-2");

        listDataChild.put(listDataHeader.get(0), list); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
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

            fetchLocationData.setTv1(tvCurrent);
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
    public void processFinish(final ArrayList<String> output, final ArrayList<String> outputIds) {
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
}
