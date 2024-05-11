package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.other;


import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.VideoPlayerActivity.ivVolume;
import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.VideoPlayerActivity.rlVolume;
import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.VideoPlayerActivity.rl_brightness;
import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.VideoPlayerActivity.runnableVolume;
import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.VideoPlayerActivity.sbVolume;
import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.VideoPlayerActivity.tvVolume;
import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.VideoPlayerActivity.videoBackgroundNightMode;
import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.VideoPlayerActivity.videoPlayerView;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Build;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.exoplayer2.ui.StyledPlayerView;

import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.R;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.VideoPlayerActivity;


public class VideoPlayerUtils {
    public static int boostLevel = 0;
    public static boolean isVideoLockMode = false;
    public static boolean isVideoNightMode = false;

    public static boolean isVolumeMin(final AudioManager audioManager) {
        int min = Build.VERSION.SDK_INT >= 28 ? audioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC) : 0;
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == min;
    }

    public static void hideSystemUi(StyledPlayerView playerView) {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    public static void adjustVolume(final Context context,
                                    final StyledPlayerView playerView, final boolean raise, boolean canBoost) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager == null) return;
        final int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        final int volumeMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        boolean volumeActive = volume != 0;

        if (volume != volumeMax) {
            boostLevel = 0;
        }

        if (volume != volumeMax || (boostLevel == 0 && !raise)) {
            if (VideoPlayerActivity.loudnessEnhancer != null)
                VideoPlayerActivity.loudnessEnhancer.setEnabled(false);
            try {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, raise ? AudioManager.ADJUST_RAISE : AudioManager.ADJUST_LOWER, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                final int volumeNew = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (raise && volume == volumeNew && !isVolumeMin(audioManager)) {
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE | AudioManager.FLAG_SHOW_UI);
                } else {
                    volumeActive = volumeNew != 0;
                    showVolume(volumeNew, volumeActive);
                }
            } catch (SecurityException ignored) {

            }
        } else {
            if (raise && boostLevel < 10)
                boostLevel++;
            else if (!raise && boostLevel > 0)
                boostLevel--;

            if (VideoPlayerActivity.loudnessEnhancer != null && canBoost) {
                try {
                    VideoPlayerActivity.loudnessEnhancer.setTargetGain(boostLevel * 200);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
            showVolume(volumeMax + boostLevel, true);
        }

        if (VideoPlayerActivity.loudnessEnhancer != null)
            VideoPlayerActivity.loudnessEnhancer.setEnabled(boostLevel > 0);
    }

    private static void showVolume(int volumeLevel, boolean isVolumeActive) {
        if (volumeLevel > 25) volumeLevel = 25;
        rlVolume.setVisibility(VISIBLE);
        tvVolume.setText((volumeLevel * 4) + " ");
        sbVolume.setProgress(volumeLevel * 4);
        if (isVolumeActive) {
            ivVolume.setImageResource(R.drawable.ply_unmute);
        } else {
            ivVolume.setImageResource(R.drawable.ply_mute);
        }
        rl_brightness.setVisibility(GONE);
        videoPlayerView.removeCallbacks(runnableVolume);
        videoPlayerView.postDelayed(runnableVolume, 500);
    }

    public enum Orientation {
        PORTRAIT(0, R.string.video_orientation_portrait),
        LANDSCAPE(1, R.string.video_orientation_landscape),
        SENSOR(2, R.string.video_orientation_auto);

        public final int value;
        public final int description;

        Orientation(int type, int description) {
            this.value = type;
            this.description = description;
        }
    }

    public static void setOrientation(Activity activity, Orientation orientation) {
        switch (orientation) {
            case LANDSCAPE:
                WindowInsetsControllerCompat windowInsetsController = WindowCompat.getInsetsController(activity.getWindow(), activity.getWindow().getDecorView());
                windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                break;
            case PORTRAIT:
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                break;
            case SENSOR:

                if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ||
                        activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                    WindowInsetsControllerCompat windowInsetsControllers = WindowCompat.getInsetsController(activity.getWindow(), activity.getWindow().getDecorView());
                    windowInsetsControllers.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
                    windowInsetsControllers.hide(WindowInsetsCompat.Type.systemBars());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    }
                }
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                break;
        }
    }

    public static Orientation getNextOrientation(Orientation orientation) {
        switch (orientation) {
            case PORTRAIT:
                return Orientation.LANDSCAPE;
            case LANDSCAPE:
                return Orientation.PORTRAIT;
            default:
                return Orientation.PORTRAIT;
        }
    }

    public static void setLockMode(boolean LockMode) {
        isVideoLockMode = LockMode;
        videoPlayerView.setUseController(LockMode);
    }

    public static void setNightMode(boolean NightMode) {
        videoBackgroundNightMode.setVisibility(NightMode ? VISIBLE : GONE);
        isVideoNightMode = NightMode;
    }

    public static boolean isVideoNightMode() {
        return isVideoNightMode;
    }

}
