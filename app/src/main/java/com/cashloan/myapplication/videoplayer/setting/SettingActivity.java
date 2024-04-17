package com.cashloan.myapplication.videoplayer.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cashloan.myapplication.videoplayer.R;
import com.cashloan.myapplication.videoplayer.activity.BaseActivity;
import com.cashloan.myapplication.videoplayer.language.LanguageActivity;

import java.util.Objects;

public class SettingActivity extends BaseActivity {
    String languageCode;
    LinearLayout language_click, rate_click, share_click, privacy_click;
    TextView setting_language_text, setting_rate_text, setting_share_text, setting_privacy_text;
    SettingActivity activity;
    ImageView back;

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
        setting_language_text = findViewById(R.id.setting_language_text);
        setting_rate_text = findViewById(R.id.setting_rate_text);
        setting_share_text = findViewById(R.id.setting_share_text);
        setting_privacy_text = findViewById(R.id.setting_privacy_text);
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
                rateUs(activity);
            }
        });

        share_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp(activity);
            }
        });

        privacy_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privacyPolicy(activity);
            }
        });

        setting_language_text.setSelected(true);
        setting_rate_text.setSelected(true);
        setting_share_text.setSelected(true);
        setting_privacy_text.setSelected(true);
    }
    public void rateUs(Activity activity) {
        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
    }

    public void shareApp(Activity activity) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name));
            String shareMessage = "\nPlease try this application\n\n" + "https://play.google.com/store/apps/details?id=" + activity.getPackageName() + "\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception ignored) {
        }
    }

    public void privacyPolicy(Activity activity) {
        try {
            Intent browserIntent;
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wisdomvalleey.blogspot.com/2023/02/wisdom-valley-privacy.html?m=1"));
            startActivity(browserIntent);
        } catch (Exception ignored) {
        }
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