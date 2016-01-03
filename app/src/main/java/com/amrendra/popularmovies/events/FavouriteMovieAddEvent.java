package com.amrendra.popularmovies.events;

import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.Movie;

/**
 * Created by Amrendra Kumar on 18/12/15.
 */
public final class FavouriteMovieAddEvent {

    Movie mMovie;

    public FavouriteMovieAddEvent(Movie movie) {
        mMovie = movie;
        Debug.c();
    }

    public Movie getMovie() {
        return mMovie;
    }
}
