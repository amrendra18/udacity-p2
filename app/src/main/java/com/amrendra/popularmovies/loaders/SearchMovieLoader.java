package com.amrendra.popularmovies.loaders;

import android.content.Context;

import com.amrendra.popularmovies.BuildConfig;
import com.amrendra.popularmovies.api.MovieClientService;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.MovieList;
import com.amrendra.popularmovies.utils.Error;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by Amrendra Kumar on 02/01/16.
 */
public class SearchMovieLoader extends CustomLoader<MovieList> {

    int page;
    String searchKey;

    public SearchMovieLoader(Context context, String search, int page) {
        super(context);
        this.page = page;
        this.searchKey = search;
    }

    @Override
    public MovieList loadInBackground() {
        Call<MovieList> call = MovieClientService.getInstance().getSearchedMovieList(BuildConfig
                .THE_MOVIE_DB_API_KEY_TOKEN, searchKey, page);
        MovieList data = null;
        Error error = Error.SUCCESS;
        try {
            Response<MovieList> response = call.execute();
            Debug.e(response.raw().toString(), false);
            if (response.isSuccess()) {
                data = response.body();
            } else {
                Debug.e("REST call for search movie fails : " + response.errorBody().toString(), false);
                error = Error.SERVER_ERROR;
            }
        } catch (IOException e) {
            Debug.e("IOError fetching the search movie list : " + e.getMessage(), true);
            error = Error.CONNECTION_ERROR;
        } catch (Exception e) {
            Debug.e("Error fetching the search movie list : " + e.getMessage(), true);
            error = Error.OTHER;
        }
        if (data == null) {
            data = new MovieList();
            data.setError(error);
        }
        return data;
    }
}
