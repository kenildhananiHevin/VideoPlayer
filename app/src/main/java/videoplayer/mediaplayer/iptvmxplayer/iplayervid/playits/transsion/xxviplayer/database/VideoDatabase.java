package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.video.VideoItem;

@Database(entities = {VideoItem.class}, version = 2,exportSchema = false)
public abstract class VideoDatabase extends RoomDatabase {
    public abstract VideoDao videoDao();

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
