package com.amrendra.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

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
        sb.append("[overview=" + overview + "]");
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
    }
}
