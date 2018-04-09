package com.mobileagro;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;


import com.example.user.mana_livechatv2.R;


/**
 * Created by ThinkPad T440s VPro on 15/09/2016.
 */
public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIMEOUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                try {
                    // Intent i = new Intent(SplashScreen.this, com.mobileagro.reborn.MainNewActivity.class);
                    Intent i = new Intent(SplashScreen.this, com.mobileagro.reborn.new_design.MainMenu.class);
                    startActivity(i);
                }
                catch (Exception e) {
                    System.out.println("Splash Screen Exception::: " + e);
                }
                // close this activity
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
