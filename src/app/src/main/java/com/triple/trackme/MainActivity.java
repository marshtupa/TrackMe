package com.triple.trackme;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.widget.Toast;
import android.widget.RelativeLayout;
import android.view.View;
import android.util.TypedValue;
import android.location.Criteria;
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
import com.google.android.gms.maps.model.MapStyleOptions;

//Библиотеки для работы с файлами
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

//добавить всплывающее сообщение
import android.widget.Toast;
import java.math.*;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, OnMapLoadedCallback, LocationListener {

    View view;
    private GoogleMap map;
    private ProgressDialog progressDialog;

    ////////////////////////////////Глобальные переменные////////////////////////////////////////////////////
    private Running CurrentRunning;

    ////////////////////////////////Константы////////////////////////////////////////////////////
    final String LOG_TAG = "myLogs";
    final String FILENAME = "file";

    final String DIR_SD = "MyFiles";
    final String FILENAME_SD = "fileSD";
    //////////////////////////////////////////////////////////////////////////////////////////////
    public static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showMapLoadProgress();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        CurrentRunning = new Running();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_map));
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(true);

        ///
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Fragment mapFragment = getSupportFragmentManager().findFragmentById(R.id.map);
        final int ZOOM_CONTROLS_ID = 0x1;
        view = mapFragment.getView().findViewById(ZOOM_CONTROLS_ID);

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Log.i("tag", " " + view.getHeight()); //height is ready
            }
        });

        if (view != null && view.getLayoutParams() instanceof RelativeLayout.LayoutParams)
        {
            RelativeLayout.LayoutParams params_zoom = (RelativeLayout.LayoutParams) view.getLayoutParams();
            params_zoom.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            params_zoom.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            final int marginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height / 2, getResources().getDisplayMetrics());
            params_zoom.setMargins(margin, height / 2 - 80, margin, margin);
        }
        ///

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

    private void showMapLoadProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Map Loading ...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_ID_ACCESS_COURSE_FINE_LOCATION: {
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    showInfo("MapInfo", "Permission granted!");
                    this.showMyLocation();
                }
                else {
                    showInfo("MapInfo", "Permission denied!");
                }
                break;
            }
        }
    }

    private String getEnabledLocationProvider() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        boolean enabled = locationManager.isProviderEnabled(bestProvider);

        if (!enabled) {
            showInfo("MapInfo", "No location provider enabled!");
            return null;
        }

        return bestProvider;
    }
  
    //////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////Функции кнопок////////////////////////////////////////////////////

    public void onClick1(View v){
        String filestr;
        //writeFile(FILENAME,"Something");
        //filestr = readFile(FILENAME);

        if (CurrentRunning.isStarted()) {
            CurrentRunning.Stop();
        }
        else{
            CurrentRunning.Start();
        }
        filestr = Double.toString(CurrentRunning.getTime());
        Toast toast = Toast.makeText(this, filestr,Toast.LENGTH_LONG);
        toast.show();
    }
    public void onClick2(View v){

    }
    public void onClick3(View v){

    }
    public void onClick4(View v){

    }
    //////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////Функции сохранения и загруки///////////////////////////////////////

    public void writeFile(String filename, String Value) {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(filename, MODE_PRIVATE)));
            // пишем данные
            bw.write(Value);
            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFile(String filename) {
        String str = "";
        String str2 = "";
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(filename)));
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                Log.d(LOG_TAG, str);
                str2 = str2 + str;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str2;
    }

    public  void writeFileSD(String filenameSD, String SDdir) {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + SDdir);
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, filenameSD);
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            bw.write("Содержимое файла на SD");
            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFileSD(String filenameSD, String SDdir) {
        String Readed = "";
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return "";
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + SDdir);
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, filenameSD);
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                Log.d(LOG_TAG, str);
                Readed += str;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Readed;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////Прочие функции///////////////////////////////////////
    //функция расчета расстояния по широте и долготе (в километрах). Широта и долгота - в градусах
    //Северная широта, восточная долгота - положительные значения
    //Южная широта, западная долгота - отрицательные значения
    public static double Distance(double latitude1, double longitude1, double latitude2, double longitude2){
        double r = 6371; //радиус Земли
        //перевод широты и долготы из градусов в радианы
        latitude1 = (latitude1*Math.PI)/180;
        latitude2 = (latitude2*Math.PI)/180;
        longitude1 = (longitude1*Math.PI)/180;
        longitude2 = (longitude2*Math.PI)/180;
        return r*Math.acos(Math.sin(latitude1)*Math.sin(latitude2) +
                Math.cos(latitude1)*Math.cos(latitude2)*Math.cos(longitude1-longitude2));
    }
    //////////////////////////////////////////////////////////////////////////////////////////////



    private void showMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        String locationProvider = this.getEnabledLocationProvider();
        if (locationProvider == null) {
            return;
        }

        final long MIN_TIME_BW_UPDATES = 1000;
        final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;

        Location myLocation = null;
        try {
            locationManager.requestLocationUpdates(
                    locationProvider,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);

            myLocation = locationManager.getLastKnownLocation(locationProvider);
        }
        catch (SecurityException e) {
            showInfo("MapInfo", "Show My Location Error: " + e.getMessage());
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
            showInfo("MapInfo", "Location not found");
        }

    }

    private void showInfo(String tag, String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Log.i(tag, message);
    }

    @Override
    public void onLocationChanged(Location location) {
        //запись пути для текущей пробежки, если она началась
        CurrentRunning.fixPosition(location.getLatitude(),location.getLongitude(),location.getSpeed(),location.getTime());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
