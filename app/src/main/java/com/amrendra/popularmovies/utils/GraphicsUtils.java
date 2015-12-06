package com.amrendra.popularmovies.utils;

import android.graphics.Color;

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
}
