package com.example.owner.studi2;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

/**
 * @param <study>
 *
 */

public class DisplayStats<study> extends AppCompatActivity {
    Button button_start;
    private int studyCounter;
    private int breakCounter;
    TextView studyTime;
    TextView breakTime;
    TextView motivations_textview;


    //This is a list of Motivational Quotes form online
    private String[] motivations = {"'Learning is the only thing the mind never exhausts, never fears, and never regrets.' – Leonardo da Vinci",
    "'If people knew how hard I worked to achieve my mastery, it wouldn’t seem so wonderful after all.'  – Michelangelo",
    "'Successful people begin where failures leave off. Never settle for ‘just getting the job done.’ Excel!' – Tom Hopkins",
    "“I find that the harder I work, the more luck I seem to have.” – Thomas Jefferson",
    "“Live as if you were to die tomorrow. Learn as if you were to live forever.” – Mahatma Gandhi",
    "“The capacity to learn is a gift; the ability to learn is a skill; the willingness to learn is a choice.” – Brian Herbert",
    "“The dictionary is the only place that success comes before work. Work is the key to success, and hard work can help you accomplish anything.” – Vince Lombardi",
    "“An investment in knowledge pays the best interest.”- Benjamin Franklin",
    "“Talent is cheaper than table salt. What separates the talented individual from the successful one is a lot of hard work.” – Stephen King",
    "“Success is no accident. It is hard work, perseverance, learning, studying, sacrifice and most of all, love of what you are doing or learning to do.” – Pelé"};


    private int userStudyTime;
    private int userBreakTime;
    boolean mode = true;
    int userStudyTime_milli = 0;
    int userBreakTime_milli = 0;
    private ActivityManager activityManager;
    private ComponentName compName;
    private DevicePolicyManager devicePolicyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_stats);
        setTitle("Progress");

        //getting user data from activity 2
        Intent intent = getIntent();
        userStudyTime = intent.getIntExtra("StudyTime", 0);
        userBreakTime = intent.getIntExtra("BreakTime", 0);
        compName = new ComponentName(this, myAdmin.class);
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        userStudyTime_milli = userStudyTime *  60   * 1000;
        userBreakTime_milli = userBreakTime * 60 * 1000;

        studyTime = (TextView) findViewById(R.id.textView_studyTime);
        breakTime = (TextView) findViewById(R.id.textView_breakTime);
        button_start = (Button) findViewById(R.id.button_end);
        motivations_textview = (TextView) findViewById(R.id.textView_motivations) ;

        setMotivationalSpeech();
        studyTimer();
        boolean active = devicePolicyManager.isAdminActive(compName);

        if (active) {
            devicePolicyManager.lockNow();
        } else {
            Toast.makeText(this, "You need to enable the Admin Device Features", Toast.LENGTH_SHORT).show();
        }
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
        startActivity(new Intent(DisplayStats.this, mainMenu.class));

    }
    public void studyTimer(){
        new CountDownTimer(userStudyTime_milli, 1000) {
            public void onTick(long millisUntilFinished) {
                if (mode) {
                    studyCounter++;
                    studyTime.setText(String.valueOf(minutesToMilli(studyCounter)));

                }
                else {
                    breakCounter = 0;
                    studyCounter = 0;
                    cancel();
                }
            }
            public void onFinish() {
                ringNotification();
                switchToBreakTime();
            }
        }.start();
    }
    public void switchToBreakTime() {
        new CountDownTimer(userBreakTime_milli, 1000) {
            public void onTick(long millisUntilFinished) {
                if(mode) {
                    breakCounter++;
                    breakTime.setText(String.valueOf(minutesToMilli(breakCounter)));

                }
                else {
                    breakCounter = 0;
                    studyCounter = 0;
                    cancel();
                }
            }

            public void onFinish() {
                ringNotification();
                setMotivationalSpeech();
                studyTimer();
            }
        }.start();
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

    public void setMotivationalSpeech(){
        Random r=new Random();
        int randomNumber=r.nextInt(motivations.length);
        motivations_textview.setText(motivations[randomNumber]);
    }

}