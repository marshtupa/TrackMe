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

    private static CurrentTrackData currentTrackData;
    private static CurrentTrackState trackState;
    private static ArrayList<Polyline> polylines;
    private static Timer trackTimer;

    private static Context mainActivityContext;
    private static TextView timeTextView;
    private static TextView distanceTextView;
    private static TextView speedTextView;
    private static ImageButton stopButton;
    private static ImageButton pauseButton;
    private static ImageButton startButton;

    public static void initializeTrack(Context context,
                                       TextView timeTextView, TextView distanceTextView, TextView speedTextView,
                                       ImageButton stopButton, ImageButton pauseButton, ImageButton startButton) {

        CurrentTrackView.mainActivityContext = context;
        CurrentTrackView.timeTextView = timeTextView;
        CurrentTrackView.distanceTextView = distanceTextView;
        CurrentTrackView.speedTextView = speedTextView;
        CurrentTrackView.stopButton = stopButton;
        CurrentTrackView.pauseButton = pauseButton;
        CurrentTrackView.startButton = startButton;

        setTrackState(CurrentTrackState.STOP);
    }

    public static void startTrack() {
        setTrackState(CurrentTrackState.START);
    }

    public static void pauseTrack() {
        setTrackState(CurrentTrackState.PAUSE);
    }

    public static void stopTrack() {
        currentTrackData.saveData();
        setTrackState(CurrentTrackState.STOP);
    }

    private static void setTrackState(CurrentTrackState trackState) {
        CurrentTrackView.trackState = trackState;

        switch (trackState) {
            case START:
                startTrackTimer();
                updateDataUI();
                updateButtonsUI(false, true, true);
                break;
            case PAUSE:
                stopTrackTimer();
                updateDataUI();
                updateButtonsUI(true, false, true);
                break;
            case STOP:
                cleanTrackData();
                cleanRoute();
                stopTrackTimer();
                updateDataUI();
                updateButtonsUI(true, false, false);
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
        ((MainActivity)mainActivityContext).setText(timeTextView, currentTrackData.timeToFormatString());
        ((MainActivity)mainActivityContext).setText(distanceTextView, currentTrackData.distanceToFormatString());
        ((MainActivity)mainActivityContext).setText(speedTextView, currentTrackData.speedToFormatString());
    }

    private static void updateButtonsUI(final boolean startButtonEnable, final boolean pauseButtonEnable, final boolean stopButtonEnable) {
        final int CHANGE_BUTTONS_DELAY = 290;

        startButton.setClickable(startButtonEnable);
        pauseButton.setClickable(pauseButtonEnable);
        stopButton.setClickable(stopButtonEnable);

        TimerTask changeButtonsTask = new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)mainActivityContext).enableButton(startButton, startButtonEnable);
                ((MainActivity)mainActivityContext).enableButton(pauseButton, pauseButtonEnable);
                ((MainActivity)mainActivityContext).enableButton(stopButton, stopButtonEnable);
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
