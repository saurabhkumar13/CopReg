package com.thefallen.copreg;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    GifImageView gifImageView;
    FrameLayout splashScreen;
    Context mContext;
    ViewGroup member1,member2,member3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        //Gif gifImageView
        gifImageView = (GifImageView) findViewById(R.id.logo);
        splashScreen = (FrameLayout) findViewById(R.id.splashScreen);
        member1 = (ViewGroup) findViewById(R.id.card1);
        member2 = (ViewGroup) findViewById(R.id.card2);
        member3 = (ViewGroup) findViewById(R.id.card3);

        setLayoutParams();
        splashAnimate();
        getCard(member1);

    }

    //Layout Parameters for splash GIF and screen
    public void setLayoutParams() {
        splashScreen.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) (DisplayHelper.getHeight(mContext) * 1.4)));
        splashScreen.setTranslationY(-DisplayHelper.dpToPx(50, mContext));
        FrameLayout.LayoutParams gifLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        gifLayoutParams.topMargin = DisplayHelper.getHeight(mContext) / 2;
        gifImageView.setLayoutParams(gifLayoutParams);
        member1.setTranslationY(DisplayHelper.getHeight(mContext));
        member2.setTranslationY(DisplayHelper.getHeight(mContext));
        member3.setTranslationY(DisplayHelper.getHeight(mContext));
    }

    //GIF animate
    public void splashAnimate() {
        int splashDelay = 2400;
        int splashDuration = 1000;
        // Start the animation
        //animate down
        splashScreen.animate()
                .setDuration(splashDuration)
                .setStartDelay(splashDelay)
                .translationY(-DisplayHelper.getHeight(mContext))
                .setInterpolator(new AnticipateOvershootInterpolator(1));
    }
    public void getCard(ViewGroup member)
    {
        member.animate()
                .translationY((int)(-DisplayHelper.getHeight(mContext)*0.5))
                .setInterpolator(new OvershootInterpolator())
                .setDuration(1000)
                .setStartDelay(3500)
                ;
    }
}
