package com.amrendra.popularmovies.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.amrendra.popularmovies.logger.Debug;

/**
 * Created by Amrendra Kumar on 08/12/15.
 */

public class MovieProvider extends ContentProvider {

    private static final int MOVIES = 100;
    private static final int MOVIE_WITH_ID = 101;

    private static final int GENRES = 200;
    private static final int GENRE_WITH_ID = 201;

    private static final int TRAILERS = 300;
    private static final int TRAILERS_WITH_ID = 301;

    private static final int REVIEWS = 400;
    private static final int REVIEWS_WITH_ID = 401;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private DBHelper mDBHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIES);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_WITH_ID);

        matcher.addURI(authority, MovieContract.PATH_GENRE, GENRES);
        matcher.addURI(authority, MovieContract.PATH_GENRE + "/*", GENRE_WITH_ID);

        matcher.addURI(authority, MovieContract.PATH_TRAILERS, TRAILERS);
        matcher.addURI(authority, MovieContract.PATH_TRAILERS + "/*", TRAILERS_WITH_ID);

        matcher.addURI(authority, MovieContract.PATH_REVIEWS, REVIEWS);
        matcher.addURI(authority, MovieContract.PATH_REVIEWS + "/*", REVIEWS_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int match = sUriMatcher.match(uri);
        Cursor retCursor;
        Debug.e("CP Query : " + uri + " match : " + match, false);
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        switch (match) {
            case MOVIES:
                retCursor = getMovieList(uri, projection, selection, selectionArgs, sortOrder);
                break;
            case MOVIE_WITH_ID:
                retCursor = getMovieDetail(uri, projection, selection, selectionArgs, sortOrder);
                break;
            case GENRES: {
                retCursor = db.query(
                        MovieContract.GenreEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
            }
            break;
            case GENRE_WITH_ID:
                retCursor = getGenreDetail(uri, projection, selection, selectionArgs, sortOrder);
                break;
            case TRAILERS:
                retCursor = getAllTrailerList(uri, projection, selection, selectionArgs, sortOrder);
                break;
            case TRAILERS_WITH_ID:
                retCursor = getTrailersForMovie(uri, projection, selection, selectionArgs,
                        sortOrder);
                break;
            case REVIEWS:
                retCursor = getAllReviewsList(uri, projection, selection, selectionArgs, sortOrder);
                break;
            case REVIEWS_WITH_ID:
                retCursor = getReviewsForMovie(uri, projection, selection, selectionArgs,
                        sortOrder);
                break;
            default:
                Debug.e("ERROR : " + uri, false);
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private Cursor getReviewsForMovie(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    private Cursor getAllReviewsList(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    private Cursor getTrailersForMovie(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    private Cursor getAllTrailerList(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    private Cursor getGenreDetail(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    private Cursor getMovieDetail(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    private Cursor getMovieList(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case GENRES:
                return MovieContract.GenreEntry.CONTENT_TYPE;
            case GENRE_WITH_ID:
                return MovieContract.GenreEntry.CONTENT_ITEM_TYPE;
            case TRAILERS:
                return MovieContract.TrailerEntry.CONTENT_TYPE;
            case TRAILERS_WITH_ID:
                return MovieContract.TrailerEntry.CONTENT_TYPE;
            case REVIEWS:
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            case REVIEWS_WITH_ID:
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            default:
                Debug.e("ERROR : " + uri, false);
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //Generally shouldn't come here, we are not adding genre one at a time
        final int match = sUriMatcher.match(uri);
        Debug.e("CP insert : " + uri + " match : " + match, false);
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Uri returnUri;
        switch (match) {
            case MOVIES: {
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MovieContract.MovieEntry.buildMovieWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
            }
            break;
            case GENRES: {
                long _id = db.insert(MovieContract.GenreEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MovieContract.GenreEntry.buildGenreWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
            }
            break;
            case TRAILERS: {
                long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MovieContract.TrailerEntry.buildTrailerWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
            }
            break;
            case REVIEWS: {
                long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MovieContract.ReviewEntry.buildReviewWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
            }
            break;
            default:
                Debug.e("ERROR : " + uri, false);
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final int match = sUriMatcher.match(uri);
        Debug.e("CP bulkInsert : " + uri + " match : " + match, false);
        String tableName = null;
        switch (match) {
            case MOVIES:
                tableName = MovieContract.MovieEntry.TABLE_NAME;
                break;
            case GENRES:
                tableName = MovieContract.GenreEntry.TABLE_NAME;
                break;
            case TRAILERS:
                tableName = MovieContract.TrailerEntry.TABLE_NAME;
                break;
            case REVIEWS:
                tableName = MovieContract.ReviewEntry.TABLE_NAME;
                break;
            default:
                Debug.e("ERROR : " + uri, false);
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        int inserted = 0;
        Debug.e("Bulk Insertion into " + tableName, false);
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (ContentValues value : values) {
                long _id = db.insert(tableName, null, value);
                if (_id != -1) {
                    inserted++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return inserted;
    }
}
