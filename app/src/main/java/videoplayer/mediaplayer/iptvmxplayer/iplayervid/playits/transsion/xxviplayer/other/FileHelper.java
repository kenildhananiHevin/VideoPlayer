package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.other;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.HideVideoDao;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.VideoDao;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.HideVideoItem;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.video.VideoItem;

public class FileHelper {

    public static AssetFileDescriptor getAssetFileDescriptor(Context context, File file) {
        try {
            return context.getContentResolver().openAssetFileDescriptor(Uri.fromFile(file), "w");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG", "getAssetFileDescriptor: " + e.getMessage());
        }
        return null;
    }

    public static String videoPath = "";

    public static void saveFileToDirectory(Context context, Uri uri, HideVideoItem hideVideoItem, VideoDao videoDao, HideVideoDao hideVideoDao) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AssetFileDescriptor fd = null;
                try {
                    fd = context.getContentResolver().openAssetFileDescriptor(uri, "w");
                    if (fd != null) {
                        OutputStream fos = fd.createOutputStream();
                        File file = new File(hideVideoItem.getVideopath());
                        InputStream in = new FileInputStream(file);

                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = in.read(buffer)) > 0) {
                            fos.write(buffer, 0, bytesRead);
                        }

                        in.close();
                        fos.close();
                        fd.close();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            MediaScannerConnection.scanFile(context, new String[]{videoPath}, null, new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    VideoItem videoItem = new VideoItem(hideVideoItem.getId(), videoPath, hideVideoItem.getVideodisplayname(),
                                            hideVideoItem.getDuration_milis(), hideVideoItem.getVideosize(), videoPath, hideVideoItem.getVideoquality(),
                                            hideVideoItem.getVideomimetype(), hideVideoItem.getDateAdded());
                                    List<VideoItem> items = new ArrayList<>();
                                    items.add(videoItem);
                                    videoDao.insertVideo(items);
                                    hideVideoDao.delete(hideVideoItem);
                                    new File(hideVideoItem.getVideopath()).delete();
                                }
                            });
                        }
                        Log.d("TAG", "Video file saved to: " + uri.toString());

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fd != null) {
                        try {
                            fd.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    private static String getRealPathFromURI(Context context, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return null;
    }


    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

}
