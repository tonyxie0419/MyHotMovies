package com.example.android.myhotmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xie on 2017/11/6.
 */

public class MovieDetail implements Parcelable{

    private String _id;
    private String _title;
    private String posterPath;
    private String voteAverage;
    private String overview;
    private String releaseDate;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this._title);
        dest.writeString(this.posterPath);
        dest.writeString(this.voteAverage);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
    }

    public MovieDetail() {
    }

    protected MovieDetail(Parcel in) {
        this._id = in.readString();
        this._title = in.readString();
        this.posterPath = in.readString();
        this.voteAverage = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
    }

    public static final Creator<MovieDetail> CREATOR = new Creator<MovieDetail>() {
        @Override
        public MovieDetail createFromParcel(Parcel source) {
            return new MovieDetail(source);
        }

        @Override
        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];
        }
    };
}
