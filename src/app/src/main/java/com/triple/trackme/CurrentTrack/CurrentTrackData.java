package com.triple.trackme.CurrentTrack;

import android.location.Location;

import com.triple.trackme.GoogleMapService;

import java.util.ArrayList;

public class CurrentTrackData {

    private int trackSeconds;
    private double trackDistance;
    private double trackSpeed;
    private ArrayList<Location> positions;


    public CurrentTrackData() {
        initializeEmptyData();
    }

    public void initializeEmptyData() {
        this.trackSeconds = 0;
        this.trackDistance = 0.0;
        this.trackSpeed = 0.0;
        this.positions = new ArrayList<Location>();
    }

    public void incrementTrackTime() {
        this.trackSeconds++;
    }

    public void newLocation(Location newLocation) {
        this.trackSpeed = newLocation.getSpeed();
        positions.add(newLocation);
        this.updateDistance(newLocation);
    }

    private void updateDistance(Location newLocation) {
        if (positions.size() > 1) {
            Location prevLocation = positions.get(positions.size() - 1);
            this.trackDistance += GoogleMapService.distanceBetweenCoordinates(prevLocation, newLocation);
        }
    }


    public String getTrackTimeStr() {
        int hours = trackSeconds / 3600;
        int minutes = (trackSeconds - hours * 3600) / 60;
        int seconds = trackSeconds % 60;

        return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }

    public String getTrackDistanceStr() {
        return String.format("%.2f", this.trackDistance/1000);
    }

    public String getTrackSpeedStr() {
        return String.format("%.1f", this.trackSpeed);
    }
}
