package com.cashloan.myapplication.videoplayer.activity;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Icon;
import android.media.audiofx.LoudnessEnhancer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Rational;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.cashloan.myapplication.videoplayer.R;
import com.cashloan.myapplication.videoplayer.database.VideoDao;
import com.cashloan.myapplication.videoplayer.database.VideoDatabase;
import com.cashloan.myapplication.videoplayer.model.video.VideoItem;
import com.cashloan.myapplication.videoplayer.other.BrightnessController;
import com.cashloan.myapplication.videoplayer.other.OnSwipeTouchListener;
import com.cashloan.myapplication.videoplayer.other.VerticalSeekBar;
import com.cashloan.myapplication.videoplayer.other.VideoPlayerUtils;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.google.android.exoplayer2.video.VideoSize;

import java.util.ArrayList;
import java.util.List;

public class VideoPlayerActivity extends BaseActivity {

    public static VideoPlayerActivity activity;
    public static StyledPlayerView playerView;
    private ExoPlayer player;
    VideoDatabase videoDatabase;
    VideoDao videoDao;
    List<VideoItem> videoItems = new ArrayList<>();
    private List<MediaItem> mediaItems;
    private VideoPlayerUtils.Orientation orientation = VideoPlayerUtils.Orientation.PORTRAIT;
    private Object mPictureInPictureParamsBuilder;
    private List<VideoItem> mVideos2 = new ArrayList<>();
    private static final int CONTROL_TYPE_PLAY_PAUSE = 1;
    private static final int CONTROL_TYPE_NEXT = 2;
    private static final int CONTROL_TYPE_PREV = 3;
    private static final String ACTION_MEDIA_CONTROL = "media_control";
    private static final String EXTRA_CONTROL_TYPE = "control_type";
    private BroadcastReceiver mReceiver;
    private boolean isCallOnStop = false;
    public static float subtitleSize = 20f;
    ImageView rotation, imgVolume, imgPipMode,player_back,speed_close,imgSpeed;
    public static LoudnessEnhancer loudnessEnhancer;
    RelativeLayout relativeLayout_touch, relativeLayout_bright;
    TextView txtPlayer,txtSpeed,txtSpeed_255,
    txtSpeed_5,
    txtSpeed_1,
    txtSpeed_25,
    txtSpeed_2;
    LinearLayout linearSpeed;

    public static FrameLayout rl_brightness;
    public static FrameLayout rlVolume;
    public static VerticalSeekBar sbBrightness;
    public static VerticalSeekBar sbVolume;
    public static TextView tvBrightness;
    public static TextView tvVolume;
    public static ImageView ivVolume;
    boolean mute = false;
    float currentSpeed = 1.0f;




    public static final Runnable runnableVolume = new Runnable() {
        @Override
        public void run() {
            rlVolume.setVisibility(GONE);
        }
    };

    public static final Runnable runnableBrightness = new Runnable() {
        @Override
        public void run() {
            rl_brightness.setVisibility(GONE);
        }
    };


    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setLightStatusBar(this);
        clearLightStatusBar(this);
        getWindow().setStatusBarColor(getColor(R.color.black));
        getWindow().setNavigationBarColor(getColor(R.color.black));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        activity = this;


        videoDatabase = VideoDatabase.getInstance(activity);
        videoDao = videoDatabase.videoDao();

        int positions = getIntent().getIntExtra("position", 0);
        String froms = getIntent().getStringExtra("from").toString();
        String names = getIntent().getStringExtra("name");


        if (froms.equals("all")) {
            videoItems = videoDao.getAllVideos();
        } else {
            videoItems = videoDao.getAllFolder(froms);
        }

        playerView = findViewById(R.id.video_view);
        rotation = findViewById(R.id.iv_rotation);
        imgVolume = findViewById(R.id.imgVolume);
        imgPipMode = findViewById(R.id.imgPipMode);
        player_back = findViewById(R.id.player_back);
        relativeLayout_touch = findViewById(R.id.relativeLayout_touch);
        relativeLayout_bright = findViewById(R.id.relativeLayout_bright);
        speed_close = findViewById(R.id.speed_close);
        txtSpeed = findViewById(R.id.txtSpeed);
        linearSpeed = findViewById(R.id.linearSpeed);
        imgSpeed = findViewById(R.id.imgSpeed);

