package com.cashloan.myapplication.videoplayer.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cashloan.myapplication.videoplayer.model.video.VideoFolderSize;
import com.cashloan.myapplication.videoplayer.model.video.VideoItem;

import java.util.List;

@Dao
public interface VideoDao {

    @Query("SELECT * FROM VideoItem ORDER BY name ASC")
    LiveData<List<VideoItem>> getAllVideosSortedByName();

    @Query("SELECT * FROM VideoItem ORDER BY name ASC")
    List<VideoItem> getAllVideos();

    @Query("SELECT videofoldername AS folderName, COUNT(*) AS folderSize FROM VideoItem GROUP BY videofoldername")
    LiveData<List<VideoFolderSize>> getFolderSizes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideo(List<VideoItem> video);

    @Query("SELECT * FROM VideoItem where videofoldername = :videofoldername ORDER BY name ASC")
    LiveData<List<VideoItem>> getAllFolderVideo(String videofoldername);

    @Query("SELECT * FROM VideoItem where videofoldername = :videofoldername ORDER BY name ASC")
   List<VideoItem> getAllFolder(String videofoldername);

    @Query("DELETE FROM videoitem WHERE recentvideopath = :path")
    void deleteByPath(String path);

    @Query("Update VideoItem SET recentvideopath=:path, name=:title  WHERE id = :id")
    void updateById(Integer id, String title, String path);

}
