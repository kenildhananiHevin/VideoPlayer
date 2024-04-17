package com.cashloan.myapplication.videoplayer.fragment.video;

import static com.cashloan.myapplication.videoplayer.adapter.VideoAdapter.VideoItems;
import static com.cashloan.myapplication.videoplayer.other.CommonClass.REQUEST_PERM_DELETE;
import static com.cashloan.myapplication.videoplayer.other.CommonClass.showToast;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cashloan.myapplication.videoplayer.R;
import com.cashloan.myapplication.videoplayer.activity.MainActivity;
import com.cashloan.myapplication.videoplayer.adapter.VideoAdapter;
import com.cashloan.myapplication.videoplayer.database.VideoDao;
import com.cashloan.myapplication.videoplayer.database.VideoDatabase;
import com.cashloan.myapplication.videoplayer.model.video.VideoItem;
import com.cashloan.myapplication.videoplayer.other.CommonClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment implements VideoAdapter.DeleteData {
    VideoDatabase videoDatabase;
    VideoDao videoDao;
    VideoAdapter videoAdapter;
    RecyclerView recycleVideo;
    int pos = -1;
    File file;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        videoDatabase = VideoDatabase.getInstance(requireActivity());
        videoDao = videoDatabase.videoDao();

        recycleVideo = view.findViewById(R.id.allvideolist);

        videoDao.getAllVideosSortedByName().observe(requireActivity(), new Observer<List<VideoItem>>() {
            @Override
            public void onChanged(List<VideoItem> videoItems) {
                recycleVideo.setLayoutManager(new LinearLayoutManager(requireActivity()));
                videoAdapter = new VideoAdapter(requireActivity(),videoItems,VideoFragment.this,"all");
                recycleVideo.setAdapter(videoAdapter);
            }
        });

        return view;
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
            CommonClass.deleteFiles(list, REQUEST_PERM_DELETE, requireActivity(), b);
        } else {
            File file = str;
            if (file.exists()) {
                if (file.delete()) {
                    removeAt(pos);
                    showToast(requireActivity(), getString(R.string.fileDeletedSuccessfully));
                    return;
                }
                showToast(requireActivity(), getString(R.string.fileNotDeleted));
            }
        }
    }

    private void removeAt(int pos) {
        videoAdapter.videoItems.remove(pos);
        videoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERM_DELETE && resultCode == -1) {
            removeAt(pos);
            videoDao.deleteByPath(file.getAbsolutePath());
        }

        if (requestCode == 1031 && resultCode == -1) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, VideoAdapter.newFile.getName());
            int updatedRows = requireActivity().getContentResolver().update(VideoAdapter.uris.get(0), values, null, null);
            if (updatedRows > 0) {
                videoDao.updateById((int) VideoItems.getId(), VideoAdapter.newFile.getName(), VideoAdapter.newFile.getAbsolutePath());
            } else {
            }
        }
    }
}