package com.triple.trackme.CurrentTrack;

import android.location.Location;

import com.triple.trackme.CurrentUser.CurrentUserData;
import com.triple.trackme.Data.Storage.Position;
import com.triple.trackme.Data.Storage.Track;
import com.triple.trackme.GoogleMapService;

import java.text.DecimalFormat;
import java.util.ArrayList;

class CurrentTrackData {

    private int allTimeInSeconds;
    private double allDistanceInMetres;
    private double currentSpeedInKmH;
    private ArrayList<Location> allPositions;

    CurrentTrackData() {
        allTimeInSeconds = 0;
        allDistanceInMetres = 0.0;
        currentSpeedInKmH = 0.0;
        allPositions = new ArrayList<Location>();
    }

    void addSeconds(int seconds) {
        allTimeInSeconds += seconds;
    }

    int getAllTimeInSeconds() {
        return allTimeInSeconds;
    }

    void newPosition(Location newPosition) {
        updateSpeed(newPosition);
        updateDistance(newPosition);
        allPositions.add(newPosition);
    }

    private void updateSpeed(Location newPosition) {
        currentSpeedInKmH = newPosition.getSpeed();
    }

    private void updateDistance(Location newPosition) {
        Location lastPosition = getLastPosition();
        if (lastPosition != null) {
            double newDistance = GoogleMapService.distanceBetweenTwoCoordinates(lastPosition, newPosition);
            allDistanceInMetres += newDistance;
        }
    }

    Location getLastPosition() {
        Location lastPosition = null;
        if (allPositions.size() > 0) {
            lastPosition = allPositions.get(allPositions.size() - 1);
        }
        return lastPosition;
    }

    void saveData() {
        Track track = toTrackData();
        CurrentUserData.addTrack(track);
    }

    private Track toTrackData() {
        Track track = new Track();
        track.distance = allDistanceInMetres;
        track.time = allTimeInSeconds;
        double avgSpeed = (allDistanceInMetres / allTimeInSeconds) * 3.6;
        track.avgSpeed = avgSpeed;
        for (Location pos : allPositions) {
            Position position = new Position();
            position.latitude = pos.getLatitude();
            position.longitude = pos.getLongitude();
            track.positions.add(position);
        }
        return track;
    }

    String timeToFormatString() {
        int hours = allTimeInSeconds / 3600;
        int minutes = (allTimeInSeconds - hours * 3600) / 60;
        int seconds = allTimeInSeconds % 60;

        String timeString = String.format("%s:%s:%s", String.format("%02d", hours), String.format("%02d", minutes), String.format("%02d", seconds));
        return timeString;
    }

    String distanceToFormatString() {
        String distanceString = new DecimalFormat("00.00").format(allDistanceInMetres / 1000);
        return distanceString;
    }

    String speedToFormatString() {
        String speedString = new DecimalFormat("00.00").format(currentSpeedInKmH);
        return speedString;
    }
}
