package videoplayer.mediaplayer.hd.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import videoplayer.mediaplayer.hd.model.HideVideoItem;
import videoplayer.mediaplayer.hd.model.video.VideoItem;

@Database(entities = {VideoItem.class, HideVideoItem.class}, version = 3,exportSchema = false)
public abstract class VideoDatabase extends RoomDatabase {
    public abstract VideoDao videoDao();
    public abstract HideVideoDao hideVideoDao();

    public static VideoDatabase instance;

    public static VideoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, VideoDatabase.class, "videoplayerdb")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
