package com.amrendra.popularmovies.model;

import com.amrendra.popularmovies.utils.Error;

import java.util.List;

/**
 * Created by Amrendra Kumar on 24/11/15.
 */
public class MovieList {

    //TODO : Add getter & Setters for member variables

    public List<Movie> results;
    public int page;

    private Error error = Error.SUCCESS;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[Page: " + page + "][ErrorCode: " + error + "]");
        if (results != null) {
            for (Movie movie : results) {
                sb.append("[Movie:" + movie.title + "]\n");
            }
        } else {
            sb.append("[MovieList : NULL]");
        }
        return sb.toString();
    }


}
