package videoplayer.mediaplayer.hd.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import videoplayer.mediaplayer.hd.R;
import videoplayer.mediaplayer.hd.adapter.HideVideoAdapter;
import videoplayer.mediaplayer.hd.adapter.VideoAdapter;
import videoplayer.mediaplayer.hd.database.HideVideoDao;
import videoplayer.mediaplayer.hd.database.VideoDao;
import videoplayer.mediaplayer.hd.database.VideoDatabase;
import videoplayer.mediaplayer.hd.model.HideVideoItem;

public class ShowVideoActivity extends BaseActivity {

    ShowVideoActivity activity;
    ImageView imgBack;
    TextView txtShowVideoName;
    public RecyclerView recycleShowVideo;
    VideoDatabase videoDatabase;
    HideVideoDao hideVideoDao;
    public HideVideoAdapter hideVideoAdapter;
    public static LinearLayout linearToolBar, linearItemBar,linearData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);

        nativeAdMedium();

        activity = this;
        videoDatabase = VideoDatabase.getInstance(activity);
        hideVideoDao = videoDatabase.hideVideoDao();

        imgBack = findViewById(R.id.imgBack);
        txtShowVideoName = findViewById(R.id.txtShowVideoName);
        recycleShowVideo = findViewById(R.id.recycleShowVideo);
        linearToolBar = findViewById(R.id.linearToolBar);
        linearItemBar = findViewById(R.id.linearItemBar);
        linearData = findViewById(R.id.linearData);

        txtShowVideoName.setSelected(true);


        recycleShowVideo.setLayoutManager(new LinearLayoutManager(activity));
        hideVideoAdapter = new HideVideoAdapter(activity, new ArrayList<>());
        recycleShowVideo.setAdapter(hideVideoAdapter);

        hideVideoDao.getAllHideVideosSortedByDate().observe(activity, new Observer<List<HideVideoItem>>() {
            @Override
            public void onChanged(List<HideVideoItem> hideVideoItems) {
                if (hideVideoItems.isEmpty()){
                    linearData.setVisibility(View.VISIBLE);
                }else {
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
}