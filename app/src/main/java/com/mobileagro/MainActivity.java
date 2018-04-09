package com.mobileagro;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

import com.mobileagro.demo1.fetchKabupaten;

import java.util.ArrayList;
import java.util.List;
import com.example.user.mana_livechatv2.R;

public class MainActivity extends AppCompatActivity implements fetchKabupaten.getKabs.AsyncResponse {

    // Declaring Your View and Variables
    AutoCompleteTextView text;
    // MultiAutoCompleteTextView text1;
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Sentra Produksi","Keseuaian Lahan", "Keseuaian Varietas"};
    String selectedKabId = "0";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    int Numboftabs =3;
    Button srcButton;
    ImageButton currButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("Android M", "Android M");
            if ((this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) || (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("This app need location access");
                alert.setMessage("Please grant location access");
                alert.setPositiveButton(R.string.ok, null);
                alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                        }
                    }
                });
                alert.show();
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ma);
        currButton = (ImageButton) findViewById(R.id.currentLocButton);
        currButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
                text.setText("");

                Tab2 tb2 = new Tab2();
                tb2.reInitCurrentLoc();
                pager.setCurrentItem(1);
            }
        });

        ImageButton imb = (ImageButton) findViewById(R.id.ChatButton);
        imb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.user.mana_livechatv2.LoginActivity.class);
                startActivity(intent);
            }
        });

        srcButton = (Button) findViewById(R.id.searchButton);
        srcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Bundle bundle = new Bundle();
                bundle.putString("KabId", "1011");

                // Fragment fragment = new Tab2();
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("");
                fragment.setArguments(bundle);
                if (fragment!=null && fragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
                }
                */
                Tab2 tb2 = new Tab2();
                tb2.reInitMarker(selectedKabId);
                pager.setCurrentItem(1);
                /*
                FragmentManager fragmentMg = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTrans = fragmentMg.beginTransaction();
                fragmentTrans.replace(R.id.pager, fragment, "Tab 2");
                fragmentTrans.addToBackStack(null);
                fragmentTrans.commit();
                */

            }
        });


        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // getSupportActionBar().setLogo(R.drawable.mana_logo);
        // getSupportActionBar().setDisplayUseLogoEnabled(true);
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setCurrentItem(1);
        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        /*
        String[] Cities={"Banjaran, Bandung, Jawa Barat ","Ungaran, Semarang, Jawa Tengah","Purworejo, Surabaya, Jawa Timur","Pancoran Mas, Depok, Jawa Barat","Pejaten, Jakarta Selatan, DKI Jakarta","Pasar Minggu, Jakarta Selatan, DKI Jakarta"};
        text=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
        // text1=(MultiAutoCompleteTextView)findViewById(R.id.multiAutoCompleteTextView1);

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,Cities);

        text.setAdapter(adapter);
        text.setThreshold(1);
        */
        // text1.setAdapter(adapter);
        //  text1.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        fetchKabupaten.getKabs asyncRequestObject = new fetchKabupaten.getKabs(this);
        asyncRequestObject.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
    public ViewPager getViewPager() {
        if (null == pager) {
            pager = (ViewPager) findViewById(R.id.pager);
        }
        return pager;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // ggetMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processFinish(final ArrayList<String> output,final ArrayList<String> outputIds) {


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
                Log.d("POSITION >> " + String.valueOf(realPos), "POSITION ID >> " + selectedKabId);
            }
        });
    }
}