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
import android.widget.Toast;

//This is the Main Menu Window
public class mainMenu extends AppCompatActivity {
    private ComponentName compName;
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    public static final int RESULT_ENABLE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Menu");
        configureNextButton();

        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        compName = new ComponentName(this, myAdmin.class);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    ////Switches to About us
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.About){
            startActivity(new Intent(mainMenu.this, AboutUs.class)); //Switches to About us
            return true;
        }
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
    protected void onResume() {
        super.onResume();
        boolean isActive = devicePolicyManager.isAdminActive(compName);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case RESULT_ENABLE :
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(mainMenu.this, "You have enabled the Admin Device features", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mainMenu.this, "Problem to enable the Admin Device features", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void configureNextButton() {
        Button nextActivityButton = (Button) findViewById(R.id.second_activity);
        nextActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mainMenu.this, enterParameters.class));
            }
        });
    }
}
