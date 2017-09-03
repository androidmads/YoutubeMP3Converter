package com.ajts.androidmads.youtubemp3converter;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ajts.androidmads.youtubemp3.YTubeMp3Service;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Downloading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }

    public void downloadSong(View view) {
        progressDialog.show();
        new YTubeMp3Service.Builder(MainActivity.this)
                .setDownloadUrl("https://youtu.be/nZDGC-tXCo0")
                .setFolderPath(new File(Environment.getExternalStorageDirectory(), "/YTMp3/Downloads").getPath())
                .setOnDownloadListener(new YTubeMp3Service.Builder.DownloadListener() {
                    @Override
                    public void onSuccess(String savedPath) {
                        Log.v("exce", savedPath);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onDownloadStarted() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.v("exce", e.getMessage());
                        progressDialog.dismiss();
                    }
                }).build();


    }
}
