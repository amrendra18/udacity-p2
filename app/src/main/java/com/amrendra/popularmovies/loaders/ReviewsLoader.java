package com.amrendra.popularmovies.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.amrendra.popularmovies.BuildConfig;
import com.amrendra.popularmovies.api.MovieClientService;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.ReviewList;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by Amrendra Kumar on 28/11/15.
 */
public class ReviewsLoader extends AsyncTaskLoader<ReviewList> {

    // Note: Be careful not to leak resources here


    private ReviewList mData;
    long movieId;

    public ReviewsLoader(Context context, long movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    public ReviewList loadInBackground() {
        Call<ReviewList> call = MovieClientService.getInstance().getReviewsList(movieId, BuildConfig
                .THE_MOVIE_DB_API_KEY_TOKEN);
        Response<ReviewList> response = null;
        ReviewList data = null;
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
    public void deliverResult(ReviewList data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }

        ReviewList oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }
    }

    private void releaseResources(ReviewList oldData) {
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
    public void onCanceled(ReviewList data) {
        super.onCanceled(data);
        releaseResources(data);
    }
}
