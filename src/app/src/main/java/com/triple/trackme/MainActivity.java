package com.triple.trackme;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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
import com.triple.trackme.CurrentTrack.CurrentTrackView;
import com.triple.trackme.CurrentUser.CurrentUserData;

import java.io.File;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, OnMapLoadedCallback, LocationListener {

    private GoogleMap map;
    private ProgressDialog progressDialog;

    public static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;
    public static File filesDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateWindow();
        setContentView(R.layout.activity_main);

        showMapLoadProgress();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setViewTrackPanelNotClickable();

        filesDir = getFilesDir();
        CurrentUserData.initializeUserData(getFilesDir());
        CurrentTrackView.initialize(this, (TextView) findViewById(R.id.timeVal),
                (TextView) findViewById(R.id.distanceVal), (TextView) findViewById(R.id.speedVal),
                (ImageButton) findViewById(R.id.buttonPause), (ImageButton) findViewById(R.id.buttonPlay));
    }

    private void setViewTrackPanelNotClickable() {
        View view = findViewById(R.id.viewCurrentTrackPanel);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    private void updateWindow() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    private void setWindowFlag(Activity activity, final int bits, boolean on) {
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Map Loading ...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);
        GoogleMapService.settingMap(map, getSupportFragmentManager(), this);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onMapLoaded() {
        progressDialog.dismiss();

        int accessCoarsePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int accessFinePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED || accessFinePermission != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION };
            ActivityCompat.requestPermissions(this, permissions, REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);
            return;
        }
        showMyLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String locationProvider = GoogleMapService.getEnabledLocationProvider(locationManager, this);
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
            Toast.makeText(this, "Show My Location Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.i("MapInfo", "Show My Location Error: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        if (myLocation != null) {
            LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
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
    public void onLocationChanged(Location location) {
        CurrentTrackView.newLocation(location, map);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }


    public void clickStartTrackButton(View view) {
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.scale);
        view.startAnimation(animScale);
        CurrentTrackView.start();
    }

    public void clickPauseTrackButton(View view) {
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.scale);
        view.startAnimation(animScale);
        CurrentTrackView.pause();
    }

    public void clickStopTrackButton(View view) {
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.scale);
        view.startAnimation(animScale);
        CurrentTrackView.stop();
    }

    public void clickProfileButton(View view) {
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.scale_interface);
        view.startAnimation(animScale);
    }

    public void clickCurrentPositionButton(View view) {
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.scale_interface);
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
