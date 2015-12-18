package com.amrendra.popularmovies.events;

import android.graphics.Bitmap;
import android.view.View;

import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.Movie;

/**
 * Created by Amrendra Kumar on 18/12/15.
 */
public final class MovieThumbnailClickEvent {
    Movie mMovie;
    Bitmap mBitmap;
    View mView;

    public MovieThumbnailClickEvent(Movie movie, Bitmap bitmap, View view) {
        Debug.c();
        mMovie = movie;
        mBitmap = bitmap;
        mView = view;
    }

    public Movie getMovie() {
        return mMovie;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public View getView() {
        return mView;
    }
}
