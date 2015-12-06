package com.amrendra.popularmovies.model;

import java.util.List;

/**
 * Created by Amrendra Kumar on 24/11/15.
 */
public class ReviewList {

    public List<Review> results;
    public int page;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[Page: " + page + "]");
        for (Review review : results) {
            sb.append("[Review:" + review.author + "]\n");
        }
        return sb.toString();
    }
}
