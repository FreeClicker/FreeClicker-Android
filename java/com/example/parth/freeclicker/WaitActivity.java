package com.example.parth.freeclicker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WaitActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    static final int SCAN_QR_CODE = 1; //request code for scan QR code activity

    private String userName;

    private static final String TAG = "EmailPassword";

    public static final String PREFS_NAME = "MyPrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            userName = user.getDisplayName();
        }

        TextView welcomeLabel = (TextView) findViewById(R.id.welcomLabel);
        welcomeLabel.setText("Hi " + userName + "!");

        Button scanButton = (Button) findViewById(R.id.button);
        scanButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //go to scan QR activity

                // Here we start the next activity, and wait for the result
                Intent intent = new Intent();
                intent.setClass(WaitActivity.this, ScanQRActivity.class);
                startActivityForResult(intent, SCAN_QR_CODE);
            }
        });

        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAuth.signOut();

                //User has successfully logged out, save this information
                // We need an Editor object to make preference changes.
                SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0); // 0 - for private mode
                SharedPreferences.Editor editor = settings.edit();

                //Set "hasLoggedIn" to false
                editor.putBoolean("hasLoggedIn", false);

                // Commit the edits!
                editor.commit();

                Intent intent = new Intent();
                intent.setClass(WaitActivity.this, MainActivity.class);
                startActivity(intent);
                WaitActivity.this.finish();
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

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",data.getStringExtra("result"));
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
            else if (resultCode == RESULT_CANCELED) {
                //nothing scanned
            }
        }
    }
}
