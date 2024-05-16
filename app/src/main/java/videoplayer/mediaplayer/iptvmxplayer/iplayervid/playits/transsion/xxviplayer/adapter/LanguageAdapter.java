package videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.adapter;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.R;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.language.LanguageActivity;
import videoplayer.mediaplayer.iptvmxplayer.iplayervid.playits.transsion.xxviplayer.model.language_model.Languages;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
    LanguageActivity activity;
    ArrayList<Languages> languages;
    public int selected = -1;
    SharedPreferences mPrefs;

    public LanguageAdapter(LanguageActivity activity, ArrayList<Languages> languages) {
        this.activity = activity;
        this.languages = languages;
        mPrefs = activity.getSharedPreferences("THEME", 0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.language_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Languages languages = this.languages.get(position);
        holder.imgFlag.setImageResource(languages.getImage());
        holder.language_name.setText(languages.getLanguageName());


//        boolean iscustomlight = mPrefs.getBoolean("iscustomlight", false);
//        if (iscustomlight) {
//            holder.language_name.setTextColor(activity.getColor(R.color.day_black));
//        } else {
//            holder.language_name.setTextColor(activity.getColor(R.color.night_black));
//        }

        if (selected == position) {
            holder.language_select.setImageResource(R.drawable.select);
        } else {
            holder.language_select.setImageResource(R.drawable.un_select);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = position;
                LanguageActivity.imgDone.setVisibility(View.VISIBLE);
                notifyDataSetChanged();
            }
        });

        if (position >= this.languages.size() - 1) {
            holder.vBorder.setVisibility(View.GONE);
        } else {
            holder.vBorder.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public int getItemCount() {
        return languages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView language_name;
        ImageView imgFlag, language_select;
        View vBorder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            language_name = itemView.findViewById(R.id.txtLanguageName);
            language_select = itemView.findViewById(R.id.language_select);
            imgFlag = itemView.findViewById(R.id.imgFlag);
            vBorder = itemView.findViewById(R.id.vBorder);
        }
    }
}
