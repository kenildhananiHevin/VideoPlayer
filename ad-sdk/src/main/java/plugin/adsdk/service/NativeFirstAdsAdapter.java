package plugin.adsdk.service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import plugin.adsdk.R;

@SuppressWarnings("unused")
public abstract class NativeFirstAdsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final BaseActivity context;

    @LayoutRes
    private final int nativeResource;

    @NativeSize
    private final int size;
    public static final int AD = 1;

    public NativeFirstAdsAdapter(BaseActivity context, @LayoutRes int nativeResource, @NativeSize int size) {
        this.context = context;
        this.nativeResource = nativeResource;
        this.size = size;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == AD) {
            AdRecyclerHolder holder;
            holder = new AdRecyclerHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(nativeResource, viewGroup, false));
            if (size == NativeSize.SMALL) context.nativeAdSmall(holder.native_ad_container);
            else if (size == NativeSize.MID) context.nativeAdMedium(holder.native_ad_container);
            else context.nativeAd(holder.native_ad_container);
            return holder;
        }
        return createView(viewGroup, viewType);
    }

    static class AdRecyclerHolder extends RecyclerView.ViewHolder {
        public FrameLayout native_ad_container;

        AdRecyclerHolder(View view) {
            super(view);
            this.native_ad_container = view.findViewById(R.id.native_ad_container);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder baseHolder, int position) {
        if (getItemViewType(baseHolder.getAdapterPosition()) != AD) {
            bindView(baseHolder, getRealPosition(baseHolder.getAdapterPosition()));
        }
    }

    public abstract void bindView(@NonNull RecyclerView.ViewHolder baseHolder, int position);

    public abstract RecyclerView.ViewHolder createView(@NonNull ViewGroup viewGroup, int viewType);

    public int viewType() {
        return 0;
    }

    public abstract int itemCount();

    private int getRealPosition(int position) {
        int additionalContent = 1; //first position native
        return position - additionalContent;
    }

    @Override
    public int getItemCount() {
        int additionalContent = itemCount() > 0 ? 1 : 0; //first position native
        return itemCount() + additionalContent;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return AD;
        return viewType();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NativeSize {
        int BIG = 0;
        int MID = 2;
        int SMALL = 1;
    }
}
