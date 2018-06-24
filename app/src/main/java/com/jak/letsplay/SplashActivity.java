package com.jak.letsplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent intent;
        if (Prefs.getInstance(getApplicationContext()).get(Constants.PREF_USERNAME).isEmpty()) {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, HomeActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
