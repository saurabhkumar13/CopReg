package com.thefallen.copreg;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    GifImageView view;
    FrameLayout splashScreen;
    View Overshoot;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        //Gif view
        view = (GifImageView) findViewById(R.id.logo);
        splashScreen = (FrameLayout) findViewById(R.id.splashScreen);
        Overshoot = (View) findViewById(R.id.Overshoot);

        //in millis
        int splashDelay = 2400;
        final int splashDuration = 1000;
        final int toolbarHeight = DisplayHelper.getHeight(mContext)/5;

        new Handler().postDelayed(new Runnable() {

            public void run() {

                // Prepare the View for the animation
                view.setVisibility(View.VISIBLE);
                view.setAlpha(1.0f);
                Log.i("height", toolbarHeight+"");
                // Start the animation
                //animate down
                splashScreen.animate()
                        .setDuration(splashDuration)
                        .translationY(-splashScreen.getHeight() + toolbarHeight)
                        .setInterpolator(new AnticipateOvershootInterpolator(1));
            }

        }, splashDelay);
    }
}
