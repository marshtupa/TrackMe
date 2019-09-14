package com.triple.trackme.Services;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.triple.trackme.R;

public class GoogleMapUtility {

    private GoogleMapUtility() { }

    public static double distanceBetweenTwoCoordinates(final Location coordinate1,
                                                       final Location coordinate2) {

        final double EARTH_RADIUS = 6371000;

        double latitude = Math.toRadians(coordinate2.getLatitude() - coordinate1.getLatitude());
        double longitude = Math.toRadians(coordinate2.getLongitude() - coordinate1.getLongitude());

        double a = Math.sin(latitude / 2) * Math.sin(latitude / 2) +
                Math.cos(Math.toRadians(coordinate1.getLatitude())) *
                Math.cos(Math.toRadians(coordinate2.getLatitude())) *
                Math.sin(longitude / 2) * Math.sin(longitude / 2);
        double b = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (double)Math.round(EARTH_RADIUS * b);
    }

    public static String getEnabledLocationProvider(final LocationManager locationManager,
                                                    final Context context) {

        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        boolean enabled = locationManager.isProviderEnabled(bestProvider);

        if (!enabled) {
            Toast.makeText(context, "No location provider enabled!",
                    Toast.LENGTH_LONG).show();
            Log.i("MapInfo", "No location provider enabled!");
            return null;
        }
        return bestProvider;
    }

    public static void settingMap(final GoogleMap map, final Context context) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.style_map));

        map.getUiSettings().setCompassEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);
    }
}
