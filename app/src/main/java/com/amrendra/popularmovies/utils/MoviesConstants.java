package com.amrendra.popularmovies.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.amrendra.popularmovies.db.MovieContract;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Amrendra Kumar on 24/11/15.
 */
public class MoviesConstants {

    // base urls
    public static final String API_BASE_URL = "https://api.themoviedb.org";
    public static final String API_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";


    public final static String IMAGE_SIZE_SMALL = "w185/";
    public final static String IMAGE_SIZE_LARGE = "w500/";

    // end points urls
    public static final String GET_MOVIES_URL = "/3/discover/movie";
    public static final String GET_REVIEWS_URL = "/3/movie/{id}/reviews";
    public static final String GET_TRAILERS_URL = "/3/movie/{id}/videos";
    public static final String GET_GENRES_URL = "/3/genre/movie/list";
    public static final String GET_MOVIE_DETAIL_URL = "/3/movie/{id}";

    // api parameters
    public static final String API_KEY = "api_key";
    public static final String SORT_BY = "sort_by";
    public static final String PAGE = "page";
    public static final String ID = "id";

    // sorting
    public static final String SORT_BY_POPULARITY = "popularity.desc";
    public static final String SORT_BY_RATINGS = "vote_average.desc";
    public static final String SORT_BY_FAVOURITES = "show_favourites";

    // by default fetching movies whose vote count > 200, so that we have good results
    public static final String VOTE_COUNT_GTE = "vote_count.gte";
    public static final int VOTE_COUNT_DEFAULT = 200;

    // trailers
    public static final String TRAILER_VIDEO_URL = "http://www.youtube.com/watch?v=";
    public static final String TRAILER_IMAGE_URL = "http://img.youtube.com/vi/%s/0.jpg";


    // since there are limited genres, and dont change often (read ever)
    // we are saving in db, but after saving in db,
    // we are reading all the values and storing in hashmap for faster access
    private static HashMap<Integer, String> genreHashMap = null;

    public static String getGenreName(Context context, int id) {
        boolean alreadyRead = PreferenceManager.getInstance(context).readValue(AppConstants
                .READ_GENRES_FROM_DB, false);
        if (genreHashMap == null || genreHashMap.size() == 0 || (!alreadyRead)) {
            Debug.e("loading genres, going to read db", false);
            if (genreHashMap != null) {
                genreHashMap.clear();
            } else {
                genreHashMap = new HashMap<>();
            }

            Cursor cursor = context.getContentResolver().query(
                    MovieContract.GenreEntry.CONTENT_URI,
                    MovieContract.GenreEntry.GENRE_PROJECTION,
                    null,
                    null,
                    null
            );

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    try {
                        genreHashMap.put(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
                    } catch (NumberFormatException e) {
                        Debug.e("NFE : " + cursor.getString(0) + " " + e.getMessage(), false);
                    }
                }
                cursor.close();
            } else {
                Debug.e("Cursor is null", false);
            }
        }
        PreferenceManager.getInstance(context).writeValue(AppConstants
                .READ_GENRES_FROM_DB, true);
        String val = genreHashMap.get(id);
        if (val == null) {
            val = "";
        }
        return val;
    }

    public static String getGenresList(Context context, List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null) {
            for (Integer i : list) {
                sb.append(getGenreName(context, i) + " ");
            }
        }
        return sb.toString().trim();
    }

    public static ArrayList<Integer> getGenresIntList(String dbLine) {
        ArrayList<Integer> list = new ArrayList<>();
        if (dbLine != null) {
            dbLine = dbLine.trim();
            String[] ints = dbLine.split(" ");
            for (String si : ints) {
                try {
                    list.add(Integer.parseInt(si));
                } catch (NumberFormatException nfe) {
                    Debug.e("NFE : fetching back list from db : " + nfe.getMessage(), false);
                }
            }
        }
        Debug.e("to fetch back : " + dbLine, false);
        Debug.array(list.toArray());
        return list;
    }

    public static String getGenresStringList(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null) {
            for (Integer i : list) {
                sb.append(Integer.toString(i) + " ");
            }
        }
        return sb.toString().trim();
    }

    public static boolean isFavouriteMovie(long movieId, Context context) {
        Uri uri = MovieContract.MovieEntry.buildMovieWithId(movieId);
        Debug.e("uri: " + uri, false);
        Cursor cursor = context.getContentResolver().query(
                uri,
                MovieContract.MovieEntry.MOVIE_PROJECTION,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? LIMIT 1",
                new String[]{Long.toString(movieId)},
                null
        );
        boolean found = false;
        if (cursor != null) {
            if (cursor.moveToNext()) {
                found = true;
            }
            cursor.close();
        }
        Debug.e("result : " + found, false);
        return found;
    }

    public static int removeFavouriteMovie(Movie movie, Context context) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        int delete = context.getContentResolver().delete(
                uri,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{Long.toString(movie.id)}
        );
        return delete;
    }

    public static Uri addFavouriteMovie(Movie movie, Context context) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        ContentValues cv = movie.movieToContentValue();
        Uri retUri = context.getContentResolver().insert(
                uri,
                cv
        );
        return retUri;
    }
}
