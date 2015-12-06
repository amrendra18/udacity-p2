package com.amrendra.popularmovies.model;

import java.util.List;

/**
 * Created by Amrendra Kumar on 24/11/15.
 */
public class MovieList {

    public List<Movie> results;
    public int page;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[Page: "+page+"]");
        for (Movie movie : results) {
            sb.append("[Movie:" + movie.title + "]\n");
        }
        return sb.toString();
    }
}
