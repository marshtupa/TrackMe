package com.triple.trackme.CurrentTrack;

import android.annotation.SuppressLint;
import android.location.Location;

import com.triple.trackme.CurrentUser.CurrentUserData;
import com.triple.trackme.Data.Storage.Position;
import com.triple.trackme.Data.Storage.Track;
import com.triple.trackme.GoogleMapService;

import java.io.File;
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

    Location getLastLocation() {
        Location lastLocation = null;
        if (positions.size() > 0) {
            lastLocation = positions.get(positions.size() - 1);
        }
        return lastLocation;
    }

    private void updateDistance(Location newLocation) {
        if (positions.size() > 1) {
            Location prevLocation = positions.get(positions.size() - 1);
            trackDistance += GoogleMapService.distanceBetweenCoordinates(prevLocation, newLocation);
        }
    }

    void saveCurrentTrackToFile() {
        Track track = currentTrackToTrackData();
        CurrentUserData.addTrack(track);
    }

    private Track currentTrackToTrackData() {
        Track track = new Track();
        track.distance = trackDistance;
        track.time = trackSeconds;
        double avgSpeed = (trackDistance / trackSeconds) * 3.6;
        track.avgSpeed = avgSpeed;
        for (Location loc : positions) {
            Position pos = new Position();
            pos.latitude = loc.getLatitude();
            pos.longitude = loc.getLongitude();
            track.positions.add(pos);
        }
        return track;
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
