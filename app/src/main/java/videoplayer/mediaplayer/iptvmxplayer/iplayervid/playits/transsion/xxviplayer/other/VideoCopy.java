package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.other;

import static android.content.Context.STORAGE_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;

import androidx.core.view.PointerIconCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.HideVideoItem;

public class VideoCopy {

    public static boolean copyVideo(String sourcePath, String destinationPath, Activity activity, HideVideoItem hideVideoItem) {
        try {
            File sourceFile = new File(sourcePath);
            File destinationFile = new File(destinationPath);
            destinationFile.getParentFile().mkdirs();

            FileChannel sourceChannel = new FileInputStream(sourceFile).getChannel();
            FileChannel destinationChannel = new FileOutputStream(destinationFile).getChannel();
            destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            sourceChannel.close();
            destinationChannel.close();

            Log.d("TAGGH", "Video copied successfully. ");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            videoGrantAnd11permission(hideVideoItem, activity);
            Log.d("TAGGH", "Error while copying video: " + e.getMessage());
        }
        return false;
    }

    public static void copyVideo(String sourcePath, String destinationPath) {
        try {
            File sourceFile = new File(sourcePath);
            File destinationFile = new File(destinationPath);
            destinationFile.getParentFile().mkdirs();
            FileChannel sourceChannel = new FileInputStream(sourceFile).getChannel();
            FileChannel destinationChannel = new FileOutputStream(destinationFile).getChannel();
            destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            sourceChannel.close();
            destinationChannel.close();
            Log.d("TAGGH", "Video copied successfully. ");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAGGH", "Error while copying video: " + e.getMessage());
        }
    }

    @SuppressLint("WrongConstant")
    public static void videoGrantAnd11permission(HideVideoItem hideVideoItem, Activity activity) {
        Intent intent;
        StorageManager storageManager = (StorageManager) activity.getSystemService(STORAGE_SERVICE);
        String videoFile = getVideoFiles(Environment.DIRECTORY_MOVIES);
        if (Build.VERSION.SDK_INT >= 29) {
            intent = storageManager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
            String replace = intent.getParcelableExtra("android.provider.extra.INITIAL_URI").toString().replace("/root/", "/document/");
            intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(replace + "%3A" + videoFile));
            Log.d("TAG", "videoGrantAnd11permission: " + videoFile);
        } else {
            intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
            intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(videoFile));
        }
        intent.addFlags(2);
        intent.addFlags(1);
        intent.addFlags(128);
        intent.addFlags(64);
        activity.startActivityForResult(intent, PointerIconCompat.TYPE_COPY);
    }

    public static String getVideoFiles(String path) {
        /*    StringBuilder sb = new StringBuilder();
         *//*     sb.append(Environment.getExternalStorageDirectory());*//*
         *//*   sb.append(File.separator);*//*
        sb.append(path);*/
  /*      sb.append(File.separator);
        sb.append("Media");
        sb.append(File.separator);
        sb.append(".Statuses");*/
        return path.replaceAll("/storage/emulated/0/", "").replaceAll("/", "%2F");
    }

}
