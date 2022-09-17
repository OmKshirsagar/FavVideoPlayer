package com.example.favvideoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.util.ArrayList;

public class VideoPlayerActivity extends AppCompatActivity {

    PlayerView playerView;
    SimpleExoPlayer player;
    int position;
    String videoTitle;
    ArrayList<MediaFiles> mVideoFiles = new ArrayList<>();

    TextView title;
    ConcatenatingMediaSource CMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        playerView = findViewById(R.id.exoplayer_view);
        getSupportActionBar().hide();
        position = getIntent().getIntExtra("position", 1);
        videoTitle = getIntent().getStringExtra("video_title");
        mVideoFiles = getIntent().getExtras().getParcelableArrayList("videoArrayList");

        title = findViewById(R.id.video_title);
        title.setText(videoTitle);

        playVideo();

    }

    private void playVideo() {
        String path = mVideoFiles.get(position).getPath();
        Uri uri = Uri.parse(path);

        player = new SimpleExoPlayer.Builder(this).build();
        DefaultDataSourceFactory dsf = new DefaultDataSourceFactory(
                this, Util.getUserAgent(this, "app")
        );

        CMS = new ConcatenatingMediaSource();
        for (int i = 0; i <mVideoFiles.size(); i++) {
            new File(String.valueOf(mVideoFiles.get(i)));
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dsf).createMediaSource(Uri.parse(String.valueOf(uri)));
            CMS.addMediaSource(mediaSource);
        }
        playerView.setPlayer(player);
        playerView.setKeepScreenOn(true);
        player.prepare(CMS);
        player.seekTo(position, C.TIME_UNSET);
        playError();
    }

    private void playError() {
        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Player.Listener.super.onPlayerError(error);
            }
        });
    }
}