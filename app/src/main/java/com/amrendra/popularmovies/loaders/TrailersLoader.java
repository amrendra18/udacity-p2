package com.amrendra.popularmovies.loaders;

import android.content.Context;

import com.amrendra.popularmovies.BuildConfig;
import com.amrendra.popularmovies.utils.Error;
import com.amrendra.popularmovies.api.MovieClientService;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.TrailerList;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by Amrendra Kumar on 28/11/15.
 */
public class TrailersLoader extends CustomLoader<TrailerList> {

    // Note: Be careful not to leak resources here
    private TrailerList mData;
    long movieId;

    public TrailersLoader(Context context, long movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    public TrailerList loadInBackground() {
        Call<TrailerList> call = MovieClientService.getInstance().getTrailersList(movieId,
                BuildConfig
                        .THE_MOVIE_DB_API_KEY_TOKEN);
        TrailerList data = new TrailerList();
        try {
            Response<TrailerList> response = call.execute();
            if (response.isSuccess()) {
                data.results = response.body().results;
            } else {
                Debug.e("REST call for TRAILERS fails : " + response.errorBody().toString(), false);
                data.setError(Error.SERVER_ERROR);
            }
        } catch (IOException e) {
            Debug.e("IOError fetching the TRAILERS list : " + e.getMessage(), true);
            data.setError(Error.CONNECTION_ERROR);
        } catch (Exception e) {
            Debug.e("Error fetching the TRAILERS list : " + e.getMessage(), true);
            data.setError(Error.OTHER);
        }
        return data;
    }
}
