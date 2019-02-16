package com.example.scanner;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import com.example.scanner.R;

public class MainActivity extends AppCompatActivity {

    private QRCodeReaderView qrCodeReaderView;
    CoordinatorLayout coordinatorLayout;
    SharedPreferences preferences;
    RelativeLayout layout;
    boolean flashOn=false;
    //backautotaproute
    CardView cardView;
    LottieAnimationView lottieAnimationView;
    //    CardView cardText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        setContentView(R.layout.activity_scanner_port);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        qrCodeReaderView = findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        cardView= findViewById(R.id.overlay);
        layout=findViewById(R.id.lineRed);
        lottieAnimationView = findViewById(R.id.animation_view);
//        result= findViewById(R.id.result1);
//        cardText= findViewById(R.id.overlay2);


        final TranslateAnimation animation1 = new TranslateAnimation(
                Animation.ABSOLUTE, -200f, Animation.ABSOLUTE, 200.0f,
                Animation.ABSOLUTE, 0.0f, Animation.ABSOLUTE, 0.0f);
        animation1.setDuration(2000);
        animation1.setRepeatCount(-1);
        animation1.setRepeatMode(Animation.REVERSE);

        layout.setAnimation(animation1);



        final ImageView flash=findViewById(R.id.imageView3);
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flashOn) {
                    qrCodeReaderView.setTorchEnabled(false);
                    flash.setImageResource(R.drawable.flash);
                }
                else {
                    qrCodeReaderView.setTorchEnabled(true);
                    flash.setImageResource(R.drawable.flashoff);
                }
                flashOn= !flashOn;
            }
        });
        preferences=getApplicationContext().getSharedPreferences("Logged",MODE_PRIVATE);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*cardView.setVisibility(View.INVISIBLE);
                layout.setVisibility(View.VISIBLE);
                qrCodeReaderView.setQRDecodingEnabled(true);*/
            }
        });
        configureCamera();
    }

    void configureCamera(){
        qrCodeReaderView.setQRDecodingEnabled(true);
//        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setBackCamera();
    }


    void displayOverlay(){
        // result.setText(preferences.getString("UID","None"));
//        cardView.setVisibility(View.VISIBLE);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            //Do some stuff
            cardView.setVisibility(View.VISIBLE);

        }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            //Do some stuff
            lottieAnimationView.playAnimation();

        }

//        lottieAnimationView.playAnimation();
//        layout.setVisibility(View.GONE);
//        cardText.setVisibility(View.VISIBLE);

        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
//                cardView.setVisibility(View.GONE);
////                cardText.setVisibility(View.INVISIBLE);
//                layout.setVisibility(View.VISILE);

                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    //Do some stuff
                    cardView.setVisibility(View.INVISIBLE);

                }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                    //Do some stuff
                    lottieAnimationView.cancelAnimation();

                }


                qrCodeReaderView.setQRDecodingEnabled(true);
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Log.e("Read", text);
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,200);

        qrCodeReaderView.setQRDecodingEnabled(false);
//        result.setText(text);
        displayOverlay();
//        fetchData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
        qrCodeReaderView.setQRDecodingEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }
}
