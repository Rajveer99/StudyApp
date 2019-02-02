package com.example.owner.studi2;

import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.content.Intent;
import android.widget.Toast;
import android.widget.Chronometer;

/**
 *
 * @param <study>
 */


public class DisplayStats<study> extends AppCompatActivity {
    Button resume;
    Button pause;
    Chronometer break_chronometer = null;
    Chronometer study_chronometer = null;
    int elapsedMillisS, elapsedMillisB, st, bt, totaltime,totalStudy, totalBreak, r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_stats);
        setTitle("Progress");
        //final MediaPlayer breakMusic = MediaPlayer.create(this, R.raw.BreakTimeSoundEffect);
        resume = (Button)findViewById(R.id.resume);
        pause = (Button)findViewById(R.id.pause);
        break_chronometer = findViewById(R.id.break_chronometer);
        study_chronometer = findViewById(R.id.study_chronometer);
        study_chronometer.start();
        pause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                break_chronometer.setBase(SystemClock.elapsedRealtime());
                study_chronometer.stop();
                break_chronometer.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);
                break_chronometer.start();
                resume.setVisibility(View.VISIBLE);

            }
        });

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                study_chronometer.start();
                resume.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
                break_chronometer.setVisibility(View.INVISIBLE);
                break_chronometer.stop();
                break_chronometer.setBase(SystemClock.elapsedRealtime());

            }
        });

        Runnable stopClock = new Runnable()
        {
            @Override
            public void run()
            {
                break_chronometer.start();
                break_chronometer.setBase(SystemClock.elapsedRealtime());
                study_chronometer.stop();
                break_chronometer.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);
                break_chronometer.start();
                resume.setVisibility(View.VISIBLE);


            }
        };
        study_chronometer.postDelayed(stopClock, 1000 * st * 60);
        for(int i = 0; i<r;i++){
            study_chronometer.start();
            study_chronometer.postDelayed(stopClock, 1000 * st * 60);

        }


        /**
        String study, break_time, repitions;
        break_time = (getIntent().getStringExtra("BreakTime"));
        study = (getIntent().getStringExtra("StudyTime"));
        repitions = (getIntent().getStringExtra("Cycles"));
        createToast(break_time);
        createToast(study);
        createToast(repitions);



        st = Integer.parseInt(study);
        bt = Integer.parseInt(break_time);
        r= Integer.parseInt(repitions);
        totaltime = (st+bt)/r;

         */

        while(elapsedMillisS<totaltime){

            elapsedMillisS = (int) (SystemClock.elapsedRealtime() - study_chronometer.getBase());
            totalStudy = msToMins(elapsedMillisS);

            if (totalStudy%st == 0){
                break_chronometer.setBase(SystemClock.elapsedRealtime());
                study_chronometer.stop();
                break_chronometer.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);
                break_chronometer.start();
                resume.setVisibility(View.VISIBLE);
          //      breakMusic.start();
                while(totalBreak<bt){
                    elapsedMillisB = (int) (SystemClock.elapsedRealtime() - break_chronometer.getBase());
                    totalBreak = msToMins(elapsedMillisB);
                    //if (totalBreak==15000){
                        //breakMusic.stop();
                  //  }


                    }
            }


            }
        study_chronometer.stop();
    }
    private void createToast(String message){
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_LONG).show();
    }

    public int msToMins(int ms) {
        int mins =(int)(ms/60000);
        return  mins;
    }




}