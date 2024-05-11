package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.HideVideoItem;

@Dao
public interface HideVideoDao {

    @Query("SELECT * FROM HideVideoItem ORDER BY DateAdded DESC")
    LiveData<List<HideVideoItem>> getAllHideVideosSortedByDate();

    @Query("SELECT * FROM HideVideoItem ORDER BY DateAdded DESC")
    List<HideVideoItem> getAllHideVideosListSortedByDate();

    @Query("SELECT * FROM HideVideoItem ORDER BY DateAdded DESC")
    List<HideVideoItem> getAllHideVideos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideo(List<HideVideoItem> video);

    @Delete
    void delete(HideVideoItem hideVideoItem);
}
