package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity;

import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.fragment.folder.FolderFragment.stringMutableLiveDataAscendingSort;
import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.fragment.folder.FolderFragment.stringMutableLiveDataDescendingSort;
import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.fragment.folder.FolderFragment.stringMutableLiveDataGrid;
import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.fragment.folder.FolderFragment.stringMutableLiveDataList;
import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.fragment.folder.FolderFragment.stringMutableLiveDataSmallestSize;
import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.fragment.video.VideoFragment.file;
import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.fragment.video.VideoFragment.stringMutableLiveDataAscendingAndDescending;
import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.fragment.video.VideoFragment.stringMutableLiveDataGridVideo;
import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.fragment.video.VideoFragment.stringMutableLiveDataListVideo;
import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.fragment.video.VideoFragment.stringMutableLiveDataSmallAndLargeAndOldDateAndNewDateSize;
import static videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.fragment.video.VideoFragment.videoListItem;

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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.easypasscodelock.Utils.EasyLock;
import plugin.adsdk.service.AppOpenManager;

import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.R;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.adapter.VideoAdapter;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.VideoDao;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database.VideoDatabase;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.fragment.folder.FolderFragment;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.fragment.video.VideoFragment;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.CustomViewPager;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.video.VideoItem;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.other.CommonClass;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.other.LocaleHelper;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.setting.SettingActivity;

public class MainActivity extends BaseActivity {

