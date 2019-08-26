package com.triple.trackme.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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
        setContentView(R.layout.activity_profile);
        setTrainingsView();
    }

    private void setTrainingsView() {
        trainingsView = findViewById(R.id.trainingsList);
        trainingsView.setHasFixedSize(true);
        trainingsManager = new LinearLayoutManager(this);
        trainingsView.setLayoutManager(trainingsManager);
        trainingsAdapter = new TrainingsAdapter(CurrentUserData.getTrackDataAll());
        trainingsView.setAdapter(trainingsAdapter);
    }
}
