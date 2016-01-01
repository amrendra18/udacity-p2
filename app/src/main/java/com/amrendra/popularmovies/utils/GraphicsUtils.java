package com.amrendra.popularmovies.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import java.util.Random;

/**
 * Created by Amrendra Kumar on 23/11/15.
 */
@SuppressWarnings("unused")
public class GraphicsUtils {
    public static int getRandomColor() {
        Random random = new Random();
        int red = random.nextInt(255);
        int green = random.nextInt(255);
        int blue = random.nextInt(255);

        return Color.argb(255, red, green, blue);
    }

    public static void statusBarTinted(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = activity.getWindow();
            w.setFlags(LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            w.setFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS, LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void statusBarRemoveTint(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = activity.getWindow();
            LayoutParams attrs = w.getAttributes();
            attrs.flags &= (~LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            attrs.flags &= (~LayoutParams.FLAG_TRANSLUCENT_STATUS);
            w.setAttributes(attrs);
            w.clearFlags(LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
}
