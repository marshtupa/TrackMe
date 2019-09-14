package com.triple.trackme.Activity.CompletedTracks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.triple.trackme.CurrentUser.CurrentUserData;
import com.triple.trackme.R;

import java.util.Timer;
import java.util.TimerTask;

public class CompletedTracksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateWindow();
        setContentView(R.layout.activity_completed_tracks);
        setTracksView();
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

    private void setTracksView() {
        RecyclerView tracksView = findViewById(R.id.tracksList);
        tracksView.setHasFixedSize(true);
        RecyclerView.LayoutManager tracksManager = new LinearLayoutManager(this);
        tracksView.setLayoutManager(tracksManager);
        RecyclerView.Adapter tracksAdapter = new TracksViewAdapter(
                this, CurrentUserData.getTrackDataAll());
        tracksView.setAdapter(tracksAdapter);
    }

    public void clickBackButton(final View view) {
        final int BUTTON_ANIMATION_DELAY = 130;
        final Animation animScale = AnimationUtils.loadAnimation(
                this, R.anim.scale_menu_button);
        view.startAnimation(animScale);
        view.setClickable(false);

        TimerTask changeButtonsTask = new TimerTask() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(
                        R.anim.activity_slide_right_out, R.anim.activity_slide_right_in);
                view.setClickable(true);
            }
        };
        Timer changeButtonsTimer = new Timer();
        changeButtonsTimer.schedule(changeButtonsTask, BUTTON_ANIMATION_DELAY);
    }
}
