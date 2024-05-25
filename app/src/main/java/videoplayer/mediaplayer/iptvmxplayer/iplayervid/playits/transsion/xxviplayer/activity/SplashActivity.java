package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity;


import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.BaseActivity.clearLightStatusBar;
import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.BaseActivity.setLightStatusBar;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatDelegate;

import plugin.adsdk.extras.BaseLauncherActivity;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.R;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.language.LanguageActivity;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.other.LocaleHelper;

public class SplashActivity extends BaseLauncherActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        handleLanguageChange();
        SharedPreferences mPrefs = getSharedPreferences("THEME", 0);
        boolean theme_boolean = mPrefs.getBoolean("iscustom", true);
        if (theme_boolean){
            UiModeManager currentNightMode = (UiModeManager)getSystemService(Context.UI_MODE_SERVICE);
            if (currentNightMode.getNightMode() == UiModeManager.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }else {
            theme_boolean = mPrefs.getBoolean("islight", true);
            if (theme_boolean){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        }
        super.onCreate(savedInstanceState);

    }

    @Override
    protected Intent destinationIntent() {
        SharedPreferences preferences = getSharedPreferences("Language", 0);
        boolean prefsString = preferences.getBoolean("language_set", false);
        if (prefsString) {
            return new Intent(new Intent(SplashActivity.this, DashBoardActivity.class));
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