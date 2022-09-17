package com.example.favvideoplayer;

import static com.example.favvideoplayer.AllowAccessActivity.REQUEST_PERMISSION_SETTINGS;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    VideoFoldersAdapter adapter;
    private ArrayList<MediaFiles> mediaFiles = new ArrayList<>();
    private ArrayList<String> allFolderList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "Click On permission > Storage", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            i.setData(uri);
            startActivityForResult(i, REQUEST_PERMISSION_SETTINGS);
        }
        recyclerView = findViewById(R.id.folders_rv);
        showFolders();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showFolders() {
        mediaFiles = fetchMedia();
        adapter = new VideoFoldersAdapter(mediaFiles, allFolderList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false));
        adapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<MediaFiles> fetchMedia() {
        ArrayList<MediaFiles> mediaFilesArrayList = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                String size = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                String dateAdded = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED));

                MediaFiles mediaFiles = new MediaFiles(id, title, displayName, size, duration, path, dateAdded);
                int index = path.lastIndexOf("/");
                String subStr = path.substring(0, index);
                if (!allFolderList.contains(subStr)) {
                    allFolderList.add(subStr);
                }
                mediaFilesArrayList.add(mediaFiles);
            } while (cursor.moveToNext());
        }
        return mediaFilesArrayList;
    }
}