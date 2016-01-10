package com.thefallen.copreg;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    GifImageView gifImageView;
    FrameLayout splashScreen;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        //Gif gifImageView
        gifImageView = (GifImageView) findViewById(R.id.logo);
        splashScreen = (FrameLayout) findViewById(R.id.splashScreen);

        setLayoutParams();

        splashAnimate();

    }

    public void setLayoutParams() {
        splashScreen.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) (DisplayHelper.getHeight(mContext) * 1.4)));
        splashScreen.setTranslationY(-DisplayHelper.dpToPx(50, mContext));
        FrameLayout.LayoutParams gifLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        gifLayoutParams.topMargin = DisplayHelper.getHeight(mContext) / 2;
        gifImageView.setLayoutParams(gifLayoutParams);

    }

    public void splashAnimate() {
        int splashDelay = 2400;

        new Handler().postDelayed(new Runnable() {
            //in millis
            int splashDuration = 1000;

            public void run() {

                // Prepare the View for the animation
                gifImageView.setVisibility(View.VISIBLE);
                gifImageView.setAlpha(1.0f);
                // Start the animation
                //animate down
                splashScreen.animate()
                        .setDuration(splashDuration)
                        .translationY(-DisplayHelper.getHeight(mContext))
                        .setInterpolator(new AnticipateOvershootInterpolator(1));
            }

        }, splashDelay);
    }

}
