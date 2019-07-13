package com.triple.trackme.CurrentTrack;

import android.annotation.SuppressLint;
import android.location.Location;

import com.triple.trackme.GoogleMapService;

import java.text.DecimalFormat;
import java.util.ArrayList;

class CurrentTrackData {

    private int trackSeconds;
    private double trackDistance;
    private double trackSpeed;
    private ArrayList<Location> positions;

    CurrentTrackData() {
        initializeEmptyData();
    }

    void initializeEmptyData() {
        trackSeconds = 0;
        trackDistance = 0.0;
        trackSpeed = 0.0;
        positions = new ArrayList<Location>();
    }

    void incrementTrackTime() {
        trackSeconds++;
    }

    void newLocation(Location newLocation) {
        trackSpeed = newLocation.getSpeed();
        updateDistance(newLocation);
        positions.add(newLocation);
    }

    private void updateDistance(Location newLocation) {
        if (positions.size() > 1) {
            Location prevLocation = positions.get(positions.size() - 1);
            trackDistance += GoogleMapService.distanceBetweenCoordinates(prevLocation, newLocation);
        }
    }

    @SuppressLint("DefaultLocale")
    String getTrackTimeStr() {
        int hours = trackSeconds / 3600;
        int minutes = (trackSeconds - hours * 3600) / 60;
        int seconds = trackSeconds % 60;

        return String.format("%s:%s:%s", String.format("%02d", hours), String.format("%02d", minutes), String.format("%02d", seconds));
    }

    @SuppressLint("DefaultLocale")
    String getTrackDistanceStr() {
        String trackDistanceStr = new DecimalFormat("00.00").format(trackDistance / 1000);
        return trackDistanceStr;
    }

    @SuppressLint("DefaultLocale")
    String getTrackSpeedStr() {
        String trackSpeedStr = new DecimalFormat("00.00").format(trackSpeed);
        return trackSpeedStr;
    }
}
