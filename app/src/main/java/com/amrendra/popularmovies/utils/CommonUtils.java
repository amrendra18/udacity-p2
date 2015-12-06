package com.amrendra.popularmovies.utils;

import java.util.Random;

/**
 * Created by Amrendra Kumar on 23/11/15.
 */
@SuppressWarnings("unused")
public class CommonUtils {
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
