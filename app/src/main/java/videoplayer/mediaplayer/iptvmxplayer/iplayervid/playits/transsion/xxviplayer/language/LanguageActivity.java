package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.language;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.R;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.BaseActivity;
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
    LanguageActivity activity;
    TextView txtSelectedLanguage;
    private FrameLayout adContainerView;
    private AdView adView;
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741";
    private static final String AD_UNIT_IDS = "ca-app-pub-3940256099942544/1033173712";
    private AtomicBoolean initialLayoutComplete = new AtomicBoolean(false);
    private InterstitialAd interstitialAd;
    private boolean adIsLoading;
    private static final long GAME_LENGTH_MILLISECONDS = 3000;
    private CountDownTimer countDownTimer;
    boolean gamePaused;
    private boolean gameOver;
    private long timerMilliseconds;
    String prefsString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        activity = this;
        SharedPreferences preferences = getSharedPreferences("Language", 0);
        prefsString = preferences.getString("language_code", "en");

        intent = getIntent().getBooleanExtra("from", false);

        languageList = findViewById(R.id.language_list);
        imgDone = findViewById(R.id.img_done);
        pbLoading = findViewById(R.id.pbLoading);
        back = findViewById(R.id.back);
        txtSelectedLanguage = findViewById(R.id.txtSelectedLanguage);
        adContainerView = findViewById(R.id.ad_view_container);

        adContainerView
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(
                        () -> {
                            if (!initialLayoutComplete.getAndSet(true)) {
                                loadBanner();
                            }
                        });

        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345")).build());

        loadAd();

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
                        startActivity(new Intent(LanguageActivity.this, MainActivity.class));
                        finish();
                    } else {
                        if (!prefsString.equals(languages.getLanguageCode())) {
                            showInterstitial();
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
        languageAdapter = new LanguageAdapter(activity, languages);
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

    private void showInterstitial() {
        if (interstitialAd != null) {
            interstitialAd.show(activity);
        } else {
            startGame();
            loadAd();
        }
    }

    public void loadAd() {
        if (adIsLoading || interstitialAd != null) {
            return;
        }
        adIsLoading = true;
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                AD_UNIT_IDS,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        activity.interstitialAd = interstitialAd;
                        adIsLoading = false;
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        activity.interstitialAd = null;
                                        finish();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        activity.interstitialAd = null;
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.i("TAG", loadAdError.getMessage());
                        interstitialAd = null;
                        adIsLoading = false;

                        String error =
                                String.format(
                                        java.util.Locale.US,
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(),
                                        loadAdError.getCode(),
                                        loadAdError.getMessage());
                        Toast.makeText(activity, "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void startGame() {
        createTimer(GAME_LENGTH_MILLISECONDS);
        gamePaused = false;
        gameOver = false;
    }

    private void createTimer(final long milliseconds) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(milliseconds, 50) {
            @Override
            public void onTick(long millisUnitFinished) {
                timerMilliseconds = millisUnitFinished;
            }

            @Override
            public void onFinish() {
                gameOver = true;
            }
        };

        countDownTimer.start();
    }

    @Override
    public void onBackPressed() {
        if (prefsStrings) {
            showInterstitial();
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

    private void loadBanner() {
        adView = new AdView(this);
        adView.setAdUnitId(AD_UNIT_ID);
        adView.setAdSize(getAdSize());

        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();

        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
        pauseGame();
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeGame();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    private void resumeGame() {
        if (gameOver || !gamePaused) {
            return;
        }
        gamePaused = false;
        createTimer(timerMilliseconds);
    }

    private void pauseGame() {
        if (gameOver || gamePaused) {
            return;
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        gamePaused = true;
    }

}