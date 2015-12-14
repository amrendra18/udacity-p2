package com.amrendra.popularmovies.loaders;

import android.content.Context;

import com.amrendra.popularmovies.BuildConfig;
import com.amrendra.popularmovies.api.MovieClientService;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.Movie;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by Amrendra Kumar on 13/12/15.
 */
public class MovieDetailLoader extends CustomLoader<Movie> {
    long movieId;

    public MovieDetailLoader(Context context, long movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    public Movie loadInBackground() {
        Call<Movie> call = MovieClientService.getInstance().getMovieDetails(movieId, BuildConfig
                .THE_MOVIE_DB_API_KEY_TOKEN);
        Movie data = null;
        try {
            Response<Movie> response = call.execute();
            if (response.isSuccess()) {
                data = response.body();
            } else {
                Debug.e("REST call for DETAIL MOVIES fails : " + response.errorBody().toString(),
                        false);
            }
        } catch (IOException e) {
            Debug.e("IOError fetching the DETAIL MOVIES list : " + e.getMessage(), true);
        } catch (Exception e) {
            Debug.e("Error fetching the DETAIL MOVIES list : " + e.getMessage(), true);
        }
        return data;
    }
}
