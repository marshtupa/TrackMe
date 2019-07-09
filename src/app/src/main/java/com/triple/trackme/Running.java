package com.triple.trackme;

import java.util.LinkedList;

public class Running{
    private LinkedList<OnePoint> Way;
    private boolean started;
    private long index;
    private double WayLength;

    Running(){
        Way = new LinkedList<>();
        Way.clear();
        started = false;
        index = 0;
        WayLength = 0;
    }
    Running(boolean Started){
        Way.clear();
        started = Started;
        index = 0;
        WayLength = 0;
    }
    //методы
    public void fixPosition(double latitude, double longitude, double speed, double time){
        if (started) {
            OnePoint previous;
            if (!Way.isEmpty()){
                previous = Way.getLast();
            }
            else {
                previous = new OnePoint(latitude, longitude, speed, time);
            }
            OnePoint a = new OnePoint(latitude, longitude, speed, time);
            Way.add(a);
            WayLength += MainActivity.Distance(previous.latitude,previous.longitude,a.latitude,a.longitude);
            index++;
        }
    }
    public void Start(){
        if (!started){
            if (Way.isEmpty()){
                Way.clear();
                Clear();
                started = true;
            }
            else{
                Continue();
            }
        }
    }
    public void Stop(){
        if (started){
            started = false;
        }
    }
    public void Continue(){
        if (!started){
            started = true;
        }
    }
    public void Clear(){
        Way.clear();
        started = false;
        index = 0;
        WayLength = 0;
    }
    public boolean isStarted(){
        return started;
    }
    public double getTime(){
        if (!Way.isEmpty()) {
            return (Way.getLast().time - Way.getFirst().time)/1000;
        }
        return 0;
    }
    public double getAverageSpeed(){
        double avgSpeed = 0;
        if ((started)&&(!Way.isEmpty())){
            for (int i = 0; i < index; i++){
                avgSpeed += Way.get(i).speed;
            }
            if ((Way.getLast().time - Way.getFirst().time) != 0){
                avgSpeed = avgSpeed/(Way.getLast().time - Way.getFirst().time);
            }
            else {
                avgSpeed = 0;
            }
        }
        return avgSpeed;
    }
}
