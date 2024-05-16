package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.setting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.AppOpenManager;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.R;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.BaseActivity;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.language.LanguageActivity;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.other.LocaleHelper;

import java.util.Objects;

public class SettingActivity extends BaseActivity {
    String languageCode;
    LinearLayout language_click, rate_click, share_click, privacy_click,theme_click;
    TextView setting_language_text, setting_rate_text, setting_share_text, setting_privacy_text,setting_theme_text,cancel,ok;
    SettingActivity activity;
    ImageView back;
    RadioGroup radio_g;
    RadioButton light, dark, custom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        activity = this;
        SharedPreferences preferences = getSharedPreferences("Language", 0);
        languageCode = preferences.getString("language_code", "en");

        language_click = findViewById(R.id.language_click);
        rate_click = findViewById(R.id.rate_click);
        share_click = findViewById(R.id.share_click);
        privacy_click = findViewById(R.id.privacy_click);
        theme_click = findViewById(R.id.theme_click);
        setting_language_text = findViewById(R.id.setting_language_text);
        setting_rate_text = findViewById(R.id.setting_rate_text);
        setting_share_text = findViewById(R.id.setting_share_text);
        setting_privacy_text = findViewById(R.id.setting_privacy_text);
        setting_theme_text = findViewById(R.id.setting_theme_text);
        back = findViewById(R.id.player_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        language_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, LanguageActivity.class));
            }
        });
        rate_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsUtility.rateUs(activity);
            }
        });

        share_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsUtility.shareApp(activity);
            }
        });

        privacy_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsUtility.privacyPolicy(activity);
            }
        });

        theme_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = activity.getSharedPreferences("Language", 0);
                String prefsString = preferences.getString("language_code", "en");
                LocaleHelper.setLocale(activity,prefsString);

                AlertDialog dialog = new AlertDialog.Builder(activity, R.style.MyTransparentBottomSheetDialogTheme).create();
                LayoutInflater layoutInflater = getLayoutInflater();
                View view = layoutInflater.inflate(R.layout.activity_theme_dailog, null);
                dialog.setView(view);
                dialog.setCanceledOnTouchOutside(false);

                radio_g = view.findViewById(R.id.radio_g);
                light = view.findViewById(R.id.light_name);
                dark = view.findViewById(R.id.dark_name);
                custom = view.findViewById(R.id.custom_name);
                cancel = view.findViewById(R.id.cancel);
                ok = view.findViewById(R.id.ok);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                custom.setChecked(false);
                light.setChecked(false);
                dark.setChecked(false);
                SharedPreferences mPrefs = getSharedPreferences("THEME", 0);
                boolean theme_boolean = mPrefs.getBoolean("iscustom", true);
                if (theme_boolean) {
                    custom.setChecked(true);
                }else {
                    theme_boolean = mPrefs.getBoolean("islight", true);
                    if (theme_boolean) {
                        light.setChecked(true);
                    } else {
                        dark.setChecked(true);
                    }
                }

                custom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        custom.setChecked(true);
                        light.setChecked(false);
                        dark.setChecked(false);
                    }
                });

                light.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        custom.setChecked(false);
                        light.setChecked(true);
                        dark.setChecked(false);
                    }
                });

                dark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        custom.setChecked(false);
                        light.setChecked(false);
                        dark.setChecked(true);
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences mPrefs = getSharedPreferences("THEME", 0);
                        if (dark.isChecked()) {
                            mPrefs.edit().putBoolean("islight", false).commit();
                            mPrefs.edit().putBoolean("iscustom", false).apply();
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            recreate();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Window window = getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.setStatusBarColor(ContextCompat.getColor(SettingActivity.this, R.color.white));
                            }
                        } else if (light.isChecked()) {
                            mPrefs.edit().putBoolean("islight", true).commit();
                            mPrefs.edit().putBoolean("iscustom", false).apply();
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            recreate();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Window window = getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.setStatusBarColor(ContextCompat.getColor(SettingActivity.this, R.color.white));
                            }
                        }
//                        StatusBarManager(mPrefs.getBoolean("islight",false));
                        else if (custom.isChecked()) {
                            UiModeManager currentNightMode = (UiModeManager) getSystemService(UI_MODE_SERVICE);
                            if (currentNightMode.getNightMode() == UiModeManager.MODE_NIGHT_YES) {
                                mPrefs.edit().putBoolean("iscustom", true).apply();
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            } else {
                                mPrefs.edit().putBoolean("iscustom", true).apply();
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            }
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
                Window window = dialog.getWindow();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int screenWidth = displayMetrics.widthPixels;
                int dialogWidth = (int) (screenWidth * 0.88);
                window.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
            }
        });


        setting_language_text.setSelected(true);
        setting_rate_text.setSelected(true);
        setting_share_text.setSelected(true);
        setting_privacy_text.setSelected(true);
        setting_theme_text.setSelected(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("Language", 0);
        String languageCode = preferences.getString("language_code", "en");
        if (!Objects.equals(this.languageCode, languageCode)) {
            recreate();
        }
    }
}