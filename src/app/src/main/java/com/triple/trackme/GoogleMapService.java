package com.triple.trackme;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MapStyleOptions;

public class GoogleMapService {

    public static double distanceBetweenCoordinates(Location location1, Location location2) {
        final double earthRadius = 6371000;

        double latitude = Math.toRadians(location2.getLatitude() - location1.getLatitude());
        double longitude = Math.toRadians(location2.getLongitude() - location1.getLongitude());

        double a = Math.sin(latitude / 2) * Math.sin(latitude / 2) +
                Math.cos(Math.toRadians(location1.getLatitude())) *
                Math.cos(Math.toRadians(location2.getLatitude())) *
                Math.sin(longitude / 2) * Math.sin(longitude / 2);
        double b = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = (double)Math.round(earthRadius * b);
        return distance;
    }

    static String getEnabledLocationProvider(LocationManager locationManager, Context context) {
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        boolean enabled = locationManager.isProviderEnabled(bestProvider);

        if (!enabled) {
            Toast.makeText(context, "No location provider enabled!", Toast.LENGTH_LONG).show();
            Log.i("MapInfo", "No location provider enabled!");
            return null;
        }
        return bestProvider;
    }

    static void settingMap(GoogleMap map, FragmentManager fragmentManager, Context context) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.style_map));

        map.getUiSettings().setCompassEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);

        final int CURRENT_POSITION_BUTTON_ID = 0x2;
        Fragment mapFragment = fragmentManager.findFragmentById(R.id.map);
        View currentPositionButton = mapFragment.getView().findViewById(CURRENT_POSITION_BUTTON_ID);
        if (currentPositionButton != null && currentPositionButton.getLayoutParams() instanceof RelativeLayout.LayoutParams)
        {
            RelativeLayout.LayoutParams params_zoom = (RelativeLayout.LayoutParams) currentPositionButton.getLayoutParams();
            params_zoom.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            params_zoom.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        }
    }
}
