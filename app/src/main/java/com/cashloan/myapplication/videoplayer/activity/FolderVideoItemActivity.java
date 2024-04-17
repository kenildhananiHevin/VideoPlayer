package com.cashloan.myapplication.videoplayer.activity;

import static com.cashloan.myapplication.videoplayer.adapter.VideoAdapter.VideoItems;
import static com.cashloan.myapplication.videoplayer.other.CommonClass.REQUEST_PERM_DELETE;
import static com.cashloan.myapplication.videoplayer.other.CommonClass.showToast;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cashloan.myapplication.videoplayer.R;
import com.cashloan.myapplication.videoplayer.adapter.VideoAdapter;
import com.cashloan.myapplication.videoplayer.database.VideoDao;
import com.cashloan.myapplication.videoplayer.database.VideoDatabase;
import com.cashloan.myapplication.videoplayer.model.video.VideoItem;
import com.cashloan.myapplication.videoplayer.other.CommonClass;

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

        videoDao.getAllFolderVideo(paths).observe(activity, new Observer<List<VideoItem>>() {
            @Override
            public void onChanged(List<VideoItem> videoItems) {
                recycleFolderVideo.setLayoutManager(new LinearLayoutManager(activity));
                videoAdapter = new VideoAdapter(activity, videoItems,activity,paths);
                recycleFolderVideo.setAdapter(videoAdapter);
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
    public void deleteclick(File str, int i) {
        if (Build.VERSION.SDK_INT >= 30) {
            pos = i;
            Intent b = new Intent();
            b.putExtra("pos", i);
            b.putExtra("flag", true);
            List<File> list = new ArrayList<>();
            list.add(str);
            file = str;
            CommonClass.deleteFiles(list, REQUEST_PERM_DELETE, activity, b);
        } else {
            File file = str;
            if (file.exists()) {
                if (file.delete()) {
                    removeAt(pos);
                    showToast(activity, getString(R.string.fileDeletedSuccessfully));
                    finish();
                    return;
                }
                showToast(activity, getString(R.string.fileNotDeleted));
            }
        }
    }

    private void removeAt(int pos) {
        videoAdapter.videoItems.remove(pos);
        videoAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERM_DELETE && resultCode == -1) {
            removeAt(pos);
            videoDao.deleteByPath(file.getAbsolutePath());
        }
        if (requestCode == 1031 && resultCode == -1) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, VideoAdapter.newFile.getName());
            int updatedRows = getContentResolver().update(VideoAdapter.uris.get(0), values, null, null);
            if (updatedRows > 0) {
                videoDao.updateById((int) VideoItems.getId(), VideoAdapter.newFile.getName(), VideoAdapter.newFile.getAbsolutePath());
            } else {
            }
        }
    }
}