package com.cashloan.myapplication.videoplayer.activity;

import static com.cashloan.myapplication.videoplayer.adapter.VideoAdapter.VideoItems;
import static com.cashloan.myapplication.videoplayer.fragment.video.VideoFragment.file;
import static com.cashloan.myapplication.videoplayer.other.CommonClass.REQUEST_PERM_DELETE;
import static com.cashloan.myapplication.videoplayer.other.CommonClass.getStringSizeLengthFile;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cashloan.myapplication.videoplayer.R;
import com.cashloan.myapplication.videoplayer.adapter.VideoAdapter;
import com.cashloan.myapplication.videoplayer.database.VideoDao;
import com.cashloan.myapplication.videoplayer.database.VideoDatabase;
import com.cashloan.myapplication.videoplayer.fragment.folder.FolderFragment;
import com.cashloan.myapplication.videoplayer.fragment.video.VideoFragment;
import com.cashloan.myapplication.videoplayer.model.CustomViewPager;
import com.cashloan.myapplication.videoplayer.model.video.VideoItem;
import com.cashloan.myapplication.videoplayer.other.LocaleHelper;
import com.cashloan.myapplication.videoplayer.setting.SettingActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends BaseActivity {

    VideoDatabase videoDatabase;
    VideoDao videoDao;
    MainActivity activity;
    public ArrayList<VideoItem> videoItemArrayList = new ArrayList<>();
    String[] permissions;
    CustomViewPager viewPager;
    TabLayout tabLayout;
    MyAdapter adapter;
    VideoFragment videoFragment = new VideoFragment();
    FolderFragment folderFragment = new FolderFragment();
    public static ProgressBar progressBar;
    String languageCode;
    ImageView imgSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        Log.d("TAG", "onCreate25689: ");

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabLayout);
        progressBar = findViewById(R.id.progressBar);
        imgSetting = findViewById(R.id.imgSetting);

        SharedPreferences preferences = getSharedPreferences("Language", 0);
        languageCode = preferences.getString("language_code", "en");
        videoDatabase = VideoDatabase.getInstance(activity);
        videoDao = videoDatabase.videoDao();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO

            };
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.POST_NOTIFICATIONS) &&
                        ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_MEDIA_IMAGES) &&
                        ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_MEDIA_VIDEO)) {
                    progressBar.setVisibility(View.GONE);
                    showSnackbar(activity, findViewById(R.id.settingA), R.string.please_allow, R.string.allow, new Runnable() {
                        @Override
                        public void run() {
                            openSettingsDialog();
                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(activity, permissions, 101);
                }
            }
        } else {
            permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    progressBar.setVisibility(View.GONE);
                    showSnackbar(activity, findViewById(R.id.settingA), R.string.please_allow, R.string.allow, new Runnable() {
                        @Override
                        public void run() {
                            openSettingsDialog();
                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(activity, permissions, 101);
                }
            }
        }



        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, SettingActivity.class));
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.folder)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.video)));

        adapter = new MyAdapter(activity, getSupportFragmentManager(), tabLayout.getTabCount());
        adapter.addFragment(folderFragment);
        adapter.addFragment(videoFragment);
        viewPager.setAdapter(adapter);
        viewPager.setPagingEnabled(false);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {

                } else if (tab.getPosition() == 1) {

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (!videoDao.getAllVideos().isEmpty()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });

        }


    }

    public void showSnackbar(Context context, View view, int text, int dismissText, final Runnable onDismiss) {
        Snackbar snackbar = Snackbar.make(
                view,
                context.getString(text),
                Snackbar.LENGTH_INDEFINITE
        );
        snackbar.setBackgroundTint(context.getColor(R.color.white));
        snackbar.setTextColor(context.getColor(R.color.black));
        snackbar.setActionTextColor(context.getColor(R.color.txt_bg));
        snackbar.setAction(context.getString(dismissText), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                if (onDismiss != null) {
                    onDismiss.run();
                }
            }
        });
        snackbar.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (101 == requestCode) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                permissions = new String[]{Manifest.permission.POST_NOTIFICATIONS,
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO

                };
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                    progressBar.setVisibility(View.GONE);
                    showSnackbar(activity, findViewById(R.id.settingA), R.string.please_allow, R.string.allow, new Runnable() {
                        @Override
                        public void run() {
                            openSettingsDialog();
                        }
                    });
                } else {
                    new AsyncCaller().execute();
                }
            } else {
                permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    progressBar.setVisibility(View.GONE);
                    showSnackbar(activity, findViewById(R.id.settingA), R.string.please_allow, R.string.allow, new Runnable() {
                        @Override
                        public void run() {
                            openSettingsDialog();
                        }
                    });
                } else {
                    new AsyncCaller().execute();
                }
            }

        }
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        List<Fragment> list = new ArrayList<>();
        int tabCount;

        public MyAdapter(MainActivity activity, @NonNull FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        public void addFragment(Fragment fragment) {
            list.add(fragment);
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERM_DELETE && resultCode == -1) {
            videoDatabase = VideoDatabase.getInstance(activity);
            videoDao = videoDatabase.videoDao();
            videoDao.deleteByPath(file.getAbsolutePath());
        }

        if (requestCode == 1031 && resultCode == -1) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, VideoAdapter.newFile.getName());
            int updatedRows = getContentResolver().update(VideoAdapter.uris.get(0), values, null, null);
            if (updatedRows > 0) {
                videoDao.updateById((int) VideoItems.getId(), VideoAdapter.newFile.getName(), VideoAdapter.newFile.getAbsolutePath());
            } else {
            }
        }

        if (requestCode == 102) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                permissions = new String[]{Manifest.permission.POST_NOTIFICATIONS,
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO

                };
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            } else {
                permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getAllVideosWithQualityInfo(activity);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private void getAllVideosWithQualityInfo(MainActivity activity) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO

            };
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        } else {
            permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        if (!videoDao.getAllVideos().isEmpty()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });

        }

        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.RESOLUTION
        };

        ContentResolver contentResolver = activity.getContentResolver();
        Uri videosUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(videosUri, projection, null, null, null);
        if (cursor != null) {
            videoItemArrayList.clear();
            Log.d("TAG", "getAllVideosWithQualityInfo: ");
            while (cursor.moveToNext()) {
                int idColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media._ID);
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
                int sizeColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.SIZE);
                int duartionColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DURATION);
                int nameColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
                int resolutionColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION);
                int mimeColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE);
                int dateColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED);

                long videoId = cursor.getLong(idColumnIndex);
                String videoPath = cursor.getString(dataColumnIndex);
                long videoSize = cursor.getLong(sizeColumnIndex);
                String videoName = cursor.getString(nameColumnIndex);
                long Duration_milis = cursor.getLong(duartionColumnIndex);
                /*String videofinalduartion = millisecToTime((int) videoDuration);*/
                String videoResolution = cursor.getString(resolutionColumnIndex);
                String videomine = cursor.getString(mimeColumnIndex);
                long datemine = cursor.getLong(dateColumnIndex);

                try {
                    String foldername = new File(videoPath).getParent();

                    VideoItem videoItem = new VideoItem(videoId, videoPath, videoName, Duration_milis, videoSize, foldername, videoResolution, videomine, datemine);
                    if (videoSize > 0) {
                        videoItemArrayList.add(videoItem);
                    }
                } catch (Exception e) {
                    Log.d("TAG", "getAllVideosWithQualityInfo: " + e.getMessage());
                }

            }
            cursor.close();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
            videoDao.insertVideo(videoItemArrayList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("Language", 0);
        String languageCode = preferences.getString("language_code", "en");
        if (!Objects.equals(this.languageCode, languageCode)) {
            recreate();
        }
    }

    boolean backClick = false;

    @Override
    public void onBackPressed() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                backClick = false;
            }
        }, 2000);
        if (backClick) {
            finishAffinity();
        } else {
            backClick = true;
            SharedPreferences preferences = getSharedPreferences("Language", 0);
            String languageCode = preferences.getString("language_code", "en");
            LocaleHelper.setLocale(activity, languageCode);
            Toast.makeText(this, R.string.press, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        new AsyncCaller().execute();
    }

    private void openSettingsDialog() {
        final String EXTRA_FRAGMENT_ARG_KEY = ":settings:fragment_args_key";
        final String EXTRA_SHOW_FRAGMENT_ARGUMENTS = ":settings:show_fragment_args";
        final String EXTRA_SYSTEM_ALERT_WINDOW = "permission_settings";

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_FRAGMENT_ARG_KEY, EXTRA_SYSTEM_ALERT_WINDOW);

        Uri uri = Uri.fromParts("package", getPackageName(), null);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(uri)
                .putExtra(EXTRA_FRAGMENT_ARG_KEY, EXTRA_SYSTEM_ALERT_WINDOW)
                .putExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS, bundle);
        startActivityForResult(intent, 102);
    }
}