package com.amrendra.popularmovies.app;


import android.app.Application;
import android.content.res.Configuration;

import com.facebook.stetho.Stetho;

/**
 * Created by Amrendra Kumar on 23/11/15.
 */

public class PopularMoviesApplication extends Application {
    private static PopularMoviesApplication mApplication = null;

    // should use static getInstance() method instead
    public PopularMoviesApplication() {
        mApplication = this;
    }

    public static PopularMoviesApplication getInstance() {
        if (mApplication == null) {
            mApplication = new PopularMoviesApplication();
        }
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
