package com.amrendra.popularmovies.model;

import com.amrendra.popularmovies.utils.Error;

import java.util.List;

/**
 * Created by Amrendra Kumar on 24/11/15.
 */
public class ReviewList {

    //TODO : Add getter & Setters for member variables

    public List<Review> results;
    public int page;

    private Error error = Error.SUCCESS;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[Page: " + page + "][ErrorCode: " + error + "]");
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
