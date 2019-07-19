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

    private enum CurrentTrackState { STOP, START, PAUSE }
    private final static int MIN_TIME_SECONDS_FOR_SAVE_TRACK = 10;

    private static CurrentTrackData currentTrackData;
    private static CurrentTrackState trackState;
    private static ArrayList<Polyline> polylines;
    private static Timer trackTimer;

    private static Context context;
    private static TextView timeTextView;
    private static TextView distanceTextView;
    private static TextView speedTextView;
    private static ImageButton stopButton;
    private static ImageButton pauseButton;
    private static ImageButton startButton;

    public static void initializeTrack(Context context,
                                       TextView timeTextView, TextView distanceTextView, TextView speedTextView,
                                       ImageButton stopButton, ImageButton pauseButton, ImageButton startButton) {

        CurrentTrackView.context = context;
        CurrentTrackView.timeTextView = timeTextView;
        CurrentTrackView.distanceTextView = distanceTextView;
        CurrentTrackView.speedTextView = speedTextView;
        CurrentTrackView.stopButton = stopButton;
        CurrentTrackView.pauseButton = pauseButton;
        CurrentTrackView.startButton = startButton;

        setTrackState(CurrentTrackState.STOP);
    }

    public static void startTrack() {
        if (trackState != CurrentTrackState.START) {
            setTrackState(CurrentTrackState.START);
        }
    }

    public static void pauseTrack() {
        if (trackState == CurrentTrackState.START) {
            setTrackState(CurrentTrackState.PAUSE);
        }
    }

    public static void stopTrack(Context context) {
        if (trackState != CurrentTrackState.STOP) {
            if (currentTrackData.getAllTimeInSeconds() >= MIN_TIME_SECONDS_FOR_SAVE_TRACK) {
                setTrackState(CurrentTrackState.PAUSE);
                StopTrackDialog stopTrackDialog = new StopTrackDialog();
                stopTrackDialog.create(context).show();
            }
            else {
                setTrackState(CurrentTrackState.STOP);
            }
        }
    }

    public static void endTrackAndSave() {
        if (trackState != CurrentTrackState.STOP) {
            currentTrackData.saveData();
            setTrackState(CurrentTrackState.STOP);
        }
    }

    private static void setTrackState(CurrentTrackState trackState) {
        CurrentTrackView.trackState = trackState;

        switch (trackState) {
            case START:
                startTrackTimer();
                updateDataUI();
                updateButtonsUI(false, true);
                break;
            case PAUSE:
                stopTrackTimer();
                updateDataUI();
                updateButtonsUI(true, false);
                break;
            case STOP:
                cleanTrackData();
                cleanRoute();
                stopTrackTimer();
                updateDataUI();
                updateButtonsUI(true, false);
                break;
        }
    }

    public static void newPosition(Location newPosition, GoogleMap map) {
        if (trackState == CurrentTrackState.START) {
            Location lastPosition = currentTrackData.getLastPosition();
            currentTrackData.newPosition(newPosition);
            if (lastPosition != null) {
                drawRoute(map, lastPosition, newPosition);
            }
        }
    }

    private static void drawRoute(GoogleMap map, Location lastPosition, Location newPosition) {
        Polyline polyline = map.addPolyline(new PolylineOptions()
                .add(new LatLng(lastPosition.getLatitude(), lastPosition.getLongitude()),
                        new LatLng(newPosition.getLatitude(), newPosition.getLongitude()))
                .width(20)
                .color(Color.argb(90, 0, 130, 255))
        );
        polylines.add(polyline);
    }

    private static void cleanTrackData() {
        currentTrackData = new CurrentTrackData();
    }

    private static void cleanRoute() {
        if (polylines != null) {
            for(Polyline line : polylines) {
                line.remove();
            }
        }
        polylines = new ArrayList<Polyline>();
    }

    private static void updateDataUI() {
        ((MainActivity) context).setText(timeTextView, currentTrackData.timeToFormatString());
        ((MainActivity) context).setText(distanceTextView, currentTrackData.distanceToFormatString());
        ((MainActivity) context).setText(speedTextView, currentTrackData.speedToFormatString());
    }

    private static void updateButtonsUI(final boolean startButtonEnable, final boolean pauseButtonEnable) {
        final int CHANGE_BUTTONS_DELAY = 290;

        startButton.setClickable(startButtonEnable);
        pauseButton.setClickable(pauseButtonEnable);
        stopButton.setClickable(false);

        TimerTask changeButtonsTask = new TimerTask() {
            @Override
            public void run() {
                ((MainActivity) context).enableButton(startButton, startButtonEnable);
                ((MainActivity) context).enableButton(pauseButton, pauseButtonEnable);
                stopButton.setClickable(true);
            }
        };
        Timer changeButtonsTimer = new Timer();
        changeButtonsTimer.schedule(changeButtonsTask, CHANGE_BUTTONS_DELAY);
    }

    private static void startTrackTimer() {
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

    private static void stopTrackTimer() {
        if (trackTimer != null) {
            trackTimer.cancel();
        }
    }
}
