package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.easypasscodelock.Utils;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.util.Log;

import java.util.List;

import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.BaseActivity;


public class LockscreenHandler extends BaseActivity implements ComponentCallbacks2 {
    private static boolean WentToBackground = false;
    private final String TAG = "EasyLock";
    private String packageName = "";

    @Override
    public void onTrimMemory(int i) {
        super.onTrimMemory(i);
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        Log.d("Activity", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName());

        if (taskInfo.size() > 0) {
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            packageName = componentInfo.getPackageName();
        }
        if (!packageName.equals(getPackageName()) && i == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            // We're in the Background
            WentToBackground = true;
            Log.d(TAG, "WentToBackground: " + WentToBackground);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (WentToBackground && EasylockSP.getString("password", null) != null) {
            // We're in the foreground & password != null
            WentToBackground = false;
            Log.d(TAG, "WentToBackground: " + WentToBackground);

            EasyLock.checkPassword(this);
        }
    }
}
