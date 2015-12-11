package com.amrendra.popularmovies.model;

/**
 * Created by Amrendra Kumar on 24/11/15.
 */
public class TrailerList extends CommonList<Trailer> {

    public int id;

    public String toString() {
        StringBuilder sb = new StringBuilder("[Page: " + page + "]");
        if (results != null) {
            for (Trailer trailer : results) {
                sb.append("[Review:" + trailer.name + "]\n");
            }
        } else {
            sb.append("[TrailerList : NULL]");
        }
        return sb.toString();
    }
}
