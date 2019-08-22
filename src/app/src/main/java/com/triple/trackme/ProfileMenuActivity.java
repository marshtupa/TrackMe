package com.triple.trackme;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.BackgroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.common.internal.Objects;
import com.google.android.material.resources.TextAppearance;
import com.triple.trackme.Data.Storage.Track;
import com.triple.trackme.R;

public class ProfileMenuActivity extends Activity {

    private Button button;
    private LinearLayout linearLayout;

    private final int USERID = 6000;
    private int countID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.profile_menu);

    }

    public void clickBackButton(View view) {
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.scale_interface);
        view.startAnimation(animScale);

        Intent intent = new Intent(ProfileMenuActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void clickEditInfoButton(View view) {
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.scale_interface);
        view.startAnimation(animScale);

        //Intent intent = new Intent(ProfileMenuActivity.this, MainActivity.class);
        //startActivity(intent);
    }

    public void TestButtonClick(View view){
        button = (Button) findViewById(R.id.TestButton); //инициатор

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count_of_tracks = 2;
                Track[] AllTracks = new Track[count_of_tracks];
                Track one = new Track();
                one.avgSpeed = 10.21;
                one.distance = 100.12;
                one.time = 400;
                one.dateTime = "01.01.2019";
                Track two = new Track();
                two.avgSpeed = 11;
                two.distance = 111;
                two.time = 14;
                two.dateTime = "02.02.2019";
                AllTracks[0] = one;
                AllTracks[1] = two;

                initAllTrainings(AllTracks,count_of_tracks);

                //DistanceTextGen.setOnClickListener(new View.OnClickListener() {
                //    @Override
                //    public void onClick(View v) {
                //        linearLayout.removeView(v);
                //    }
                //});
            }
        });
    }


    private void initTextLabel(String Text, TextView NewTextLabel, TextView source){
        LinearLayout.LayoutParams lpParams = new LinearLayout.LayoutParams( source.getWidth(), source.getHeight() );
        lpParams.setMargins(0,0,0,0);
        NewTextLabel.setText(Text);
        NewTextLabel.setLayoutParams(lpParams);
        NewTextLabel.setTextColor(source.getTextColors());
        NewTextLabel.setFontFeatureSettings(source.getFontFeatureSettings());
        NewTextLabel.setGravity(source.getGravity());
        NewTextLabel.setTextAlignment(source.getTextAlignment());
        NewTextLabel.setTextSize(source.getTextSize()/3);
        NewTextLabel.setX(source.getX());
        NewTextLabel.setY(source.getY());
        NewTextLabel.setTypeface(source.getTypeface());
        NewTextLabel.setLeft(source.getLeft());
        NewTextLabel.setRight(source.getRight());
        NewTextLabel.setTop(source.getTop());
        NewTextLabel.setBottom(source.getBottom());
    }

    private void initAllTrainings(Track[] tracks, int CountOfTracks){
        linearLayout = (LinearLayout) findViewById(R.id.LinearLayoutProfileMenu); //поле вывода
        final View ExampleCard;
        final androidx.constraintlayout.widget.ConstraintLayout LabelFrame;
        ExampleCard = findViewById(R.id.training_card);
        LabelFrame = findViewById(R.id.frameLayout);
        intContainer LAST_ID = new intContainer();
        LAST_ID.setValue(USERID);

        if (CountOfTracks > 0){
            //init all tracks
            for (int i = 0; i < CountOfTracks; i++){
                if (tracks[i] != null){
                    //creating frame
                    //main vidjet
                    androidx.constraintlayout.widget.ConstraintLayout Vidjet =
                            new androidx.constraintlayout.widget.ConstraintLayout(getApplicationContext());
                    //ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(10,10);
                    LinearLayout.LayoutParams lpParams = new LinearLayout.LayoutParams(ExampleCard.getWidth(),ExampleCard.getHeight());
                    lpParams.setMargins(0,20,0,20);
                    Vidjet.setLayoutParams(lpParams);
                    Vidjet.setX(ExampleCard.getX());
                    Vidjet.setBackgroundColor(ExampleCard.getSolidColor());
                    Vidjet.setBackground(ExampleCard.getBackground());


                    Vidjet.setId(LAST_ID.getValue());
                    LAST_ID.increaseCount();
                    linearLayout.addView(Vidjet);

                    //frame
                    androidx.constraintlayout.widget.ConstraintLayout LabelFrameGen =
                            new androidx.constraintlayout.widget.ConstraintLayout(getApplicationContext());
                    LabelFrameGen.setLayoutParams(
                            new LinearLayout.LayoutParams( LabelFrame.getWidth(), LabelFrame.getHeight() )
                    );
                    LabelFrameGen.setX(LabelFrame.getX());
                    LabelFrameGen.setBackgroundColor(LabelFrame.getSolidColor());
                    LabelFrameGen.setBackground(ExampleCard.getBackground());
                    LabelFrameGen.setForegroundGravity(LabelFrame.getForegroundGravity());
                    LabelFrameGen.setId(LAST_ID.getValue());
                    LAST_ID.increaseCount();
                    Vidjet.addView(LabelFrameGen);

                    //textLabels
                    AddNewTrainingStats(tracks[i],LabelFrameGen, LAST_ID);
                }
            }
        }
    }

    private void AddNewTrainingStats(Track track, androidx.constraintlayout.widget.ConstraintLayout LabelFrameGen, intContainer LAST_ID){
        double distance = track.distance;
        double avgSpeed = track.avgSpeed;
        double time = track.time;
        String date = track.dateTime;

        final TextView AvgSpeedText;
        final TextView DistanceText;
        final TextView TimeText;
        final TextView SpeedLabel;
        final TextView DistanceLabel;
        DistanceText = findViewById(R.id.trainingDistance1);
        AvgSpeedText = findViewById(R.id.trainingAvgSpeed1);
        TimeText = findViewById(R.id.trainingTime1);
        SpeedLabel = findViewById(R.id.speedLabel1);
        DistanceLabel = findViewById(R.id.distanceLabel2);

        String distanceStr = "";
        int koef = 10;
        int km = 0; int m = 0;
        km = (int) ((distance - (distance % 1000))/1000);
        m = (int) (distance % 1000);
        distanceStr = distanceStr + km;
        if (km < 1000){
            distanceStr = distanceStr + ".";
            if (km < 10){
                if (m < 10) distanceStr = distanceStr + "0";
                if (m < 100) distanceStr = distanceStr + "0";
            }
            if ((km < 100)&&(km >= 10)){
                m = (int) ((m - (m % 10))/10);
                if (m < 10) distanceStr = distanceStr + "0";
            }
            if (km >= 100){
                m = (int) ((m - (m % 100))/100);
            }
            distanceStr = distanceStr + m;
        }
        TextView DistanceTextGen = new TextView(getApplicationContext());
        initTextLabel(distanceStr,DistanceTextGen,DistanceText);
        DistanceTextGen.setId(LAST_ID.getValue());
        LAST_ID.increaseCount();
        LabelFrameGen.addView(DistanceTextGen);

        String speedStr = "";
        int kilometrs = 0; int metrs = 0;
        kilometrs = (int) (avgSpeed - (avgSpeed % 1));
        metrs = (int) ((avgSpeed*100) % 100);
        if (kilometrs < 10) speedStr = speedStr + "0";
        speedStr = speedStr + kilometrs + ".";
        if (metrs < 10) speedStr = speedStr + "0";
        speedStr = speedStr + metrs;
        TextView AvgSpeedTextGen = new TextView(getApplicationContext());
        initTextLabel(speedStr, AvgSpeedTextGen,AvgSpeedText);
        AvgSpeedTextGen.setId(LAST_ID.getValue());
        LAST_ID.increaseCount();
        LabelFrameGen.addView(AvgSpeedTextGen);

        String timeStr = "";
        int hours = 0; int minutes = 0; int seconds = 0;
        seconds = (int)(time % 60);
        minutes = (int)(time - seconds)/60;
        hours = (int)(minutes - (minutes % 60))/60;
        if (hours < 10) timeStr = timeStr + "0";
        timeStr = timeStr + hours + ":";
        if (minutes < 10) timeStr = timeStr + "0";
        timeStr = timeStr + minutes + ":";
        if (seconds < 10) timeStr = timeStr + "0";
        timeStr = timeStr + seconds;
        TextView TimeTextGen = new TextView(getApplicationContext());
        initTextLabel(timeStr, TimeTextGen,TimeText);
        TimeTextGen.setId(LAST_ID.getValue());
        LAST_ID.increaseCount();
        LabelFrameGen.addView(TimeTextGen);

        TextView SpeedLabelGen = new TextView(getApplicationContext());
        initTextLabel(SpeedLabel.getText().toString(), SpeedLabelGen,SpeedLabel);
        SpeedLabelGen.setId(LAST_ID.getValue());
        LAST_ID.increaseCount();
        LabelFrameGen.addView(SpeedLabelGen);

        TextView DistanceLabelGen = new TextView(getApplicationContext());
        initTextLabel(DistanceLabel.getText().toString(), DistanceLabelGen,DistanceLabel);
        DistanceLabelGen.setId(LAST_ID.getValue());
        LAST_ID.increaseCount();
        LabelFrameGen.addView(DistanceLabelGen);
    }

}
