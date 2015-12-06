package com.amrendra.popularmovies.api;

import com.amrendra.popularmovies.model.MovieList;
import com.amrendra.popularmovies.model.ReviewList;
import com.amrendra.popularmovies.model.TrailerList;
import com.amrendra.popularmovies.utils.MoviesConstants;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Amrendra Kumar on 24/11/15.
 */
public interface MoviesEndPointInterface {

    @GET(MoviesConstants.GET_MOVIES_URL)
    Call<MovieList> getMovieList(@Query(MoviesConstants.API_KEY) String apiKey,
                                 @Query(MoviesConstants.SORT_BY) String sortBy, @Query(MoviesConstants.PAGE) int
                                         page, @Query(MoviesConstants.VOTE_COUNT_GTE) int vote_count);

    @GET(MoviesConstants.GET_REVIEWS_URL)
    Call<ReviewList> getReviewsList(@Path(MoviesConstants.ID) long movie, @Query(MoviesConstants.API_KEY) String
            apiKey);

    @GET(MoviesConstants.GET_TRAILERS_URL)
    Call<TrailerList> getTrailersList(@Path(MoviesConstants.ID) long movie, @Query(MoviesConstants
            .API_KEY)
    String apiKey);


}
