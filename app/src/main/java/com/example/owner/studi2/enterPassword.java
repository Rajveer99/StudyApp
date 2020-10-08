package com.example.owner.studi2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


/**
 * New Activity where the user will enter their password
 */
public class enterPassword extends AppCompatActivity {

    /**
     * This is the Enter Page which we will probably need to @DELETE
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);
        setTitle("Enter Password");
    }
}
