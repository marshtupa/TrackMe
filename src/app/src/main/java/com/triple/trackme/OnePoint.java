package com.triple.trackme;

//класс одной точки
public class OnePoint{
    double latitude;
    double longitude;
    double speed; //m/s
    double time;
    OnePoint(){
        latitude = 0;
        longitude = 0;
        speed = 0;
        time = 0;
    }
    OnePoint(double Latitude, double Longitude, double Speed, double Time){
        latitude = Latitude;
        longitude = Longitude;
        if (Speed >= 0) speed = Speed; else speed = 0;
        if (Time >= 0) time = Time; else time = 0;
    }
}
