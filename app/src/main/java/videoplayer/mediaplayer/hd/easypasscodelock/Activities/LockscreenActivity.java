package videoplayer.mediaplayer.hd.easypasscodelock.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import videoplayer.mediaplayer.hd.R;
import videoplayer.mediaplayer.hd.easypasscodelock.Interfaces.ActivityChanger;
import videoplayer.mediaplayer.hd.easypasscodelock.Utils.EasylockSP;
import videoplayer.mediaplayer.hd.easypasscodelock.Utils.LockscreenHandler;

public class LockscreenActivity extends LockscreenHandler implements ActivityChanger {

    @SuppressWarnings("rawtypes")
    private static Class classToGoAfter;
    private final int[] passButtonIds = {R.id.lbtn1, R.id.lbtn2, R.id.lbtn3, R.id.lbtn4, R.id.lbtn5, R.id.lbtn6, R.id.lbtn7, R.id.lbtn8, R.id.lbtn9, R.id.lbtn0};
    //
    private final String checkStatus = "check";
    private final String setStatus = "set";
    private final String setStatus1 = "set1";
    private final String changeStatus = "change";
    private final String changeStatus1 = "change1";
    private final String changeStatus2 = "change2";
    String tempPass = "";
    public ImageView img_select, img_select_one, img_select_two, img_select_three;
    //    private TextView textViewHAHA;
    private String passString = "", realPass = "";
    private String status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_lockscreen);

        EasylockSP.init(this);
        realPass = getPassword();
        initViews();

        status = getIntent().getExtras().getString("passStatus", "check");
