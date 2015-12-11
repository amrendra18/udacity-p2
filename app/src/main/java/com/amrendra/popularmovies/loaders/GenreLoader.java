package com.amrendra.popularmovies.loaders;

import android.content.Context;

import com.amrendra.popularmovies.BuildConfig;
import com.amrendra.popularmovies.api.MovieClientService;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.GenreList;
import com.amrendra.popularmovies.utils.Error;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by Amrendra Kumar on 10/12/15.
 */
public class GenreLoader extends CustomLoader<GenreList> {

    public GenreLoader(Context context) {
        super(context);
    }

    @Override
    public GenreList loadInBackground() {
        Call<GenreList> call = MovieClientService.getInstance().getGenresList(BuildConfig
                .THE_MOVIE_DB_API_KEY_TOKEN);
        GenreList data = null;
        Error error = Error.SUCCESS;
        try {
            Response<GenreList> response = call.execute();
            if (response.isSuccess()) {
                data = response.body();
            } else {
                Debug.e("REST call for GENRES fails : " + response.errorBody().toString(), false);
                error = Error.SERVER_ERROR;
            }
        } catch (IOException e) {
            Debug.e("IOError fetching the GENRES list : " + e.getMessage(), true);
            error = Error.CONNECTION_ERROR;
        } catch (Exception e) {
            Debug.e("Error fetching the GENRES list : " + e.getMessage(), true);
            error = Error.OTHER;
        }
        if (data == null) {
            data = new GenreList();
            data.setError(error);
        }
        return data;
    }
}
