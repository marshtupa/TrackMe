package com.triple.trackme.Activity.CompletedTracks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;

import com.triple.trackme.Data.Storage.Track;
import com.triple.trackme.Data.Work.ImageFilesUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

class DownloadImageMapTask extends AsyncTask<Object, Void, Bitmap> {

    private TracksViewAdapter.TrackViewHolder holder;
    private Track trackData;

    @Override
    protected Bitmap doInBackground(final Object... objects) {
        String urlRequest = (String)objects[0];
        holder = (TracksViewAdapter.TrackViewHolder)objects[1];
        trackData = (Track)objects[2];

        Bitmap bitmap = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(urlRequest);
        try {
            HttpResponse response = httpclient.execute(request);
            InputStream inputStream = response.getEntity().getContent();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return bitmap;
    }

    protected void onPostExecute(final Bitmap bitmap) {
        if (bitmap != null && holder != null) {
            holder.mapLoadProgress.setVisibility(View.INVISIBLE);
            holder.imageMap.setImageBitmap(bitmap);
            holder.imageMap.setVisibility(View.VISIBLE);
            saveBitmapImage(bitmap);
        }
    }

    private void saveBitmapImage(final Bitmap bitmap) {
        try {
            ImageFilesUtils.writeBitmapToFile(trackData.mapImagePath, bitmap);
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
