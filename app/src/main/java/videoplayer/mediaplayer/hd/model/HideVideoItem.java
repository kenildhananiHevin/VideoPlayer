package videoplayer.mediaplayer.hd.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(indices = {@Index(value = "recentvideopath", unique = true)})
public class HideVideoItem implements Serializable {
    @PrimaryKey
    long id = -1;
    @ColumnInfo(name = "recentvideopath")
    private String videopath;
    @ColumnInfo(name = "name")
    private String videodisplayname;
    @ColumnInfo(name = "recentvideoduration")
    private long Duration_milis;
    @ColumnInfo(name = "videofolderpath")
    private String videofolderpath;
    @ColumnInfo(name = "isplaylist")
    private boolean isplaylist;
    private long videosize;
    private String videofoldername;
    private String videoquality;
    private String videomimetype;
    private long DateAdded;


    public HideVideoItem() {
    }

    public HideVideoItem(long id, String path, String displayName, long Duration_milis, long size, String folderPath, String videoQuality, String MimeType, long DateAdded) {
        this.id = id;
        this.videopath = path;
        this.videodisplayname = displayName;
        this.Duration_milis = Duration_milis;
        this.videosize = size;
        this.videofoldername = folderPath;
        this.videoquality = videoQuality;
        this.videomimetype = MimeType;
        this.DateAdded = DateAdded;
    }

    public boolean getIsplaylist() {
        return isplaylist;
    }

    public void setIsplaylist(boolean isplaylist) {
        this.isplaylist = isplaylist;
    }

    public String getVideopath() {
        return videopath;
    }

    public void setVideopath(String videopath) {
        this.videopath = videopath;
    }

    public String getVideodisplayname() {
        return videodisplayname;
    }

    public void setVideodisplayname(String videodisplayname) {
        this.videodisplayname = videodisplayname;
    }

    public long getDuration_milis() {
        return Duration_milis;
    }

    public void setDuration_milis(long duration_milis) {
        Duration_milis = duration_milis;
    }

    public long getVideosize() {
        return videosize;
    }

    public void setVideosize(long videosize) {
        this.videosize = videosize;
    }

    public String getVideofoldername() {
        return videofoldername;
    }

    public void setVideofoldername(String videofoldername) {
        this.videofoldername = videofoldername;
    }

    public String getVideoquality() {
        return videoquality;
    }

    public void setVideoquality(String videoquality) {
        this.videoquality = videoquality;
    }

    public String getVideomimetype() {
        return videomimetype;
    }

    public void setVideomimetype(String videomimetype) {
        this.videomimetype = videomimetype;
    }

    public String getVideofolderpath() {
        return videofolderpath;
    }

    public void setVideofolderpath(String videofolderpath) {
        this.videofolderpath = videofolderpath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDateAdded() {
        return DateAdded;
    }

    public void setDateAdded(long dateAdded) {
        DateAdded = dateAdded;
    }

}