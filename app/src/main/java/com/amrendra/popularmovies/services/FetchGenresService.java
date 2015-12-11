package com.amrendra.popularmovies.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

import com.amrendra.popularmovies.BuildConfig;
import com.amrendra.popularmovies.api.MovieClientService;
import com.amrendra.popularmovies.db.MovieContract;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.Genre;
import com.amrendra.popularmovies.model.GenreList;
import com.amrendra.popularmovies.utils.AppConstants;
import com.amrendra.popularmovies.utils.PreferenceManager;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by Amrendra Kumar on 10/12/15.
 */
public class FetchGenresService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FetchGenresService() {
        super("FetchGenresService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Debug.c();
        Call<GenreList> call = MovieClientService.getInstance().getGenresList(BuildConfig
                .THE_MOVIE_DB_API_KEY_TOKEN);
        GenreList data = null;
        try {
            Response<GenreList> response = call.execute();
            if (response.isSuccess()) {
                data = response.body();
                Debug.e(response.raw().toString(), false);
            } else {
                Debug.e("REST call for GENRES fails : " + response.errorBody().toString(), false);
            }
        } catch (IOException e) {
            Debug.e("IOError fetching the GENRES list : " + e.getMessage(), true);
        } catch (Exception e) {
            Debug.e("Error fetching the GENRES list : " + e.getMessage(), true);
        }
        Debug.e("" + data, false);
        if (data != null && data.genres != null) {
            int count = data.genres.size();
            ContentValues[] genreValues = new ContentValues[count];
            for (int i = 0; i < count; i++) {
                ContentValues cv = new ContentValues();
                Genre genre = data.genres.get(i);
                cv.put(MovieContract.GenreEntry.COLUMN_GENRE_ID, genre.id);
                cv.put(MovieContract.GenreEntry.COLUMN_GENRE_NAME, genre.name);
                genreValues[i] = cv;
            }
            int added = getApplicationContext().getContentResolver().bulkInsert(
                    MovieContract.GenreEntry.CONTENT_URI,
                    genreValues
            );
            Debug.e("Added genres : " + added, false);
            if (added > 0) {
                PreferenceManager.getInstance(getApplicationContext()).writeValue(AppConstants
                        .FETCHED_GENRES_FROM_SERVER, true);
            }
        }
    }
}
