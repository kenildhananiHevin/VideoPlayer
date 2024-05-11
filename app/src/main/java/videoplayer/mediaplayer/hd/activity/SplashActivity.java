package videoplayer.mediaplayer.hd.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import plugin.adsdk.extras.BaseLauncherActivity;
import videoplayer.mediaplayer.hd.R;
import videoplayer.mediaplayer.hd.language.LanguageActivity;
import videoplayer.mediaplayer.hd.other.LocaleHelper;

public class SplashActivity extends BaseLauncherActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        handleLanguageChange();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Intent destinationIntent() {
        SharedPreferences preferences = getSharedPreferences("Language", 0);
        boolean prefsString = preferences.getBoolean("language_set", false);
        if (prefsString) {
            return new Intent(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            return new Intent(new Intent(SplashActivity.this, LanguageActivity.class).putExtra("from", true));
        }
    }

    @Override
    protected String extraAppContentText() {
        return getString(plugin.adsdk.R.string.app_content);
    }

    @Override
    protected int extraAppContentImage() {
        return R.mipmap.ic_new_launcher;
    }

    public static final String BASE_URL = "https://ht.askforad.com/";

    @Override
    protected String baseURL() {
        return BASE_URL;
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleLanguageChange();
    }

    public SplashActivity() {
        super(R.layout.activity_splash, plugin.adsdk.R.layout.ad_activity_extra_dashboard);
    }

    protected void handleLanguageChange() {
        SharedPreferences preferences = getSharedPreferences("Language", 0);
        String languageCode = preferences.getString("language_code", "en");
        LocaleHelper.setLocale(SplashActivity.this, languageCode);
    }


}