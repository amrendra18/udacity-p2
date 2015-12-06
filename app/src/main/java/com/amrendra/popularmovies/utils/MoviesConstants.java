package com.amrendra.popularmovies.utils;

/**
 * Created by Amrendra Kumar on 24/11/15.
 */
public class MoviesConstants {

    // base urls
    public static final String API_BASE_URL = "https://api.themoviedb.org";
    public static final String API_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";


    public final static String IMAGE_SIZE_SMALL = "w185/";
    public final static String IMAGE_SIZE_LARGE = "w500/";

    // end points urls
    public static final String GET_MOVIES_URL = "/3/discover/movie";
    public static final String GET_REVIEWS_URL = "/3/movie/{id}/reviews";
    public static final String GET_TRAILERS_URL = "/3/movie/{id}/videos";

    // api parameters
    public static final String API_KEY = "api_key";
    public static final String SORT_BY = "sort_by";
    public static final String PAGE = "page";
    public static final String ID = "id";

    // sorting
    public static final String SORT_BY_POPULARITY = "popularity.desc";
    public static final String SORT_BY_RATINGS = "vote_average.desc";
    public static final String SORT_BY_FAVOURITES = "show_favourites";

    // by default fetching movies whose vote count > 200, so that we have good results
    public static final String VOTE_COUNT_GTE = "vote_count.gte";
    public static final int VOTE_COUNT_DEFAULT = 200;

    // trailers
    public static final String TRAILER_VIDEO_URL = "http://www.youtube.com/watch?v=";
    public static final String TRAILER_IMAGE_URL = "http://img.youtube.com/vi/%s/0.jpg";
}
