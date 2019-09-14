package com.triple.trackme.Activity.CompletedTrainings;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.triple.trackme.Data.Storage.Track;
import com.triple.trackme.Data.Work.ImageFilesUtility;
import com.triple.trackme.R;
import com.triple.trackme.Services.ImageMapUtility;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TrainingsViewAdapter
        extends RecyclerView.Adapter<TrainingsViewAdapter.TrainingViewHolder> {

    private Context context;
    private ArrayList<Track> trainingsData;
    private boolean first;

    TrainingsViewAdapter(final Context context, final ArrayList<Track> trainingsData) {
        super();
        this.context = context;
        this.trainingsData = trainingsData;
    }

    @Override
    public int getItemCount() {
        return trainingsData.size();
    }

    @Override
    public TrainingViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_training_panel, parent, false);
        return new TrainingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrainingViewHolder holder, final int position) {
        Track trackData = trainingsData.get(position);
        setupTrackCard(holder, trackData);
    }

    private void setupTrackCard(final TrainingViewHolder holder, final Track trackData) {
        setDistanceValue(holder, trackData);
        setSpeedValue(holder, trackData);
        setTimeValue(holder, trackData);
        setDateValue(holder, trackData);
        setMapImage(holder, trackData);
    }

    private void setDistanceValue(final TrainingViewHolder holder, final Track trackData) {
        String distanceString = new DecimalFormat("00.00")
                .format(trackData.distance / 1000);
        holder.distanceValue.setText(distanceString);
    }

    private void setSpeedValue(final TrainingViewHolder holder, final Track trackData) {
        String speedString = new DecimalFormat("00.00")
                .format(trackData.avgSpeed);
        holder.speedValue.setText(speedString);
    }

    private void setTimeValue(final TrainingViewHolder holder, final Track trackData) {
        int hours = trackData.time / 3600;
        int minutes = (trackData.time - hours * 3600) / 60;
        int seconds = trackData.time % 60;
        String timeString = String.format("%s:%s:%s",
                String.format("%02d", hours),
                String.format("%02d", minutes),
                String.format("%02d", seconds));
        holder.timeValue.setText(timeString);
    }

    private void setDateValue(final TrainingViewHolder holder, final Track trackData) {
        Date trackDate = new Date(trackData.dateTime);
        Date currentDate = new Date();
        SimpleDateFormat formatter;
        if (trackDate.getYear() < currentDate.getYear()) {
            formatter = new SimpleDateFormat("dd MMMM y - hh:mm a");
        }
        else {
            formatter = new SimpleDateFormat("dd MMMM - hh:mm a");
        }
        String dateString = formatter.format(trackDate);
        holder.dateValue.setText(dateString);
    }

    private void setMapImage(final TrainingViewHolder holder, final Track trackData) {
        if (ImageFilesUtility.isFileExists(trackData.mapImagePath)) {
            holder.mapLoadProgress.setVisibility(View.INVISIBLE);
            holder.imageMap.setVisibility(View.VISIBLE);

            try {
                Bitmap bitmap = ImageFilesUtility.readBitmapFromFile(trackData.mapImagePath);
                holder.imageMap.setImageBitmap(bitmap);
            }
            catch (IOException exception) {
                exception.printStackTrace();
                holder.imageMap.setVisibility(View.INVISIBLE);
                holder.mapLoadProgress.setVisibility(View.VISIBLE);
            }
        }
        else {
            holder.imageMap.setVisibility(View.INVISIBLE);
            holder.mapLoadProgress.setVisibility(View.VISIBLE);

            new DownloadImageMapTask().execute(
                    ImageMapUtility.getImageUrl(
                            context.getString(R.string.google_maps_static_key),
                            trackData),
                    holder,
                    trackData);
        }
    }

    class TrainingViewHolder extends RecyclerView.ViewHolder {

        TextView distanceValue;
        TextView speedValue;
        TextView timeValue;
        TextView dateValue;
        ImageView imageMap;
        ProgressBar mapLoadProgress;

        TrainingViewHolder(final View view) {
            super(view);
            distanceValue = view.findViewById(R.id.distanceValue);
            speedValue = view.findViewById(R.id.speedValue);
            timeValue = view.findViewById(R.id.timeValue);
            dateValue = view.findViewById(R.id.dateValue);
            imageMap = view.findViewById(R.id.imageMap);
            mapLoadProgress = view.findViewById(R.id.mapLoadProgress);
            setMargins(view);
        }

        private void setMargins(final View view) {
            final int MARGIN = 15;

            int top = 0;
            if (!first) {
                top = getPixelValue(MARGIN);
                first = true;
            }
            int left = getPixelValue(MARGIN);
            int right = getPixelValue(MARGIN);
            int bottom = getPixelValue(MARGIN);

            ViewGroup.MarginLayoutParams params =
                    (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(left, top, right, bottom);
        }

        private int getPixelValue(final int dip) {
            Resources resources = context.getResources();
            return (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dip,
                    resources.getDisplayMetrics()
            );
        }
    }
}
