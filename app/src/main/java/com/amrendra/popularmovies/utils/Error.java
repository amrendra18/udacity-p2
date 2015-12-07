package com.amrendra.popularmovies.utils;

/**
 * Created by Amrendra Kumar on 07/12/15.
 */
public enum Error {
    SUCCESS(0, "SUCCESS"),
    CONNECTION_ERROR(1, "Network Error!!"),
    SERVER_ERROR(2, "Server Error!!"),
    OTHER(3, "Other Error!!");

    private final int code;
    private final String description;

    Error(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "Error[" + code + ": " + description + "]";
    }
}