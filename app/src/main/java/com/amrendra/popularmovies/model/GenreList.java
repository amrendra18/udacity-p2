package com.amrendra.popularmovies.model;

import com.amrendra.popularmovies.utils.Error;

import java.util.List;

/**
 * Created by Amrendra Kumar on 10/12/15.
 */
public class GenreList {

    public List<Genre> genres;

    private Error error = Error.SUCCESS;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GenreList{\n");
        if (genres != null) {
            for (Genre genre : genres) {
                sb.append(genre.toString());
            }
        }
        sb.append("}\n");
        return sb.toString();
    }
}
