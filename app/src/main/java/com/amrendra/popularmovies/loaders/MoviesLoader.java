package com.amrendra.popularmovies.loaders;

import android.content.Context;

import com.amrendra.popularmovies.BuildConfig;
import com.amrendra.popularmovies.utils.Error;
import com.amrendra.popularmovies.api.MovieClientService;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.MovieList;
import com.amrendra.popularmovies.utils.MoviesConstants;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by Amrendra Kumar on 28/11/15.
 */
public class MoviesLoader extends CustomLoader<MovieList> {

    // Note: Be careful not to leak resources here
    String sortBy;
    int page;
    int vote_count = MoviesConstants.VOTE_COUNT_DEFAULT;

    public MoviesLoader(Context context, String sortBy, int page) {
        super(context);
        this.sortBy = sortBy;
        this.page = page;
    }

    @Override
    public MovieList loadInBackground() {
        Call<MovieList> call = MovieClientService.getInstance().getMovieList(BuildConfig
                .THE_MOVIE_DB_API_KEY_TOKEN, sortBy, page, vote_count);
        MovieList data = null;
        Error error = Error.SUCCESS;
        try {
            Response<MovieList> response = call.execute();
            if (response.isSuccess()) {
                data = response.body();
            } else {
                Debug.e("REST call for MOVIES fails : " + response.errorBody().toString(), false);
                error = Error.SERVER_ERROR;
            }
        } catch (IOException e) {
            Debug.e("IOError fetching the MOVIES list : " + e.getMessage(), true);
            error = Error.CONNECTION_ERROR;
        } catch (Exception e) {
            Debug.e("Error fetching the MOVIES list : " + e.getMessage(), true);
            error = Error.OTHER;
        }
        if (data == null) {
            data = new MovieList();
            data.setError(error);
        }
        return data;
    }
}
