package com.amrendra.popularmovies.model;

import com.amrendra.popularmovies.utils.Error;

import java.util.List;

/**
 * Created by Amrendra Kumar on 10/12/15.
 */
public class CommonList<T> {
    public List<T> results;
    public int page;

    private Error error = Error.SUCCESS;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
