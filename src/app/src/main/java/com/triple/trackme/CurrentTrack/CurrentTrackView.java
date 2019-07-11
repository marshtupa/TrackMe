package com.triple.trackme.CurrentTrack;

import android.content.Context;
import android.location.Location;
import android.widget.ImageButton;
import android.widget.TextView;

import com.triple.trackme.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

public class CurrentTrackView {

    private static boolean isStart;
    private static boolean isInProcess;

    private static CurrentTrackData currentTrackData;

    private static Context context;
    private static TextView timeTextView;
    private static TextView distanceTextView;
    private static TextView speedTextView;
    private static ImageButton pauseButton;
    private static ImageButton startButton;

    private static Timer trackTimer;


    public static void initialize(Context context, TextView timeTextView, TextView distanceTextView,
                                  TextView speedTextView, ImageButton pauseButton, ImageButton startButton) {
        isStart = false;
        isInProcess = false;

        currentTrackData = new CurrentTrackData();

        CurrentTrackView.context = context;
        CurrentTrackView.timeTextView = timeTextView;
        CurrentTrackView.distanceTextView = distanceTextView;
        CurrentTrackView.speedTextView = speedTextView;
        CurrentTrackView.pauseButton = pauseButton;
        CurrentTrackView.startButton = startButton;

        updateDataUI();
        updateButtonsUI(true, false);
    }

    public static void start() {
        if (isStart) {
            if (!isInProcess) {
                initializeDataResume();
            }
        }
        else {
            initializeDataStart();
        }
    }

    public static void pause() {
        if (isStart && isInProcess) {
            initializeDataPause();
        }
    }

    public static void stop() {
        if (isStart) {
            initializeDataStop();
        }
    }

    public static void newLocation(Location newLocation) {
        currentTrackData.newLocation(newLocation);
    }


    private static void initializeDataStart() {
        isStart = true;
        isInProcess = true;

        currentTrackData.initializeEmptyData();

        updateDataUI();
        updateButtonsUI(false, true);
        startTimer();
    }

    private static void initializeDataPause() {
        isStart = true;
        isInProcess = false;

        updateDataUI();
        updateButtonsUI(true, false);
        trackTimer.cancel();
    }

    private static void initializeDataResume() {
        isStart = true;
        isInProcess = true;

        updateDataUI();
        updateButtonsUI(false, true);
        startTimer();
    }

    private static void initializeDataStop() {
        isStart = false;
        isInProcess = false;

        currentTrackData.initializeEmptyData();

        updateDataUI();
        updateButtonsUI(true, false);
        trackTimer.cancel();
    }


    private static void updateDataUI() {
        ((MainActivity)context).setText(timeTextView, currentTrackData.getTrackTimeStr());
        ((MainActivity)context).setText(distanceTextView, currentTrackData.getTrackDistanceStr());
        ((MainActivity)context).setText(speedTextView, currentTrackData.getTrackSpeedStr());
    }

    private static void updateButtonsUI(boolean startButtonEnable, boolean pauseButtonEnable) {
        ((MainActivity)context).enableButton(startButton, startButtonEnable);
        ((MainActivity)context).enableButton(pauseButton, pauseButtonEnable);
    }

    private static void startTimer() {
        TimerTask repeatedTimerTask = new TimerTask() {
            public void run() {
                if (isStart) {
                    currentTrackData.incrementTrackTime();
                    updateDataUI();
                }
            }
        };
        trackTimer = new Timer("TrackTimer");
        trackTimer.scheduleAtFixedRate(repeatedTimerTask, 1000, 1000);
    }

}
