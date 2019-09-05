package com.triple.trackme.Activity;

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
import com.triple.trackme.TrainingsAdapter;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView trainingsView;
    private RecyclerView.Adapter trainingsAdapter;
    private RecyclerView.LayoutManager trainingsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateWindow();
        setContentView(R.layout.activity_profile);
        setTrainingsView();
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

    private void setTrainingsView() {
        trainingsView = findViewById(R.id.trainingsList);
        trainingsView.setHasFixedSize(true);
        trainingsManager = new LinearLayoutManager(this);
        trainingsView.setLayoutManager(trainingsManager);
        trainingsAdapter = new TrainingsAdapter(CurrentUserData.getTrackDataAll());
        trainingsView.setAdapter(trainingsAdapter);
    }

    public void clickBackButton(final View view) {
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.scale_interface);
        view.startAnimation(animScale);
        finish();
    }
}
