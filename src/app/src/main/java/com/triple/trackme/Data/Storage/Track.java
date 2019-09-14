package com.triple.trackme.Data.Storage;

import java.util.ArrayList;

public class Track {

    public long id;
    public String dateTime;
    public double distance;
    public int time;
    public double avgSpeed;
    public String mapImagePath;
    public ArrayList<Position> positions;

    public Track(final long id, final String dateTime, final double distance,
                 final int time, final double avgSpeed, final String mapImagePath,
                 final ArrayList<Position> positions) {

        this.id = id;
        this.dateTime = dateTime;
        this.distance = distance;
        this.time = time;
        this.avgSpeed = avgSpeed;
        this.mapImagePath = mapImagePath;
        this.positions = positions;
    }
}
