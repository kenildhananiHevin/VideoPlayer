package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.fragment.video;

import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.MainActivity.linearItemBar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.NativeAdsAdapter;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.R;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.adapter.VideoAdapter;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.VideoDao;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.VideoDatabase;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.video.VideoItem;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.other.CommonClass;

public class VideoFragment extends Fragment implements VideoAdapter.DeleteData {
    VideoDatabase videoDatabase;
    VideoDao videoDao;
    public VideoAdapter videoAdapter;
    public RecyclerView recycleVideo;
    int pos = -1;
    public static File file;
    public static ArrayList<VideoItem> videoListItem;
    public String videoPrefsString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        ((BaseActivity) getActivity()).nativeAdMedium(view.findViewById(R.id.native_ad_container));

        videoDatabase = VideoDatabase.getInstance(requireActivity());
        videoDao = videoDatabase.videoDao();

        recycleVideo = view.findViewById(R.id.allvideolist);

        SharedPreferences video_preferences = requireActivity().getSharedPreferences("Linear", 0);
        videoPrefsString = video_preferences.getString("layout", "LinearLayoutManager");

        if (videoPrefsString.equals("LinearLayoutManager")) {
            recycleVideo.setLayoutManager(new LinearLayoutManager(requireActivity()));
            videoAdapter = new VideoAdapter((BaseActivity) requireActivity(), new ArrayList<>(), VideoFragment.this, "all", videoPrefsString, requireActivity().findViewById(R.id.linearItemBar));
            recycleVideo.setAdapter(videoAdapter);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(), 2);
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
            recycleVideo.setLayoutManager(gridLayoutManager);
            videoAdapter = new VideoAdapter((BaseActivity) requireActivity(), new ArrayList<>(), VideoFragment.this, "all", videoPrefsString, requireActivity().findViewById(R.id.linearItemBar));
            recycleVideo.setAdapter(videoAdapter);
        }


        videoDao.getAllVideosSortedByDate().observe(requireActivity(), new Observer<List<VideoItem>>() {
            @Override
            public void onChanged(List<VideoItem> videoItems) {
                nameSorting(videoItems);
            }
        });

        stringMutableLiveDataSmallAndLargeAndOldDateAndNewDateSize.observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                smallAndLargeAndOldDateAndNewDateSize();
            }
        });

        stringMutableLiveDataAscendingAndDescending.observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ascendingAndDescending();
            }
        });

        stringMutableLiveDataGridVideo.observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                gridVideo();
            }
        });

        stringMutableLiveDataListVideo.observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                listVideo();
            }
        });

        return view;
    }

    public static MutableLiveData<String> stringMutableLiveDataSmallAndLargeAndOldDateAndNewDateSize = new MutableLiveData<>();
    public static MutableLiveData<String> stringMutableLiveDataAscendingAndDescending = new MutableLiveData<>();
    public static MutableLiveData<String> stringMutableLiveDataGridVideo = new MutableLiveData<>();
    public static MutableLiveData<String> stringMutableLiveDataListVideo = new MutableLiveData<>();

    public void smallAndLargeAndOldDateAndNewDateSize(){
        nameSorting(videoAdapter.videoItems);
    }

    public void ascendingAndDescending(){
        nameSorting(videoAdapter.videoItems);
    }

    public void gridVideo(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(), 2);
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
        recycleVideo.setLayoutManager(gridLayoutManager);
        videoAdapter = new VideoAdapter((BaseActivity) requireActivity(), videoAdapter.videoItems, new VideoAdapter.DeleteData() {
            @Override
            public void deleteclick(File str, int i, VideoItem videoItem) {
                deleteclick(str, i, videoItem);
            }

            @Override
            public void moveFiles(File str, int i, VideoItem videoItem) {
                moveFiles(str, i, videoItem);
            }

            @Override
            public void deleteclicks(ArrayList<VideoItem> videoItem) {
                deleteclicks(videoItem);
            }
        }, "all", "GridLayoutManager", linearItemBar);
        recycleVideo.setAdapter(videoAdapter);
    }

    public void listVideo(){
        recycleVideo.setLayoutManager(new LinearLayoutManager(requireActivity()));
        videoAdapter = new VideoAdapter((BaseActivity) requireActivity(), videoAdapter.videoItems, new VideoAdapter.DeleteData() {
            @Override
            public void deleteclick(File str, int i, VideoItem videoItem) {
                deleteclick(str, i, videoItem);
            }

            @Override
            public void moveFiles(File str, int i, VideoItem videoItem) {
                moveFiles(str, i, videoItem);
            }

            @Override
            public void deleteclicks(ArrayList<VideoItem> videoItem) {
                deleteclicks(videoItem);
            }
        }, "all", "LinearLayoutManager", linearItemBar);
        recycleVideo.setAdapter(videoAdapter);
    }

    public void nameSorting(List<VideoItem> videoItems) {
        SharedPreferences video_preferences = requireActivity().getSharedPreferences("Sorting", 0);
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

        videoPrefsString = video_preferences.getString("nameSort", "AtoZ");
        videoAdapter.videoItems = videoItems;
        if (videoPrefsString.equals("AtoZ")) {
            videoAdapter.videoItems.sort(alphabetically);
        } else if (videoPrefsString.equals("ZtoA")) {
            videoAdapter.videoItems.sort(reverseAlphabetically);
        } else if (videoPrefsString.equals("DateOld")) {
            videoAdapter.videoItems.sort(alphabeticallyDate);
        } else if (videoPrefsString.equals("DateNew")) {
            videoAdapter.videoItems.sort(reverseAlphabeticallyDate);
        } else if (videoPrefsString.equals("SizeSmall")) {
            videoAdapter.videoItems.sort(alphabeticallySizeSmall);
        } else if (videoPrefsString.equals("SizeLarge")) {
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
            CommonClass.videoDeleteFiles(list, CommonClass.REQUEST_PERM_DELETE, requireActivity(), b);
        } else {
            Uri contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            String where = MediaStore.Video.Media._ID + " = ?";
            String[] args = new String[]{String.valueOf(videoItem.getId())};
            requireActivity().getContentResolver().delete(contentUri, where, args);
            File file = str;
            file.delete();
            removeAt(pos);
            videoDao.deleteByPath(file.getAbsolutePath());
            CommonClass.showToast(requireActivity(), getString(R.string.fileDeletedSuccessfully));
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
            CommonClass.moveDeleteFiles(list, CommonClass.REQUEST_PERM_MOVE, requireActivity(), b);
        } else {
            Uri contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            String where = MediaStore.Video.Media._ID + " = ?";
            String[] args = new String[]{String.valueOf(videoItem.getId())};
            requireActivity().getContentResolver().delete(contentUri, where, args);
            File file = str;
            file.delete();
            removeAt(pos);
            videoDao.deleteByPath(file.getAbsolutePath());
            CommonClass.showToast(requireActivity(), getString(R.string.fileDeletedSuccessfully));
        }
    }

    @Override
    public void deleteclicks(ArrayList<VideoItem> videoItems) {
        videoListItem = videoItems;
        if (Build.VERSION.SDK_INT >= 30) {
            Intent b = new Intent();
            b.putExtra("flag", true);
            List<File> list = new ArrayList<>();
            for (VideoItem videoItem : videoItems) {
                list.add(new File(videoItem.getVideopath()));
            }
            CommonClass.videoDeleteFiles(list, CommonClass.REQUEST_PERM_DELETES, requireActivity(), b);
        } else {
            Uri contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            for (VideoItem videoItem : videoItems) {
                String where = MediaStore.Video.Media._ID + " = ?";
                String[] args = new String[]{String.valueOf(videoItem.getId())};
                requireActivity().getContentResolver().delete(contentUri, where, args);
                File file = new File(videoItem.getVideopath());
                if (file.exists() && file.delete()) {
                    videoDao.deleteByPath(file.getAbsolutePath());
                    CommonClass.showToast(requireActivity(), getString(R.string.fileDeletedSuccessfully));
                } else {
                    Log.e("DeleteError", "Failed to delete file: " + file.getAbsolutePath());
                }
            }
        }
    }

    private void removeAt(int pos) {
        videoAdapter.notifyDataSetChanged();
    }

}