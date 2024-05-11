package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import plugin.adsdk.service.NativeAdsAdapter;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.R;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.adapter.VideoAdapter;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.VideoDao;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.VideoDatabase;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.video.VideoItem;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.other.CommonClass;

public class FolderVideoItemActivity extends BaseActivity implements VideoAdapter.DeleteData {

    ImageView imgBack;
    TextView txtFolderVideoName;
    FolderVideoItemActivity activity;
    public RecyclerView recycleFolderVideo;
    VideoDatabase videoDatabase;
    VideoDao videoDao;
    public VideoAdapter videoAdapter;
    int pos = -1;
    File file;
    public String folderVideoPrefsString;
    public ArrayList<VideoItem> folderListItem;
    public static LinearLayout linearToolBar, linearItemBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_video_item);

        nativeAdMedium();

        activity = this;
        videoDatabase = VideoDatabase.getInstance(activity);
        videoDao = videoDatabase.videoDao();

        String name = getIntent().getStringExtra("name");
        String paths = getIntent().getStringExtra("path");

        imgBack = findViewById(R.id.imgBack);
        txtFolderVideoName = findViewById(R.id.txtFolderVideoName);
        recycleFolderVideo = findViewById(R.id.recycleFolderVideo);
        linearToolBar = findViewById(R.id.linearToolBar);
        linearItemBar = findViewById(R.id.linearItemBar);

        txtFolderVideoName.setText(name);
        txtFolderVideoName.setSelected(true);

        SharedPreferences video_preferences = getSharedPreferences("Linear", 0);
        folderVideoPrefsString = video_preferences.getString("layout", "LinearLayoutManager");

        if (videoDao.getAllListFolderVideo(paths).size()>=5){
            nativeAdMedium();
        }else {
            findViewById(R.id.native_ad_container).setVisibility(View.GONE);
        }

        if (folderVideoPrefsString.equals("LinearLayoutManager")) {
            recycleFolderVideo.setLayoutManager(new LinearLayoutManager(activity));
            videoAdapter = new VideoAdapter(activity, new ArrayList<>(), activity, paths, folderVideoPrefsString, linearItemBar);
            recycleFolderVideo.setAdapter(videoAdapter);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemViewType = videoAdapter.getItemViewType(position);
                    if (itemViewType == NativeAdsAdapter.AD) {
                        return 2;
                    }
                    return 1;
                }
            });
            recycleFolderVideo.setLayoutManager(gridLayoutManager);
            videoAdapter = new VideoAdapter(activity, new ArrayList<>(), activity, paths, folderVideoPrefsString, linearItemBar);
            recycleFolderVideo.setAdapter(videoAdapter);
        }


        videoDao.getAllFolderVideo(paths).observe(activity, new Observer<List<VideoItem>>() {
            @Override
            public void onChanged(List<VideoItem> videoItems) {
                folderVideoName(videoItems);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void folderVideoName(List<VideoItem> videoItems) {
        SharedPreferences video_preferences = getSharedPreferences("Sorting", 0);
        Comparator<VideoItem> alphabetically = new Comparator<VideoItem>() {
            @Override
            public int compare(VideoItem item1, VideoItem item2) {
                return item1.getVideodisplayname().compareTo(item2.getVideodisplayname());
            }
        };

        Comparator<VideoItem> alphabeticallyDate = new Comparator<VideoItem>() {
            @Override
            public int compare(VideoItem item1, VideoItem item2) {
                return Long.compare(item1.getDateAdded(), item2.getDateAdded());
            }
        };

        Comparator<VideoItem> reverseAlphabetically = new Comparator<VideoItem>() {
            @Override
            public int compare(VideoItem item1, VideoItem item2) {
                return item2.getVideodisplayname().compareTo(item1.getVideodisplayname());
            }
        };

        Comparator<VideoItem> reverseAlphabeticallyDate = new Comparator<VideoItem>() {
            @Override
            public int compare(VideoItem item1, VideoItem item2) {
                return Long.compare(item2.getDateAdded(), item1.getDateAdded());
            }
        };

        Comparator<VideoItem> alphabeticallySizeSmall = new Comparator<VideoItem>() {
            @Override
            public int compare(VideoItem item1, VideoItem item2) {
                return Long.compare(item1.getVideosize(), item2.getVideosize());
            }
        };

        Comparator<VideoItem> reverseAlphabeticallySizeLarge = new Comparator<VideoItem>() {
            @Override
            public int compare(VideoItem item1, VideoItem item2) {
                return Long.compare(item2.getVideosize(), item1.getVideosize());
            }
        };

        folderVideoPrefsString = video_preferences.getString("nameSort", "AtoZ");
        videoAdapter.videoItems = videoItems;
        if (folderVideoPrefsString.equals("AtoZ")) {
            videoAdapter.videoItems.sort(alphabetically);
        } else if (folderVideoPrefsString.equals("ZtoA")) {
            videoAdapter.videoItems.sort(reverseAlphabetically);
        } else if (folderVideoPrefsString.equals("DateOld")) {
            videoAdapter.videoItems.sort(alphabeticallyDate);
        } else if (folderVideoPrefsString.equals("DateNew")) {
            videoAdapter.videoItems.sort(reverseAlphabeticallyDate);
        } else if (folderVideoPrefsString.equals("SizeSmall")) {
            videoAdapter.videoItems.sort(alphabeticallySizeSmall);
        } else if (folderVideoPrefsString.equals("SizeLarge")) {
            videoAdapter.videoItems.sort(reverseAlphabeticallySizeLarge);
        }
        videoAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteclick(File str, int i, VideoItem videoItem) {
        if (Build.VERSION.SDK_INT >= 30) {
            pos = i;
            Intent b = new Intent();
            b.putExtra("pos", i);
            b.putExtra("flag", true);
            List<File> list = new ArrayList<>();
            list.add(str);
            file = str;
            CommonClass.videoDeleteFiles(list, CommonClass.REQUEST_PERM_DELETE, activity, b);
        } else {
            Uri contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            String where = MediaStore.Video.Media._ID + " = ?";
            String[] args = new String[]{String.valueOf(videoItem.getId())};
            getContentResolver().delete(contentUri, where, args);
            File file = str;
            file.delete();
            removeAt(pos);
            videoDao.deleteByPath(file.getAbsolutePath());
            CommonClass.showToast(activity, getString(R.string.fileDeletedSuccessfully));
        }
    }

    @Override
    public void moveFiles(File str, int i, VideoItem videoItem) {
        if (Build.VERSION.SDK_INT >= 30) {
            pos = i;
            Intent b = new Intent();
            b.putExtra("pos", i);
            b.putExtra("flag", true);
            List<File> list = new ArrayList<>();
            list.add(str);
            file = str;
            CommonClass.moveDeleteFiles(list, CommonClass.REQUEST_PERM_MOVE, activity, b);
        } else {
            Uri contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            String where = MediaStore.Video.Media._ID + " = ?";
            String[] args = new String[]{String.valueOf(videoItem.getId())};
            getContentResolver().delete(contentUri, where, args);
            File file = str;
            file.delete();
            removeAt(pos);
            videoDao.deleteByPath(file.getAbsolutePath());
            CommonClass.showToast(activity, getString(R.string.fileDeletedSuccessfully));
        }
    }

    @Override
    public void deleteclicks(ArrayList<VideoItem> videoItems) {
        folderListItem = videoItems;
        if (Build.VERSION.SDK_INT >= 30) {
            Intent b = new Intent();
            b.putExtra("flag", true);
            List<File> list = new ArrayList<>();
            for (VideoItem videoItem : videoItems) {
                list.add(new File(videoItem.getVideopath()));
            }
            CommonClass.videoDeleteFiles(list, CommonClass.REQUEST_PERM_DELETES, activity, b);
        } else {
            Uri contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            for (VideoItem videoItem : videoItems) {
                String where = MediaStore.Video.Media._ID + " = ?";
                String[] args = new String[]{String.valueOf(videoItem.getId())};
                getContentResolver().delete(contentUri, where, args);
                File file = new File(videoItem.getVideopath());
                if (file.exists() && file.delete()) {
                    videoDao.deleteByPath(file.getAbsolutePath());
                    CommonClass.showToast(activity, getString(R.string.fileDeletedSuccessfully));
                } else {
                    Log.e("DeleteError", "Failed to delete file: " + file.getAbsolutePath());
                }
            }
        }
    }

    private void removeAt(int pos) {
        videoAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CommonClass.REQUEST_PERM_DELETE && resultCode == -1) {
            removeAt(pos);
            videoDao.deleteByPath(file.getAbsolutePath());
        }
        if (requestCode == CommonClass.REQUEST_PERM_MOVE && resultCode == -1) {
            removeAt(pos);
            videoDao.deleteByPath(file.getAbsolutePath());
            file.delete();
        }

        if (requestCode == CommonClass.REQUEST_PERM_DELETES && resultCode == -1) {
            removeAt(pos);
            videoDao.delete(folderListItem);
            linearItemBar.setVisibility(View.GONE);
            linearToolBar.setVisibility(View.VISIBLE);
        }

        if (requestCode == 1031 && resultCode == -1) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, VideoAdapter.newFile.getName());
            int updatedRows = getContentResolver().update(VideoAdapter.uris.get(0), values, null, null);
            if (updatedRows > 0) {
                videoDao.updateById((int) VideoAdapter.VideoItems.getId(), VideoAdapter.newFile.getName(), VideoAdapter.newFile.getAbsolutePath());
            } else {
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (videoAdapter.isSelectionEnabledVideo) {
            videoAdapter.deSelectAll();
        } else {
            backPressed();
        }
    }
}