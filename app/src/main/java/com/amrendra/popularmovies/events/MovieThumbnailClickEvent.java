package com.amrendra.popularmovies.events;

import android.graphics.Bitmap;

import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.Movie;

/**
 * Created by Amrendra Kumar on 18/12/15.
 */
public final class MovieThumbnailClickEvent {
    Movie mMovie;
    Bitmap mBitmap;

    public MovieThumbnailClickEvent(Movie movie, Bitmap bitmap) {
        Debug.c();
        mMovie = movie;
        mBitmap = bitmap;
    }

    public Movie getMovie() {
        return mMovie;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}
