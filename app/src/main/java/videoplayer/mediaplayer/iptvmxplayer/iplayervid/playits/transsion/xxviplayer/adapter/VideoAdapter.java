package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.adapter;

import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.NativeAdsAdapter;
import plugin.adsdk.service.NativeFirstAdsAdapter;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.R;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.VideoPlayerActivity;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.VideoDao;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.VideoDatabase;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.video.VideoItem;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.other.CommonClass;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.other.LocaleHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends NativeFirstAdsAdapter {

    BaseActivity activity;
    public List<VideoItem> videoItems;
    DeleteData deleteData;
    LinearLayout play, rename, share, delete;
    ImageView close;
    TextView txtVideoName;
    public static VideoItem VideoItems;
    public static List<Uri> uris;
    public static File newFile;
    String from;
    VideoDatabase videoDatabase;
    VideoDao videoDao;

    public VideoAdapter(BaseActivity activity, List<VideoItem> videoItems, DeleteData deleteData, String from) {
        super(activity, plugin.adsdk.R.layout.ad_layout_native_medium, NativeSize.MID);
        this.activity = activity;
        this.videoItems = videoItems;
        this.deleteData = deleteData;
        this.from = from;
        videoDatabase = VideoDatabase.getInstance(activity);
        videoDao = videoDatabase.videoDao();

    }

    @NonNull
    @Override
    public ViewHolder createView(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.video_item, parent, false));
    }


    @Override
    public void bindView(@NonNull RecyclerView.ViewHolder baseHolder, int position) {
        final ViewHolder holder = (ViewHolder) baseHolder;
        VideoItem videoItem = videoItems.get(position);
            String Duration = CommonClass.millisecToTime((int) videoItem.getDuration_milis());
            String Date = CommonClass.convertMillisToTime(new File(videoItem.getVideopath()).lastModified(), "dd/MM/yyyy");
            String VideoName = videoItem.getVideodisplayname();
            String VideoPath = videoItem.getVideopath();
            String videofinalduration = CommonClass.getStringSizeLengthFile(videoItem.getVideosize());
            String VideoSize = videofinalduration;


            Glide.with(activity)
                    .load(VideoPath)
                    .override(200, 200)
                    .into(holder.img_video_preview);

            holder.text_video_name.setText("" + VideoName);
            holder.txt_video_duration.setText("" + Duration);
            holder.txt_video_size.setText("" + VideoSize);
            holder.txt_video_date.setText("" + Date);

            holder.text_video_name.setSelected(true);
            holder.txt_video_duration.setSelected(true);
            holder.txt_video_size.setSelected(true);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                    intent.putExtra("name", VideoName);
                    activity.startActivity(intent);
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
                    txtVideoName = view1.findViewById(R.id.txtVideoName);
                    txtVideoName.setText("" + VideoName);
                    txtVideoName.setSelected(true);

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CommonClass.sharevideo(activity, VideoPath);
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
                            intent.putExtra("name", VideoName);
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
                            final File file = new File(VideoPath);
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
                                    deleteData.deleteclick(new File(VideoPath), 0, videoItem);
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
    }

    @Override
    public int itemCount() {
        return videoItems.size();
    }

    public interface DeleteData {
        void deleteclick(File str, int i, VideoItem videoItem);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_video_duration, text_video_name, txt_video_size, txt_video_date;
        ImageView img_video_preview, more;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_video_duration = itemView.findViewById(R.id.txt_video_duration);
            text_video_name = itemView.findViewById(R.id.text_video_name);
            txt_video_size = itemView.findViewById(R.id.txt_video_size);
            img_video_preview = itemView.findViewById(R.id.img_video_preview);
            txt_video_date = itemView.findViewById(R.id.txt_video_date);
            more = itemView.findViewById(R.id.more);
        }
    }
}
