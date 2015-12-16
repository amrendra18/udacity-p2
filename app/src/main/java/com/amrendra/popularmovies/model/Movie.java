package com.amrendra.popularmovies.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.amrendra.popularmovies.db.MovieContract;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.utils.MoviesConstants;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amrendra Kumar on 24/11/15.
 */
public class Movie implements Parcelable {

    @SerializedName("id")
    public long id;
    @SerializedName("vote_count")
    public long votesCount;

    @SerializedName("original_title")
    public String originalTitle;

    @SerializedName("original_language")
    public String originalLanguage;

    @SerializedName("title")
    public String title;
    @SerializedName("overview")
    public String overview;
    @SerializedName("release_date")
    public String releaseDate;
    @SerializedName("backdrop_path")
    public String backdropPath;
    @SerializedName("poster_path")
    public String posterPath;

    @SerializedName("popularity")
    public double popularity;

    @SerializedName("vote_average")
    public double averageVote;

    @SerializedName("homepage")
    public String homepage;

    @SerializedName("imdb_id")
    public String imdbid;

    @SerializedName("revenue")
    public long revenue;

    @SerializedName("runtime")
    public int runtime;

    @SerializedName("tagline")
    public String tagline;

    @SerializedName("genre_ids")
    public ArrayList<Integer> genreIds;

    public String showGenres;

    public Movie() {

    }

    protected Movie(Parcel in) {
        id = in.readLong();
        votesCount = in.readLong();
        title = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        backdropPath = in.readString();
        posterPath = in.readString();
        popularity = in.readDouble();
        averageVote = in.readDouble();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        homepage = in.readString();
        imdbid = in.readString();
        revenue = in.readLong();
        runtime = in.readInt();
        tagline = in.readString();

        genreIds = in.readArrayList(List.class.getClassLoader());
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Movie{\n");
        sb.append("[id=" + id + "]");
        sb.append("[title=" + title + "]");
        sb.append("[vote_count=" + votesCount + "]");
        sb.append("[release_date=" + releaseDate + "]");
        sb.append("[popularity=" + popularity + "]");
        sb.append("[vote_average=" + averageVote + "]");
        sb.append("[backdrop_path=" + backdropPath + "]");
        sb.append("[poster_path=" + posterPath + "]");
        sb.append("[revenue=" + revenue + "]");
        sb.append("[imdbid=" + imdbid + "]");
        sb.append("[tagline=" + tagline + "]");
        sb.append("[runtime=" + runtime + "]");
        sb.append("[homepage=" + homepage + "]");
        //sb.append("[overview=" + overview + "]");
        if (genreIds != null) {
            for (Integer i : genreIds) {
                sb.append("[genreids=" + i + "]");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(votesCount);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(backdropPath);
        dest.writeString(posterPath);
        dest.writeDouble(popularity);
        dest.writeDouble(averageVote);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(homepage);
        dest.writeString(imdbid);
        dest.writeLong(revenue);
        dest.writeInt(runtime);
        dest.writeString(tagline);
        dest.writeList(genreIds);
    }

    public ContentValues movieToContentValue() {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, id);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, posterPath);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, overview);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, releaseDate);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_GENRE_IDS, MoviesConstants
                .getGenresStringList(genreIds));
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, title);
        cv.put(MovieContract.MovieEntry.COLUMN_ORIGNAL_LANGUAGE, originalLanguage);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, backdropPath);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY, popularity);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT, votesCount);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, averageVote);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_HOMEPAGE, homepage);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMDB, imdbid);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_REVENUE, revenue);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RUNTIME, runtime);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TAGLINE, tagline);
        return cv;
    }


    public static ArrayList<Movie> cursorToList(Cursor cursor) {
        ArrayList<Movie> list = new ArrayList<>();
        if (cursor != null) {
            int idIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            int titleIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
            int votesCountIndex = cursor.getColumnIndex(MovieContract.MovieEntry
                    .COLUMN_MOVIE_VOTE_COUNT);
            int overviewIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW);
            int releaseDateIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE);
            int backdropPathIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH);
            int posterPathIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH);
            int popularityIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY);
            int averageVoteIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE);
            int originalLanguageIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGNAL_LANGUAGE);
            int genreIdsIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_GENRE_IDS);

            int homepageIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_HOMEPAGE);
            int imdbidIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_IMDB);
            int revenueIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_REVENUE);
            int taglineIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TAGLINE);
            int runtimeIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RUNTIME);
            while (cursor.moveToNext()) {
                Movie movie = new Movie();
                movie.id = cursor.getInt(idIndex);
                movie.title = cursor.getString(titleIndex);
                movie.votesCount = cursor.getInt(votesCountIndex);
                movie.overview = cursor.getString(overviewIndex);
                movie.releaseDate = cursor.getString(releaseDateIndex);
                movie.backdropPath = cursor.getString(backdropPathIndex);
                movie.posterPath = cursor.getString(posterPathIndex);
                movie.popularity = cursor.getDouble(popularityIndex);
                movie.averageVote = cursor.getDouble(averageVoteIndex);
                movie.originalLanguage = cursor.getString(originalLanguageIndex);
                movie.showGenres = cursor.getString(genreIdsIndex);

                /*
                movie.homepage = cursor.getString(homepageIndex);
                movie.imdbid = cursor.getString(imdbidIndex);
                movie.revenue  = cursor.getInt(revenueIndex);
                movie.runtime = cursor.getInt(runtimeIndex);
                movie.tagline = cursor.getString(taglineIndex);
                 */
                Debug.e(movie.toString(), false);
                list.add(movie);
            }

            cursor.close();
        }
        return list;
    }

}
