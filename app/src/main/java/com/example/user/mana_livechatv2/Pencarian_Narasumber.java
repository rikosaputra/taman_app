package com.example.user.mana_livechatv2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.user.mana_livechatv2.app.EndPoints;
import com.example.user.mana_livechatv2.app.MyApplication;
import com.example.user.mana_livechatv2.gcm.GcmIntentService;
import com.example.user.mana_livechatv2.helper.SearchableSpinner;
import com.example.user.mana_livechatv2.model.ChatRoom;
import com.example.user.mana_livechatv2.model.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class Pencarian_Narasumber extends AppCompatActivity implements OnMapReadyCallback, OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks, com.google.android.gms.location.LocationListener, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public static final String TAGS = Pencarian_Narasumber.class.getSimpleName();
    private GoogleMap mMap;
    int respon;
    private RadioButton radioSexButton;
    private Button btnDisplay;
    private RadioGroup radioSexGroup;
    boolean permisi = false;
    protected LocationListener locationListener;
    LatLng saya;
    SearchableSpinner mySpinner;
    String Komo;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    CheckBox pakar;
    CheckBox penyuluh;
    CheckBox peneliti;
    ArrayList<String> result = new ArrayList<String>();
    ArrayList<String> result1 = new ArrayList<String>();
    ArrayList<LatLng> posisi = new ArrayList<LatLng>();
    ArrayList<String> nama = new ArrayList<String>();

    ArrayList<Integer> x = new ArrayList<Integer>();
    ArrayList<Integer> y = new ArrayList<Integer>();
    ArrayList<String> address = new ArrayList<String>();
    TextView tv1;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    String Namachat;
    HashMap<String, String[]> abc = new HashMap<String, String[]>();
    private static String TAG = "Keterangan";
    Location lokasi;
    Button btnClosePopup;
    Button chatting;
    ProgressDialog progress;
    android.os.Handler handler = new android.os.Handler();




    // json object response url
//    private String urlJsonObj = "http://202.56.170.37/mobile-agro/connection.php";
    //"http://api.androidhive.info/volley/person_object.json"
    // json array response url
    private String urlJsonArry = "http://202.56.170.37/mobile-agro/narasumber.php";
    private String urlJsonArry1 = "http://202.56.170.37/mobile-agro/komoditas.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_pencarian__narasumber);
        progress=new ProgressDialog(this);
        progress.setMessage("Mengambil Data");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();


        new makeJsonArrayRequest().execute();
        new makeJsonArrayRequest1().execute();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        find_and_modify_text_view();

//        //intentnya harus ngasih data user juga, belom dihandle
//        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
//        fab2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                Intent intent = new Intent(Pencarian_Narasumber.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();


