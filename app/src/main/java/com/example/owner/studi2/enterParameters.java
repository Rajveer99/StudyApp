package com.example.owner.studi2;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class enterParameters extends AppCompatActivity {

    ArrayList<String> listGoals = new ArrayList<String>();
    private int goalCounter;
    //store data from user
    int breakTimeInput;
    int cyclesInput;
    int studyTimeInput;
    TextView breakTime;
    TextView studyTime;
    TextView cycles;
    Button submitButton;
    public static final int RESULT_ENABLE = 11;
    private Button enterGoalButton;
    private DevicePolicyManager devicePolicyManager;
    private TextView enterGoalText;
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
        enterGoalButton = (Button) findViewById(R.id.enterGoalsButton);
        enterGoalText = (TextView) findViewById(R.id.enterGoalsText);

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

                    if (studyTimeInput <= 0 || breakTimeInput <= 0 || cyclesInput <= 0) {

                        Poppop("Please enter values greater than 0");
                    }else if (studyTimeInput < 15) {
                        Poppop("Study Time must be longer than 15 minutes");
                    } else if (breakTimeInput > studyTimeInput) {
                        Poppop("Break Time cannot be longer than Study Time");
                    }  else if (breakTimeInput > 30) {
                        Poppop("Break Time must be shorter than 30 minutes");
                    } else {
                        Intent intent = new Intent(enterParameters.this, DisplayStats.class);
                        intent.putExtra("StudyTime", studyTimeInput);
                        intent.putExtra("BreakTime", breakTimeInput);
                        intent.putExtra("Cycles", cyclesInput);
                        intent.putExtra("goals", listGoals);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    createToast("Please don't leave anything blank");
                }
            }

        });

        enterGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goalCounter < 5) {
                    String goal = enterGoalText.getText().toString();
                    if(listGoals.contains(goal)){
                        createToast("Goal already added. Please add a new one");
                    }
                    else{
                        listGoals.add(goal);
                        enterGoalText.setText("");
                        goalCounter++;
                    }
                }
                else{
                    createToast("Can't add more than 5 Goals");
                }
            }
        });
    }
    private void Poppop(String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(enterParameters.this);
        builder.setCancelable(true);
        builder.setTitle("Error");
        builder.setMessage(text);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        boolean isActive = devicePolicyManager.isAdminActive(compName);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
       /* if(id == R.id.id_profile){
            startActivity(new Intent(enterParameters.this, enterPassword.class));

            //write logic
            return true;
        }*/
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

