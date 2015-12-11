package com.amrendra.popularmovies.model;

/**
 * Created by Amrendra Kumar on 24/11/15.
 */
public class MovieList extends CommonList<Movie> {
    public String toString() {
        StringBuilder sb = new StringBuilder("[Page: " + page + "]");
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
