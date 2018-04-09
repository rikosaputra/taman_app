package com.mobileagro.reborn.new_design;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

import com.example.user.mana_livechatv2.R;
import com.mobileagro.reborn.MainNewActivity;
import com.mobileagro.reborn.SentraNewActivity;
import com.mobileagro.reborn.TeknologiPertanian;

public class MainMenu extends AppCompatActivity {
    ImageSwitcher imageSwitcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        imageSwitcher = (ImageSwitcher) findViewById(R.id.imgSw);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_XY);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400));
                return myView;
            }
        });
        imageSwitcher.setImageResource(R.drawable.block_image_1);
        imageSwitcher.postDelayed(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                if (i%3==0) {
                    imageSwitcher.setImageResource(R.drawable.block_image_3);
                }
                else if (i%2==0)
                    imageSwitcher.setImageResource(R.drawable.block_image_2);
                else
                    imageSwitcher.setImageResource(R.drawable.block_image_1);
                i++;
                imageSwitcher.postDelayed(this, 3000);
            }
        }, 3000);

        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);
        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);
    }
    public void clickKomoditasNew(View v) {
        Intent intent = new Intent(MainMenu.this, SentraNewActivity.class);
        startActivity(intent);
    }
    public void clickTeknologiNew(View v) {
        Intent intent = new Intent(MainMenu.this, TeknologiPertanian.class);
        startActivity(intent);
    }
}
