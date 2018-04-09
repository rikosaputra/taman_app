package com.example.user.mana_livechatv2;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.mana_livechatv2.helper.Db_ChatRoom;
import com.example.user.mana_livechatv2.model.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.user.mana_livechatv2.adapter.ChatRoomsAdapter;
import com.example.user.mana_livechatv2.app.Config;
import com.example.user.mana_livechatv2.app.EndPoints;
import com.example.user.mana_livechatv2.app.MyApplication;
import com.example.user.mana_livechatv2.gcm.GcmIntentService;
import com.example.user.mana_livechatv2.gcm.NotificationUtils;
import com.example.user.mana_livechatv2.helper.SimpleDividerItemDecoration;
import com.example.user.mana_livechatv2.model.ChatRoom;
import com.example.user.mana_livechatv2.model.Message;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, com.google.android.gms.location.LocationListener, GoogleApiClient.OnConnectionFailedListener {

    private String TAG = MainActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ArrayList<ChatRoom> chatRoomArrayList;
    private ChatRoomsAdapter mAdapter;
    private RecyclerView recyclerView;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    boolean flag = true;
//    android.os.Handler handler = new android.os.Handler();
    private ProgressDialog pDialog;
    TextView stats;

//    Runnable myRunnable = new Runnable() {
//        public void run() {
//            fetchChatRooms();
////            doTheAutoRefresh();
//        }
//    };

    //Database untuk menyimpan data-data chat room
    private Db_ChatRoom db_chatRoom = new Db_ChatRoom(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Check for login session. If not logged in launch
         * login activity
         * */
        if (MyApplication.getInstance().getPrefManager().getUser() == null) {
            launchLoginActivity();
        }

        setContentView(R.layout.activity_main);
        stats = (TextView) findViewById(R.id.status);



//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayShowHomeEnabled(false);
        pDialog=new ProgressDialog(this);
        pDialog.setMessage("Mengambil data...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showDialog();
        MyApplication.getInstance().getPrefManager().storeCurrentChatRoom("0");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
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
        /**
         * Broadcast receiver calls in two scenarios
         * 1. gcm registration is completed
         * 2. when new push notification is received
         * */
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    subscribeToGlobalTopic();

                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL
                    Log.e(TAG, "GCM registration id is sent to our server");

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    handlePushNotification(intent);
                } else if (intent.getAction().equals(Config.LOGOUT_ATTEMPT)) {
//                    Log.d("debug_logout", "logoutAttempt");
                    db_chatRoom.drop();
//                    handler.removeCallbacks(myRunnable);
                    MyApplication.getInstance().logout();
                }
            }
        };

        chatRoomArrayList = new ArrayList<>();
        mAdapter = new ChatRoomsAdapter(this, chatRoomArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

//        fetchChatRooms();

        recyclerView.addOnItemTouchListener(new ChatRoomsAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new ChatRoomsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity
                ChatRoom chatRoom = chatRoomArrayList.get(position);
                chatRoom.setLastMessage("");
                chatRoom.setUnreadCount(0);
                Intent intent = new Intent(MainActivity.this, ChatRoomActivity.class);
                intent.putExtra("chat_room_id", chatRoom.getId());
                intent.putExtra("name", chatRoom.getName());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        /**
         * Always check for google play services availability before
         * proceeding further with GCM
         * */
        if (checkPlayServices()) {
            registerGCM();
            fetchChatRooms();
        }
//        doTheAutoRefresh();
    }


    /**
     * Handles new push notification
     */
    private void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);
