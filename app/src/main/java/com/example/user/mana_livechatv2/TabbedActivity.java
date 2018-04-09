package com.example.user.mana_livechatv2;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.user.mana_livechatv2.app.Config;


/**
 * Created by user on 19/07/2016.
 */
public class TabbedActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        ImageButton toolbar = (ImageButton) findViewById(R.id.toolbarbutton);
        toolbar.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                //Keluarin pilihan logout
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                Intent logoutIntent = new Intent(TabbedActivity.this, MainActivity.class);
                                logoutIntent.setAction(Config.LOGOUT_ATTEMPT);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(logoutIntent);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(TabbedActivity.this);
                builder.setMessage("Anda yakin ingin keluar aplikasi?").setPositiveButton("Ya", dialogClickListener)
                        .setNegativeButton("Tidak", dialogClickListener).show();
            }
        });

        final TabHost tabHost = getTabHost();

        // Tab for Photos
        TabHost.TabSpec photospec = tabHost.newTabSpec("Konsultasi");
        // setting Title and Icon for the Tab
        photospec.setIndicator("Konsultasi", getResources().getDrawable(R.drawable.bg_bubble_white));
        Intent chatIntent = new Intent(this, MainActivity.class);
        photospec.setContent(chatIntent);

        // Tab for Songs
        TabHost.TabSpec songspec = tabHost.newTabSpec("Narasumber");
        songspec.setIndicator("Narasumber", getResources().getDrawable(R.drawable.bg_bubble_gray));
        Intent mapIntent = new Intent(this, Pencarian_Narasumber.class);
        songspec.setContent(mapIntent);

        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
//            View v = tabHost.getTabWidget().getChildAt(i);
//            v.setBackgroundResource(R.drawable.tabs);

            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(getResources().getColor(R.color.bg_chat));
        }

        // Adding all TabSpec to TabHost
        tabHost.addTab(photospec); // Adding photos tab
        tabHost.addTab(songspec); // Adding songs tab
        //tabHost.addTab(videospec); // Adding videos tab
    }
}
