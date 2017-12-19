package com.example.android.myhotmovies.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by xie on 2017/12/15.
 */

public class DatabaseContract {
    private DatabaseContract() {
    }

    /**
     * Content provider authority.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.myhotmovies";

    private static final String SCHEME = "content";

    /**
     * Base URI. (content://com.example.android.myhotmovies)
     */
    public static final Uri BASE_CONTENT_URI = new Uri.Builder()
            .scheme(SCHEME)
            .authority(CONTENT_AUTHORITY)
            .build();

    public static final String TYPE_POPULAR = "popular";
    public static final String TYPE_TOP_RATED = "top_rated";
    public static final String TYPE_FAVORITE = "favorite";
    public static final String TYPE_TRAILER = "trailer";
    public static final String TYPE_REVIEW = "review";

    public static class MovieEntry implements BaseColumns{
        public static final String TABLE_POPULAR_MOVIE = "popular_movie";
        public static final String TABLE_TOP_RATED_MOVIE = "rated_movie";
        public static final String TABLE_FAVORITE_MOVIE = "favorite_movie";

        public static final Uri TABLE_POPULAR_MOVIE_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_POPULAR_MOVIE).build();

        public static final Uri TABLE_TOP_RATED_MOVIE_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_TOP_RATED_MOVIE).build();

        public static final Uri TABLE_FAVORITE_MOVIE_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_FAVORITE_MOVIE).build();

        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_MOVIE_ID = "movie_id";
        public static final String COLUMN_NAME_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_NAME_POSTER_PATH = "poster_path";
        public static final String COLUMN_NAME_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_RELEASE_DATE = "release_date";
    }
}
