package com.triple.trackme.Activity.Main;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.triple.trackme.Activity.CompletedTracks.CompletedTracksActivity;
import com.triple.trackme.CurrentTrack.CurrentTrackView;
import com.triple.trackme.CurrentUser.CurrentUserData;
import com.triple.trackme.R;
import com.triple.trackme.Services.GoogleMapUtils;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity
        implements OnMapReadyCallback, OnMapLoadedCallback, LocationListener {

    private GoogleMap map;
    private ProgressDialog loadMapProgressDialog;

    public static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;
    public static File filesDir;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateWindow();
        setContentView(R.layout.activity_main);

        showMapLoadProgress();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setViewTrackPanelNotClickable();

        filesDir = getFilesDir();
        CurrentUserData.initializeUserData();
        CurrentTrackView.initializeTrack(this,
                (TextView) findViewById(R.id.timeVal),
                (TextView) findViewById(R.id.distanceVal),
                (TextView) findViewById(R.id.speedVal),
                (ImageButton) findViewById(R.id.buttonStop),
                (ImageButton) findViewById(R.id.buttonPause),
                (ImageButton) findViewById(R.id.buttonPlay));
    }

    private void setViewTrackPanelNotClickable() {
        View view = findViewById(R.id.viewCurrentTrackPanel);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                return true;
            }
        });
    }

    private void updateWindow() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    private void setWindowFlag(final Activity activity, final int bits, final boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void showMapLoadProgress() {
        loadMapProgressDialog = new ProgressDialog(this);
        loadMapProgressDialog.setTitle("Map Loading ...");
        loadMapProgressDialog.setMessage("Please wait...");
        loadMapProgressDialog.setCancelable(true);
        loadMapProgressDialog.show();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);
        GoogleMapUtils.settingMap(map, this);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onMapLoaded() {
        loadMapProgressDialog.dismiss();

        int accessCoarsePermission = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int accessFinePermission = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED
                || accessFinePermission != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION };
            ActivityCompat.requestPermissions(this, permissions,
                    REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);
            return;
        }
        showMyLocation();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final String permissions[],
                                           final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_ID_ACCESS_COURSE_FINE_LOCATION) {
            if (grantResults.length > 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                Log.i("MapInfo", "Permission denied!");
                this.showMyLocation();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                Log.i("MapInfo", "Permission denied!");
            }
        }
    }

    private void showMyLocation() {
        final double DISTANCE_TO_CENTER = 0.005;

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String locationProvider = GoogleMapUtils.getEnabledLocationProvider(
                locationManager, this);
        if (locationProvider == null) {
            return;
        }

        final long MIN_TIME_BW_UPDATES = 1000;
        final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
        Location myLocation;
        try {
            locationManager.requestLocationUpdates(
                    locationProvider,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            myLocation = locationManager.getLastKnownLocation(locationProvider);
        }
        catch (SecurityException e) {
            Toast.makeText(this, "Show My Location Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.i("MapInfo", "Show My Location Error: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        if (myLocation != null) {
            LatLng latLng = new LatLng(myLocation.getLatitude(),
                    myLocation.getLongitude() - DISTANCE_TO_CENTER);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(15)
                    .bearing(90)
                    .tilt(40)
                    .build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else {
            Toast.makeText(this, "Location not found", Toast.LENGTH_LONG).show();
            Log.i("MapInfo", "Location not found");
        }

    }


    @Override
    public void onLocationChanged(final Location location) {
        CurrentTrackView.newPosition(location, map);
    }

    @Override
    public void onStatusChanged(final String provider, final int status, final Bundle extras) { }

    @Override
    public void onProviderEnabled(final String provider) { }

    @Override
    public void onProviderDisabled(final String provider) { }


    public void clickStartTrackButton(final View view) {
        final Animation animScale = AnimationUtils.loadAnimation(
                this, R.anim.scale_track_button);
        view.startAnimation(animScale);
        CurrentTrackView.startTrack();
    }

    public void clickPauseTrackButton(final View view) {
        final Animation animScale = AnimationUtils.loadAnimation(
                this, R.anim.scale_track_button);
        view.startAnimation(animScale);
        CurrentTrackView.pauseTrack();
    }

    public void clickStopTrackButton(final View view) {
        final Animation animScale = AnimationUtils.loadAnimation(
                this, R.anim.scale_track_button);
        view.startAnimation(animScale);
        CurrentTrackView.stopTrack(this);
    }

    public void clickCompletedTracksButton(final View view) {
        final int BUTTON_ANIMATION_DELAY = 200;
        final Animation animScale = AnimationUtils.loadAnimation(
                this, R.anim.scale_menu_button);
        view.startAnimation(animScale);
        view.setClickable(false);

        TimerTask changeButtonsTask = new TimerTask() {
            @Override
            public void run() {
                Intent completedTracksIntent = new Intent(
                        MainActivity.this, CompletedTracksActivity.class);
                startActivity(completedTracksIntent);
                overridePendingTransition(
                        R.anim.activity_slide_left_in, R.anim.activity_slide_left_out);
                view.setClickable(true);
            }
        };
        Timer changeButtonsTimer = new Timer();
        changeButtonsTimer.schedule(changeButtonsTask, BUTTON_ANIMATION_DELAY);
    }

    public void clickCurrentPositionButton(final View view) {
        final Animation animScale = AnimationUtils.loadAnimation(
                this, R.anim.scale_menu_button);
        view.startAnimation(animScale);
        showMyLocation();
    }


    public void setText(final TextView text, final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }

    public void enableButton(final ImageButton button, final boolean enable){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

}
