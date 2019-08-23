package com.triple.trackme.Data.Storage;

import java.util.ArrayList;

public class Track {

    public String dateTime;
    public double distance;
    public int time;
    public double avgSpeed;
    public ArrayList<Position> positions;

    public Track(String dateTime, double distance, int time, double avgSpeed, ArrayList<Position> positions) {
        this.dateTime = dateTime;
        this.distance = distance;
        this.time = time;
        this.avgSpeed = avgSpeed;
        this.positions = positions;
    }
}
