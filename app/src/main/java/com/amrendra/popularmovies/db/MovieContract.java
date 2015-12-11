package com.amrendra.popularmovies.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Amrendra Kumar on 08/12/15.
 */
/*
Code for content provider taken from Sunshine App by Udacity
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.amrendra.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVOURITE = "favourite";

    public static final class MovieEntry implements BaseColumns {

    }
}
