package com.amrendra.popularmovies.events;

import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.Movie;

/**
 * Created by Amrendra Kumar on 18/12/15.
 */
public final class FavouriteMovieAddEvent {

    Movie mMovie;
    int idx;

    public FavouriteMovieAddEvent(Movie movie, int idx) {
        mMovie = movie;
        idx = idx;
        Debug.c();
    }

    public Movie getMovie() {
        return mMovie;
    }

    public int getIdx() {
        return idx;
    }
}
