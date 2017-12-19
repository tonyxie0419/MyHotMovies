package com.example.android.myhotmovies.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.myhotmovies.provider.DatabaseContract.MovieEntry;

/**
 * Created by xie on 2017/12/15.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "Movies.db";
    public static final int DATABASE_VERSION = 5;

    private static final String TYPE_TEXT = "TEXT";
    private static final String TYPE_INTEGER = "INTEGER";

    private static final String CREATE_POPULAR_MOVIE =
            "CREATE TABLE" + " " + MovieEntry.TABLE_POPULAR_MOVIE + "("
                    + MovieEntry.COLUMN_NAME_ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, "
                    + MovieEntry.COLUMN_NAME_MOVIE_ID + " " + TYPE_TEXT + " UNIQUE, "
                    + MovieEntry.COLUMN_NAME_MOVIE_TITLE + " " + TYPE_TEXT + ", "
                    + MovieEntry.COLUMN_NAME_POSTER_PATH + " " + TYPE_TEXT + ", "
                    + MovieEntry.COLUMN_NAME_VOTE_AVERAGE + " " + TYPE_TEXT + ", "
                    + MovieEntry.COLUMN_NAME_OVERVIEW + " " + TYPE_TEXT + ", "
                    + MovieEntry.COLUMN_NAME_RELEASE_DATE + " " + TYPE_TEXT + ")";

    private static final String CREATE_RATED_MOVIE =
            "CREATE TABLE" + " " + MovieEntry.TABLE_TOP_RATED_MOVIE + "("
                    + MovieEntry.COLUMN_NAME_ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, "
                    + MovieEntry.COLUMN_NAME_MOVIE_ID + " " + TYPE_TEXT + " UNIQUE, "
                    + MovieEntry.COLUMN_NAME_MOVIE_TITLE + " " + TYPE_TEXT + ", "
                    + MovieEntry.COLUMN_NAME_POSTER_PATH + " " + TYPE_TEXT + ", "
                    + MovieEntry.COLUMN_NAME_VOTE_AVERAGE + " " + TYPE_TEXT + ", "
                    + MovieEntry.COLUMN_NAME_OVERVIEW + " " + TYPE_TEXT + ", "
                    + MovieEntry.COLUMN_NAME_RELEASE_DATE + " " + TYPE_TEXT + ")";

    private static final String CREATE_FAVORITE_MOVIE =
            "CREATE TABLE" + " " + MovieEntry.TABLE_FAVORITE_MOVIE + "("
                    + MovieEntry.COLUMN_NAME_ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, "
                    + MovieEntry.COLUMN_NAME_MOVIE_ID + " " + TYPE_TEXT + " UNIQUE, "
                    + MovieEntry.COLUMN_NAME_MOVIE_TITLE + " " + TYPE_TEXT + ", "
                    + MovieEntry.COLUMN_NAME_POSTER_PATH + " " + TYPE_TEXT + ", "
                    + MovieEntry.COLUMN_NAME_VOTE_AVERAGE + " " + TYPE_TEXT + ", "
                    + MovieEntry.COLUMN_NAME_OVERVIEW + " " + TYPE_TEXT + ", "
                    + MovieEntry.COLUMN_NAME_RELEASE_DATE + " " + TYPE_TEXT + ")";

    private static final String SQL_DELETE_TABLE_POPULAR_MOVIE =
            "DROP TABLE IF EXISTS " + MovieEntry.TABLE_POPULAR_MOVIE;

    private static final String SQL_DELETE_TABLE_RATED_MOVIE =
            "DROP TABLE IF EXISTS " + MovieEntry.TABLE_TOP_RATED_MOVIE;

    private static final String SQL_DELETE_TABLE_FAVORITE_MOVIE =
            "DROP TABLE IF EXISTS " + MovieEntry.TABLE_FAVORITE_MOVIE;

    public MyDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_POPULAR_MOVIE);
        db.execSQL(CREATE_RATED_MOVIE);
        db.execSQL(CREATE_FAVORITE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE_POPULAR_MOVIE);
        db.execSQL(SQL_DELETE_TABLE_RATED_MOVIE);
        db.execSQL(SQL_DELETE_TABLE_FAVORITE_MOVIE);
        onCreate(db);
    }
}
