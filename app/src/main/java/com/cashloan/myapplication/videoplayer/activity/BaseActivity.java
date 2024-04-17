package com.cashloan.myapplication.videoplayer.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cashloan.myapplication.videoplayer.other.LocaleHelper;


public class BaseActivity extends AppCompatActivity {
    public String prefsString;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        handleLanguageChange();
        SharedPreferences preferences = getSharedPreferences("Language", 0);
        prefsString = preferences.getString("language_code", "en");
        LocaleHelper.setLocale(BaseActivity.this, prefsString);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleLanguageChange();
    }

    protected void handleLanguageChange() {
        SharedPreferences preferences = getSharedPreferences("Language", 0);
        String languageCode = preferences.getString("language_code", "en");
        LocaleHelper.setLocale(BaseActivity.this, languageCode);
    }
}
