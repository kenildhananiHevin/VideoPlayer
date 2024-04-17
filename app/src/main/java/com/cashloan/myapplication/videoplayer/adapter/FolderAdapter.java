package com.cashloan.myapplication.videoplayer.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cashloan.myapplication.videoplayer.R;
import com.cashloan.myapplication.videoplayer.activity.FolderVideoItemActivity;
import com.cashloan.myapplication.videoplayer.model.video.VideoFolderSize;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    FragmentActivity requireActivity;
    List<VideoFolderSize> folderItem;

    public FolderAdapter(FragmentActivity requireActivity, List<VideoFolderSize> folderItem) {
        this.requireActivity = requireActivity;
        this.folderItem = folderItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(requireActivity).inflate(R.layout.folder_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FolderAdapter.ViewHolder holder, int position) {
        VideoFolderSize item = folderItem.get(position);

        String FolderSize = String.valueOf(item.getFolderSize());

        String path = item.getFolderName();
        String[] parts = path.split("/");

        holder.folder_name.setText(parts[parts.length - 1]);
        holder.folder_size.setText("(" + FolderSize + " " +requireActivity.getString(R.string.videos) + ")");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity.startActivity(new Intent(requireActivity, FolderVideoItemActivity.class)
                        .putExtra("name", parts[parts.length - 1])
                        .putExtra("path", path));
            }
        });

    }


    @Override
    public int getItemCount() {
        return folderItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView folder_name, folder_size;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folder_name = itemView.findViewById(R.id.folder_name);
            folder_size = itemView.findViewById(R.id.folder_size);
        }
    }
}
