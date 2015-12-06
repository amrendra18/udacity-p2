package com.amrendra.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.amrendra.popularmovies.logger.Debug;


/**
 * Created by Amrendra Kumar on 23/11/15.
 */
public class PreferenceManager {
    private static final String SHARED_PREF_FILE_NAME = "popular_movies";

    SharedPreferences mSharedPreference;
    SharedPreferences.Editor mEditor;

    static PreferenceManager mSharedPreferenceMgr = null;

    private PreferenceManager(Context context) {
        mSharedPreference = context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreference.edit();
    }

    public static PreferenceManager getInstance(Context context) {
        if (mSharedPreferenceMgr == null) {
            mSharedPreferenceMgr = new PreferenceManager(context);
        }
        return mSharedPreferenceMgr;
    }

    public int readValue(String key, int defaultValue) {
        return mSharedPreference.getInt(key, defaultValue);
    }

    public void writeValue(String key, int value) {
        mEditor.putInt(key, value).commit();
    }

    public String readValue(String key, String defaultValue) {
        return mSharedPreference.getString(key, defaultValue);
    }

    public void writeValue(String key, String value) {
        mEditor.putString(key, value).commit();
    }

    public void clear(){
        mEditor.clear().commit();
    }


    public void debug() {
        Debug.preferences(mSharedPreference);
    }
}
