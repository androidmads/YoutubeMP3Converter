package com.ajts.androidmads.youtubemp3;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

public class YTubeMp3Service {

    private File file = null;
    private Builder builder;

    private YTubeMp3Service(Builder mBuilder) {
        builder = mBuilder;
        startDownload(builder.downloadUrl);
    }

    private void startDownload(String link) {
        try {
            if(!isNetworkAvailable()){
                builder.downloadListener.onError(new Exception("No Internet Connection"));
                return;
            }
            String youtubeUrl = "http://www.youtubeinmp3.com/fetch/?format=JSON&video=%s";
            Request request = new Request.Builder()
                    .url(String.format(youtubeUrl, link))
                    .build();
            new OkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    builder.downloadListener.onError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String stringResponse = response.body().string();
                    builder.downloadListener.onDownloadStarted();
                    Log.v("stringResp", stringResponse);
                    try {
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        String downloadLink = jsonObject.getString("link");
                        String downloadTitle = jsonObject.getString("title");
                        saveMp3(downloadLink, downloadTitle);
                    } catch (Exception e) {
                        builder.downloadListener.onError(e);
                    }
                }
            });
        } catch (Exception e) {
            builder.downloadListener.onError(e);
        }
    }

    private void saveMp3(String link, final String title) {
        try {
            Request request = new Request.Builder()
                    .url(link)
                    .build();
            new OkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    builder.downloadListener.onError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String fileName = title.replaceAll("[^a-zA-Z]+", "");
                    if (!builder.folder.exists()) {
                        boolean folderCreated = builder.folder.mkdir();
                        Log.v("folderCreated", folderCreated + "");
                    }
                    file = new File(builder.folder.getPath() + "/" + fileName + ".mp3");
                    try {
                        boolean fileCreated = file.createNewFile();
                        Log.v("fileCreated", fileCreated + "");
                        BufferedSink sink = Okio.buffer(Okio.sink(file));
                        sink.writeAll(response.body().source());
                        sink.close();
                        builder.downloadListener.onSuccess(file.getPath());
                    } catch (Exception e) {
                        builder.downloadListener.onError(e);
                    }
                }
            });
        } catch (Exception e) {
            builder.downloadListener.onError(e);
        }
    }

    // Check Internet Connection
    @SuppressWarnings("deprecation")
    private boolean isNetworkAvailable() {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) builder.activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);

            if (netInfo != null
                    && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null
                        && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }

    public static class Builder {

        DownloadListener downloadListener = null;
        File folder = null;
        String downloadUrl = "";
        Activity activity;

        public Builder(Activity mActivity) {
            activity = mActivity;
        }

        public Builder setDownloadUrl(String url) {
            downloadUrl = url;
            return this;
        }

        public Builder setFolderPath(String folderPath) {
            folder = new File(folderPath);
            return this;
        }

        public Builder setOnDownloadListener(DownloadListener downloadListener) {
            this.downloadListener = downloadListener;
            return this;
        }

        public YTubeMp3Service build() {
            return new YTubeMp3Service(this);
        }

        public interface DownloadListener {
            void onSuccess(String savedPath);

            void onDownloadStarted();

            void onError(Exception e);
        }

    }

}