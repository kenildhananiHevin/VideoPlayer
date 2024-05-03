package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import plugin.adsdk.service.BaseActivity;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.R;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.activity.FolderVideoItemActivity;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.video.VideoFolderSize;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    BaseActivity requireActivity;
    public List<VideoFolderSize> folderItem;

    public FolderAdapter(BaseActivity requireActivity, List<VideoFolderSize> folderItem) {
        this.requireActivity = requireActivity;
        this.folderItem = folderItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(requireActivity).inflate(R.layout.folder_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FolderAdapter.ViewHolder holder, int position) {
        VideoFolderSize item = folderItem.get(position);

        String FolderSize = String.valueOf(item.getFolderSize());

        String path = item.getFolderName();
        String[] parts = path.split("/");

        holder.folder_name.setText(parts[parts.length - 1]);
        holder.folder_size.setText("(" + FolderSize + " " + requireActivity.getString(R.string.videos) + ")");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity.showInterstitial(new Intent(requireActivity, FolderVideoItemActivity.class)
                        .putExtra("name", parts[parts.length - 1])
                        .putExtra("path", path));
            }
        });

    }


    @Override
    public int getItemCount() {
        return folderItem.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView folder_name, folder_size;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folder_name = itemView.findViewById(R.id.folder_name);
            folder_size = itemView.findViewById(R.id.folder_size);
        }
    }
}
