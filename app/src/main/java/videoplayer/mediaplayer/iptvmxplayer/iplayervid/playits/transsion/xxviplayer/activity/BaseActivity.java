package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.R;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.other.LocaleHelper;


public class BaseActivity extends plugin.adsdk.service.BaseActivity {
    public String prefsString;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        SharedPreferences preferences = getSharedPreferences("Language", 0);
        prefsString = preferences.getString("language_code", "en");
        LocaleHelper.setLocale(BaseActivity.this, prefsString);
        super.onCreate(savedInstanceState);
    }

    public static void setLightStatusBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public static void clearLightStatusBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleLanguageChange();
        SharedPreferences mPrefs = getSharedPreferences("THEME", 0);
        boolean theme_boolean = mPrefs.getBoolean("iscustom", true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.white));
            window.setNavigationBarColor(getResources().getColor(R.color.white));
        }
        if (theme_boolean){

            UiModeManager currentNightMode = (UiModeManager)getSystemService(Context.UI_MODE_SERVICE);
            if (currentNightMode.getNightMode() == UiModeManager.MODE_NIGHT_YES) {

                clearLightStatusBar(this);
            } else {

                setLightStatusBar(this);
            }
        }else {

            theme_boolean = mPrefs.getBoolean("islight", true);
            if (theme_boolean){
                setLightStatusBar(this);
            }else {
                clearLightStatusBar(this);
            }
        }
    }

    protected void handleLanguageChange() {
        SharedPreferences preferences = getSharedPreferences("Language", 0);
        String languageCode = preferences.getString("language_code", "en");
        LocaleHelper.setLocale(BaseActivity.this, languageCode);
    }
}
