package com.example.favvideoplayer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AllowAccessActivity extends AppCompatActivity {

    public static final int STORAGE_PERMISSION = 1;
    public static final int REQUEST_PERMISSION_SETTINGS = 12;
    Button allowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allow_access);
        allowBtn = findViewById(R.id.AllowBtn);
        SharedPreferences pref = getSharedPreferences("AllowAccess", MODE_PRIVATE);
        String value = pref.getString("Allow", "");
        if (value.equals("OK")) {
            startActivity(new Intent(AllowAccessActivity.this, MainActivity.class));
            finish();
        } else {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("Allow", "OK");
            editor.apply();
        }
        allowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(AllowAccessActivity.this, MainActivity.class));
                } else {
                    ActivityCompat.requestPermissions(AllowAccessActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                String p = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    boolean showRationale = shouldShowRequestPermissionRationale(p);
                    if (!showRationale) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("App Permissions")
                                .setMessage("Storage Permissions required for functioning!\nTo allow: \nSettings>Apps>Fav Video Player>Permissions>storage")
                                .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        i.setData(uri);
                                        startActivityForResult(i, REQUEST_PERMISSION_SETTINGS);
                                    }
                                }).create().show();
                    }
                } else {
                    ActivityCompat.requestPermissions(AllowAccessActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
                }
            }
        } else {
            startActivity(new Intent(AllowAccessActivity.this, MainActivity.class));
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(AllowAccessActivity.this, MainActivity.class));
            finish();
        }
    }
}