//        Log.d(TAG, "retreivepushnotification()->MainActivity");
        // if the push is of chat room message
        // simply update the UI unread messages count
        if (type == Config.PUSH_TYPE_CHATROOM) {
            Message message = (Message) intent.getSerializableExtra("message");
            String chatRoomId = intent.getStringExtra("chat_room_id");
            Log.d("debug", "push_type_chatroom");
            Log.d("debug_push_chat_room", "chatRoom ID : " + chatRoomId);
            Log.d("debug_push_chat_room", "message : " + message.getMessage());
            if (message != null && chatRoomId != null) {
//                updateRow(chatRoomId, message);
                //UPDATE ADAPTER
                updateAdapter();
            }

        } else if (type == Config.PUSH_TYPE_USER) {
            // push belongs to user alone
            // just showing the message in a toast
            Message message = (Message) intent.getSerializableExtra("message");
        }
    }

    private void updateAdapter() {
        ArrayList<ChatRoom> temp = db_chatRoom.getAllChatRooms();
        if (temp.size() != 0) {
            stats.setText("");
        }
        for (int a = 0; a < temp.size(); a++) {
            Log.d("debug_content_db", "chat room name :" + temp.get(a).getName());
            Log.d("debug_content_db", "chat room id :" + temp.get(a).getId());
            Log.d("debug_content_db", "chat room unreadcount :" + temp.get(a).getUnreadCount());
            Log.d("debug_content_db", "chat room last message :" + temp.get(a).getLastMessage());
        }
        for (int i = 0; i < temp.size(); i++) {
            boolean exist = false;
            for (ChatRoom cr : chatRoomArrayList) {
                if (cr.getId().equals(temp.get(i).getId())) {
                    int index = chatRoomArrayList.indexOf(cr);
                    chatRoomArrayList.remove(index);

                    chatRoomArrayList.add(index, temp.get(i));
                    exist = true;
                    Log.d("debug_updateAdapter", "update chatRoom, ID : " + temp.get(i).getId());
                    break;
                }
            }
            if (!exist) {
                chatRoomArrayList.add(temp.get(i));
                Log.d("debug_updateAdapter", "insert chatRoom, ID : " + temp.get(i).getId());
            }
        }
        mAdapter.notifyDataSetChanged();
    }
    /**
     * Updates the chat list unread count and the last message
     */
    private void updateRow(String chatRoomId, Message message) {
        boolean exist = false;
        for (ChatRoom cr : chatRoomArrayList) {
            if (cr.getId().equals(chatRoomId)) {

                int index = chatRoomArrayList.indexOf(cr);
                cr.setLastMessage(message.getMessage());

                cr.setUnreadCount(cr.getUnreadCount() + 1);
                db_chatRoom.updateChatRoom(Integer.toString(cr.getUnreadCount()), cr.getLastMessage(), chatRoomId);
                chatRoomArrayList.remove(index);
                chatRoomArrayList.add(index, cr);
                Log.d("debug_push_chat_room", "updateRow, ID : " + chatRoomId);
                exist = true;
                break;
            }
        }
        if (exist == false) {
            ChatRoom newcr = new ChatRoom();
            newcr.setLastMessage(message.getMessage());
            newcr.setUnreadCount(1);
            newcr.setId(chatRoomId);
            newcr.setName(message.getUser().getFullname());
            newcr.setTimestamp(message.getCreatedAt());
            chatRoomArrayList.add(newcr);
//            db_chatRoom.insertChatRoom(newcr);
            Log.d("debug_push_chat_room", "insertRow, ID, fullname : " + chatRoomId + "," + newcr.getName());
        }
        mAdapter.notifyDataSetChanged();
    }


    /**
     * fetching the chat rooms by making http call
     */
    private void fetchChatRooms() {
        String username = MyApplication.getInstance().getPrefManager().getUser().getName();
        String endPoint = EndPoints.CHAT_ROOM_SPECIFIC.replace("_ID_", username);
        Log.d("debug", "masuk fetchchatrooms");

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray chatRoomsArray = obj.getJSONArray("chat_rooms");
                        for (int i = 0; i < chatRoomsArray.length(); i++) {
                            JSONObject chatRoomsObj = (JSONObject) chatRoomsArray.get(i);
                            ChatRoom cr = new ChatRoom();
                            cr.setId(chatRoomsObj.getString("chat_room_id"));
                            cr.setName(set(chatRoomsObj.getString("name")));
                            cr.setLastMessage("");
                            cr.setUnreadCount(0);
                            cr.setTimestamp(chatRoomsObj.getString("created_at"));
                            //Insert chat room
                            if (db_chatRoom.getChatRoom(Integer.parseInt(cr.getId())) == null) {
                                db_chatRoom.insertChatRoom(cr);
                            }
                            else {
                                Log.d("DEBUG", "sudah ada di database");
                            }
                        }

//                         updateAdapter();

                    } else {
                        // error in fetching chat rooms
                        stats.setText("Tidak ada pesan yang disimpan.");
                    }
                    updateAdapter();
                    hideDialog();

//                    doTheAutoRefresh();
                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Terjadi error, silahkan coba lagi", Toast.LENGTH_LONG).show();
                    hideDialog();
                }

                mAdapter.notifyDataSetChanged();
//                Log.d("debug_adapter_main", Integer.toString(mAdapter.getItemCount()));
                // subscribing to all chat room topics
                subscribeToAllTopics();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Internet Anda bermasalah, silahkan coba lagi", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });

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
    // subscribing to global topic
    private void subscribeToGlobalTopic() {
//        Log.d("debug_topic", "subscribe->global()");
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE);
        intent.putExtra(GcmIntentService.TOPIC, Config.TOPIC_GLOBAL);
        startService(intent);
    }

    // Subscribing to all chat room topics
    // each topic name starts with `topic_` followed by the ID of the chat room
    // Ex: topic_1, topic_2
    private void subscribeToAllTopics() {
        //Log.d("debug_topic", "subscribe->specific()");
        for (ChatRoom cr : chatRoomArrayList) {
            Intent intent = new Intent(this, GcmIntentService.class);
            intent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE);
            intent.putExtra(GcmIntentService.TOPIC, "topic_" + cr.getId());
            startService(intent);
        }
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        Log.d("debug", "masuk onResume main activity");
        MyApplication.getInstance().getPrefManager().storeCurrentChatRoom("0");

        updateAdapter();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.LOGOUT_ATTEMPT));
        // clearing the notification tray
        NotificationUtils.clearNotifications();
    }

    @Override
    protected void onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
            mGoogleApiClient.disconnect();
        }
    }

    // starting the service to register with GCM
    private void registerGCM() {
//        Log.d("debug_register", "registerGCM()");
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "Perangkat Anda tidak mendukung, Google Play Services belum terpasang", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
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
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            flag = false;
                            break;
                    }
                }
            };
            if(flag) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Aplikasi tidak dapat izin untuk akses lokasi, silahkan buka pengaturan").setNegativeButton("Ok", dialogClickListener).show();
            }


            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        }

        else {Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

//            double la = location.getLatitude();
//            double lo = location.getLongitude();
//            LatLng apa = new LatLng(la, lo);
            //postAddress(apa, MyApplication.getInstance().getPrefManager().getUser().getId());

            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);
            } else {
                handleNewLocation(location);
            }

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
//        * Google Play services can resolve some errors it detects.
//                * If the error has a resolution, try sending an Intent to
//        * start a Google Play services activity that can resolve
//                * error.
//                */
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

    private void handleNewLocation(Location location) {
//        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        if (MyApplication.getInstance().getPrefManager().getUser().isNarasumber().equals("true")) {
        postAddress(latLng,MyApplication.getInstance().getPrefManager().getUser().getId());}

    }
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



//    private void doTheAutoRefresh() {
//        handler.postDelayed(myRunnable, 1000);
//    }
}