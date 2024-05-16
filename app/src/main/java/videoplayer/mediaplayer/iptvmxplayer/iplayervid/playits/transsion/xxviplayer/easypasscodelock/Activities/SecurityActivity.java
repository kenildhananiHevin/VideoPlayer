package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.easypasscodelock.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.R;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.BaseActivity;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.ShowVideoActivity;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.easypasscodelock.Interfaces.ActivityChanger;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.easypasscodelock.Utils.EasylockSP;


public class SecurityActivity extends BaseActivity {

    TextView txtNext,txtShowVideoName,txtWName;
    SecurityActivity activity;
    EditText edtQuestion;
    ImageView imgBack;
    private static ActivityChanger activityChanger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        activity = this;

        EasylockSP.init(activity);
        if (activityChanger == null) {
            activityChanger = new LockscreenActivity();
        }

        String status = getIntent().getExtras().getString("passStatus", "change");

        txtNext = findViewById(R.id.txtNext);
        txtShowVideoName = findViewById(R.id.txtShowVideoName);
        edtQuestion = findViewById(R.id.edtQuestion);
        txtWName = findViewById(R.id.txtWName);
        imgBack = findViewById(R.id.imgBack);

        txtShowVideoName.setSelected(true);
        txtWName.setSelected(true);

        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtQuestion.getText().toString().isEmpty()){
                    Toast.makeText(activity, R.string.please_enter_security_question, Toast.LENGTH_SHORT).show();
                }else {
                    if (status.equals("change")) {
                        if (EasylockSP.getString("Security", null).equals(edtQuestion.getText().toString())) {
                            activity.finish();
                            activityChanger.activityClass(ShowVideoActivity.class);
                            Intent intent = new Intent(activity, LockscreenActivity.class);
                            intent.putExtra("passStatus", "set");
                            intent.putExtra("passStatus2", "changes");
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(activity, R.string.wrong_answer, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        EasylockSP.put("Security", edtQuestion.getText().toString());
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}