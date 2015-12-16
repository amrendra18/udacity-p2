package com.amrendra.popularmovies.loaders;

import android.content.Context;
import android.database.Cursor;

import com.amrendra.popularmovies.db.MovieContract;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.Movie;

import java.util.List;

/**
 * Created by Amrendra Kumar on 10/12/15.
 */
public class FavouriteLoader extends CustomLoader<List<Movie>> {

    public FavouriteLoader(Context context) {
        super(context);
        Debug.c();
    }

    @Override
    public List<Movie> loadInBackground() {
        Debug.c();
        Cursor cursor = getContext().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.MOVIE_PROJECTION_DETAIL,
                null,
                null,
                null
        );

        List<Movie> list = Movie.cursorToList(cursor);
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }
}
