package videoplayer.mediaplayer.hd.other;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class CommonClass {

    public static final int REQUEST_PERM_DELETE = 1221;
    public static final int REQUEST_PERM_MOVE = 1223;
    public static final int REQUEST_PERM_DELETES = 1222;

    private static Context context;

    public static void showToast(Context mContext, String toastText) {
        Toast.makeText(mContext, toastText, Toast.LENGTH_SHORT).show();
    }

    public CommonClass(Context context2) {
        context = context2;
    }

    public static void sharevideo(Activity activity, String Path) {
        File f = new File(Path);
        Uri uriPath = Uri.parse(f.getPath());

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey please check this application " + "https://play.google.com/store/apps/details?id=" + activity.getPackageName());
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriPath);
        shareIntent.setType("video/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.startActivity(Intent.createChooser(shareIntent, "send"));
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void videoDeleteFiles(@Nullable List<File> files, final int requestCode, Activity activity, @Nullable Intent fillInIntent) {
        if (files == null || files.isEmpty()) {
            return;
        }
        List<Uri> uris = files.stream().map(file -> {
            long mediaID = getFilePathToMediaID(file.getAbsolutePath(), activity);
            String fileType = getFileType(file);
            if (fileType.equalsIgnoreCase("video")) {
                return ContentUris.withAppendedId(MediaStore.Video.Media.getContentUri("external"), mediaID);
            } else {
                return ContentUris.withAppendedId(MediaStore.Images.Media.getContentUri("external"), mediaID);
            }
        }).collect(Collectors.toList());

        PendingIntent pi = MediaStore.createDeleteRequest(activity.getContentResolver(), uris);
        try {
            IntentSender intentSender = pi.getIntentSender();
            activity.startIntentSenderForResult(intentSender, requestCode, fillInIntent, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void moveDeleteFiles(@Nullable List<File> files, final int requestCode, Activity activity, @Nullable Intent fillInIntent) {
        if (files == null || files.isEmpty()) {
            return;
        }
        List<Uri> uris = files.stream().map(file -> {
            long mediaID = getFilePathToMediaID(file.getAbsolutePath(), activity);
            String fileType = getFileType(file);
            if (fileType.equalsIgnoreCase("video")) {
                return ContentUris.withAppendedId(MediaStore.Video.Media.getContentUri("external"), mediaID);
            } else {
                return ContentUris.withAppendedId(MediaStore.Images.Media.getContentUri("external"), mediaID);
            }
        }).collect(Collectors.toList());

        PendingIntent pi = MediaStore.createWriteRequest(activity.getContentResolver(), uris);
        try {
            IntentSender intentSender = pi.getIntentSender();
            activity.startIntentSenderForResult(intentSender, requestCode, fillInIntent, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }


    private static String getFileType(File file) {
        String mimeType = URLConnection.guessContentTypeFromName(file.getAbsolutePath());
        if (mimeType != null) {
            if (mimeType.startsWith("video")) {
                return "video";
            } else if (mimeType.startsWith("image")) {
                return "image";
            }
        }
        return "";
    }

    private static long getFilePathToMediaID(String songPath, Context context) {
        long id = 0;
        String selection = MediaStore.Audio.Media.DATA + "=?";
        String[] selectionArgs = new String[]{songPath};
        String[] projection = new String[]{MediaStore.Audio.Media._ID};
        try (android.database.Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"),
                projection, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                id = cursor.getLong(idIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static String millisecToTime(int millisec) {
        int sec = millisec / 1000;
        int second = sec % 60;
        int minute = sec / 60;
        int hour = 0;

        if (minute >= 60) {
            hour = minute / 60;
            minute %= 60;
        }

        if (hour > 0) {
            return hour + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second);
        } else {
            return (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second);
        }
    }

    public static String convertMillisToTime(long millis,String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, (Locale.US));
        Calendar calendar = Calendar.getInstance((Locale.US));
        calendar.setTimeInMillis(millis);
        calendar.set(Calendar.SECOND, 0);

        return dateFormat.format(calendar.getTime());
    }


    public static String getStringSizeLengthFile(long size) {
        DecimalFormat df = new DecimalFormat("00.00");
        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;
        if (size < sizeMb)
            return df.format(size / sizeKb) + " KB";
        else if (size < sizeGb)
            return df.format(size / sizeMb) + " MB";
        else if (size < sizeTerra)
            return df.format(size / sizeGb) + " GB";
        return "";
    }

}
