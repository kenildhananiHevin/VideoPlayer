package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.adapter;


import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.ShowVideoActivity.item;
import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.ShowVideoActivity.pathsItem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.R;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.ShowVideoActivity;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.VideoPlayerActivity;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.HideVideoDao;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.VideoDao;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.VideoDatabase;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.HideVideoItem;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.SelectionModelVideo;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.video.VideoItem;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.other.CommonClass;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.other.VideoCopy;

public class HideVideoAdapter extends RecyclerView.Adapter<HideVideoAdapter.ViewHolder> {
    ShowVideoActivity activity;
    public List<HideVideoItem> hideVideoItems;
    public boolean isSelectionEnabledVideo = false;
    SelectionModelVideo selectionModelVideo = new SelectionModelVideo();
    VideoDatabase videoDatabase;
    VideoDao videoDao;
    HideVideoDao hideVideoDao;

    public HideVideoAdapter(ShowVideoActivity activity, List<HideVideoItem> hideVideoItems) {
        this.activity = activity;
        this.hideVideoItems = hideVideoItems;
        videoDatabase = VideoDatabase.getInstance(activity);
        videoDao = videoDatabase.videoDao();
        hideVideoDao = videoDatabase.hideVideoDao();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.video_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HideVideoAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        HideVideoItem hideVideoItem = hideVideoItems.get(position);
        String durations = CommonClass.millisecToTime((int) hideVideoItem.getDuration_milis());
        String dates = CommonClass.convertMillisToTime(new File(hideVideoItem.getVideopath()).lastModified(), "dd/MM/yyyy");
        String hideVideoName = hideVideoItem.getVideodisplayname();
        String hideVideoPath = hideVideoItem.getVideopath();
        String hideVideofinalduration = CommonClass.getStringSizeLengthFile(hideVideoItem.getVideosize());
        String hideVideoSize = hideVideofinalduration;

        Glide.with(activity)
                .load(hideVideoPath)
                .override(200, 200)
                .into(holder.img_video_preview);

        holder.text_video_name.setText("" + hideVideoName);
        holder.txt_video_duration.setText("" + durations);
        holder.txt_video_size.setText("" + hideVideoSize);
        holder.txt_video_date.setText("" + dates);

        holder.unHideVideo.setVisibility(View.VISIBLE);
        holder.more.setVisibility(View.GONE);


        holder.unHideVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item = hideVideoItem;
                String sourcePath = hideVideoItem.getVideopath();
                /*String destinationPath = hideVideoItem.getVideofoldername() + File.separator + hideVideoItem.getVideodisplayname();*/
                String destinationPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + File.separator + hideVideoItem.getVideodisplayname();
                pathsItem = destinationPath;

                if (VideoCopy.copyVideo(sourcePath, destinationPath,activity,hideVideoItem)){
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        MediaScannerConnection.scanFile(activity, new String[]{destinationPath}, null, new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                VideoItem videoItem = new VideoItem(hideVideoItem.getId(),destinationPath,hideVideoItem.getVideodisplayname(),
                                        hideVideoItem.getDuration_milis(),hideVideoItem.getVideosize(),hideVideoItem.getVideofoldername(),hideVideoItem.getVideoquality(),
                                        hideVideoItem.getVideomimetype(),hideVideoItem.getDateAdded());
                                List<VideoItem> items = new ArrayList<>();
                                items.add(videoItem);
                                videoDao.insertVideo(items);
                                hideVideoDao.delete(hideVideoItem);
                                new File(hideVideoItem.getVideopath()).delete();
                            }
                        });
//                    }
                }
            }
        });


       /* holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item = hideVideoItem;
                String vfile = hideVideoItem.getVideodisplayname();
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                if (Build.VERSION.SDK_INT >= 29) {
                    intent.setType("video/*");
                } else {
                    intent.setType("video/*");
                }
                intent.putExtra(Intent.EXTRA_TITLE, vfile);
                Log.d("TAGhj", "onClick: Main " + hideVideoItem.getVideofoldername());
                String path = hideVideoItem.getVideofoldername();
                Uri loc_pickerInitialUri = Uri.parse(path);
                intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, loc_pickerInitialUri);
                activity.startActivityForResult(intent, 108);
            }
        });*/



        holder.text_video_name.setSelected(true);
        holder.txt_video_duration.setSelected(true);
        holder.txt_video_size.setSelected(true);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSelectionEnabledVideo && selectionModelVideo.getSelectedVideo().isEmpty()) {
                    if (new File(hideVideoItem.getVideopath()).length() <= 0) {
                        Toast.makeText(activity, R.string.video_not_supported, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        VideoPlayerActivity.activity.finish();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    Intent intent = new Intent(activity, VideoPlayerActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("from", "Hidden");
                    intent.putExtra("name", hideVideoName);
                    SharedPreferences video_preferences = activity.getSharedPreferences("Sorting", 0);
                    String videoPrefsSorting = video_preferences.getString("nameSort", "AtoZ");
                    intent.putExtra("sorting", videoPrefsSorting);
                    activity.startActivity(intent);
                } else {
                    /*selectionModelVideo.toggleSelection(position, videoItem);
                    isSelectionEnabledVideo = true;
                    notifyItemChanged(position);
                    if (getSelectedVideoMessage() <= 0) {
                        deSelectAll();
                        activity.findViewById(R.id.linearItemBar).setVisibility(View.GONE);
                    }*/
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return hideVideoItems.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_video_duration, text_video_name, txt_video_size, txt_video_date;
        ImageView img_video_preview, unHideVideo,more;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_video_duration = itemView.findViewById(R.id.txt_video_duration);
            text_video_name = itemView.findViewById(R.id.text_video_name);
            txt_video_size = itemView.findViewById(R.id.txt_video_size);
            img_video_preview = itemView.findViewById(R.id.img_video_preview);
            txt_video_date = itemView.findViewById(R.id.txt_video_date);
            unHideVideo = itemView.findViewById(R.id.un_hide);
            more = itemView.findViewById(R.id.more);
        }
    }
}
