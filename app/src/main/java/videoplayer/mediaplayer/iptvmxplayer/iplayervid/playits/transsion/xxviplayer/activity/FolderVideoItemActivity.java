package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.R;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.adapter.VideoAdapter;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.VideoDao;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.VideoDatabase;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.video.VideoItem;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.other.CommonClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderVideoItemActivity extends BaseActivity implements VideoAdapter.DeleteData {

    ImageView imgBack;
    TextView txtFolderVideoName;
    FolderVideoItemActivity activity;
    RecyclerView recycleFolderVideo;
    VideoDatabase videoDatabase;
    VideoDao videoDao;
    VideoAdapter videoAdapter;
    int pos = -1;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_video_item);

        activity = this;
        videoDatabase = VideoDatabase.getInstance(activity);
        videoDao = videoDatabase.videoDao();

        String name = getIntent().getStringExtra("name");
        String paths = getIntent().getStringExtra("path");

        imgBack = findViewById(R.id.imgBack);
        txtFolderVideoName = findViewById(R.id.txtFolderVideoName);
        recycleFolderVideo = findViewById(R.id.recycleFolderVideo);

        txtFolderVideoName.setText(name);
        txtFolderVideoName.setSelected(true);

        recycleFolderVideo.setLayoutManager(new LinearLayoutManager(activity));
        videoAdapter = new VideoAdapter(activity, new ArrayList<>(), activity, paths);
        recycleFolderVideo.setAdapter(videoAdapter);

        videoDao.getAllFolderVideo(paths).observe(activity, new Observer<List<VideoItem>>() {
            @Override
            public void onChanged(List<VideoItem> videoItems) {
                videoAdapter.videoItems = videoItems;
                videoAdapter.notifyDataSetChanged();

            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
            CommonClass.deleteFiles(list, CommonClass.REQUEST_PERM_DELETE, activity, b);
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
}