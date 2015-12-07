package com.amrendra.popularmovies.loaders;

import android.content.Context;

import com.amrendra.popularmovies.BuildConfig;
import com.amrendra.popularmovies.utils.Error;
import com.amrendra.popularmovies.api.MovieClientService;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.ReviewList;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by Amrendra Kumar on 28/11/15.
 */
public class ReviewsLoader extends CustomLoader<ReviewList> {

    // Note: Be careful not to leak resources here
    long movieId;

    public ReviewsLoader(Context context, long movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    public ReviewList loadInBackground() {
        Call<ReviewList> call = MovieClientService.getInstance().getReviewsList(movieId, BuildConfig
                .THE_MOVIE_DB_API_KEY_TOKEN);
        ReviewList data = new ReviewList();
        try {
            Response<ReviewList> response = call.execute();
            if (response.isSuccess()) {
                data.results = response.body().results;
            } else {
                Debug.e("REST call for REVIEWS fails : " + response.errorBody().toString(), false);
                data.setError(Error.SERVER_ERROR);
            }
        } catch (IOException e) {
            Debug.e("IOError fetching the REVIEWS list : " + e.getMessage(), true);
            data.setError(Error.CONNECTION_ERROR);
        } catch (Exception e) {
            Debug.e("Error fetching the REVIEWS list : " + e.getMessage(), true);
            data.setError(Error.OTHER);
        }
        return data;
    }
}
