package videoplayer.mediaplayer.hd.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import videoplayer.mediaplayer.hd.model.HideVideoItem;
import videoplayer.mediaplayer.hd.model.video.VideoFolderSize;
import videoplayer.mediaplayer.hd.model.video.VideoItem;

@Dao
public interface HideVideoDao {

    @Query("SELECT * FROM HideVideoItem ORDER BY DateAdded DESC")
    LiveData<List<HideVideoItem>> getAllHideVideosSortedByDate();

    @Query("SELECT * FROM HideVideoItem ORDER BY DateAdded DESC")
    List<HideVideoItem> getAllHideVideos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideo(List<HideVideoItem> video);

    @Delete
    void delete(HideVideoItem hideVideoItem);
}
