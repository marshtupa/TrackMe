package com.triple.trackme;

public class GoogleMapService {

    public static double distanceBetweenCoordinates(double latitude1, double longitude1, double latitude2, double longitude2) {
        final double earthRadius = 6371000;

        double latitude = Math.toRadians(latitude2 - latitude1);
        double longitude = Math.toRadians(longitude2 - longitude1);

        double a = Math.sin(latitude / 2) * Math.sin(latitude / 2) +
                Math.cos(Math.toRadians(latitude1)) *
                Math.cos(Math.toRadians(latitude2)) *
                Math.sin(longitude / 2) * Math.sin(longitude / 2);
        double b = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = (double)Math.round(earthRadius * b);
        return distance;
    }
}
