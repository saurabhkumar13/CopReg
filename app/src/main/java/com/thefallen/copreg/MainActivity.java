package com.thefallen.copreg;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (TextView) findViewById(R.id.logo);

        int secondsDelayed = 3;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                // Prepare the View for the animation
                view.setVisibility(View.VISIBLE);
                view.setAlpha(0.0f);

                // Start the animation
                view.animate()
                        .translationY(view.getHeight())
                        .alpha(1.0f);
            }
        }, secondsDelayed * 1000);
    }
}
