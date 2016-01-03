package com.amrendra.popularmovies.loaders;

import android.content.Context;

import com.amrendra.popularmovies.BuildConfig;
import com.amrendra.popularmovies.api.MovieClientService;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.ReviewList;
import com.amrendra.popularmovies.utils.Error;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by Amrendra Kumar on 28/11/15.
 */
public class ReviewsLoader extends CustomLoader<ReviewList> {
    long movieId;

    public ReviewsLoader(Context context, long movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    public ReviewList loadInBackground() {
        Call<ReviewList> call = MovieClientService.getInstance().getReviewsList(movieId, BuildConfig
                .THE_MOVIE_DB_API_KEY_TOKEN);
        ReviewList data = null;
        Error error = Error.SUCCESS;
        try {
            Response<ReviewList> response = call.execute();
            if (response.isSuccess()) {
                data = response.body();
            } else {
                Debug.e("REST call for REVIEWS fails : " + response.errorBody().toString(), false);
                error = Error.SERVER_ERROR;
            }
        } catch (IOException e) {
            Debug.e("IOError fetching the REVIEWS list : " + e.getMessage(), true);
            error = Error.CONNECTION_ERROR;
        } catch (Exception e) {
            Debug.e("Error fetching the REVIEWS list : " + e.getMessage(), true);
            error = Error.OTHER;
        }
        if (data == null) {
            data = new ReviewList();
            data.setError(error);
        }
        return data;
    }
}
