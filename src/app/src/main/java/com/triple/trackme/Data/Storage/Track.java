package com.triple.trackme.Data.Storage;

import java.util.ArrayList;

public class Track {

    public String dateTime;
    public double distance;
    public int time;
    public double avgSpeed;
    public ArrayList<Position> positions;

    public Track(final String dateTime, final double distance, final int time,
                 final double avgSpeed, final ArrayList<Position> positions) {

        this.dateTime = dateTime;
        this.distance = distance;
        this.time = time;
        this.avgSpeed = avgSpeed;
        this.positions = positions;
    }
}
