package com.amrendra.popularmovies.events;

/**
 * Created by Amrendra Kumar on 18/12/15.
 */
public final class FavouriteMovieDeleteEvent {
    int idx;

    public FavouriteMovieDeleteEvent(int idx) {
        this.idx = idx;
    }

    public int getIdx() {
        return idx;
    }
}
