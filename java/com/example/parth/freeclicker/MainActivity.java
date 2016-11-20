package com.example.parth.freeclicker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private String IP;
    private String port;
    private String room;
    private String userName;
    private String uID;

    static final int SCAN_QR_CODE = 1; //request code for scan QR code activity

    static final int WAIT_FOR_USER = 2; //request code for scan QR code activity


    //Give your SharedPreferences file a name and save it to a static variable
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

        if (!hasLoggedIn) {
            //go directly to login activity.

            // Here we start the next activity, and then call finish()
            // so that our own will stop running and be removed from the
            // history stack
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }

        if (hasLoggedIn) {
            //go to scan QR activity

            // Here we start the next activity, and wait for the result
            /*Intent intent = new Intent();
            intent.setClass(MainActivity.this, ScanQRActivity.class);
            startActivityForResult(intent, SCAN_QR_CODE);*/

            Intent intent = new Intent();
            intent.setClass(MainActivity.this, WaitActivity.class);
            startActivityForResult(intent, WAIT_FOR_USER);

        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            userName = user.getDisplayName();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            uID = user.getUid();
        }



        Button optionA  = (Button) findViewById(R.id.button0);
        optionA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        Button optionB  = (Button) findViewById(R.id.button1);
        optionB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        Button optionC  = (Button) findViewById(R.id.button2);
        optionC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        Button optionD  = (Button) findViewById(R.id.button3);
        optionD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        Button optionE  = (Button) findViewById(R.id.button4);
        optionE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SCAN_QR_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                //scanned QR code
                String result = data.getStringExtra("result");
                String[] x = result.split("::");
                IP = x[0];
                port = x[1];
                room = x[2];
            }
            else if (resultCode == RESULT_CANCELED) {
                //nothing scanned
            }
        }
        else if (requestCode == WAIT_FOR_USER) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                //scanned QR code
                String result = data.getStringExtra("result");
                String[] x = result.split("::");
                IP = x[0];
                port = x[1];
                room = x[2];

                TextView quizRoom = (TextView) findViewById(R.id.quizRoom);
                quizRoom.setText("Room : " + room);
            }
            else if (resultCode == RESULT_CANCELED) {
                //nothing scanned
            }
        }
    }
    
}
