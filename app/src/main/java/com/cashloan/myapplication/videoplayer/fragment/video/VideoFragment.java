package com.cashloan.myapplication.videoplayer.fragment.video;

import static com.cashloan.myapplication.videoplayer.adapter.VideoAdapter.VideoItems;
import static com.cashloan.myapplication.videoplayer.other.CommonClass.REQUEST_PERM_DELETE;
import static com.cashloan.myapplication.videoplayer.other.CommonClass.showToast;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class VideoFragment extends Fragment implements VideoAdapter.DeleteData {
    VideoDatabase videoDatabase;
    VideoDao videoDao;
    VideoAdapter videoAdapter;
    RecyclerView recycleVideo;
    int pos = -1;
    public static File file;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        videoDatabase = VideoDatabase.getInstance(requireActivity());
        videoDao = videoDatabase.videoDao();

        recycleVideo = view.findViewById(R.id.allvideolist);

        recycleVideo.setLayoutManager(new LinearLayoutManager(requireActivity()));
        videoAdapter = new VideoAdapter(requireActivity(), new ArrayList<>(), VideoFragment.this, "all");
        recycleVideo.setAdapter(videoAdapter);

        videoDao.getAllVideosSortedByName().observe(requireActivity(), new Observer<List<VideoItem>>() {
            @Override
            public void onChanged(List<VideoItem> videoItems) {
                videoAdapter.videoItems = videoItems;
                videoAdapter.notifyDataSetChanged();
            }
        });

        return view;
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
            CommonClass.deleteFiles(list, REQUEST_PERM_DELETE, requireActivity(), b);
        } else {
            Uri contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            String where = MediaStore.Video.Media._ID + " = ?";
            String[] args = new String[]{String.valueOf(videoItem.getId())};
            requireActivity().getContentResolver().delete(contentUri, where, args);
            File file = str;
            file.delete();
            removeAt(pos);
            videoDao.deleteByPath(file.getAbsolutePath());
            showToast(requireActivity(), getString(R.string.fileDeletedSuccessfully));
        }
    }

    private void removeAt(int pos) {
        videoAdapter.notifyDataSetChanged();
    }

}