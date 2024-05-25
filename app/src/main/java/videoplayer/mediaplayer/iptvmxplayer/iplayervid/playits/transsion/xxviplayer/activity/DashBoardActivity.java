package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import plugin.adsdk.service.AdsUtility;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.R;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.other.LocaleHelper;

public class DashBoardActivity extends BaseActivity {

    TextView txtDasHd, txtVideo, txtDasHdDesc, txtShare, txtRate, txtPrivacy;
    ConstraintLayout constraintDashScreen;
    LinearLayout linearShare, linearRate, linearPrivacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        nativeAdSmall();

        txtDasHd = findViewById(R.id.txtDasHd);
        txtVideo = findViewById(R.id.txtVideo);
        txtDasHdDesc = findViewById(R.id.txtDasHdDesc);
        txtShare = findViewById(R.id.txtShare);
        txtRate = findViewById(R.id.txtRate);
        txtPrivacy = findViewById(R.id.txtPrivacy);
        constraintDashScreen = findViewById(R.id.constraintDashScreen);
        linearShare = findViewById(R.id.linearShare);
        linearRate = findViewById(R.id.linearRate);
        linearPrivacy = findViewById(R.id.linearPrivacy);

        txtDasHd.setSelected(true);
        txtVideo.setSelected(true);
        txtDasHdDesc.setSelected(true);
        txtShare.setSelected(true);
        txtRate.setSelected(true);
        txtPrivacy.setSelected(true);

        constraintDashScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardActivity.this, MainActivity.class));
            }
        });

        linearShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsUtility.shareApp(DashBoardActivity.this);

            }
        });

        linearPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsUtility.privacyPolicy(DashBoardActivity.this);
            }
        });

        linearRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsUtility.rateUs(DashBoardActivity.this);
            }
        });

    }

    boolean backClick = false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
            Toast.makeText(this, R.string.press, Toast.LENGTH_SHORT).show();
        }
    }
}