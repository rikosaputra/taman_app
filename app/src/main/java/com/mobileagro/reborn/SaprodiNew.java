package com.mobileagro.reborn;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.user.mana_livechatv2.R;
import com.example.user.mana_livechatv2.RegisterActivity;
import com.mobileagro.reborn.saprodi.AlsinActivity;
import com.mobileagro.reborn.saprodi.BenihActivity;
import com.mobileagro.reborn.saprodi.LoginSaprodi;
import com.mobileagro.reborn.saprodi.PupukActivity;

import java.util.ArrayList;
import java.util.List;

public class SaprodiNew extends AppCompatActivity {
    private Spinner spinner1;
    String[] kodeKom = {"SI","SL","SH","SP","PG","KD","JG"};
    private ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saprodi_new);

        List<String> list = new ArrayList<String>();
        list.add("Padi Sawah Irigasi");
        list.add("Padi Sawah Lebak");
        list.add("Padi Tadah Hujan");
        list.add("Padi Pasang Surut");
        list.add("Padi Ladang");
        list.add("Kedelai");
        list.add("Jagung");

        spinner1 = (Spinner) findViewById(R.id.komoditasSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);

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
    }
    public void clickBenih(View v) {
        Intent intent = new Intent(getBaseContext(), BenihActivity.class);
        int selectedIndex = spinner1.getSelectedItemPosition();
        intent.putExtra("KODE_KOM", kodeKom[selectedIndex]);
        startActivity(intent);
    }
    public void clickPupuk(View v) {
        Intent intent = new Intent(getBaseContext(), PupukActivity.class);
        int selectedIndex = spinner1.getSelectedItemPosition();
        intent.putExtra("KODE_KOM", kodeKom[selectedIndex]);
        startActivity(intent);
    }
    public void clickAlsin(View v) {
        Intent intent = new Intent(getBaseContext(), AlsinActivity.class);
        int selectedIndex = spinner1.getSelectedItemPosition();
        intent.putExtra("KODE_KOM", kodeKom[selectedIndex]);
        startActivity(intent);
    }
    public void clickRegister(View v) {
        /*
        Intent intent = new Intent(getBaseContext(), RegisterSaprodi.class);
        int selectedIndex = spinner1.getSelectedItemPosition();
        intent.putExtra("KODE_KOM", kodeKom[selectedIndex]);
        startActivity(intent);
        */
        Intent i = new Intent(getBaseContext(), RegisterActivity.class);
        startActivity(i);
        finish();
    }
    public void clickLogin(View v) {

        Intent i = new Intent(getBaseContext(), LoginSaprodi.class);
        startActivity(i);
        finish();
    }
}
