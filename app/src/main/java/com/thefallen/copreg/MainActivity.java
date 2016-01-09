package com.thefallen.copreg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    GifImageView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Gif view
        view = (GifImageView) findViewById(R.id.logo);

        //Get dimensions
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int height = size.y;

        //in millis
        int splashDelay = 2400;

        new Handler().postDelayed(new Runnable() {

            public void run() {

                // Prepare the View for the animation
                view.setVisibility(View.VISIBLE);
                view.setAlpha(1.0f);

                // Start the animation
                //animate down
                view.animate()
                        .translationY(view.getHeight())
                        .alpha(1.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                //animate up
                                view.animate()
                                        .translationY(-height)
                                        .alpha(0.0f)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                view.setVisibility(View.GONE);
                                            }
                                        });
                            }
                        });

            }
        }, splashDelay);
    }
}
