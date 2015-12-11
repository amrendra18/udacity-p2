package com.amrendra.popularmovies.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.amrendra.popularmovies.BuildConfig;
import com.amrendra.popularmovies.api.MovieClientService;
import com.amrendra.popularmovies.db.DBHelper;
import com.amrendra.popularmovies.db.MovieContract.GenreEntry;
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
            DBHelper dbHelper = new DBHelper(getApplicationContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            int added = 0;
            try {
                for (Genre genre : data.genres) {
                    ContentValues value = new ContentValues();
                    value.put(GenreEntry.COLUMN_GENRE_ID, genre.id);
                    value.put(GenreEntry.COLUMN_GENRE_NAME, genre.name);
                    long _id = db.insert(GenreEntry.TABLE_NAME, null, value);
                    if (_id != -1) {
                        added++;
                    }
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            Debug.e("Added genres : " + added, false);
            if(added > 0){
                PreferenceManager.getInstance(getApplicationContext()).writeValue(AppConstants
                        .FETCHED_GENRES, true);
            }
        }
    }
}
