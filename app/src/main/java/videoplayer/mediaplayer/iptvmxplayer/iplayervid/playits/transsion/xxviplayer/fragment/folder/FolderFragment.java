package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.fragment.folder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import plugin.adsdk.service.BaseActivity;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.R;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.adapter.FolderAdapter;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.VideoDao;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.VideoDatabase;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.video.VideoFolderSize;

import java.util.ArrayList;
import java.util.List;

public class FolderFragment extends Fragment {
    RecyclerView recyclerView;
    VideoDatabase videoDatabase;
    VideoDao videoDao;
    FolderAdapter folderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folder, container, false);

        ((BaseActivity) getActivity()).bannerAd(view.findViewById(R.id.banner_ad_container));

        videoDatabase = VideoDatabase.getInstance(requireActivity());
        videoDao = videoDatabase.videoDao();

        recyclerView = view.findViewById(R.id.allFolderList);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        folderAdapter = new FolderAdapter((BaseActivity) requireActivity(),new ArrayList<>());
        recyclerView.setAdapter(folderAdapter);

        videoDao.getFolderSizes().observe(requireActivity(), new Observer<List<VideoFolderSize>>() {
            @Override
            public void onChanged(List<VideoFolderSize> videoFolderSizes) {
                folderAdapter.folderItem = videoFolderSizes;
                folderAdapter.notifyDataSetChanged();
            }
        });


        return view;
    }
}