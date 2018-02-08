package com.truiton.rtmpplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    //this Starts the LOCAL side of the app
    public void local_onlcik(View view) {
        Intent intent = new Intent(this,Local_Front_Cam.class);
        startActivity(intent);

    }

// this starts the INTERNET / REMOTE  side of the app
    public void remote_onlcik(View view) {
       // Intent intent = new Intent(this,IpPreference.class);
        //startActivity(intent);

        Ip_Dialog  ip_dialog= new Ip_Dialog();
        ip_dialog.show(getSupportFragmentManager(),"IP address ");
    }



}
