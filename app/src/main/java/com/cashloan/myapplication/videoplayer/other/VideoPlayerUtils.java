package com.cashloan.myapplication.videoplayer.other;


import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.cashloan.myapplication.videoplayer.activity.VideoPlayerActivity.ivVolume;
import static com.cashloan.myapplication.videoplayer.activity.VideoPlayerActivity.playerView;
import static com.cashloan.myapplication.videoplayer.activity.VideoPlayerActivity.rlVolume;
import static com.cashloan.myapplication.videoplayer.activity.VideoPlayerActivity.rl_brightness;
import static com.cashloan.myapplication.videoplayer.activity.VideoPlayerActivity.runnableVolume;
import static com.cashloan.myapplication.videoplayer.activity.VideoPlayerActivity.sbVolume;
import static com.cashloan.myapplication.videoplayer.activity.VideoPlayerActivity.tvVolume;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.cashloan.myapplication.videoplayer.R;
import com.cashloan.myapplication.videoplayer.activity.VideoPlayerActivity;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import java.io.File;
import java.io.InputStream;


public class VideoPlayerUtils {
    public static int boostLevel = 0;

    public static String getRealPathFromURI(Context context, Uri contentURI) {

        String filePath = getRealPathFromURI2(contentURI);
        if (filePath == null || !filePath.substring(filePath.lastIndexOf(File.separatorChar)).contains(".")) {
            try {
                Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int idx = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    filePath = cursor.getString(idx);
                    cursor.close();
                }
                return filePath;
            } catch (Exception e) {
                return "";
            }
        }
        return filePath;
    }

    private static String getRealPathFromURI2(Uri contentURI) {
        String uriPath = contentURI.getPath();
        if (uriPath != null) {
            try {
                String sub1 = uriPath.substring(uriPath.indexOf(File.separator) + 1);
                String sub2 = sub1.substring(sub1.indexOf(File.separator) + 1);
                if ((File.separator + sub2).contains(Environment.getExternalStorageDirectory().toString())) {
                    return File.separator + sub2;
                } else {
                    return Environment.getExternalStorageDirectory().toString() + File.separator + sub2;
                }
            } catch (Exception ignored) {

            }
        }
        return null;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static float pxToDp(float px) {
        return px / Resources.getSystem().getDisplayMetrics().density;
    }

    public static boolean fileExists(final Context context, final Uri uri) {
        if ("file".equals(uri.getScheme())) {
            final File file = new File(uri.getPath());
            return file.exists();
        } else {
            try {
                final InputStream inputStream = context.getContentResolver().openInputStream(uri);
                inputStream.close();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

 /*   public static void hideSystemUi(final CustomStyledPlayerView playerView) {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        // demo
//        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    public static void showSystemUi(final CustomStyledPlayerView playerView) {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }*/

    public static String getFileName(Context context, Uri uri) {
        String result = null;
        try {
            if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                try (Cursor cursor = context.getContentResolver().query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        final int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        if (columnIndex > -1)
                            result = cursor.getString(columnIndex);
                    }
                }
            }
            if (result == null) {
                result = uri.getPath();
                int cut = result.lastIndexOf('/');
                if (cut != -1) {
                    result = result.substring(cut + 1);
                }
            }
//            if (result.indexOf(".") > 0)
//                result = result.substring(0, result.lastIndexOf("."));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result == null ? "" : result;
    }

    public static boolean isVolumeMax(final AudioManager audioManager) {
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

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
        playerView.removeCallbacks(runnableVolume);
        playerView.postDelayed(runnableVolume, 500);
    }

//    public static void setButtonEnabled(final Context context, final ImageView button,
//                                        final boolean enabled) {
//        button.setEnabled(enabled);
//        button.setAlpha(enabled ? (float) context.getResources().getInteger(R.integer.exo_media_button_opacity_percentage_enabled) / 100 : (float) context.getResources().getInteger(R.integer.exo_media_button_opacity_percentage_disabled) / 100
//        );
//    }

  /*  public static void showText(final CustomStyledPlayerView playerView, final String text,
                                final long timeout) {
        playerView.removeCallbacks(playerView.textClearRunnable);
        playerView.clearIcon();
        playerView.setCustomErrorMessage(text);
        playerView.postDelayed(playerView.textClearRunnable, timeout);
    }

    public static void showText(final CustomStyledPlayerView playerView, final String text) {
        showText(playerView, text, 1200);
    }*/

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
            /*case SYSTEM:
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                break;*/
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

    public static boolean isRotated(final Format format) {
        return format.rotationDegrees == 90 || format.rotationDegrees == 270;
    }

    public static boolean isPortrait(final Format format) {
        if (isRotated(format)) {
            return format.width > format.height;
        } else {
            return format.height > format.width;
        }
    }

    public static String formatMilis(long time) {
        final int totalSeconds = Math.abs((int) time / 1000);
        final int seconds = totalSeconds % 60;
        final int minutes = totalSeconds % 3600 / 60;
        final int hours = totalSeconds / 3600;

        return (hours > 0 ? String.format("%d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds));
    }

    public static String formatMilisSign(long time) {
        if (time > -1000 && time < 1000)
            return formatMilis(time);
        else
            return (time < 0 ? "−" : "+") + formatMilis(time);
    }

    public static void log(final String text) {
//        if (BuildConfig.DEBUG) {

//        }
    }

    public static void setViewParams(final View view, int paddingLeft, int paddingTop,
                                     int paddingRight, int paddingBottom, int marginLeft, int marginTop, int marginRight,
                                     int marginBottom) {
        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        view.setLayoutParams(layoutParams);
    }

    public static void setViewParams2(final View view, int paddingLeft, int paddingTop,
                                      int paddingRight, int paddingBottom, int marginLeft, int marginTop, int marginRight,
                                      int marginBottom) {
        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        view.setLayoutParams(layoutParams);
    }

    public static void setViewParams3(final View view, int paddingLeft, int paddingTop,
                                      int paddingRight, int paddingBottom, int marginLeft, int marginTop, int marginRight,
                                      int marginBottom) {
        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        view.setLayoutParams(layoutParams);
    }

    public static boolean isDeletable(final Context context, final Uri uri) {
        try {
            if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                try (Cursor cursor = context.getContentResolver().query(uri, new String[]{DocumentsContract.Document.COLUMN_FLAGS}, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        final int columnIndex = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_FLAGS);
                        if (columnIndex > -1) {
                            int flags = cursor.getInt(columnIndex);
                            return (flags & DocumentsContract.Document.FLAG_SUPPORTS_DELETE) == DocumentsContract.Document.FLAG_SUPPORTS_DELETE;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isSupportedNetworkUri(final Uri uri) {
        final String scheme = uri.getScheme();
        return scheme.startsWith("http") || scheme.equals("rtsp");
    }

   /* public static boolean isTvBox(Activity activity) {
        return activity.getResources().getBoolean(R.bool.tv_box);
    }*/

    /*public static boolean isShownGuide(Context context) {
        return SharedPreferencesUtils.getBoolean(context, "SHOWN_GUIDE", false);
    }

    public static void setShownGuide(Context context) {
        SharedPreferencesUtils.putBoolean(context, "SHOWN_GUIDE", true);
    }*/
}
