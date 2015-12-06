package com.amrendra.popularmovies.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.amrendra.popularmovies.BuildConfig;
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
public class MoviesLoader extends AsyncTaskLoader<MovieList> {

    // Note: Be careful not to leak resources here


    private MovieList mData;
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
        Response<MovieList> response = null;
        MovieList data = null;
        try {
            response = call.execute();
            Debug.i(response.raw().toString(), false);
            data = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }


    @Override
    public void deliverResult(MovieList data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }

        MovieList oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }
    }

    private void releaseResources(MovieList oldData) {
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }

        if (takeContentChanged() || mData == null) {
            forceLoad();
        }
    }


    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if (mData != null) {
            releaseResources(mData);
            mData = null;
        }
    }

    @Override
    public void onCanceled(MovieList data) {
        super.onCanceled(data);
        releaseResources(data);
    }
}
