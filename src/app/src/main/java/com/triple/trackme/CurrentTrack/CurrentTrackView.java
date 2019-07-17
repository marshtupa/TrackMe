package com.triple.trackme.CurrentTrack;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.triple.trackme.MainActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CurrentTrackView {

    public enum CurrentTrackState { STOP, START, PAUSE }
    private static CurrentTrackState trackState;

    private static ArrayList<Polyline> polylines;
    private static CurrentTrackData currentTrackData;

    private static Context context;
    private static TextView timeTextView;
    private static TextView distanceTextView;
    private static TextView speedTextView;
    private static ImageButton stopButton;
    private static ImageButton pauseButton;
    private static ImageButton startButton;

    private static Timer trackTimer;

    public static void initialize(Context context,
                                  TextView timeTextView, TextView distanceTextView, TextView speedTextView,
                                  ImageButton stopButton, ImageButton pauseButton, ImageButton startButton) {

        trackState = CurrentTrackState.STOP;

        currentTrackData = new CurrentTrackData();
        polylines = new ArrayList<Polyline>();

        CurrentTrackView.context = context;
        CurrentTrackView.timeTextView = timeTextView;
        CurrentTrackView.distanceTextView = distanceTextView;
        CurrentTrackView.speedTextView = speedTextView;
        CurrentTrackView.stopButton = stopButton;
        CurrentTrackView.pauseButton = pauseButton;
        CurrentTrackView.startButton = startButton;

        updateDataUI();
        updateButtonsUI(true, false);
    }

    public static void start() {
        if (trackState == CurrentTrackState.PAUSE) {
            initializeDataResume();
        }
        else {
            initializeDataStart();
        }
    }

    public static void pause() {
        if (trackState == CurrentTrackState.START) {
            initializeDataPause();
        }
    }

    public static void stop() {
        if (trackState == CurrentTrackState.START || trackState == CurrentTrackState.PAUSE) {
            currentTrackData.saveData();
            cleanRoute();
            initializeDataStop();
        }
    }

    public static void newLocation(Location newLoc, GoogleMap map) {
        if (trackState == CurrentTrackState.START) {
            Location lastLoc = currentTrackData.getLastPosition();
            currentTrackData.newPosition(newLoc);
            if (lastLoc != null) {
                drawRoute(map, lastLoc, newLoc);
            }
        }
    }

    private static void drawRoute(GoogleMap map, Location lastLoc, Location newLoc) {
        Polyline polyline = map.addPolyline(new PolylineOptions()
                .add(new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude()), new LatLng(newLoc.getLatitude(), newLoc.getLongitude()))
                .width(20)
                .color(Color.argb(90, 0, 130, 255))
        );
        polylines.add(polyline);
    }

    private static void cleanRoute() {
        for(Polyline line : polylines) {
            line.remove();
        }
        polylines = new ArrayList<Polyline>();
    }

    private static void initializeDataStart() {
        trackState = CurrentTrackState.START;

        currentTrackData = new CurrentTrackData();

        updateDataUI();
        updateButtonsUI(false, true);
        startTimer();
    }

    private static void initializeDataPause() {
        trackState = CurrentTrackState.PAUSE;

        updateDataUI();
        updateButtonsUI(true, false);
        trackTimer.cancel();
    }

    private static void initializeDataResume() {
        trackState = CurrentTrackState.START;

        updateDataUI();
        updateButtonsUI(false, true);
        startTimer();
    }

    private static void initializeDataStop() {
        trackState = CurrentTrackState.STOP;

        currentTrackData = new CurrentTrackData();

        updateDataUI();
        updateButtonsUI(true, false);
        trackTimer.cancel();
    }

    private static void updateDataUI() {
        ((MainActivity)context).setText(timeTextView, currentTrackData.timeToFormatString());
        ((MainActivity)context).setText(distanceTextView, currentTrackData.distanceToFormatString());
        ((MainActivity)context).setText(speedTextView, currentTrackData.speedToFormatString());
    }

    private static void updateButtonsUI(final boolean startButtonEnable, final boolean pauseButtonEnable) {
        if (startButtonEnable) {
            startButton.setClickable(true);
            pauseButton.setClickable(false);
        }
        else {
            startButton.setClickable(false);
            pauseButton.setClickable(true);
        }

        TimerTask changeButtonsTask = new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)context).enableButton(startButton, startButtonEnable);
                ((MainActivity)context).enableButton(pauseButton, pauseButtonEnable);
            }
        };
        Timer changeButtonsTimer = new Timer();
        changeButtonsTimer.schedule(changeButtonsTask, 290);
    }

    private static void startTimer() {
        TimerTask repeatedTimerTask = new TimerTask() {
            public void run() {
                if (trackState == CurrentTrackState.START) {
                    currentTrackData.addSeconds(1);
                    updateDataUI();
                }
            }
        };
        trackTimer = new Timer("TrackTimer");
        trackTimer.scheduleAtFixedRate(repeatedTimerTask, 1000, 1000);
    }
}
