package videoplayer.mediaplayer.hd.adapter;

import static videoplayer.mediaplayer.hd.activity.MainActivity.linearItemBar;
import static videoplayer.mediaplayer.hd.activity.MainActivity.linearToolBar;
import static videoplayer.mediaplayer.hd.activity.MainActivity.tabLayout;
import static videoplayer.mediaplayer.hd.activity.MainActivity.tabLineView;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import plugin.adsdk.service.BaseActivity;
import videoplayer.mediaplayer.hd.R;
import videoplayer.mediaplayer.hd.activity.FolderVideoItemActivity;
import videoplayer.mediaplayer.hd.activity.VideoPlayerActivity;
import videoplayer.mediaplayer.hd.database.HideVideoDao;
import videoplayer.mediaplayer.hd.database.VideoDao;
import videoplayer.mediaplayer.hd.database.VideoDatabase;
import videoplayer.mediaplayer.hd.model.HideVideoItem;
import videoplayer.mediaplayer.hd.model.SelectionModelVideo;
import videoplayer.mediaplayer.hd.model.video.VideoItem;
import videoplayer.mediaplayer.hd.other.CommonClass;
import videoplayer.mediaplayer.hd.other.LocaleHelper;
import videoplayer.mediaplayer.hd.other.VideoCopy;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    BaseActivity activity;
    public List<VideoItem> videoItems;
    public String list;
    DeleteData deleteData;
    LinearLayout play, rename, share, delete,hide;
    ImageView close;
    TextView txtVideoName;
    public static VideoItem VideoItems;
    public static List<Uri> uris;
    public static File newFile;
    String from;
    VideoDatabase videoDatabase;
    VideoDao videoDao;
    HideVideoDao hideVideoDao;
    public boolean isSelectionEnabledVideo = false;
    SelectionModelVideo selectionModelVideo = new SelectionModelVideo();


    public VideoAdapter(BaseActivity activity, List<VideoItem> videoItems, DeleteData deleteData, String from, String list,View viewById) {
        this.activity = activity;
        this.videoItems = videoItems;
        this.deleteData = deleteData;
        this.from = from;
        this.list = list;
        videoDatabase = VideoDatabase.getInstance(activity);
        videoDao = videoDatabase.videoDao();
        hideVideoDao = videoDatabase.hideVideoDao();
    }


    public void deSelectAll() {
        isSelectionEnabledVideo = false;
        selectionModelVideo.clearSelection();
        activity.findViewById(R.id.linearItemBar).setVisibility(View.GONE);
        activity.findViewById(R.id.linearToolBar).setVisibility(View.VISIBLE);
        try {
            activity.findViewById(R.id.tabLayout).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.tabLineView).setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.getMessage();
        }

        notifyDataSetChanged();
    }

    public int getSelectedVideoMessage() {
        return selectionModelVideo.getSelectedVideo().size();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (list.equals("LinearLayoutManager")) {
            return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.video_item, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.video_grid_item, parent, false));
        }
    }


    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {
        VideoItem videoItem = videoItems.get(position);
        String durations = CommonClass.millisecToTime((int) videoItem.getDuration_milis());
        String dates = CommonClass.convertMillisToTime(new File(videoItem.getVideopath()).lastModified(), "dd/MM/yyyy");
        String videoName = videoItem.getVideodisplayname();
        String videoPath = videoItem.getVideopath();
        String videofinalduration = CommonClass.getStringSizeLengthFile(videoItem.getVideosize());
        String videoSize = videofinalduration;


        Glide.with(activity)
                .load(videoPath)
                .override(200, 200)
                .into(holder.img_video_preview);

        holder.text_video_name.setText("" + videoName);
        holder.txt_video_duration.setText("" + durations);
        holder.txt_video_size.setText("" + videoSize);
        holder.txt_video_date.setText("" + dates);

        holder.text_video_name.setSelected(true);
        holder.txt_video_duration.setSelected(true);
        holder.txt_video_size.setSelected(true);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSelectionEnabledVideo && selectionModelVideo.getSelectedVideo().isEmpty()) {
                    if (new File(videoItem.getVideopath()).length() <= 0) {
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
                    intent.putExtra("from", from);
                    intent.putExtra("name", videoName);
                    SharedPreferences video_preferences = activity.getSharedPreferences("Sorting", 0);
                    String videoPrefsSorting = video_preferences.getString("nameSort", "AtoZ");
                    intent.putExtra("sorting", videoPrefsSorting);
                    activity.startActivity(intent);
                } else {
                    selectionModelVideo.toggleSelection(position, videoItem);
                    isSelectionEnabledVideo = true;
                    notifyItemChanged(position);
                    if (getSelectedVideoMessage() <= 0) {
                        deSelectAll();
                        activity.findViewById(R.id.linearItemBar).setVisibility(View.GONE);
                    }
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                activity.findViewById(R.id.linearItemBar).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.linearToolBar).setVisibility(View.GONE);
                try {
                    activity.findViewById(R.id.tabLayout).setVisibility(View.GONE);
                    activity.findViewById(R.id.tabLineView).setVisibility(View.GONE);
                }catch (Exception e){
                    e.getMessage();
                }

                selectionModelVideo.toggleSelection(position, videoItem);
                isSelectionEnabledVideo = true;
                notifyItemChanged(position);
                if (getSelectedVideoMessage() <= 0) {
                    deSelectAll();
                }
                return true;
            }
        });

        if (selectionModelVideo.isSelected(videoItem)) {
            activity.findViewById(R.id.linearItemBar).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.linearToolBar).setVisibility(View.GONE);
            try {
                activity.findViewById(R.id.tabLayout).setVisibility(View.GONE);
                activity.findViewById(R.id.tabLineView).setVisibility(View.GONE);
            }catch (Exception e){
                e.getMessage();
            }

            holder.selectImg.setVisibility(View.VISIBLE);
            holder.more.setVisibility(View.GONE);
        }else {
            holder.more.setVisibility(View.VISIBLE);
            holder.selectImg.setVisibility(View.GONE);
        }

        activity.findViewById(R.id.imgDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<VideoItem> deleteList = new ArrayList<>();
                deleteList.addAll(selectionModelVideo.getSelectedVideo());
                deSelectAll();
                activity.findViewById(R.id.linearItemBar).setVisibility(View.GONE);
                SharedPreferences preferences = activity.getSharedPreferences("Language", 0);
                String prefsString = preferences.getString("language_code", "en");
                LocaleHelper.setLocale(activity, prefsString);

                AlertDialog delete_dialog = new AlertDialog.Builder(activity, R.style.MyTransparentBottomSheetDialogTheme).create();
                LayoutInflater layoutInflater = activity.getLayoutInflater();
                View view1 = layoutInflater.inflate(R.layout.delete_layout, null);
                delete_dialog.setView(view1);
                delete_dialog.setCanceledOnTouchOutside(false);
                TextView cancel = view1.findViewById(R.id.delete_cancel);
                TextView delete_btn = view1.findViewById(R.id.delete_ok);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete_dialog.dismiss();
                    }
                });

                delete_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteData.deleteclicks(deleteList);
                        delete_dialog.dismiss();
                    }
                });

                delete_dialog.show();
                Window window = delete_dialog.getWindow();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int screenWidth = displayMetrics.widthPixels;
                int dialogWidth = (int) (screenWidth * 0.88);
                window.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
            }
        });

        ((TextView)activity.findViewById(R.id.txtSelected)).setText(activity.getString(R.string.selected,
                selectionModelVideo.getSelectedVideo().size()));

        ((ImageView)activity.findViewById(R.id.imgSelect)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllSelected()){
                    ((ImageView) activity.findViewById(R.id.imgSelect)).setImageResource(R.drawable.selecte_player);
                    deSelectAll();
                    notifyDataSetChanged();
                }else {
                    ((ImageView) activity.findViewById(R.id.imgSelect)).setImageResource(R.drawable.unselect_de_player);
                    selectionModelVideo.selectAll((ArrayList<VideoItem>) videoItems);
                    notifyDataSetChanged();
                }

            }
        });

        ((ImageView)activity.findViewById(R.id.imgDeleteBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deSelectAll();
            }
        });

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = activity.getSharedPreferences("Language", 0);
                String prefsString = preferences.getString("language_code", "en");
                LocaleHelper.setLocale(activity, prefsString);

                AlertDialog dialog = new AlertDialog.Builder(activity, R.style.MyTransparentBottomSheetDialogTheme).create();
                LayoutInflater layoutInflater = activity.getLayoutInflater();
                View view1 = layoutInflater.inflate(R.layout.more_dailog, null);
                dialog.setView(view1);
                dialog.setCanceledOnTouchOutside(false);

                play = view1.findViewById(R.id.play);
                rename = view1.findViewById(R.id.rename);
                share = view1.findViewById(R.id.share);
                delete = view1.findViewById(R.id.delete);
                close = view1.findViewById(R.id.close);
                hide = view1.findViewById(R.id.hide);
                txtVideoName = view1.findViewById(R.id.txtVideoName);
                txtVideoName.setText("" + videoName);
                txtVideoName.setSelected(true);

                hide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sourcePath = videoItem.getVideopath();
                        String destinationPath = activity.getFilesDir().getAbsolutePath() + File.separator + "videos" + File.separator + videoItem.getVideodisplayname();

                        VideoCopy.copyVideo(sourcePath, destinationPath);
                        deleteData.moveFiles(new File(videoItem.getVideopath()), 0, videoItem);
                        HideVideoItem hideVideoItemShow = new HideVideoItem(videoItem.getId(),destinationPath,videoItem.getVideodisplayname(),
                                videoItem.getDuration_milis(),videoItem.getVideosize(),videoItem.getVideofoldername(),videoItem.getVideoquality(),
                                videoItem.getVideomimetype(),videoItem.getDateAdded());
                        List<HideVideoItem> items = new ArrayList<>();
                        items.add(hideVideoItemShow);
                        hideVideoDao.insertVideo(items);
                        dialog.dismiss();
                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommonClass.sharevideo(activity, videoPath);
                        dialog.dismiss();
                    }
                });

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            VideoPlayerActivity.activity.finish();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                        Intent intent = new Intent(activity, VideoPlayerActivity.class);
                        intent.putExtra("position", position);
                        intent.putExtra("from", from);
                        intent.putExtra("name", videoName);
                        activity.startActivity(intent);
                        dialog.dismiss();
                    }
                });

                rename.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VideoItems = videoItems.get(position);
                        SharedPreferences preferences = activity.getSharedPreferences("Language", 0);
                        String prefsString = preferences.getString("language_code", "en");
                        LocaleHelper.setLocale(activity, prefsString);

                        AlertDialog rename_dialog = new AlertDialog.Builder(activity, R.style.MyTransparentBottomSheetDialogTheme).create();
                        LayoutInflater layoutInflater = activity.getLayoutInflater();
                        View view1 = layoutInflater.inflate(R.layout.rename_layout, null);
                        rename_dialog.setView(view1);
                        rename_dialog.setCanceledOnTouchOutside(false);
                        final EditText editText = view1.findViewById(R.id.rename_et);
                        TextView cancel = view1.findViewById(R.id.rename_cancel);
                        TextView rename_btn = view1.findViewById(R.id.rename_ok);
                        final File file = new File(videoPath);
                        String nameText = file.getName();
                        nameText = nameText.substring(0, nameText.lastIndexOf("."));
                        editText.setText(nameText);
                        editText.requestFocus();

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rename_dialog.dismiss();
                            }
                        });

                        rename_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (editText.getText().toString().isEmpty()) {
                                    editText.setError(activity.getString(R.string.please_enter_a_valid_text));
                                } else {
                                    String onlyPath = file.getParentFile().getAbsolutePath();
                                    String ext = file.getAbsolutePath();
                                    ext = ext.substring(ext.lastIndexOf("."));
                                    String newPath = onlyPath + "/" + editText.getText().toString() + ext;
                                    newFile = new File(newPath);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                        MediaScannerConnection.scanFile(activity, new String[]{file.getPath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                                            @Override
                                            public void onScanCompleted(String path, Uri uri) {
                                                uris = new ArrayList<>();
                                                uris.add(uri);
                                                try {
                                                    PendingIntent pi = MediaStore.createWriteRequest(activity.getContentResolver(), uris);
                                                    activity.startIntentSenderForResult(pi.getIntentSender(), 1031, null, 0, 0, PendingIntent.FLAG_IMMUTABLE);
                                                } catch (ActivityNotFoundException e) {
                                                    e.printStackTrace();
                                                } catch (IntentSender.SendIntentException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    } else {
                                        ContentResolver contentResolver = activity.getContentResolver();
                                        ContentValues values = new ContentValues();
                                        Uri fromUri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, String.valueOf(videoItem.getId()));
                                        values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, editText.getText().toString());
                                        try {
                                            contentResolver.update(fromUri, values, null, null);
                                        } catch (Exception e) {
                                        }
                                        MediaScannerConnection.scanFile(activity, new String[]{newFile.getPath(), file.getPath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                                            @Override
                                            public void onScanCompleted(String path, Uri uri) {
                                                videoDao.updateById((int) VideoItems.getId(), newFile.getName(), newFile.getAbsolutePath());
                                            }
                                        });
                                        boolean rename = file.renameTo(newFile);
                                        notifyItemChanged(position);
                                        Toast.makeText(activity, R.string.successfull, Toast.LENGTH_SHORT).show();
                                    }
                                    rename_dialog.dismiss();
                                }
                            }
                        });

                        rename_dialog.show();
                        Window window = rename_dialog.getWindow();
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int screenWidth = displayMetrics.widthPixels;
                        int dialogWidth = (int) (screenWidth * 0.88);
                        window.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                        window.setGravity(Gravity.CENTER);
                        dialog.dismiss();
                    }
                });


                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences preferences = activity.getSharedPreferences("Language", 0);
                        String prefsString = preferences.getString("language_code", "en");
                        LocaleHelper.setLocale(activity, prefsString);

                        AlertDialog delete_dialog = new AlertDialog.Builder(activity, R.style.MyTransparentBottomSheetDialogTheme).create();
                        LayoutInflater layoutInflater = activity.getLayoutInflater();
                        View view1 = layoutInflater.inflate(R.layout.delete_layout, null);
                        delete_dialog.setView(view1);
                        delete_dialog.setCanceledOnTouchOutside(false);
                        TextView cancel = view1.findViewById(R.id.delete_cancel);
                        TextView delete_btn = view1.findViewById(R.id.delete_ok);

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                delete_dialog.dismiss();
                            }
                        });

                        delete_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteData.deleteclick(new File(videoPath), 0, videoItem);
                                delete_dialog.dismiss();
                            }
                        });

                        delete_dialog.show();
                        Window window = delete_dialog.getWindow();
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int screenWidth = displayMetrics.widthPixels;
                        int dialogWidth = (int) (screenWidth * 0.88);
                        window.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                        window.setGravity(Gravity.CENTER);
                        dialog.dismiss();
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int screenWidth = displayMetrics.widthPixels;
                int dialogWidth = (int) (screenWidth * 0.88);
                window.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
            }
        });
        if (isAllSelected()){
            ((ImageView) activity.findViewById(R.id.imgSelect)).setImageResource(R.drawable.unselect_de_player);
        }else {
            ((ImageView) activity.findViewById(R.id.imgSelect)).setImageResource(R.drawable.selecte_player);
        }



    }

    public boolean isAllSelected() {
        return selectionModelVideo.getSelectedVideo().size() >= videoItems.size();
    }


    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    public interface DeleteData {
        void deleteclick(File str, int i, VideoItem videoItem);
        void moveFiles(File str, int i, VideoItem videoItem);
        void deleteclicks(ArrayList<VideoItem> videoItem);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_video_duration, text_video_name, txt_video_size, txt_video_date;
        ImageView img_video_preview, more,selectImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_video_duration = itemView.findViewById(R.id.txt_video_duration);
            text_video_name = itemView.findViewById(R.id.text_video_name);
            txt_video_size = itemView.findViewById(R.id.txt_video_size);
            img_video_preview = itemView.findViewById(R.id.img_video_preview);
            txt_video_date = itemView.findViewById(R.id.txt_video_date);
            more = itemView.findViewById(R.id.more);
            selectImg = itemView.findViewById(R.id.selectImg);
        }
    }
}
