package videoplayer.mediaplayer.hd.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import videoplayer.mediaplayer.hd.model.video.VideoItem;

public class SelectionModelVideo {
    private Set<Integer> selectedItemsVideo = new HashSet<>();
    private List<VideoItem> selectedVideo = new ArrayList<>();

    public boolean isSelected(VideoItem videoItem) {
        for (VideoItem it : selectedVideo) {
            if (((VideoItem) it).getId() == videoItem.getId()) {
                return true;
            }
        }
        return false;
    }

    public void toggleSelection(int position, VideoItem videoItem) {
        if (isSelected(videoItem)) {
            selectedVideo.removeIf(it -> ((VideoItem) it).getId() == videoItem.getId());
            selectedItemsVideo.remove(position);
        } else {
            selectedVideo.add(videoItem);
            selectedItemsVideo.add(position);
        }
    }

    public void selectAll(ArrayList<VideoItem> list) {
        selectedItemsVideo.clear();
        selectedVideo.clear();
        for (int i = 0; i < list.size(); i++) {
            selectedItemsVideo.add(i);
        }
        selectedVideo.addAll(list);
    }


    public void clearSelection() {
        selectedItemsVideo.clear();
        selectedVideo.clear();
    }
    public ArrayList<VideoItem> getSelectedVideo() {
        return new ArrayList<>(selectedVideo);
    }
}
