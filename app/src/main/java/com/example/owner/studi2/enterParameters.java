package com.example.owner.studi2;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class enterParameters extends AppCompatActivity {

    //store data from user
    int breakTimeInput;
    int cyclesInput;
    int studyTimeInput;

    TextView breakTime;
    TextView studyTime;
    TextView cycles;

    Button submitButton;

    public static final int RESULT_ENABLE = 11;

    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName compName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_);
        setTitle("Enter Parameters");
        configureNextButton();

        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        compName = new ComponentName(this, myAdmin.class);

        studyTime = (TextView) findViewById(R.id.study);
        cycles = (TextView) findViewById(R.id.repetitions);
        breakTime = (TextView) findViewById(R.id.breakTime);

        Intent intent1 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent1.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
        intent1.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Additional text explaining why we need this permission");
        startActivityForResult(intent1, RESULT_ENABLE);

        submitButton = (Button) findViewById(R.id.button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //user input
                    studyTimeInput = Integer.valueOf(studyTime.getText().toString());
                    breakTimeInput = Integer.valueOf(breakTime.getText().toString());
                    cyclesInput = Integer.valueOf(cycles.getText().toString());


                    if(studyTimeInput == 0 || breakTimeInput == 0 || cyclesInput == 0){
                        createToast("Please enter values greater than 0");
                    }
                    else if (studyTimeInput < 15 && breakTimeInput > 10) {
                        createToast("Study Time must be longer 20 minutes and Break Time " +
                                "must not be longer than 10 minutes");

                    } else if (studyTimeInput < 15) {
                        createToast("Study Time must be >= 20 minutes");
                    } else if (breakTimeInput > 10) {
                        createToast("Break Time must < 10 minutes");

                    } else {

                        Intent intent = new Intent(enterParameters.this, DisplayStats.class);
                        intent.putExtra("StudyTime", studyTimeInput);
                        intent.putExtra("BreakTime", breakTimeInput);
                        intent.putExtra("Cycles", cyclesInput);
                        startActivity(intent);

                    }

                }catch (Exception e){
                    createToast("Please don't leave anything blank");
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isActive = devicePolicyManager.isAdminActive(compName);
        //disable.setVisibility(isActive ? View.VISIBLE : View.GONE);
        //submitButton.setVisibility(isActive ? View.GONE : View.VISIBLE);
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
                startActivity(new Intent(enterParameters.this, DisplayStats.class));
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
            startActivity(new Intent(enterParameters.this, enterPassword.class));

            //write logic
            return true;
        }
        if(id == R.id.id_set){
            devicePolicyManager.removeActiveAdmin(compName);
            //disable.setVisibility(View.GONE);
            //enable.setVisibility(View.VISIBLE);
            return true;
        }

        if (id == R.id.id_setEnable){
            Intent intent1 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent1.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
            intent1.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Additional text explaining why we need this permission");
            startActivityForResult(intent1, RESULT_ENABLE);
        }

        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case RESULT_ENABLE :
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(enterParameters.this, "You have enabled the Admin Device features", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(enterParameters.this, "Problem to enable the Admin Device features", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

