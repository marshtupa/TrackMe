package com.triple.trackme.Activity.CompletedTracks;

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
import com.triple.trackme.Data.Work.ImageFilesUtils;
import com.triple.trackme.R;
import com.triple.trackme.Services.ImageMapUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TracksViewAdapter
        extends RecyclerView.Adapter<TracksViewAdapter.TrackViewHolder> {

    private final Context context;
    private final ArrayList<Track> tracksData;
    private boolean first;

    TracksViewAdapter(final Context context, final ArrayList<Track> tracksData) {
        super();
        this.context = context;
        this.tracksData = tracksData;
    }

    @Override
    public int getItemCount() {
        return tracksData.size();
    }

    @Override
    public TrackViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_track_panel, parent, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrackViewHolder holder, final int position) {
        Track trackData = tracksData.get(position);
        setupTrackCard(holder, trackData);
    }

    private void setupTrackCard(final TrackViewHolder holder, final Track trackData) {
        setDistanceValue(holder, trackData);
        setSpeedValue(holder, trackData);
        setTimeValue(holder, trackData);
        setDateValue(holder, trackData);
        setMapImage(holder, trackData);
    }

    private void setDistanceValue(final TrackViewHolder holder, final Track trackData) {
        String distanceString = new DecimalFormat("00.00")
                .format(trackData.distance / 1000);
        holder.distanceValue.setText(distanceString);
    }

    private void setSpeedValue(final TrackViewHolder holder, final Track trackData) {
        String speedString = new DecimalFormat("00.00")
                .format(trackData.avgSpeed);
        holder.speedValue.setText(speedString);
    }

    private void setTimeValue(final TrackViewHolder holder, final Track trackData) {
        int hours = trackData.time / 3600;
        int minutes = (trackData.time - hours * 3600) / 60;
        int seconds = trackData.time % 60;
        String timeString = String.format("%s:%s:%s",
                String.format("%02d", hours),
                String.format("%02d", minutes),
                String.format("%02d", seconds));
        holder.timeValue.setText(timeString);
    }

    private void setDateValue(final TrackViewHolder holder, final Track trackData) {
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

    private void setMapImage(final TrackViewHolder holder, final Track trackData) {
        if (ImageFilesUtils.isFileExists(trackData.mapImagePath)) {
            holder.mapLoadProgress.setVisibility(View.INVISIBLE);
            holder.imageMap.setVisibility(View.VISIBLE);

            try {
                Bitmap bitmap = ImageFilesUtils.readBitmapFromFile(trackData.mapImagePath);
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
                    ImageMapUtils.getImageUrl(
                            context.getString(R.string.google_maps_static_key),
                            trackData),
                    holder,
                    trackData);
        }
    }

    class TrackViewHolder extends RecyclerView.ViewHolder {

        TextView distanceValue;
        TextView speedValue;
        TextView timeValue;
        TextView dateValue;
        ImageView imageMap;
        ProgressBar mapLoadProgress;

        TrackViewHolder(final View view) {
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
