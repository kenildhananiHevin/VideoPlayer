package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.other;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.view.WindowManager;

import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.R;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.VideoPlayerActivity;


public class BrightnessController {

    private static BrightnessController sInstance;

    private int currentBrightnessLevel;


    private BrightnessController() {
        currentBrightnessLevel = 75;
    }

    public static BrightnessController getInstance() {
        if (sInstance == null) {
            sInstance = new BrightnessController();
        }
        return sInstance;
    }

    public float getScreenBrightness(Activity activity) {
        return activity.getWindow().getAttributes().screenBrightness;
    }

    public void setScreenBrightness(Activity activity, final float brightness) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = brightness;
        activity.getWindow().setAttributes(lp);
    }

    public int getCurrentBrightnessLevel() {
        return currentBrightnessLevel;
    }

    public void setCurrentBrightnessLevel(int currentBrightnessLevel) {
        this.currentBrightnessLevel = currentBrightnessLevel;
    }

    public void changeBrightness(Activity activity, final StyledPlayerView playerView, final boolean increase, final boolean canSetAuto) {
        int newBrightnessLevel = (increase ? currentBrightnessLevel + 1 : currentBrightnessLevel - 1);
        if (canSetAuto && newBrightnessLevel < 0)
            currentBrightnessLevel = -1;
        else if (newBrightnessLevel >= 0 && newBrightnessLevel <= 100)
            currentBrightnessLevel = newBrightnessLevel;

        if (currentBrightnessLevel == -1 && canSetAuto)
            setScreenBrightness(activity, WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE);
        else if (currentBrightnessLevel != -1)
            setScreenBrightness(activity, levelToBrightness(currentBrightnessLevel));

        if (currentBrightnessLevel == -1 && canSetAuto) {
            showBrightness(currentBrightnessLevel, true);
        } else {
            showBrightness(currentBrightnessLevel, false);
        }
    }

    public void showBrightness(int brightnessLevel, boolean isAuto) {
        VideoPlayerActivity.rl_brightness.setVisibility(VISIBLE);
        VideoPlayerActivity.rlVolume.setVisibility(GONE);
        VideoPlayerActivity.sbBrightness.setProgress(brightnessLevel);
        if (isAuto) {
            VideoPlayerActivity.tvBrightness.setText(R.string.auto);
        } else {
            VideoPlayerActivity.tvBrightness.setText(brightnessLevel + " ");
        }
        VideoPlayerActivity.playerView.removeCallbacks(VideoPlayerActivity.runnableBrightness);
        VideoPlayerActivity.playerView.postDelayed(VideoPlayerActivity.runnableBrightness, 500);
    }


    public float levelToBrightness(final int level) {
        final double d = 0.064 + 0.936 / (double) 100 * (double) level;
        return (float) (d * d);
    }
}