package com.awesomewallpaper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.WallpaperViewHolder> {

    private Context context;
    private List<WallpaperModel> wallpaperModelList;

    public WallpaperAdapter(Context context, List<WallpaperModel> wallpaperModelList) {
        this.context = context;
        this.wallpaperModelList = wallpaperModelList;
    }

    @NonNull
    @Override
    public WallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wallpaper_item, parent, false);
        return new WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperViewHolder holder, int position) {
        WallpaperModel wallpaperModel = wallpaperModelList.get(position);
        Glide.with(context)
                .load(wallpaperModel.getMediumUrl())
                .into(holder.imageView);

        // Set up click listener for the image
        holder.imageView.setOnClickListener(v -> {
            // Pass the full-size image URL to FullScreenActivity
            Intent intent = new Intent(context, FullScreenActivity.class);
            intent.putExtra("image_url", wallpaperModel.getOriginalUrl()); // Pass the original image URL
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return wallpaperModelList.size();
    }

    public static class WallpaperViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public WallpaperViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.wallpaper_image); // assuming you have an ImageView in your wallpaper_item layout
        }
    }
}
