package com.triple.trackme.Information;

import android.app.Activity;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class CurrentTrack {

    private static boolean isStart;
    private static int trackSeconds;

    private static TextView secondsTextView;

    public static void start(TextView secondsTextView) {
        isStart = true;
        trackSeconds = 0;
        CurrentTrack.secondsTextView = secondsTextView;

        startTimer();
    }

    private static void startTimer() {
        TimerTask repeatedTimerTask = new TimerTask() {
            public void run() {
                if (isStart) {
                    trackSeconds++;
                    updateUI();
                }
            }
        };
        Timer trackTimer = new Timer("TrackTimer");
        trackTimer.scheduleAtFixedRate(repeatedTimerTask, 1000, 1000);
    }

    private static void updateUI() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                text.setText(value);
//            }
//        });
        // secondsTextView.setText("" + trackSeconds);
    }
}
