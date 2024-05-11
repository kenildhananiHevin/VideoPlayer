package videoplayer.mediaplayer.hd.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import videoplayer.mediaplayer.hd.R;
import videoplayer.mediaplayer.hd.language.LanguageActivity;
import videoplayer.mediaplayer.hd.model.language_model.Languages;

import java.util.ArrayList;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
    LanguageActivity activity;
    ArrayList<Languages> languages;
    public int selected = -1;

    public LanguageAdapter(LanguageActivity activity, ArrayList<Languages> languages) {
        this.activity = activity;
        this.languages = languages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.language_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageAdapter.ViewHolder holder, int position) {

        Languages languages = this.languages.get(position);
        holder.imgFlag.setImageResource(languages.getImage());
        holder.language_name.setText(languages.getLanguageName());

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
        }else {
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
