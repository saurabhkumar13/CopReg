package com.thefallen.copreg;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by mayank on 10/01/16.
 */
public class DisplayHelper {

    //http://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android
    //converts dp into pixels

    public static int dpToPx(int dp, Context mContext) {

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    //Returns height of the current display
    public static int getHeight(Context mContext){

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }
}
