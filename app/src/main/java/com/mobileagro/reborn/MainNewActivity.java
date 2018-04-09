package com.mobileagro.reborn;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.user.mana_livechatv2.R;
import com.mobileagro.reborn.bantuan.BantuanIndexActivity;
import com.mobileagro.reborn.bantuan.BantuanTentang;

public class MainNewActivity extends AppCompatActivity {
    ImageSwitcher imageSwitcher;
    Button ButtonSentra, ButtonLahan, ButtonVarietas, ButtonKonsul, ButtonAlsintan;
    TextView tv1, tv2, tv3;

    // private SliderLayout mDemoSlider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

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
        setImageTouch();
        setTextLink();

        // mDemoSlider = (SliderLayout)findViewById(R.id.slider_01);
        /*
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Test 1", R.drawable.block_image_1);
        file_maps.put("Test 2", R.drawable.block_image_2);
        for(String name : file_maps.keySet()){
            TextSliderView tsv = new TextSliderView(this);
            tsv.description(name).image(file_maps.get(name)).setScaleType(BaseSliderView.ScaleType.Fit);

            mDemoSlider.addSlider(tsv);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        */
    }

    public void clickSentra(View v) {
        Intent intent = new Intent(MainNewActivity.this, SentraNewActivity.class);
        startActivity(intent);
    }
    public void clickLahan(View v) {
        Intent intent = new Intent(MainNewActivity.this, LahanNewActivity.class);
        startActivity(intent);
    }
    public void clickKonsul(View v) {
        Intent intent = new Intent(MainNewActivity.this, com.example.user.mana_livechatv2.LoginActivity.class);
        startActivity(intent);
    }
    public void clickVarietas(View v) {
       //  Intent intent = new Intent(MainNewActivity.this, VarietasNewActivity.class);
        Intent intent = new Intent(MainNewActivity.this, VarietasAllNewActivity.class);
        startActivity(intent);
    }
    public void clickBantuan(View v) {
        Intent intent = new Intent(MainNewActivity.this, BantuanIndexActivity.class);
        startActivity(intent);
    }
    public void clickTentang(View v) {
        Intent intent = new Intent(MainNewActivity.this, BantuanTentang.class);
        startActivity(intent);
    }
    public void clickAlsintan(View v) {
        // Intent intent = new Intent(MainNewActivity.this, alsintan_new.class);
        Intent intent = new Intent(MainNewActivity.this, SaprodiNew.class);
        startActivity(intent);
    }
    private void setTextLink() {
        tv1 = (TextView) findViewById(R.id.textView1);
        tv1.setMovementMethod(LinkMovementMethod.getInstance());
        tv1.setPaintFlags(tv1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tv1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tv1.setTextColor(getResources().getColorStateList(R.color.on_link_touch));
                } else {
                    tv1.setTextColor(getResources().getColorStateList(R.color.tabsScrollColor));
                }
                return false;
            }
        });

        tv2 = (TextView) findViewById(R.id.textView3);
        tv2.setMovementMethod(LinkMovementMethod.getInstance());
        tv2.setPaintFlags(tv1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tv2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tv2.setTextColor(getResources().getColorStateList(R.color.on_link_touch));
                } else {
                    tv2.setTextColor(getResources().getColorStateList(R.color.tabsScrollColor));
                }
                return false;
            }
        });

        tv3 = (TextView) findViewById(R.id.textView5);
        tv3.setMovementMethod(LinkMovementMethod.getInstance());
        tv3.setPaintFlags(tv1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tv3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tv3.setTextColor(getResources().getColorStateList(R.color.on_link_touch));
                } else {
                    tv3.setTextColor(getResources().getColorStateList(R.color.tabsScrollColor));
                }
                return false;
            }
        });
    }

    private void setImageTouch() {
        /*
        ButtonSentra = (Button) findViewById(R.id.buttonSentra);
        ButtonSentra.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ButtonSentra.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.sentra_hover,0,0);

                } else {
                    ButtonSentra.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.sentra_a,0,0);
                }
                return false;
            }
        });

        ButtonLahan = (Button) findViewById(R.id.buttonLahan);
        ButtonLahan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ButtonLahan.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.lahan_hover,0,0);
                } else {
                    ButtonLahan.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.lahan_a,0,0);
                }
                return false;
            }
        });

        ButtonVarietas = (Button) findViewById(R.id.buttonVarietas);
        ButtonVarietas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ButtonVarietas.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.varietas_hover,0,0);
                } else {
                    ButtonVarietas.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.varietas_a,0,0);
                }
                return false;
            }
        });

        ButtonKonsul = (Button) findViewById(R.id.buttonKonsul);
        ButtonKonsul.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ButtonKonsul.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.konsul_hover,0,0);
                } else {
                    ButtonKonsul.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.konsul_a,0,0);
                }
                return false;
            }
        });


        ButtonAlsintan = (Button) findViewById(R.id.buttonAlsintan);
        ButtonAlsintan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ButtonAlsintan.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.alsintan_hover,0,0);
                } else {
                    ButtonAlsintan.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.alsintan_a,0,0);
                }
                return false;
            }
        });
        */
    }
}
