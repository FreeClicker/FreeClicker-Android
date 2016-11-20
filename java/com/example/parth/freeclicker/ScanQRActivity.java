package com.example.parth.freeclicker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

public class ScanQRActivity extends AppCompatActivity {

    String scanned = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        try {

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

            startActivityForResult(intent, 0);

        } catch (Exception e) {

            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                scanned = data.getStringExtra("SCAN_RESULT");
                JSONObject reader = null;
                String room = null;
                String IP = null;
                int port = -1;

                try {
                    reader = new JSONObject(scanned);
                    room = reader.getString("room");
                    IP = reader.getString("ip");
                    port = reader.getInt("port");
                } catch (JSONException e) {

                }


                String result = IP + "::" + port + "::" + room;
                //String result = "192.168.123.69::1234::room name";

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",result);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
            if(resultCode == RESULT_CANCELED){
                //handle cancel
            }
        }
    }
}