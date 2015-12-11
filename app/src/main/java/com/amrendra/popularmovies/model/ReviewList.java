package com.amrendra.popularmovies.model;

/**
 * Created by Amrendra Kumar on 24/11/15.
 */
public class ReviewList extends CommonList<Review> {

    public int id;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[Page: " + page + "]");
        if (results != null) {
            for (Review review : results) {
                sb.append("[Review:" + review.author + "]\n");
            }
        } else {
            sb.append("[ReviewList : NULL]");
        }
        return sb.toString();
    }
}
