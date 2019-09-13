package com.triple.trackme.Activity.CompletedTrainings;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;

class DownloadImageMapTask extends AsyncTask<Object, Void, Bitmap> {

    private TrainingsViewAdapter.TrainingViewHolder holder;

    @Override
    protected Bitmap doInBackground(final Object... objects) {
        String urlRequest = (String)objects[0];
        holder = (TrainingsViewAdapter.TrainingViewHolder)objects[1];

        Bitmap bmp = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(urlRequest);

        InputStream inputStream = null;
        try {
            HttpResponse response = httpclient.execute(request);
            inputStream = response.getEntity().getContent();
            bmp = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return bmp;
    }

    protected void onPostExecute(final Bitmap bmp) {
        if (bmp != null && holder != null) {
            holder.mapLoadProgress.setVisibility(View.INVISIBLE);
            holder.imageMap.setImageBitmap(bmp);
            holder.imageMap.setVisibility(View.VISIBLE);
        }
    }
}
