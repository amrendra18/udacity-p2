package com.amrendra.popularmovies.model;

import java.util.List;

/**
 * Created by Amrendra Kumar on 24/11/15.
 */
public class TrailerList {

    public List<Trailer> results;
    public int page;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[Page: " + page + "]");
        for (Trailer trailer : results) {
            sb.append("[trailer:" + trailer.site + "]\n");
        }
        return sb.toString();
    }
}
