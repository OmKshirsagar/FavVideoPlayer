package com.example.favvideoplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class VideoFilesAdapter extends RecyclerView.Adapter<VideoFilesAdapter.ViewHolder> {
    private ArrayList<MediaFiles> videoList;
    private Context context;

    public VideoFilesAdapter(ArrayList<MediaFiles> videoList, Context context) {
        this.videoList = videoList;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoFilesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoFilesAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.videoName.setText(videoList.get(position).getDisplayName());
        String size = videoList.get(position).getSize();
        holder.videoSize.setText(android.text.format.Formatter.formatFileSize(context, Long.parseLong(size)));
        double milisecs = Double.parseDouble(videoList.get(position).getDuration());
        holder.videoDuration.setText(timeConversion((long) milisecs));

        Glide.with(context).load(new File(videoList.get(position).getPath()))
                        .into(holder.thumbnail);

        holder.menu_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "menu", Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, VideoPlayerActivity.class);
                i.putExtra("position", position);
                i.putExtra("video_title", videoList.get(position).getDisplayName());
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("videoArrayList", videoList);
                i.putExtras(bundle);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail, menu_more;
        TextView videoName, videoSize, videoDuration;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            menu_more = itemView.findViewById(R.id.video_menu_more);
            videoName = itemView.findViewById(R.id.video_name);
            videoSize = itemView.findViewById(R.id.video_size);
            videoDuration = itemView.findViewById(R.id.video_duration);

        }
    }
    public String timeConversion(long value){
        String videoTime;
        int duration = (int) value;
        int hrs = (duration/3600000);
        int mins = (duration/60000) % 60000;
        int secs = duration%60000/1000;

        if (hrs > 0){
            videoTime = String.format("%02d:%02d:%02d", hrs, mins, secs);
        } else {
            videoTime = String.format("%02d:%02d", mins, secs);
        }
        return videoTime;
    }
}