//        if (status.equals(setStatus)) textViewHAHA.setText(R.string.enter_a_new_password_txt);
        String disableStatus = "disable";
        if (status.equals(disableStatus)) {
            EasylockSP.put("password", null);
            Toast.makeText(this, getString(R.string.password_disabled_txt), Toast.LENGTH_SHORT).show();
            gotoActivity();
        }
    }

    private void initViews() {
        /*textViewHAHA = findViewById(R.id.span_text);
        textViewDot = findViewById(R.id.dotText);*/
        img_select = findViewById(R.id.img_select);
        img_select_one = findViewById(R.id.img_select_one);
        img_select_two = findViewById(R.id.img_select_two);
        img_select_three = findViewById(R.id.img_select_three);
        TextView textViewForgotPassword = findViewById(R.id.forgot_pass_textview);
        ImageView buttonEnter = findViewById(R.id.lbtnEnter);
        ImageView imageButtonDelete = findViewById(R.id.lbtnDelete);
        /*ConstraintLayout relativeLayoutBackground = findViewById(R.id.background_layout);*/
        /*relativeLayoutBackground.setBackgroundColor(EasyLock.backgroundColor);*/

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LockscreenActivity.this, SecurityActivity.class);
                intent.putExtra("passStatus", "change");
                startActivity(intent);
            }
        });

        imageButtonDelete.setOnClickListener(view -> {
            if (passString.length() > 0)
                passString = passString.substring(0, passString.length() - 1);
            passString = "";
            img_select.setImageResource(R.drawable.un_select_ig);
            img_select_one.setImageResource(R.drawable.un_select_ig);
            img_select_two.setImageResource(R.drawable.un_select_ig);
            img_select_three.setImageResource(R.drawable.un_select_ig);
//            textViewDot.setText(passString);
        });

        buttonEnter.setOnClickListener(view -> {
            switch (status) {
                case checkStatus:
                    if (passString.isEmpty()) {
                        Toast.makeText(LockscreenActivity.this, R.string.please_enter_password, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (passString != null && passString.equals(realPass)) {
                        gotoActivity();
                    } else {
                        passString = "";
                        img_select.setImageResource(R.drawable.un_select_ig);
                        img_select_one.setImageResource(R.drawable.un_select_ig);
                        img_select_two.setImageResource(R.drawable.un_select_ig);
                        img_select_three.setImageResource(R.drawable.un_select_ig);

                        Toast.makeText(this, getString(R.string.incorrect_password_txt), Toast.LENGTH_SHORT).show();

//                        textViewDot.setText(passString);
                    }
                    break;

                //
                case setStatus:
                    if (passString.isEmpty()) {
                        passString = "";
                        img_select.setImageResource(R.drawable.un_select_ig);
                        img_select_one.setImageResource(R.drawable.un_select_ig);
                        img_select_two.setImageResource(R.drawable.un_select_ig);
                        img_select_three.setImageResource(R.drawable.un_select_ig);
                        Toast.makeText(LockscreenActivity.this, R.string.please_enter_password, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    img_select.setImageResource(R.drawable.un_select_ig);
                    img_select_one.setImageResource(R.drawable.un_select_ig);
                    img_select_two.setImageResource(R.drawable.un_select_ig);
                    img_select_three.setImageResource(R.drawable.un_select_ig);
                    tempPass = passString;
                    passString = "";
                    status = setStatus1;

//                    textViewHAHA.setText(R.string.confirm_password_txt);
//                    textViewDot.setText(passString);
                    break;

                //
                case setStatus1:
                    //
                    if (passString.equals(tempPass)) {
                        EasylockSP.put("password", passString);
                        Toast.makeText(LockscreenActivity.this, getString(R.string.password_is_set_txt), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LockscreenActivity.this, SecurityActivity.class);
                        intent.putExtra("passStatus", "set");
                        startActivityForResult(intent, 105);
//                        gotoActivity();
                    } else {

                        tempPass = passString;
                        passString = "";
                        tempPass = "";
                        status = setStatus;

                        img_select.setImageResource(R.drawable.un_select_ig);
                        img_select_one.setImageResource(R.drawable.un_select_ig);
                        img_select_two.setImageResource(R.drawable.un_select_ig);
                        img_select_three.setImageResource(R.drawable.un_select_ig);

//                        textViewDot.setText(passString);
//                        textViewHAHA.setText(R.string.enter_a_new_password_txt);
                        Toast.makeText(LockscreenActivity.this, getString(R.string.please_enter_a_new_password_again_txt), Toast.LENGTH_SHORT).show();
                    }
                    break;

                //
                case changeStatus:
                    if (passString.equals(realPass)) {
                        tempPass = passString;
                        passString = "";
                        tempPass = "";
                        status = changeStatus1;

//                        textViewHAHA.setText(R.string.enter_a_new_password_txt);
//                        textViewDot.setText(passString);
                    } else {
                        passString = "";
//                        textViewDot.setText(passString);
                        Toast.makeText(LockscreenActivity.this, getString(R.string.please_enter_current_password_txt), Toast.LENGTH_SHORT).show();
                    }
                    break;

                //
                case changeStatus1:
                    tempPass = passString;
                    passString = "";
                    status = changeStatus2;

//                    textViewHAHA.setText(R.string.confirm_password_txt);
//                    textViewDot.setText(passString);
                    break;

                //
                case changeStatus2:
                    if (passString.equals(tempPass)) {
                        EasylockSP.put("password", passString);
                        Toast.makeText(LockscreenActivity.this, getString(R.string.password_changed_txt), Toast.LENGTH_SHORT).show();
                        gotoActivity();
                    } else {

                        tempPass = passString;
                        passString = "";
                        tempPass = "";
                        status = changeStatus1;

//                        textViewDot.setText(passString);
//                        textViewHAHA.setText(R.string.enter_a_new_password_txt);
                        Toast.makeText(LockscreenActivity.this, getString(R.string.please_enter_a_new_password_again_txt), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        });


        for (int passButtonId : passButtonIds) {
            final ImageView imageView = findViewById(passButtonId);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (passString.length() >= 4) {
                        Toast.makeText(LockscreenActivity.this, getString(R.string.max_8_characters_txt), Toast.LENGTH_SHORT).show();
                    } else {
                        passString += imageView.getTag().toString();
                    }
                    if (passString.length() == 1) {
                        img_select.setImageResource(R.drawable.selece_code_ig);
                    } else if (passString.length() == 2) {
                        img_select_one.setImageResource(R.drawable.selece_code_ig);
                    } else if (passString.length() == 3) {
                        img_select_two.setImageResource(R.drawable.selece_code_ig);
                    } else if (passString.length() == 4) {
                        img_select_three.setImageResource(R.drawable.selece_code_ig);
                    }
//                    textViewDot.setText(passString);
                }
            });
        }
    }

    private String getPassword() {
        return EasylockSP.getString("password", null);
    }

    private void gotoActivity() {
        Intent intent = new Intent(LockscreenActivity.this, classToGoAfter);
        startActivity(intent);
        finish();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void activityClass(Class activityClassToGo) {
        classToGoAfter = activityClassToGo;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK & requestCode == 105) {
            gotoActivity();
        }
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        if (status.equals("check")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            } else {
                ActivityCompat.finishAffinity(this);
            }
        }
    }*/

}
