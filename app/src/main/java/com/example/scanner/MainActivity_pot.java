package com.example.scanner;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity_pot extends AppCompatActivity {

    private QRCodeReaderView qrCodeReaderView;

    CoordinatorLayout coordinatorLayout;
    SharedPreferences preferences;
    RelativeLayout layout;

    boolean flashOn=false;

    CardView cardView;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_pot);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        qrCodeReaderView = findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        cardView= findViewById(R.id.overlay);
        layout=findViewById(R.id.lineRed);
        lottieAnimationView = findViewById(R.id.animation_view);

        final TranslateAnimation animation1 = new TranslateAnimation(
                Animation.ABSOLUTE, 0.0f, Animation.ABSOLUTE, 0.0f,
                Animation.ABSOLUTE, -400f, Animation.ABSOLUTE, 400.0f);
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
    }



    void displayOverlay(){

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

            cardView.setVisibility(View.VISIBLE);

        }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

            lottieAnimationView.playAnimation();

        }


        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

                    cardView.setVisibility(View.INVISIBLE);

                }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

                    lottieAnimationView.cancelAnimation();

                }


                qrCodeReaderView.setQRDecodingEnabled(true);
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onQRCodeRead(final String text, PointF[] points) {
        Log.e("Read", text);
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,200);

        qrCodeReaderView.setQRDecodingEnabled(false);

        displayOverlay();
        String url = null;
        RequestQueue queue = Volley.newRequestQueue(this);
        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null) {
            url = (String) b.get("url");
        }
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", text);

                return params;
            }
        };
        queue.add(postRequest);

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
