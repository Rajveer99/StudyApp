package com.example.owner.studi2;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DisplayStats<study> extends AppCompatActivity {
    Button button_start;
    private int studyCounter;
    private int breakCounter;
    TextView studyTime;
    TextView breakTime;

    CheckBox checkBox1;
    CheckBox checkBox2;
    CheckBox checkBox3;
    CheckBox checkBox4;
    CheckBox checkBox5;

    private ProgressBar progressBar;

    private int count = 0;

    private int userStudyTime;
    private int userBreakTime;
    private int userRepetitions;
    boolean mode = true;
    int userStudyTime_milli = 0;
    int userBreakTime_milli = 0;
    private ActivityManager activityManager;
    private ComponentName compName;
    private DevicePolicyManager devicePolicyManager;
    ArrayList<String> userGoalList;

    ArrayList<CheckBox> checkboxList = new ArrayList<>();

    ArrayList<String> goalsCompleted = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_stats);
        setTitle("Progress");
        progressBar = (ProgressBar) findViewById(R.id.disProgressBar);
        //getting user data from activity 2
        Intent intent = getIntent();
        userStudyTime = intent.getIntExtra("StudyTime", 0);
        userBreakTime = intent.getIntExtra("BreakTime", 0);
        userRepetitions =  intent.getIntExtra("Cycles", 0);
        userGoalList = (ArrayList<String>) getIntent().getSerializableExtra("goals");

        compName = new ComponentName(this, myAdmin.class);
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        userStudyTime_milli = userStudyTime *  60   * 1000;
        userBreakTime_milli = userBreakTime * 60 * 1000;

        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
        checkBox5 = (CheckBox) findViewById(R.id.checkBox5);


        checkboxList.add(checkBox1);
        checkboxList.add(checkBox2);
        checkboxList.add(checkBox3);
        checkboxList.add(checkBox4);
        checkboxList.add(checkBox5);

        studyTime = (TextView) findViewById(R.id.textView_studyTime);
        breakTime = (TextView) findViewById(R.id.textView_breakTime);
        button_start = (Button) findViewById(R.id.button_end);

        for (int i = 0; i < userGoalList.size(); i++){
            checkboxList.get(checkboxList.size() - 1 - i).setText(userGoalList.get(i));
            checkboxList.get(checkboxList.size() - 1 - i).setVisibility(View.VISIBLE);
        }

        studyTimer();
        boolean active = devicePolicyManager.isAdminActive(compName);

        if (active) {
            devicePolicyManager.lockNow();
        } else {
            Toast.makeText(this, "You need to enable the Admin Device Features", Toast.LENGTH_SHORT).show();
        }

        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton) view).isChecked()){
                    goalsCompleted.add(checkBox1.getText().toString());
                    checkBox1.setPaintFlags(checkBox1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    checkBox1.setEnabled(false);
                }
            }
        });

        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton) view).isChecked()){
                    goalsCompleted.add(checkBox2.getText().toString());
                    checkBox2.setPaintFlags(checkBox1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    checkBox2.setEnabled(false);
                }
            }
        });
        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton) view).isChecked()){
                    goalsCompleted.add(checkBox3.getText().toString());
                    checkBox3.setPaintFlags(checkBox1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    checkBox3.setEnabled(false);
                }
            }
        });

        checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton) view).isChecked()){
                    goalsCompleted.add(checkBox4.getText().toString());
                    checkBox4.setPaintFlags(checkBox1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    checkBox4.setEnabled(false);                }
            }
        });

        checkBox5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton) view).isChecked()){
                    goalsCompleted.add(checkBox5.getText().toString());
                    checkBox5.setPaintFlags(checkBox1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    checkBox5.setEnabled(false);
                }
            }
        });

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endActivity();
            }
        });

    }

    public void endActivity(){
        studyTime.setText("Ended");
        breakTime.setText("Ended");
        mode = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayStats.this);
        builder.setCancelable(true);

        if (userGoalList.size() > 0){
            float percentCompleted = goalsCompleted.size()/userGoalList.size() * 100;
            if (percentCompleted == 100){
                builder.setTitle("Congratulations");
                builder.setMessage("Amazing work!! You've accomplished " + (int) percentCompleted + "% of your goals");
            }
            else if(percentCompleted > 0){
                builder.setTitle("Almost there");
                builder.setMessage("Great work! You've accomplished " + (int) percentCompleted + "% of your goals");
            }
            else{
                builder.setTitle("Let's try again");
                builder.setMessage("You've not been able to accomplish your goals. " +
                        "It's okay. Let's try again!");
            }
        }

        builder.setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                Intent intent = new Intent(DisplayStats.this, enterParameters.class);
                startActivity(intent);
            }
        });

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();
                Intent intent = new Intent(DisplayStats.this, mainMenu.class);
                startActivity(intent);
            }
        });
        builder.show();
    }

    public void studyTimer(){
        new CountDownTimer(userStudyTime_milli, 1000) {
            public void onTick(long millisUntilFinished) {
                if (mode) {
                    studyCounter++;
                    float minute = (float) millisUntilFinished / 60000;
                    //studyTime.setText(String.valueOf(minutesToMilli(studyCounter)));
                    studyTime.setText(minutesToMilli(studyCounter));

                    float val = (float) minute / userStudyTime;
                    progressBar.setProgress((int) Math.floor(100.0 * (val)));
                } else {
                    breakCounter = 0;
                    studyCounter = 0;
                    cancel();
                }
            }
            public void onFinish() {
                count+=1;
                ringNotification();
                switchToBreakTime();
            }
        }.start();
    }
    public void switchToBreakTime() {
        if (count < userRepetitions) {
            new CountDownTimer(userBreakTime_milli, 1000) {
                public void onTick(long millisUntilFinished) {
                    if (mode) {
                        breakCounter++;
                        float minute = (float) millisUntilFinished / 60000;
                       // breakTime.setText(String.valueOf(minutesToMilli(breakCounter)));
                        breakTime.setText(minutesToMilli(breakCounter));
                        float val = (float) minute / userBreakTime;
                        progressBar.setProgress((int) Math.floor(100.0 * (val)));
                    } else {
                        breakCounter = 0;
                        studyCounter = 0;
                        cancel();
                    }
                }
                public void onFinish() {
                    ringNotification();
                    studyTimer();
                }
            }.start();
        }
        else{
            endActivity();
        }
    }

    public String minutesToMilli(int mins){
        String startTime = "00:00:00";
        int m = mins / 60 + Integer.parseInt(startTime.substring(3,4));
        int s = mins % 60 + Integer.parseInt(startTime.substring(6,7));
        int h = (mins % 60) / 60 + Integer.parseInt(startTime.substring(0,1));
        String newtime = h+":"+m+":"+s;
        return newtime;
    }
    public void ringNotification(){
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}