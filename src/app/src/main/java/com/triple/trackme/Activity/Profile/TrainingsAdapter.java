package com.triple.trackme.Activity.Profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.triple.trackme.Data.Storage.Track;
import com.triple.trackme.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TrainingsAdapter extends RecyclerView.Adapter<TrainingsAdapter.TrainingViewHolder> {

    private ArrayList<Track> trainingsData;

    public TrainingsAdapter(ArrayList<Track> trainingsData) {
        this.trainingsData = trainingsData;
    }

    @Override
    public TrainingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_training_panel, parent, false);
        TrainingViewHolder holder = new TrainingViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TrainingViewHolder holder, int position) {
        Track trackData = trainingsData.get(position);

        String distanceString = new DecimalFormat("00.00").format(trackData.distance / 1000);
        String speedString = new DecimalFormat("00.00").format(trackData.avgSpeed);

        int hours = trackData.time / 3600;
        int minutes = (trackData.time - hours * 3600) / 60;
        int seconds = trackData.time % 60;
        String timeString = String.format("%s:%s:%s", String.format("%02d", hours), String.format("%02d", minutes), String.format("%02d", seconds));

        holder.distanceValue.setText(distanceString);
        holder.speedValue.setText(speedString);
        holder.timeValue.setText(timeString);
    }

    @Override
    public int getItemCount() {
        return trainingsData.size();
    }

    public class TrainingViewHolder extends RecyclerView.ViewHolder {

        TextView distanceValue;
        TextView speedValue;
        TextView timeValue;

        TrainingViewHolder(View view) {
            super(view);
            distanceValue = view.findViewById(R.id.distanceValue);
            speedValue = view.findViewById(R.id.speedValue);
            timeValue = view.findViewById(R.id.timeValue);
        }
    }
}
