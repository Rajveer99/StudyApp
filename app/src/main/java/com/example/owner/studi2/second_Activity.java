package com.example.owner.studi2;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class second_Activity extends AppCompatActivity {

    //store data from user
    int breakTimeInput;
    int cyclesInput;
    int studyTimeInput;

    TextView breakTime;
    TextView studyTime;
    TextView cycles;

    Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_);
        setTitle("Enter Parameters");
        configureNextButton();

        studyTime = (TextView) findViewById(R.id.study);
        cycles = (TextView) findViewById(R.id.repetitions);
        breakTime = (TextView) findViewById(R.id.breakTime);

        submitButton = (Button) findViewById(R.id.button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //user input
                    studyTimeInput = Integer.valueOf(studyTime.getText().toString());
                    breakTimeInput = Integer.valueOf(breakTime.getText().toString());
                    cyclesInput = Integer.valueOf(cycles.getText().toString());

                    /**
                    if(studyTimeInput == 0 || breakTimeInput == 0 || cyclesInput == 0){
                        createToast("Please enter values greater than 0");
                    }
                    else if (studyTimeInput < 20 && breakTimeInput > 10) {
                        createToast("Study Time must be longer 20 minutes and Break Time " +
                                "must not be longer than 10 minutes");

                    } else if (studyTimeInput < 20) {
                        createToast("Study Time must be >= 20 minutes");
                    } else if (breakTimeInput > 10) {
                        createToast("Break Time must < 10 minutes");

                    } else {
                        Intent intent = new Intent(second_Activity.this, DisplayStats.class);
                        intent.putExtra("StudyTime", studyTimeInput);
                        intent.putExtra("BreakTime", breakTimeInput);
                        intent.putExtra("Cycles", cyclesInput);
                        startActivity(intent);
                    }*/
                     Intent intent = new Intent(second_Activity.this, DisplayStats.class);
                     intent.putExtra("StudyTime", studyTimeInput);
                     intent.putExtra("BreakTime", breakTimeInput);
                     intent.putExtra("Cycles", cyclesInput);
                     startActivity(intent);

                }catch (Exception e){
                    createToast("Please don't leave anything blank");
                }
            }
        });

    }

    //Creates Toasts
    private void createToast(String message){
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_LONG).show();
    }

    /**
     * Moves To Display Stats Activity
     */
    private void configureNextButton(){
        Button nextActivityButton = (Button) findViewById(R.id.button);
        nextActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(second_Activity.this, DisplayStats.class));
            }
        });
    }


    /**
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.id_profile){
            startActivity(new Intent(second_Activity.this, enterPassword.class));

            //write logic
            return true;
        }
        if(id == R.id.id_set){
            //write logic
            return true;
        }
        return true;
    }
}