//            }
//        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        boolean lokasiAda = isLocationEnabled(Pencarian_Narasumber.this);

        if (!lokasiAda) {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            Log.i(TAG, "All location settings are satisfied.");
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the result
                                // in onActivityResult().
                                status.startResolutionForResult(Pencarian_Narasumber.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                            break;
                    }
                }


            });
        }

        //displayLocationSettingsRequest(Pencarian_Narasumber.this);



    }

    Runnable myRunnable = new Runnable() {
        public void run() {
//            abc.clear();
//            nama.clear();
//            jabatans.clear();
//
//
//            posisi.clear();
//            new makeJsonArrayRequest().execute();
//            doTheAutoRefresh();
        }
    };
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
            mGoogleApiClient.disconnect();
        }
    }


    private void handleNewLocation(Location location) {
//        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        saya = latLng;
        if (MyApplication.getInstance().getPrefManager().getUser().isNarasumber().equals("true")) {
            postAddress(latLng,MyApplication.getInstance().getPrefManager().getUser().getId());}
        mMap.setMyLocationEnabled(true);
        showResult(saya);
//        MarkerOptions options = new MarkerOptions()
//                .position(latLng)
//                .title("Saya di sini!");
//
//        mMap.addMarker(options);
        float zoomLevel = 13; //This goes up to 21
       mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }


    private PopupWindow pwindo;

    /**
     * Method to make json array request where response starts with [
     * */
    private class makeJsonArrayRequest extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {


            JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //Log.d(TAG, response.toString());
                            progress.setMax(response.length());
                            try {
                                // Parsing json array response
                                // loop through each json object
                                respon = response.length();

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject narasumber = (JSONObject) response
                                            .get(i);
                                    progress.setProgress(i+1);


                                    String name = narasumber.getString("fullname");
                                    String nip = narasumber.getString("nip");
                                    String jabfung = narasumber.getString("jabfung");
                                    String pendidikan = narasumber.getString("pendidikan");
                                    String jurusan = narasumber.getString("jurusan");
                                    String telpon = narasumber.getString("telpon");
                                    String komoditas = narasumber.getString("komoditas");
                                    String token = narasumber.getString("gcm_registration_id");
                                    String lat = narasumber.getString("lat");
                                    String lo = narasumber.getString("long");
                                    String username = narasumber.getString("name");
                                    String id = narasumber.getString("id");


                                    String[] DataNarasumber = new String[12];


                                    DataNarasumber[0] = name;
                                    DataNarasumber[1] = nip;
                                    if (jabfung.equals("null"))
                                        DataNarasumber[2] = "-";
                                    else
                                        DataNarasumber[2] = jabfung;
                                    if (pendidikan.equals("null"))
                                        DataNarasumber[3] = "-";
                                    else
                                        DataNarasumber[3] = pendidikan;
                                    if (jurusan.equals("null"))
                                        DataNarasumber[4] = "-";
                                    else
                                        DataNarasumber[4] = jurusan;
                                    if (telpon.equals("null"))
                                        DataNarasumber[5] = "-";
                                    else
                                        DataNarasumber[5] = telpon;
                                    if (komoditas.equals("null"))
                                        DataNarasumber[6] = "-";
                                    else
                                        DataNarasumber[6] = komoditas;
                                    DataNarasumber[7] = token;
                                    DataNarasumber[8] = lat;
                                    DataNarasumber[9] = lo;
                                    DataNarasumber[10] = username;
                                    DataNarasumber[11] = id;
                                    if(nama.contains(name)){
                                        abc.get(name)[6] += ", "+ komoditas;
                                    }
                                    else{
                                    abc.put(name, DataNarasumber);
                                    nama.add(name);}

                                    if( progress.getMax()==response.length()){
                                        progress.dismiss();
                                    }
                                }

                                //txtResponse.setText(jsonResponse);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        "Terjadi error, silahkan coba lagi",
                                        Toast.LENGTH_LONG).show();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getApplicationContext(),
                            "Internet Anda bermasalah, silahkan coba lagi", Toast.LENGTH_SHORT).show();


                }
            });

            MyApplication.getInstance().addToRequestQueue(req);
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {




        }
    }
    private class makeJsonArrayRequest1 extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {


            JsonArrayRequest req = new JsonArrayRequest(urlJsonArry1,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //Log.d(TAG, response.toString());
                            progress.setMax(response.length());
                            try {
                                // Parsing json array response
                                // loop through each json object
                                List<String> categories = new ArrayList<String>();
                                categories.add("Semua Komoditas");
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject narasumber = (JSONObject) response
                                            .get(i);
                                    progress.setProgress(i+1);


                                    String komoditas = narasumber.getString("nama");

                                    categories.add(komoditas);





                                    if( progress.getMax()==response.length()){
                                        progress.dismiss();
                                    }
                                }



                                // Creating adapter for spinner
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Pencarian_Narasumber.this, android.R.layout.simple_spinner_item, categories);
//                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
//                                spinner.setAdapter(dataAdapter);
                                mySpinner = (SearchableSpinner) findViewById(R.id.spinner);
                                mySpinner.setTitle("Pilih Komoditas");
                                mySpinner.setPositiveButton("Tutup");
                                mySpinner.onSearchableItemClicked(this,0);
                                mySpinner.setOnItemSelectedListener(Pencarian_Narasumber.this);
                                mySpinner.setAdapter(dataAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        "Terjadi error, silahkan coba lagi",
                                        Toast.LENGTH_LONG).show();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getApplicationContext(),
                            "Internet Anda bermasalah", Toast.LENGTH_SHORT).show();


                }
            });

            MyApplication.getInstance().addToRequestQueue(req);
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            // Spinner element


            // Drop down layout style - list view with radio button






        }
    }

    private void initiatePopupWindow(Marker m, HashMap<String, String[]> x) {
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) Pencarian_Narasumber.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.tes,
                    (ViewGroup) findViewById(R.id.popup_element));
            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            pwindo = new PopupWindow(layout, layout.getMeasuredWidth() + layout.getMeasuredWidth(), layout.getMeasuredHeight(), true);

            final String tokens =x.get(m.getTitle())[7];
            Namachat = x.get(m.getTitle())[10];



            //nama
            ((TextView) pwindo.getContentView().findViewById(R.id.txtView)).append(m.getTitle());
            //nip
            ((TextView) pwindo.getContentView().findViewById(R.id.txtView2)).append(x.get(m.getTitle())[1]);
            //jabfung
            ((TextView) pwindo.getContentView().findViewById(R.id.txtView3)).append(x.get(m.getTitle())[2]);
            //pendidikan
            ((TextView) pwindo.getContentView().findViewById(R.id.txtView4)).append(x.get(m.getTitle())[3]);
            //jurusan
            ((TextView) pwindo.getContentView().findViewById(R.id.txtView5)).append(x.get(m.getTitle())[4]);
            //telpon
            ((TextView) pwindo.getContentView().findViewById(R.id.txtView6)).append(x.get(m.getTitle())[5]);
            //komoditas
            ((TextView) pwindo.getContentView().findViewById(R.id.txtView7)).append(x.get(m.getTitle())[6]);

            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            chatting = (Button) layout.findViewById(R.id.buttonchat);
            if (MyApplication.getInstance().getPrefManager().getUser().isNarasumber().equals("true")) {
//                Log.d("debug_narasumber", "DIA ADALAH NARASUMBER");
                chatting.setVisibility(View.GONE);
            }

            //Memulai chat
            chatting.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    // Intent i = new Intent(this, "apaaan".class);
                    //startActivity(i);

