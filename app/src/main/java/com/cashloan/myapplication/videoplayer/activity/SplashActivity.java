package com.cashloan.myapplication.videoplayer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.cashloan.myapplication.videoplayer.R;
import com.cashloan.myapplication.videoplayer.language.LanguageActivity;

    public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("Language", 0);
                boolean prefsString = preferences.getBoolean("language_set", false);
                if (prefsString){
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }else {
                    Intent i = new Intent(SplashActivity.this, LanguageActivity.class);
                    i.putExtra("from", true);
                    startActivity(i);
                    finish();
                }

            }
        }, 2000);
    }
}