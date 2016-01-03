package com.amrendra.popularmovies.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Amrendra Kumar on 08/12/15.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.amrendra.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_GENRE = "genre";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_TRAILERS = "trailers";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_ORIGNAL_LANGUAGE = "original_lang";
        public static final String COLUMN_MOVIE_POPULARITY = "popularity";
        public static final String COLUMN_MOVIE_GENRE_IDS = "genre_ids";
        public static final String COLUMN_MOVIE_VOTE_COUNT = "vote_count";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_FAVOURITE = "favourite";
        public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";
        public static final String COLUMN_MOVIE_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_MOVIE_HOMEPAGE = "homepage";
        public static final String COLUMN_MOVIE_IMDB = "imdb";
        public static final String COLUMN_MOVIE_REVENUE = "revenue";
        public static final String COLUMN_MOVIE_RUNTIME = "runtime";
        public static final String COLUMN_MOVIE_TAGLINE = "tagline";

        public static Uri buildMovieWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getMovieIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

        public static final String[] MOVIE_PROJECTION_GRID = new String[]{
                _ID,
                COLUMN_MOVIE_ID, //0
                COLUMN_MOVIE_POSTER_PATH,//1
        };

        public static final String[] MOVIE_PROJECTION = new String[]{
                _ID,//0
                COLUMN_MOVIE_ID,
                COLUMN_MOVIE_TITLE,
                COLUMN_MOVIE_OVERVIEW,
                COLUMN_ORIGNAL_LANGUAGE,
                COLUMN_MOVIE_POPULARITY,
                COLUMN_MOVIE_GENRE_IDS,
                COLUMN_MOVIE_VOTE_COUNT,
                COLUMN_MOVIE_VOTE_AVERAGE,
                COLUMN_MOVIE_RELEASE_DATE,
                COLUMN_MOVIE_FAVOURITE,
                COLUMN_MOVIE_POSTER_PATH,
                COLUMN_MOVIE_BACKDROP_PATH
        };

        public static final String[] MOVIE_PROJECTION_DETAIL = new String[]{
                _ID,
                COLUMN_MOVIE_ID,
                COLUMN_MOVIE_TITLE,
                COLUMN_MOVIE_OVERVIEW,
                COLUMN_ORIGNAL_LANGUAGE,
                COLUMN_MOVIE_POPULARITY,
                COLUMN_MOVIE_GENRE_IDS,
                COLUMN_MOVIE_VOTE_COUNT,
                COLUMN_MOVIE_VOTE_AVERAGE,
                COLUMN_MOVIE_RELEASE_DATE,
                COLUMN_MOVIE_FAVOURITE,
                COLUMN_MOVIE_POSTER_PATH,
                COLUMN_MOVIE_BACKDROP_PATH,
                COLUMN_MOVIE_HOMEPAGE,
                COLUMN_MOVIE_IMDB,
                COLUMN_MOVIE_REVENUE,
                COLUMN_MOVIE_RUNTIME,
                COLUMN_MOVIE_TAGLINE
        };
    }

    public static final class GenreEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GENRE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GENRE;
        public static final String TABLE_NAME = "genre";
        public static final String COLUMN_GENRE_ID = "genre_id";
        public static final String COLUMN_GENRE_NAME = "genre_name";

        public static Uri buildGenreWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String[] GENRE_PROJECTION = new String[]{
                COLUMN_GENRE_ID,
                COLUMN_GENRE_NAME
        };
    }

    public static final class TrailerEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS)
                .build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;
        public static final String TABLE_NAME = "trailers";
        public static final String COLUMN_TRAILER_ID = "trailer_id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_YOUTUBE_KEY = "youtube_key";
        public static final String COLUMN_NAME = "title";

        public static Uri buildTrailerWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getMovieIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

        public static final String[] TRAILER_PROJECTION = new String[]{
                _ID,
                COLUMN_MOVIE_ID,
                COLUMN_YOUTUBE_KEY,
                COLUMN_NAME
        };
    }

    public static final class ReviewEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
        public static final String TABLE_NAME = "reviews";
        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";

        public static Uri buildReviewWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String[] REVIEW_PROJECTION = new String[]{
                _ID,
                COLUMN_MOVIE_ID,
                COLUMN_AUTHOR,
                COLUMN_CONTENT,
                COLUMN_URL
        };

        public static long getMovieIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }
    }

}
