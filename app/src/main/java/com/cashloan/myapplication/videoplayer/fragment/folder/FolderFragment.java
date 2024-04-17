package com.cashloan.myapplication.videoplayer.fragment.folder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cashloan.myapplication.videoplayer.R;
import com.cashloan.myapplication.videoplayer.adapter.FolderAdapter;
import com.cashloan.myapplication.videoplayer.database.VideoDao;
import com.cashloan.myapplication.videoplayer.database.VideoDatabase;
import com.cashloan.myapplication.videoplayer.model.video.VideoFolderSize;
import com.cashloan.myapplication.videoplayer.model.video.VideoItem;

import java.util.List;

public class FolderFragment extends Fragment {

    RecyclerView recyclerView;
    VideoDatabase videoDatabase;
    VideoDao videoDao;
    FolderAdapter folderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folder, container, false);

        videoDatabase = VideoDatabase.getInstance(requireActivity());
        videoDao = videoDatabase.videoDao();

        recyclerView = view.findViewById(R.id.allFolderList);

        videoDao.getFolderSizes().observe(requireActivity(), new Observer<List<VideoFolderSize>>() {
            @Override
            public void onChanged(List<VideoFolderSize> videoFolderSizes) {
                recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
                folderAdapter = new FolderAdapter(requireActivity(),videoFolderSizes);
                recyclerView.setAdapter(folderAdapter);
            }
        });


        return view;
    }
}