//                    ChatRoom chatRoom = chatRoomArrayList.get(position);
//                    Intent intent = new Intent(MainActivity.this, ChatRoomActivity.class);
//                    intent.putExtra("chat_room_id", chatRoom.getId());
//                    intent.putExtra("name", chatRoom.getName());
//                    startActivity(intent);
//                if (MyApplication.getInstance().getPrefManager().getUser().isNarasumber().equals("false"))
                    makeChatRoom(Namachat,tokens);
//                else
//                    Toast.makeText(getApplicationContext(), "Anda tidak dapat menggunakan fitur ini", Toast.LENGTH_LONG).show();


                }
            });;
            btnClosePopup = (Button) layout.findViewById(R.id.btn_close_popup);
            btnClosePopup.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeChatRoom(final String narasumber, final String token) {
        //String endPoint = EndPoints.CHAT_ROOM_MAKE.replace("_ID_", narasumber);
        //Log.d("debug_make_room", endPoint);
        //Log.d("debug_make_room", narasumber);
        progress.setMessage("Mohon tunggu sebentar...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.CHAT_ROOM_MAKE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response_makeChatRoom(): " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray chatRoomsArray = obj.getJSONArray("chat_room");
                        JSONObject room = (JSONObject) chatRoomsArray.get(0);
                        ChatRoom cr = new ChatRoom();
                        cr.setId(room.getString("chat_room_id"));
                        cr.setName(set(room.getString("name")));
                        cr.setLastMessage("");
                        cr.setUnreadCount(0);
                        cr.setTimestamp(room.getString("created_at"));

                        //Subscription first time attempt
//                        Intent subscribe_new = new Intent(getApplicationContext(), GcmIntentService.class);
//                        subscribe_new.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE_USER_ADDED_TOPIC);
//                        subscribe_new.putExtra(GcmIntentService.TOPIC, "topic_" + cr.getId());
//                        subscribe_new.putExtra(GcmIntentService.NARASUMBER, token);
//                        Log.d("debug_token_pencarian", token);
//                        startService(subscribe_new);

                        Intent subscribeintent = new Intent(getApplicationContext(), GcmIntentService.class);
                        subscribeintent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE);
                        subscribeintent.putExtra(GcmIntentService.TOPIC, "topic_" + cr.getId());
                        startService(subscribeintent);

                        Intent intent = new Intent(Pencarian_Narasumber.this, ChatRoomActivity.class);
                        intent.putExtra("chat_room_id", cr.getId());
                        intent.putExtra("name", cr.getName());
                        progress.hide();
                        startActivity(intent);
                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                        Log.d("debug",  "" + obj.getJSONObject("error").getString("message"));
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Terjadi error, silahkan coba lagi", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Internet Anda bermasalah, silahkan coba lagi", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("narasumber_name", narasumber);
                params.put("user_name", MyApplication.getInstance().getPrefManager().getUser().getName());

                Log.e(TAG, "Params: " + params.toString());

                return params;
            }

            ;
        };
        //);

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    //membuat nama chat room
    private String set (String str) {
        User user = MyApplication.getInstance().getPrefManager().getUser();
        String[] names = str.split("/");

        if (names[0].equals(user.getFullname())) {
            return names[1];
        }
        else {
            return names[0];
        }
    }

    private OnClickListener cancel_button_click_listener = new OnClickListener() {
        public void onClick(View v) {
            pwindo.dismiss();

        }
    };


    private void find_and_modify_text_view() {
        //pakar = (CheckBox) findViewById(R.id.checkBox);
        peneliti = (CheckBox) findViewById(R.id.checkBox3);
        penyuluh = (CheckBox) findViewById(R.id.checkBox2);

        ImageButton get_view_button = (ImageButton) findViewById(R.id.button);
        get_view_button.setOnClickListener(get_view_button_listener);
    }

    private Button.OnClickListener get_view_button_listener = new Button.OnClickListener() {
        public void onClick(View v) {
            // Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
//
            //makeJsonArrayRequest();

            mMap.clear();

            result = new ArrayList<>();
            result1 = new ArrayList<>();
            result1.add(Komo);

//            if (pakar.isChecked()) {
//                result.add((String) pakar.getText());
//            }
            if (penyuluh.isChecked()) {
                result.add((String) penyuluh.getText());
            }
            if (peneliti.isChecked()) {
                result.add((String) peneliti.getText());
            }
            showResult(saya);


        }
    };


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        Komo = item;
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public void showResult(LatLng x) {
boolean komoditi = true;
        mMap.clear();
        mMap.setMyLocationEnabled(true);
        for (int i = 0; i < abc.size(); i++) {
            //Log.d("debug_map", posisi.get(i).toString());
            //handleNewLocation(lokasi);

            float zoomLevel = 12; //This goes up to 21
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(x, zoomLevel));


            String splitter = abc.get(nama.get(i))[2];
            String [] xxx = splitter.trim().split(" ");

            if(!(abc.get(nama.get(i))[8].equals("null"))){
                if(result1.contains("Semua Komoditas")){

                    if ((result.contains(xxx[0]))&&
                            !MyApplication.getInstance().getPrefManager().getUser().getFullname().equals(abc.get(nama.get(i))[0])) {

                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(abc.get(nama.get(i))[8]),Double.parseDouble(abc.get(nama.get(i))[9])) )
                                .title(abc.get(nama.get(i))[0])
                                .snippet(abc.get(nama.get(i))[2])
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.person)));

                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override

                            public void onInfoWindowClick(Marker marker) {

                                initiatePopupWindow(marker, abc);


                            }
                        });

                    }}

                else {
                    if((result.contains(xxx[0]))&& abc.get(nama.get(i))[6].contains(result1.get(0) )&&
                            komoditi && !MyApplication.getInstance().getPrefManager().getUser().getFullname().equals(abc.get(nama.get(i))[0])){

                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(abc.get(nama.get(i))[8]),Double.parseDouble(abc.get(nama.get(i))[9])))
                                .title(abc.get(nama.get(i))[0])
                                .snippet(abc.get(nama.get(i))[2])
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.person)));

                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override

                            public void onInfoWindowClick(Marker marker) {

                                initiatePopupWindow(marker, abc);


                            }
                        });

                    }
                }}
        }
    }


    @Override
    public void onConnected(Bundle bundle) {


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.{

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            //requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        }

        else {Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            lokasi = location;
//            double la = location.getLatitude();
//            double lo = location.getLongitude();
//            LatLng apa = new LatLng(la, lo);
            //postAddress(apa, MyApplication.getInstance().getPrefManager().getUser().getId());

            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);
            } else {
                handleNewLocation(location);
            }

        }}

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
/*
     * Google Play services can resolve some errors it detects.
     * If the error has a resolution, try sending an Intent to
     * start a Google Play services activity that can resolve
     * error.
     */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            /*
             * Thrown if Google Play services canceled the original
             * PendingIntent
             */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
        /*
         * If no resolution is available, display a dialog to the
         * user with the error.
         */
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p1;

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(Pencarian_Narasumber.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }


        });
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        switch (menuItem.getItemId()) {
//            case R.id.action_logout:
//                db_chatRoom.drop();
//                MainActivity.handler.removeCallbacks(myRunnable);
//                MyApplication.getInstance().logout();
//                //handler= null;
//                break;
//        }
//        return super.onOptionsItemSelected(menuItem);
//    }
    private void postAddress (final LatLng test, final String namaNarasumber){




        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.ALAMAT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Internet Anda bermasalah, silahkan coba lagi", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("lat","" + test.latitude);
                params.put("long","" +  test.longitude);
                params.put("id", namaNarasumber);

                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
    private void doTheAutoRefresh() {
        handler.postDelayed(myRunnable, 8000);
    }

}