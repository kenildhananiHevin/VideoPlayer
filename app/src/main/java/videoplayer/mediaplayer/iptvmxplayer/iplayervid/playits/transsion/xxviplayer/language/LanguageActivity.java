package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.language;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import plugin.adsdk.service.BaseCallback;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.R;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.BaseActivity;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.DashBoardActivity;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.MainActivity;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.adapter.LanguageAdapter;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.language_model.Languages;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.other.LocaleHelper;

public class LanguageActivity extends BaseActivity {

    RecyclerView languageList;
    public static ImageView back, imgDone;
    LanguageAdapter languageAdapter;
    ArrayList<Languages> languages = new ArrayList<>();
    boolean intent;
    boolean prefsStrings;
    boolean backClick = false;
    ProgressBar pbLoading;
//    LanguageActivity activity;
    TextView txtSelectedLanguage;
    String prefsString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        nativeAd();

//        activity = this;
        SharedPreferences preferences = getSharedPreferences("Language", 0);
        prefsString = preferences.getString("language_code", "en");

        intent = getIntent().getBooleanExtra("from", false);

        languageList = findViewById(R.id.language_list);
        imgDone = findViewById(R.id.img_done);
        pbLoading = findViewById(R.id.pbLoading);
        back = findViewById(R.id.back);
        txtSelectedLanguage = findViewById(R.id.txtSelectedLanguage);

        imgDone.setSelected(true);
        txtSelectedLanguage.setSelected(true);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Handler handler = new Handler(Looper.getMainLooper());
        long delay = 1000L + new Random().nextInt(500);
        handler.postDelayed(() -> {
            pbLoading.setVisibility(View.GONE);
            languageList.setVisibility(View.VISIBLE);
        }, delay);

        languages.add(new Languages(R.drawable.player_english, "English", "en"));
        languages.add(new Languages(R.drawable.player_hindi, "Hindi", "hi"));
        languages.add(new Languages(R.drawable.player_spanish, "Spanish", "es"));
        languages.add(new Languages(R.drawable.player_french, "French", "fr"));
        languages.add(new Languages(R.drawable.player_russian, "Russian", "ru"));
        languages.add(new Languages(R.drawable.player_portuguese, "Portuguese", "pt"));
        languages.add(new Languages(R.drawable.player_german, "German", "de"));
        languages.add(new Languages(R.drawable.player_indonesian, "Indonesian", "in"));
        languages.add(new Languages(R.drawable.player_chinese, "Chinese", "zh"));
        languages.add(new Languages(R.drawable.player_filipino, "Filipino", "fil"));
        languages.add(new Languages(R.drawable.player_italian, "Italian", "it"));
        languages.add(new Languages(R.drawable.player_afrikaans, "Afrikaans", "af"));
        languages.add(new Languages(R.drawable.player_japanese, "Japanese", "ja"));
        languages.add(new Languages(R.drawable.player_korean, "Korean", "ko"));
        languages.add(new Languages(R.drawable.player_polish, "Polish", "pl"));
        languages.add(new Languages(R.drawable.player_thai, "Thai", "th"));
        languages.add(new Languages(R.drawable.player_turkish, "Turkish", "tr"));
        languages.add(new Languages(R.drawable.player_ukrainian, "Ukrainian", "uk"));
        languages.add(new Languages(R.drawable.player_vietnamese, "Vietnamese", "vi"));

        imgDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (languageAdapter.selected != -1) {
                    Languages languages = LanguageActivity.this.languages.get(languageAdapter.selected);
                    LocaleHelper.setLocale(LanguageActivity.this, languages.getLanguageCode());
                    SharedPreferences prefsStringss = getSharedPreferences("Language", 0);
                    SharedPreferences.Editor editor = prefsStringss.edit();
                    String languageCode = languages.getLanguageCode();
                    editor.putString("language_code", languageCode);
                    editor.putBoolean("language_set", true);
                    editor.apply();
                    if (intent) {
                        startActivity(new Intent(LanguageActivity.this, DashBoardActivity.class));
                        finish();
                    } else {
                        if (!prefsString.equals(languages.getLanguageCode())) {
                            showInterstitial(new BaseCallback() {
                                @Override
                                public void completed() {
                                    finish();
                                }
                            });
                        } else {
                            finish();
                        }
                    }

                } else {
                    Toast.makeText(LanguageActivity.this, R.string.select_language, Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (intent) {
//            imgDone.setVisibility(View.GONE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                imgDone.setVisibility(View.VISIBLE);
            }
        }

        languageList.setLayoutManager(new LinearLayoutManager(this));
        languageAdapter = new LanguageAdapter(this, languages);
        languageList.setAdapter(languageAdapter);


        SharedPreferences preference = getSharedPreferences("Language", 0);
        prefsStrings = preference.getBoolean("language_set", false);
        if (prefsStrings) {
            for (int i = 0; i < languages.size(); i++) {
                if (Objects.equals(languages.get(i).getLanguageCode(), prefsString)) {
                    languageAdapter.selected = i;
                    languageAdapter.notifyDataSetChanged();
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (prefsStrings) {
            showInterstitial(new BaseCallback() {
                @Override
                public void completed() {
                    finish();
                }
            });
        } else {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    backClick = false;
                }
            }, 2000);
            if (backClick) {
                finishAffinity();
            } else {
                backClick = true;
                Toast.makeText(LanguageActivity.this, R.string.press, Toast.LENGTH_SHORT).show();
            }
        }
    }

}