package com.truiton.rtmpplayer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class Remote_Front_Cam extends AppCompatActivity  {
    private String mFilePath;
    private SurfaceView mSurface;
    private SurfaceHolder holder;
    private LibVLC libvlc;
    private MediaPlayer mMediaPlayer = null;
    private int mVideoWidth;
    private int mVideoHeight;
    WifiManager wifiManager;


    private TextView mName;
    private TextView frontCam;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front_cam);
        ConnectivityManager mgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mgr.getActiveNetworkInfo();

        if (netInfo != null) {
            if (netInfo.isConnected()) {

            }else {
                //No internet
            }
        } else {
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this);
            // Setting Dialog Title
            alertDialog2.setTitle("NETWORK not available");
            // Setting Dialog Message
            alertDialog2.setMessage("Please ensure that you have internet connection on your device");
            // Setting Icon to Dialog
            alertDialog2.setIcon(R.drawable.nonetwork);
            // Setting Positive "Yes" Btn
            alertDialog2.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            alertDialog2.show();
        }

        mName = (TextView) findViewById(R.id.etName_SL);
        frontCam=(TextView) findViewById(R.id.frontCam);
        frontCam.setBackgroundColor(Color.BLACK);
        mName.setBackgroundColor(Color.BLACK);
        mName.setBackgroundColor(Color.parseColor("#50000000"));
         frontCam.setBackgroundColor(Color.parseColor("#50000000"));
         //retieve the text input from then IpPreferance class to the front_cam class
         SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
         mFilePath = "rtsp://"+prefs.getString(getString(R.string.name), "")+":5554/Front-Camera.sdp";
         //pass the font IP address to the left of the screen
         mName = (TextView) findViewById(R.id.etName_SL);
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = "IP: "+ mPreferences.getString(getString(R.string.name), "");
        mName.setText(name);


        //rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov
        mSurface = (SurfaceView) findViewById(R.id.surfaceView);
        holder = mSurface.getHolder();

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tdate = (TextView) findViewById(R.id.clock);
                                tdate.setBackgroundColor(Color.BLACK);
                                tdate.setBackgroundColor(Color.parseColor("#50000000"));
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String dateString = sdf.format(date);
                                tdate.setText(dateString);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
         }



    // Catch touch events here
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            final ImageButton button0=(ImageButton)findViewById(R.id.settings);
            final ImageButton button1= (ImageButton) findViewById(R.id.refresh);
            final ImageButton button3=(ImageButton)findViewById(R.id.cameraswitch)
                    ;
            final TextView textView=(TextView) findViewById(R.id.frontCam);
            final TextView textView1=(TextView) findViewById(R.id.etName_SL);
            final TextView textView2=(TextView) findViewById(R.id.clock);

            button0.setVisibility(View.INVISIBLE);
            button1.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
            textView1.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);
            return true;
        }
        else if(event.getAction() == MotionEvent.ACTION_UP) {
            final ImageButton button0= (ImageButton) findViewById(R.id.settings);
            final ImageButton button1= (ImageButton) findViewById(R.id.refresh);
            final ImageButton button3=(ImageButton)findViewById(R.id.cameraswitch);
            final TextView textView=(TextView) findViewById(R.id.frontCam);
            final TextView textView1=(TextView) findViewById(R.id.etName_SL);
            final TextView textView2=(TextView) findViewById(R.id.clock);

            button0.setVisibility(View.VISIBLE);
            button1.setVisibility(View.VISIBLE);
            button3.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            return true;
        }
        return super.onTouchEvent(event);
    }



    @Override
    protected void onResume() {
        super.onResume();
        createPlayer(mFilePath);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
//to disable the baclbutton on device
@Override
public void onBackPressed() {
    Intent intent = new Intent(this,MainActivity.class);
    startActivity(intent);

    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    System.exit(0);
}

    private void createPlayer(String media) {
        releasePlayer();
        try {
            if (media.length() > 0) {
                Toast toast = Toast.makeText(this, media, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0,
                        0);

                toast.show();
            }

            // Create LibVLC
            // TODO: make this more robust, and sync with audio demo
            ArrayList<String> options = new ArrayList<String>();
            options.add("--no-drop-late-frames");
            options.add("--no-skip-frames");
            options.add("--rtsp-tcp");
            options.add("--audio-time-stretch");
            options.add("-vvv");
            libvlc = new LibVLC(this, options);
            holder.setKeepScreenOn(true);

            // Creating media player
            mMediaPlayer = new MediaPlayer(libvlc);
            mMediaPlayer.setEventListener(mPlayerListener);

            // Seting up video output
            final IVLCVout vout = mMediaPlayer.getVLCVout();
            vout.setVideoView(mSurface);
            vout.attachViews();

            Media m = new Media(libvlc, Uri.parse(media));
            m.setHWDecoderEnabled(true, false);
            m.addOption(":network-caching=300");
            m.addOption(":clock-jitter=0");
            mMediaPlayer.setMedia(m);
            mMediaPlayer.play();
        } catch (Exception e) {
            Toast.makeText(this, "Error in creating player!", Toast
                    .LENGTH_LONG).show();
        }
    }

    private void releasePlayer() {
        if (libvlc == null)
            return;
        mMediaPlayer.stop();
        final IVLCVout vout = mMediaPlayer.getVLCVout();
        vout.detachViews();
        holder = null;
        libvlc.release();
        libvlc = null;

        mVideoWidth = 0;
        mVideoHeight = 0;
    }

    /**
     * Registering callbacks
     */
    private MediaPlayer.EventListener mPlayerListener = new MyPlayerListener(this);


    public void buttonClick(View view) {
        Intent intent =new Intent(Remote_Front_Cam.this,Remote_Back_Cam.class);
        startActivity(intent);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        System.exit(0);
    }

    public void refresh(View view) {


        overridePendingTransition( 0, 0);
        startActivity(getIntent());
        overridePendingTransition( 0, 0);
        System.exit(1);
    }
    // this starts the INTERNET / REMOTE  side of the app
    public void settings_onClick(View view) {
        // Intent intent = new Intent(this,IpPreference.class);
        //startActivity(intent);
        openDialog();
    }

    private void openDialog() {
        Ip_Dialog  ip_dialog= new Ip_Dialog();
        ip_dialog.show(getSupportFragmentManager(),"IP address ");

    }

    public void ref(View view) {

        overridePendingTransition( 0, 0);
        startActivity(getIntent());
        overridePendingTransition( 0, 0);
        System.exit(1);
    }


    private static class MyPlayerListener implements MediaPlayer.EventListener {
        private WeakReference<Remote_Front_Cam> mOwner;

        public MyPlayerListener(Remote_Front_Cam owner) {
            mOwner = new WeakReference<Remote_Front_Cam>(owner);
        }

        @Override
        public void onEvent(MediaPlayer.Event event) {
            Remote_Front_Cam player = mOwner.get();

            switch (event.type) {
                case MediaPlayer.Event.EndReached:
                    player.releasePlayer();
                    break;
                case MediaPlayer.Event.Playing:
                case MediaPlayer.Event.Paused:
                case MediaPlayer.Event.Stopped:
                default:
                    break;
            }
        }
    }
}