    VideoDatabase videoDatabase;
    VideoDao videoDao;
    MainActivity activity;
    public ArrayList<VideoItem> videoItemArrayList = new ArrayList<>();
    String[] permissions;
    CustomViewPager viewPager;
    public static TabLayout tabLayout;
    MyAdapter adapter;
    VideoFragment videoFragment = new VideoFragment();
    FolderFragment folderFragment = new FolderFragment();
    public static ProgressBar progressBar;
    public static LinearLayout linearToolBar, linearItemBar;
    public static View tabLineView;
    String languageCode;
    ImageView imgSetting, imgMenu,imgLock;
    LinearLayout linearList, linearGrid, linearSort, linearSortZ, linearDateOld, linearDateNew, linearSizeSmall, linearSizeLarge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppOpenManager.blockAppOpen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        Log.d("TAG", "onCreate25689: ");

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabLayout);
        progressBar = findViewById(R.id.progressBar);
        imgSetting = findViewById(R.id.imgSetting);
        imgMenu = findViewById(R.id.imgMenu);
        linearToolBar = findViewById(R.id.linearToolBar);
        linearItemBar = findViewById(R.id.linearItemBar);
        tabLineView = findViewById(R.id.tabLineView);
        imgLock = findViewById(R.id.imgLock);

        SharedPreferences preferences = getSharedPreferences("Language", 0);
        languageCode = preferences.getString("language_code", "en");
        videoDatabase = VideoDatabase.getInstance(activity);
        videoDao = videoDatabase.videoDao();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO

            };
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.POST_NOTIFICATIONS) && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_MEDIA_IMAGES) && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_MEDIA_VIDEO)) {
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
        }else{
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


        imgLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyLock.setPassword(activity, ShowVideoActivity.class);
                /*startActivity(new Intent(activity, ShowVideoActivity.class));*/
            }
        });


        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, SettingActivity.class));
            }
        });

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog menu_dialog = new AlertDialog.Builder(activity, R.style.MyTransparentBottomSheetDialogTheme).create();
                LayoutInflater layoutInflater = getLayoutInflater();
                View inflate = layoutInflater.inflate(R.layout.menu_dailog, null);
                menu_dialog.setView(inflate);

                linearList = inflate.findViewById(R.id.linearList);
                linearGrid = inflate.findViewById(R.id.linearGrid);
                linearSort = inflate.findViewById(R.id.linearSort);
                linearSortZ = inflate.findViewById(R.id.linearSortZ);
                linearDateOld = inflate.findViewById(R.id.linearDateOld);
                linearDateNew = inflate.findViewById(R.id.linearDateNew);
                linearSizeSmall = inflate.findViewById(R.id.linearSizeSmall);
                linearSizeLarge = inflate.findViewById(R.id.linearSizeLarge);

                if (viewPager.getCurrentItem() == 0) {
                    linearDateOld.setVisibility(View.GONE);
                    linearDateNew.setVisibility(View.GONE);
                }

                linearList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences grid_preferences = getSharedPreferences("Linear", 0);
                        grid_preferences.edit().putString("layout", "LinearLayoutManager").apply();
                        if (viewPager.getCurrentItem() == 0) {
                            adapter.notifyDataSetChanged();
                            stringMutableLiveDataList.postValue("");
                        } else if (viewPager.getCurrentItem() == 1) {
                            adapter.notifyDataSetChanged();
                            stringMutableLiveDataListVideo.postValue("");
                        }
                        menu_dialog.dismiss();
                    }
                });

                linearGrid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences grid_preferences = getSharedPreferences("Linear", 0);
                        grid_preferences.edit().putString("layout", "GridLayoutManager").apply();
                        if (viewPager.getCurrentItem() == 0) {
                            adapter.notifyDataSetChanged();
                            stringMutableLiveDataGrid.postValue("");
                        } else if (viewPager.getCurrentItem() == 1) {
                            adapter.notifyDataSetChanged();
                            stringMutableLiveDataGridVideo.postValue("");
                        }
                        menu_dialog.dismiss();
                    }
                });

                linearSort.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences grid_preferences = getSharedPreferences("Sorting", 0);
                        grid_preferences.edit().putString("nameSort", "AtoZ").apply();
                        if (viewPager.getCurrentItem() == 0) {
                            adapter.notifyDataSetChanged();
                            stringMutableLiveDataAscendingSort.postValue("");
                        } else if (viewPager.getCurrentItem() == 1) {
                            adapter.notifyDataSetChanged();
                            stringMutableLiveDataAscendingAndDescending.postValue("");
                        }
                        menu_dialog.dismiss();
                    }
                });

                linearSortZ.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences grid_preferences = getSharedPreferences("Sorting", 0);
                        grid_preferences.edit().putString("nameSort", "ZtoA").apply();
                        if (viewPager.getCurrentItem() == 0) {
                            adapter.notifyDataSetChanged();
                            stringMutableLiveDataDescendingSort.postValue("");
                        } else if (viewPager.getCurrentItem() == 1) {
                            adapter.notifyDataSetChanged();
                            stringMutableLiveDataAscendingAndDescending.postValue("");
                        }
                        menu_dialog.dismiss();
                    }
                });

                linearDateOld.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences grid_preferences = getSharedPreferences("Sorting", 0);
                        grid_preferences.edit().putString("nameSort", "DateOld").apply();
                        if (viewPager.getCurrentItem() == 1) {
                            adapter.notifyDataSetChanged();
                            stringMutableLiveDataSmallAndLargeAndOldDateAndNewDateSize.postValue("");
                        }
                        menu_dialog.dismiss();
                    }
                });

                linearDateNew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences grid_preferences = getSharedPreferences("Sorting", 0);
                        grid_preferences.edit().putString("nameSort", "DateNew").apply();
                        if (viewPager.getCurrentItem() == 1) {
                            adapter.notifyDataSetChanged();
                            stringMutableLiveDataSmallAndLargeAndOldDateAndNewDateSize.postValue("");
                        }
                        menu_dialog.dismiss();
                    }
                });

                linearSizeSmall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences grid_preferences = getSharedPreferences("Sorting", 0);
                        grid_preferences.edit().putString("nameSort", "SizeSmall").apply();
                        if (viewPager.getCurrentItem() == 0) {
                            adapter.notifyDataSetChanged();
                            stringMutableLiveDataSmallestSize.postValue("");
                        } else if (viewPager.getCurrentItem() == 1) {
                            adapter.notifyDataSetChanged();
                            stringMutableLiveDataSmallAndLargeAndOldDateAndNewDateSize.postValue("");
                        }
                        menu_dialog.dismiss();
                    }
                });

                linearSizeLarge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences grid_preferences = getSharedPreferences("Sorting", 0);
                        grid_preferences.edit().putString("nameSort", "SizeLarge").apply();
                        if (viewPager.getCurrentItem() == 0) {
                            adapter.notifyDataSetChanged();
                            stringMutableLiveDataSmallestSize.postValue("");
                        } else if (viewPager.getCurrentItem() == 1) {
                            adapter.notifyDataSetChanged();
                            stringMutableLiveDataSmallAndLargeAndOldDateAndNewDateSize.postValue("");
                        }
                        menu_dialog.dismiss();
                    }
                });


                menu_dialog.show();
                Window window = menu_dialog.getWindow();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int screenWidth = displayMetrics.widthPixels;
                int dialogWidth = (int) (screenWidth * 0.80);
                window.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
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

        if (!videoDao.getAllVideos().isEmpty()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });

        }


    }

    public void showSnackbar(Context context, View view, int text, int dismissText, final Runnable onDismiss) {
        Snackbar snackbar = Snackbar.make(view, context.getString(text), Snackbar.LENGTH_INDEFINITE);
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
                permissions = new String[]{Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO

                };
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
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
        if (requestCode == CommonClass.REQUEST_PERM_DELETE && resultCode == -1) {
            videoDatabase = VideoDatabase.getInstance(activity);
            videoDao = videoDatabase.videoDao();
            videoDao.deleteByPath(file.getAbsolutePath());
        }

        if (requestCode == CommonClass.REQUEST_PERM_MOVE && resultCode == -1) {
            videoDatabase = VideoDatabase.getInstance(activity);
            videoDao = videoDatabase.videoDao();
            videoDao.deleteByPath(file.getAbsolutePath());
            file.delete();
        }

        if (requestCode == CommonClass.REQUEST_PERM_DELETES && resultCode == -1) {
            videoDatabase = VideoDatabase.getInstance(activity);
            videoDao = videoDatabase.videoDao();
            videoDao.delete(videoListItem);
            linearItemBar.setVisibility(View.GONE);
            linearToolBar.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            tabLineView.setVisibility(View.VISIBLE);
        }

        if (requestCode == 1031 && resultCode == -1) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, VideoAdapter.newFile.getName());
            int updatedRows = getContentResolver().update(VideoAdapter.uris.get(0), values, null, null);
            if (updatedRows > 0) {
                videoDao.updateById((int) VideoAdapter.VideoItems.getId(), VideoAdapter.newFile.getName(), VideoAdapter.newFile.getAbsolutePath());
            } else {
            }
        }

        if (requestCode == 102) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                permissions = new String[]{Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO

                };
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
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
            permissions = new String[]{Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO

            };
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        } else {
            permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        if (!videoDao.getAllVideos().isEmpty()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });

        }

        String[] projection = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.MIME_TYPE, MediaStore.Video.Media.DATE_ADDED, MediaStore.Video.Media.RESOLUTION};

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
        if (videoFragment.videoAdapter.isSelectionEnabledVideo) {
            videoFragment.videoAdapter.deSelectAll();
        } else {
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
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(uri).putExtra(EXTRA_FRAGMENT_ARG_KEY, EXTRA_SYSTEM_ALERT_WINDOW).putExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS, bundle);
        startActivityForResult(intent, 102);
    }
}