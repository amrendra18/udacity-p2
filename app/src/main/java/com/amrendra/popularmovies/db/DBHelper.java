package com.amrendra.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.amrendra.popularmovies.db.MovieContract.GenreEntry;
import com.amrendra.popularmovies.db.MovieContract.MovieEntry;
import com.amrendra.popularmovies.db.MovieContract.TrailerEntry;
import com.amrendra.popularmovies.db.MovieContract.ReviewEntry;
import com.amrendra.popularmovies.logger.Debug;

/**
 * Created by Amrendra Kumar on 08/12/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "filmie.db";


    private static final String SQL_CREATE_GENRE_TABLE = "CREATE TABLE " + GenreEntry.TABLE_NAME + " (" +
            GenreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            GenreEntry.COLUMN_GENRE_ID + " INTEGER NOT NULL, " +
            GenreEntry.COLUMN_GENRE_NAME + " TEXT NOT NULL, " +
            "UNIQUE (" + GenreEntry.COLUMN_GENRE_ID + ") ON CONFLICT REPLACE)";

    private static final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + "("
            + MovieEntry._ID + " INTEGER NOT NULL PRIMARY KEY,"
            + MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL,"
            + MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL,"
            + MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT,"
            + MovieEntry.COLUMN_MOVIE_GENRE_IDS + " TEXT,"
            + MovieEntry.COLUMN_ORIGNAL_LANGUAGE + " TEXT, "
            + MovieEntry.COLUMN_MOVIE_POPULARITY + " REAL,"
            + MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " REAL,"
            + MovieEntry.COLUMN_MOVIE_VOTE_COUNT + " INTEGER,"
            + MovieEntry.COLUMN_MOVIE_BACKDROP_PATH + " TEXT,"
            + MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT,"
            + MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT,"
            + MovieEntry.COLUMN_MOVIE_HOMEPAGE + " TEXT,"
            + MovieEntry.COLUMN_MOVIE_IMDB + " TEXT,"
            + MovieEntry.COLUMN_MOVIE_TAGLINE + " TEXT,"
            + MovieEntry.COLUMN_MOVIE_REVENUE + " INTEGER,"
            + MovieEntry.COLUMN_MOVIE_RUNTIME + " INTEGER,"
            + MovieEntry.COLUMN_MOVIE_FAVOURITE + " INTEGER NOT NULL DEFAULT 0,"
            + "UNIQUE (" + MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE)";

    private static final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + TrailerEntry.TABLE_NAME + "("
            + TrailerEntry._ID + " INTEGER NOT NULL PRIMARY KEY,"
            + TrailerEntry.COLUMN_TRAILER_ID + " TEXT NOT NULL,"
            + TrailerEntry.COLUMN_MOVIE_ID + " INTEGER  NOT NULL,"
            + TrailerEntry.COLUMN_YOUTUBE_KEY + " TEXT,"
            + TrailerEntry.COLUMN_NAME + " TEXT,"
            + "UNIQUE (" + TrailerEntry.COLUMN_TRAILER_ID + ") ON CONFLICT REPLACE)";

    private static final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME + "("
            + ReviewEntry._ID + " INTEGER NOT NULL PRIMARY KEY,"
            + ReviewEntry.COLUMN_REVIEW_ID + " TEXT NOT NULL,"
            + ReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL,"
            + ReviewEntry.COLUMN_AUTHOR + " TEXT,"
            + ReviewEntry.COLUMN_CONTENT + " TEXT,"
            + ReviewEntry.COLUMN_URL + " REAL,"
            + "UNIQUE (" + ReviewEntry.COLUMN_REVIEW_ID + ") ON CONFLICT REPLACE)";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Debug.c();
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_GENRE_TABLE);
        db.execSQL(SQL_CREATE_TRAILER_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Debug.c();
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GenreEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
        onCreate(db);
    }
}
