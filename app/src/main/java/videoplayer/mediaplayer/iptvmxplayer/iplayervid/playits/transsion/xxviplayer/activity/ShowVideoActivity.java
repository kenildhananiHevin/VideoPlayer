package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.PointerIconCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.R;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.adapter.HideVideoAdapter;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.HideVideoDao;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.VideoDao;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.VideoDatabase;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.HideVideoItem;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.video.VideoItem;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.other.FileHelper;

public class ShowVideoActivity extends BaseActivity {

    ShowVideoActivity activity;
    ImageView imgBack;
    TextView txtShowVideoName;
    public RecyclerView recycleShowVideo;
    VideoDatabase videoDatabase;
    HideVideoDao hideVideoDao;
    VideoDao videoDao;
    public HideVideoAdapter hideVideoAdapter;
    public static LinearLayout linearToolBar, linearItemBar, linearData;
    public static HideVideoItem item;
    public static String pathsItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);

        activity = this;
        videoDatabase = VideoDatabase.getInstance(activity);
        hideVideoDao = videoDatabase.hideVideoDao();
        videoDao = videoDatabase.videoDao();

        imgBack = findViewById(R.id.imgBack);
        txtShowVideoName = findViewById(R.id.txtShowVideoName);
        recycleShowVideo = findViewById(R.id.recycleShowVideo);
        linearToolBar = findViewById(R.id.linearToolBar);
        linearItemBar = findViewById(R.id.linearItemBar);
        linearData = findViewById(R.id.linearData);

        txtShowVideoName.setSelected(true);

        if (hideVideoDao.getAllHideVideosListSortedByDate().size() >= 5) {
            nativeAdMedium();
        } else {
            findViewById(R.id.native_ad_container).setVisibility(View.GONE);
        }

        recycleShowVideo.setLayoutManager(new LinearLayoutManager(activity));
        hideVideoAdapter = new HideVideoAdapter(activity, new ArrayList<>());
        recycleShowVideo.setAdapter(hideVideoAdapter);

        hideVideoDao.getAllHideVideosSortedByDate().observe(activity, new Observer<List<HideVideoItem>>() {
            @Override
            public void onChanged(List<HideVideoItem> hideVideoItems) {
                if (hideVideoItems.isEmpty()) {
                    linearData.setVisibility(View.VISIBLE);
                } else {
                    linearData.setVisibility(View.GONE);
                }
                hideVideoAdapter.hideVideoItems = hideVideoItems;
                hideVideoAdapter.notifyDataSetChanged();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 108) {
            Uri uri = data != null ? data.getData() : null;
            if (uri != null) {
                FileHelper.saveFileToDirectory(activity, uri, item, videoDao, hideVideoDao);
            }
        }

        if (resultCode == RESULT_OK && requestCode == PointerIconCompat.TYPE_COPY){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                MediaScannerConnection.scanFile(activity, new String[]{pathsItem}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        VideoItem videoItem = new VideoItem(item.getId(),pathsItem,item.getVideodisplayname(),
                                item.getDuration_milis(),item.getVideosize(),item.getVideofoldername(),item.getVideoquality(),
                                item.getVideomimetype(),item.getDateAdded());
                        List<VideoItem> items = new ArrayList<>();
                        items.add(videoItem);
                        videoDao.insertVideo(items);
                        hideVideoDao.delete(item);
                        new File(item.getVideopath()).delete();
                    }
                });
            }
        }
    }
}