        rl_brightness = findViewById(R.id.rl_brightness);
        rlVolume = findViewById(R.id.rl_volume);
        sbBrightness = findViewById(R.id.sb_brightness);
        tvBrightness = findViewById(R.id.tv_brightness);
        tvVolume = findViewById(R.id.tv_volume);
        sbVolume = findViewById(R.id.sb_volume);
        ivVolume = findViewById(R.id.iv_volume);
        txtPlayer = findViewById(R.id.txtPlayer);
        txtSpeed_255 = findViewById(R.id.txtSpeed_255);
        txtSpeed_5 = findViewById(R.id.txtSpeed_5);
        txtSpeed_1 = findViewById(R.id.txtSpeed_1);
        txtSpeed_25 = findViewById(R.id.txtSpeed_25);
        txtSpeed_2 = findViewById(R.id.txtSpeed_2);

        txtPlayer.setSelected(true);
        txtSpeed.setSelected(true);
        txtPlayer.setText(names);

        imgSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearSpeed.setVisibility(View.VISIBLE);
                playerView.hideController();
            }
        });

        speed_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearSpeed.setVisibility(GONE);
            }
        });

        txtSpeed_255.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSpeed_255.setBackground(getDrawable(R.drawable.select_border));
                txtSpeed_25.setBackground(getDrawable(R.drawable.speed_border));
                txtSpeed_5.setBackground(getDrawable(R.drawable.speed_border));
                txtSpeed_2.setBackground(getDrawable(R.drawable.speed_border));
                txtSpeed_1.setBackground(getDrawable(R.drawable.speed_border));
                currentSpeed = 0.25f;
                PlaybackParameters playbackParameters = new PlaybackParameters(currentSpeed);
                if (player != null) player.setPlaybackParameters(playbackParameters);
                imgSpeed.setImageResource(R.drawable.speed_1);
                linearSpeed.setVisibility(GONE);
            }
        });

        txtSpeed_25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSpeed_25.setBackground(getDrawable(R.drawable.select_border));
                txtSpeed_255.setBackground(getDrawable(R.drawable.speed_border));
                txtSpeed_5.setBackground(getDrawable(R.drawable.speed_border));
                txtSpeed_2.setBackground(getDrawable(R.drawable.speed_border));
                txtSpeed_1.setBackground(getDrawable(R.drawable.speed_border));
                currentSpeed = 1.25f;
                PlaybackParameters playbackParameters = new PlaybackParameters(currentSpeed);
                if (player != null) player.setPlaybackParameters(playbackParameters);
                imgSpeed.setImageResource(R.drawable.speed_3);
                linearSpeed.setVisibility(GONE);
            }
        });

        txtSpeed_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSpeed_5.setBackground(getDrawable(R.drawable.select_border));
                txtSpeed_2.setBackground(getDrawable(R.drawable.speed_border));
                txtSpeed_25.setBackground(getDrawable(R.drawable.speed_border));
                txtSpeed_255.setBackground(getDrawable(R.drawable.speed_border));
                txtSpeed_1.setBackground(getDrawable(R.drawable.speed_border));
                currentSpeed = 0.5f;
                PlaybackParameters playbackParameters = new PlaybackParameters(currentSpeed);
                if (player != null) player.setPlaybackParameters(playbackParameters);
                imgSpeed.setImageResource(R.drawable.speed_2);
                linearSpeed.setVisibility(GONE);
            }
        });

        txtSpeed_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSpeed_2.setBackground(getDrawable(R.drawable.select_border));
                txtSpeed_25.setBackground(getDrawable(R.drawable.speed_border));
                txtSpeed_255.setBackground(getDrawable(R.drawable.speed_border));
                txtSpeed_5.setBackground(getDrawable(R.drawable.speed_border));
                txtSpeed_1.setBackground(getDrawable(R.drawable.speed_border));
                currentSpeed = 2.0f;
                PlaybackParameters playbackParameters = new PlaybackParameters(currentSpeed);
                if (player != null) player.setPlaybackParameters(playbackParameters);
                imgSpeed.setImageResource(R.drawable.speed_4);
                linearSpeed.setVisibility(GONE);
            }
        });

        txtSpeed_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSpeed_1.setBackground(getDrawable(R.drawable.speed_border));
                txtSpeed_5.setBackground(getDrawable(R.drawable.speed_border));
                txtSpeed_2.setBackground(getDrawable(R.drawable.speed_border));
                txtSpeed_25.setBackground(getDrawable(R.drawable.speed_border));
                txtSpeed_255.setBackground(getDrawable(R.drawable.speed_border));
                currentSpeed = 1.0f;
                PlaybackParameters playbackParameters = new PlaybackParameters(currentSpeed);
                if (player != null) player.setPlaybackParameters(playbackParameters);
                imgSpeed.setImageResource(R.drawable.player_speed);
                linearSpeed.setVisibility(GONE);
            }
        });

        player_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgPipMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    enterPiP();
                }
            }
        });

        rotation.setOnClickListener(view -> {
            orientation = VideoPlayerUtils.getNextOrientation(orientation);
            setIconOrientation(orientation);
            VideoPlayerUtils.setOrientation(VideoPlayerActivity.this, orientation);
            resetHideCallbacks();
        });

        imgVolume.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                if (mute) {
                    player.setVolume(100);
                    imgVolume.setImageResource(R.drawable.player_volume);
                    mute = false;
                } else {
                    player.setVolume(0);
                    imgVolume.setImageResource(R.drawable.player_mute);
                    mute = true;
                }
            }
        });

        if (isPiPSupported()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mPictureInPictureParamsBuilder = new PictureInPictureParams.Builder();
            }
            updatePictureInPictureActions(false);
        }

        player = new ExoPlayer.Builder(activity).build();
        mediaItems = new ArrayList<>();
        for (VideoItem videoItem : videoItems) {
            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoItem.getVideopath()));
            mediaItems.add(mediaItem);
        }
        player.setMediaItems(mediaItems);
        player.prepare();
        player.seekTo(positions, 0);
        playerView.setPlayer(player);
        if (!isInPip()) playerView.showController();
        player.play();


        relativeLayout_touch.setOnTouchListener(new OnSwipeTouchListener(activity) {
            @Override
            public void onSwipeDown() {
                super.onSwipeDown();
                VideoPlayerUtils.adjustVolume(VideoPlayerActivity.this, playerView, false, false);
            }

            @Override
            public void onClick() {
                super.onClick();
                if (playerView.isControllerFullyVisible()) {
                    playerView.hideController();
                } else {
                    playerView.showController();
                }
            }

            @Override
            public void onSwipeUp() {
                super.onSwipeUp();
                VideoPlayerUtils.adjustVolume(VideoPlayerActivity.this, playerView, true, false);
            }
        });

        relativeLayout_bright.setOnTouchListener(new OnSwipeTouchListener(activity) {
            @Override
            public void onSwipeDown() {
                super.onSwipeDown();
                BrightnessController.getInstance().changeBrightness(VideoPlayerActivity.this, playerView, false, false);
            }

            @Override
            public void onClick() {
                super.onClick();
                if (playerView.isControllerFullyVisible()) {
                    playerView.hideController();
                } else {
                    playerView.showController();
                }
            }

            @Override
            public void onSwipeUp() {
                super.onSwipeUp();
                BrightnessController.getInstance().changeBrightness(VideoPlayerActivity.this, playerView, true, false);
            }
        });

        DefaultTimeBar timeBar = playerView.findViewById(com.google.android.exoplayer2.ui.R.id.exo_progress);
        if (timeBar != null) {
            timeBar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
            timeBar.setScrubberColor(ContextCompat.getColor(this, R.color.txt_bg));
            timeBar.setPlayedColor(ContextCompat.getColor(this, R.color.txt_bg));
            timeBar.setUnplayedColor(ContextCompat.getColor(this, R.color.white));
            timeBar.setBufferedColor(ContextCompat.getColor(this, R.color.white));
        }

        player.addListener(new Player.Listener() {
            @Override
            public void onVideoSizeChanged(VideoSize videoSize) {
                Player.Listener.super.onVideoSizeChanged(videoSize);
                String videoSizeText = "Video Size: " + videoSize.width + "x" + videoSize.height;
                Log.d("TAGONE", "onVideoSizeChanged: " + videoSizeText);

                boolean isLandscape = videoSize.width > videoSize.height;
                if (isLandscape) {
                    rotation.performClick();
                } else {
                    Log.d("TAGONE", "Video is not in landscape mode");
                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                playerView.setKeepScreenOn(isPlaying);
                if (isPiPSupported()) {
                    updatePictureInPictureActions(isPlaying);
                }
                if (isPlaying) {
                    playerView.setControllerShowTimeoutMs(2500);
                } else {
                    playerView.setControllerShowTimeoutMs(-1);
                }
            }

            public void onAudioSessionIdChanged(int audioSessionId) {
                if (loudnessEnhancer != null) {
                    loudnessEnhancer.release();
                }
                try {
                    loudnessEnhancer = new LoudnessEnhancer(audioSessionId);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void clearLightStatusBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(0);
    }
    public static void setLightStatusBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }


    private boolean isPiPSupported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    boolean hasPiPPermission() {
        final AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        return appOpsManager != null && AppOpsManager.MODE_ALLOWED == appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_PICTURE_IN_PICTURE, android.os.Process.myUid(), getPackageName());
    }

    private boolean isInPip() {
        if (!isPiPSupported()) return false;
        return isInPictureInPictureMode();
    }

    @TargetApi(26)
    void updatePictureInPictureActions(final boolean isPlaying) {
        final ArrayList<RemoteAction> actions = new ArrayList<>();

        final PendingIntent intentPrevious = PendingIntent.getBroadcast(VideoPlayerActivity.this, CONTROL_TYPE_PREV, new Intent(ACTION_MEDIA_CONTROL).putExtra(EXTRA_CONTROL_TYPE, CONTROL_TYPE_PREV), PendingIntent.FLAG_IMMUTABLE);
            final Icon iconPause = Icon.createWithResource(VideoPlayerActivity.this, R.drawable.img_previous_pip);

        final PendingIntent intentPlayPause = PendingIntent.getBroadcast(VideoPlayerActivity.this, CONTROL_TYPE_PLAY_PAUSE, new Intent(ACTION_MEDIA_CONTROL).putExtra(EXTRA_CONTROL_TYPE, CONTROL_TYPE_PLAY_PAUSE), PendingIntent.FLAG_IMMUTABLE);
        final Icon iconPlayPause = Icon.createWithResource(VideoPlayerActivity.this, isPlaying ? R.drawable.ic_pause : R.drawable.img_play_pip);

        final PendingIntent intentNext = PendingIntent.getBroadcast(VideoPlayerActivity.this, CONTROL_TYPE_NEXT, new Intent(ACTION_MEDIA_CONTROL).putExtra(EXTRA_CONTROL_TYPE, CONTROL_TYPE_NEXT), PendingIntent.FLAG_IMMUTABLE);
        final Icon iconNext = Icon.createWithResource(VideoPlayerActivity.this, R.drawable.img_next_pip);

        actions.add(new RemoteAction(iconPause, "Previous", "Previous", intentPrevious));
        actions.add(new RemoteAction(iconPlayPause, isPlaying ? "Pause" : "Play", isPlaying ? "Pause" : "Play", intentPlayPause));
        actions.add(new RemoteAction(iconNext, "Next", "Next", intentNext));

        if (mPictureInPictureParamsBuilder instanceof PictureInPictureParams.Builder) {
            ((PictureInPictureParams.Builder) mPictureInPictureParamsBuilder).setActions(actions);
            try {
                setPictureInPictureParams(((PictureInPictureParams.Builder) mPictureInPictureParamsBuilder).build());
            } catch (IllegalStateException ignored) {
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onUserLeaveHint() {
        if (isPiPSupported() && hasPiPPermission()) enterPiP();
        else super.onUserLeaveHint();
    }

    private void enterPiP() {
        final AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        if (appOpsManager != null && AppOpsManager.MODE_ALLOWED != appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_PICTURE_IN_PICTURE, Process.myUid(), getPackageName())) {
            startActivity(new Intent("android.settings.PICTURE_IN_PICTURE_SETTINGS", Uri.fromParts("package", getPackageName(), null)));
            return;
        }

        playerView.setControllerAutoShow(false);
        playerView.hideController();

        if (player != null) {
            final Format format = player.getVideoFormat();

            if (format != null) {
                final View videoSurfaceView = playerView.getVideoSurfaceView();
                if (videoSurfaceView instanceof SurfaceView) {
                    ((SurfaceView) videoSurfaceView).getHolder().setFixedSize(format.width, format.height);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ((PictureInPictureParams.Builder) mPictureInPictureParamsBuilder).setAspectRatio(new Rational(format.width, format.height));
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            enterPictureInPictureMode(((PictureInPictureParams.Builder) mPictureInPictureParamsBuilder).build());
        }
        mVideos2.addAll(videoItems);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                videoItems.clear();
                videoItems.addAll(mVideos2);
            }
        }, 1000);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        if (isInPictureInPictureMode) {
            setSubtitleSizePiP();
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent == null || !ACTION_MEDIA_CONTROL.equals(intent.getAction()) || player == null) {
                        return;
                    }

                    switch (intent.getIntExtra(EXTRA_CONTROL_TYPE, 0)) {
                        case CONTROL_TYPE_PLAY_PAUSE:
                            if (player != null) {
                                if (player.isPlaying()) {
                                    player.pause();
                                } else {
                                    player.play();
                                }
                            }
                            break;
                        case CONTROL_TYPE_PREV:
                            findViewById(com.google.android.exoplayer2.R.id.exo_prev).performClick();
                            break;
                        case CONTROL_TYPE_NEXT:
                            findViewById(com.google.android.exoplayer2.R.id.exo_next).performClick();
                            break;
                    }
                }
            };
            registerReceiver(mReceiver, new IntentFilter(ACTION_MEDIA_CONTROL));
        } else {
            if (mReceiver != null) {
                unregisterReceiver(mReceiver);
                mReceiver = null;
            }
            if (isCallOnStop) finishAndRemoveTask();
            else {
                setSubtitleSize(subtitleSize, getResources().getConfiguration().orientation);
                playerView.setControllerAutoShow(true);
                if (player != null) {
                    if (player.isPlaying()) VideoPlayerUtils.hideSystemUi(playerView);
                    else playerView.showController();
                }
            }
        }
    }

    public void setSubtitleSize(float subtitlesScale, int orientation) {
        final SubtitleView subtitleView = playerView.getSubtitleView();
        if (subtitleView != null) {
            float size;

            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                size = SubtitleView.DEFAULT_TEXT_SIZE_FRACTION * subtitlesScale;
            } else {
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                float ratio = ((float) metrics.heightPixels / (float) metrics.widthPixels);
                if (ratio < 1) ratio = 1 / ratio;
                size = SubtitleView.DEFAULT_TEXT_SIZE_FRACTION * subtitlesScale / ratio;
            }
            subtitleView.setFractionalTextSize(SubtitleView.DEFAULT_TEXT_SIZE_FRACTION * size);
        }
    }

    void setSubtitleSizePiP() {
        final SubtitleView subtitleView = playerView.getSubtitleView();
        if (subtitleView != null)
            subtitleView.setFractionalTextSize(SubtitleView.DEFAULT_TEXT_SIZE_FRACTION);
    }

    private void setIconOrientation(VideoPlayerUtils.Orientation orientation) {
        switch (orientation) {
            case LANDSCAPE:
            case PORTRAIT:
                rotation.setImageResource(R.drawable.player_rotation);
                break;
        }
    }

    void resetHideCallbacks() {
        if (player != null && player.isPlaying()) {
            playerView.setControllerShowTimeoutMs(2500);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }

    @Override
    public void onBackPressed() {
        if (isPiPSupported() && hasPiPPermission()) {
            enterPiP();
        } else {
            finishAndRemoveTask();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        player.play();
        setSubtitleSize(subtitleSize, getResources().getConfiguration().orientation);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isCallOnStop = true;
        player.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isCallOnStop = false;
    }


}