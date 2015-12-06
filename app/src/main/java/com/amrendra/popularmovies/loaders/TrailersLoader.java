package com.amrendra.popularmovies.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.amrendra.popularmovies.BuildConfig;
import com.amrendra.popularmovies.api.MovieClientService;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.TrailerList;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by Amrendra Kumar on 28/11/15.
 */
public class TrailersLoader extends AsyncTaskLoader<TrailerList> {

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
        Response<TrailerList> response = null;
        TrailerList data = null;
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
    public void deliverResult(TrailerList data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }

        TrailerList oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }
    }

    private void releaseResources(TrailerList oldData) {
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
    public void onCanceled(TrailerList data) {
        super.onCanceled(data);
        releaseResources(data);
    }
}
