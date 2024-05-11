package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.fragment.folder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import plugin.adsdk.service.BaseActivity;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.R;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.adapter.FolderAdapter;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.VideoDao;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.VideoDatabase;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.video.VideoFolderSize;

public class FolderFragment extends Fragment {
    public RecyclerView recyclerView;
    VideoDatabase videoDatabase;
    VideoDao videoDao;
    public FolderAdapter folderAdapter;
    public String folderPrefsString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folder, container, false);

        ((BaseActivity) getActivity()).bannerAd(view.findViewById(R.id.banner_ad_container));

        videoDatabase = VideoDatabase.getInstance(requireActivity());
        videoDao = videoDatabase.videoDao();

        recyclerView = view.findViewById(R.id.allFolderList);

        SharedPreferences folder_preferences = requireActivity().getSharedPreferences("Linear", 0);
        folderPrefsString = folder_preferences.getString("layout", "LinearLayoutManager");
        if (folderPrefsString.equals("LinearLayoutManager")) {
            recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            folderAdapter = new FolderAdapter((BaseActivity) requireActivity(), new ArrayList<>(), folderPrefsString);
            recyclerView.setAdapter(folderAdapter);
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
            folderAdapter = new FolderAdapter((BaseActivity) requireActivity(), new ArrayList<>(), folderPrefsString);
            recyclerView.setAdapter(folderAdapter);
        }

        videoDao.getFolderSizes().observe(requireActivity(), new Observer<List<VideoFolderSize>>() {
            @Override
            public void onChanged(List<VideoFolderSize> videoFolderSizes) {
                folderName(videoFolderSizes);
            }
        });

        stringMutableLiveDataGrid.observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                updateGridRecycle();
            }
        });

        stringMutableLiveDataList.observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                updateListRecycle();
            }
        });

        stringMutableLiveDataAscendingSort.observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ascendingSort();
            }
        });

        stringMutableLiveDataDescendingSort.observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                descendingSort();
            }
        });

        stringMutableLiveDataSmallestSize.observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                smallestSize();
            }
        });

        return view;
    }

    public static MutableLiveData<String> stringMutableLiveDataGrid = new MutableLiveData<>();
    public static MutableLiveData<String> stringMutableLiveDataList = new MutableLiveData<>();
    public static MutableLiveData<String> stringMutableLiveDataAscendingSort = new MutableLiveData<>();
    public static MutableLiveData<String> stringMutableLiveDataDescendingSort = new MutableLiveData<>();
    public static MutableLiveData<String> stringMutableLiveDataSmallestSize = new MutableLiveData<>();

    public void updateGridRecycle() {
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        folderAdapter = new FolderAdapter((BaseActivity) requireActivity(), folderAdapter.folderItem, "GridLayoutManager");
        recyclerView.setAdapter(folderAdapter);
    }

    public void updateListRecycle() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        folderAdapter = new FolderAdapter((BaseActivity) requireActivity(), folderAdapter.folderItem, "LinearLayoutManager");
        recyclerView.setAdapter(folderAdapter);
    }

    public void ascendingSort(){
        folderName(folderAdapter.folderItem);
    }

    public void descendingSort(){
        folderName(folderAdapter.folderItem);
    }

    public void smallestSize(){
        folderName(folderAdapter.folderItem);
    }

    public void folderName(List<VideoFolderSize> videoFolderSizes) {
        SharedPreferences folder_preferences = requireActivity().getSharedPreferences("Sorting", 0);
        Comparator<VideoFolderSize> alphabetically = new Comparator<VideoFolderSize>() {
            @Override
            public int compare(VideoFolderSize item1, VideoFolderSize item2) {
                return item1.getFolderName().compareTo(item2.getFolderName());
            }
        };
        Comparator<VideoFolderSize> reverseAlphabetically = new Comparator<VideoFolderSize>() {
            @Override
            public int compare(VideoFolderSize item1, VideoFolderSize item2) {
                return item2.getFolderName().compareTo(item1.getFolderName());
            }
        };

        Comparator<VideoFolderSize> alphabeticallySizeSmall = new Comparator<VideoFolderSize>() {
            @Override
            public int compare(VideoFolderSize item1, VideoFolderSize item2) {
                return Long.compare(item1.getFolderSize(), item2.getFolderSize());
            }
        };

        Comparator<VideoFolderSize> reverseAlphabeticallySizeLarge = new Comparator<VideoFolderSize>() {
            @Override
            public int compare(VideoFolderSize item1, VideoFolderSize item2) {
                return Long.compare(item2.getFolderSize(), item1.getFolderSize());
            }
        };

        folderPrefsString = folder_preferences.getString("nameSort", "AtoZ");
        folderAdapter.folderItem = videoFolderSizes;
        if (folderPrefsString.equals("AtoZ")) {
            folderAdapter.folderItem.sort(alphabetically);
        } else if (folderPrefsString.equals("ZtoA")) {
            folderAdapter.folderItem.sort(reverseAlphabetically);
        } else if (folderPrefsString.equals("SizeSmall")) {
            folderAdapter.folderItem.sort(alphabeticallySizeSmall);
        } else if (folderPrefsString.equals("SizeLarge")) {
            folderAdapter.folderItem.sort(reverseAlphabeticallySizeLarge);
        }
        folderAdapter.notifyDataSetChanged();
    }
}