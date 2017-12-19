package com.example.android.myhotmovies.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.myhotmovies.provider.DatabaseContract.MovieEntry;

/**
 * Created by xie on 2017/12/15.
 */

public class MyContentProvider extends ContentProvider {

    private static final String TAG = "MyContentProvider";

    public static final int POPULAR_MOVIE_DIR = 0;
    public static final int POPULAR_MOVIE_ITEM = 1;
    public static final int RATED_MOVIE_DIR = 2;
    public static final int RATED_MOVIE_ITEM = 3;
    public static final int FAVORITE_MOVIE_DIR = 4;
    public static final int FAVORITE_MOVIE_ITEM = 5;

    public static final String AUTHORITY = DatabaseContract.CONTENT_AUTHORITY;
    private static UriMatcher uriMatcher;
    private MyDatabaseHelper dbHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, MovieEntry.TABLE_POPULAR_MOVIE, POPULAR_MOVIE_DIR);
        uriMatcher.addURI(AUTHORITY, MovieEntry.TABLE_POPULAR_MOVIE + "/*", POPULAR_MOVIE_ITEM);
        uriMatcher.addURI(AUTHORITY, MovieEntry.TABLE_TOP_RATED_MOVIE, RATED_MOVIE_DIR);
        uriMatcher.addURI(AUTHORITY, MovieEntry.TABLE_TOP_RATED_MOVIE + "/*", RATED_MOVIE_ITEM);
        uriMatcher.addURI(AUTHORITY, MovieEntry.TABLE_FAVORITE_MOVIE, FAVORITE_MOVIE_DIR);
        uriMatcher.addURI(AUTHORITY, MovieEntry.TABLE_FAVORITE_MOVIE + "/*", FAVORITE_MOVIE_ITEM);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MyDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case POPULAR_MOVIE_DIR:
                cursor = db.query(MovieEntry.TABLE_POPULAR_MOVIE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case POPULAR_MOVIE_ITEM:
                String popularMovieId = uri.getLastPathSegment();
                cursor = db.query(MovieEntry.TABLE_POPULAR_MOVIE,
                        projection,
                        MovieEntry.COLUMN_NAME_MOVIE_ID + " = ?",
                        new String[]{popularMovieId},
                        null,
                        null,
                        sortOrder);
                break;
            case RATED_MOVIE_DIR:
                cursor = db.query(MovieEntry.TABLE_TOP_RATED_MOVIE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case RATED_MOVIE_ITEM:
                String ratedMovieId = uri.getLastPathSegment();
                cursor = db.query(MovieEntry.TABLE_TOP_RATED_MOVIE,
                        projection,
                        MovieEntry.COLUMN_NAME_MOVIE_ID + " = ?",
                        new String[]{ratedMovieId},
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITE_MOVIE_DIR:
                cursor = db.query(MovieEntry.TABLE_FAVORITE_MOVIE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITE_MOVIE_ITEM:
                String favoriteMovieId = uri.getLastPathSegment();
                cursor = db.query(MovieEntry.TABLE_FAVORITE_MOVIE,
                        projection,
                        MovieEntry.COLUMN_NAME_MOVIE_ID + " = ?",
                        new String[]{favoriteMovieId},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                break;
        }
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                return null;
            }
            return cursor;
        }
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)) {
            case POPULAR_MOVIE_DIR:
            case POPULAR_MOVIE_ITEM:
                long newPopularId = -1;
                try {
                    newPopularId = db.insertOrThrow(
                            MovieEntry.TABLE_POPULAR_MOVIE,
                            null,
                            values);
                } catch (SQLiteConstraintException e) {
                    //不做任何处理
                }
                if (newPopularId != -1) {
                    uriReturn = Uri.parse(MovieEntry.TABLE_POPULAR_MOVIE_CONTENT_URI + "/" + newPopularId);
                    Log.d(TAG, "uriReturn: " + uriReturn);
                }
                break;
            case RATED_MOVIE_DIR:
            case RATED_MOVIE_ITEM:
                long newRatedId = -1;
                try {
                    newRatedId = db.insertOrThrow(
                            MovieEntry.TABLE_TOP_RATED_MOVIE,
                            null,
                            values);
                } catch (SQLiteConstraintException e) {
                    //不做任何处理
                }
                if (newRatedId != -1) {
                    uriReturn = Uri.parse(MovieEntry.TABLE_POPULAR_MOVIE_CONTENT_URI + "/" + newRatedId);
                    Log.d(TAG, "uriReturn: " + uriReturn);
                }
                break;
            case FAVORITE_MOVIE_DIR:
            case FAVORITE_MOVIE_ITEM:
                long newFavoriteId = -1;
                try {
                    newFavoriteId = db.insertOrThrow(
                            MovieEntry.TABLE_FAVORITE_MOVIE,
                            null,
                            values);
                } catch (SQLiteConstraintException e) {
                    //不做任何处理
                }
                if (newFavoriteId != -1) {
                    uriReturn = Uri.parse(MovieEntry.TABLE_POPULAR_MOVIE_CONTENT_URI + "/" + newFavoriteId);
                    Log.d(TAG, "uriReturn: " + uriReturn);
                }
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updateRows = 0;
        switch (uriMatcher.match(uri)) {
            case POPULAR_MOVIE_DIR:
                updateRows = db.update(MovieEntry.TABLE_POPULAR_MOVIE, values, selection, selectionArgs);
                break;
            case POPULAR_MOVIE_ITEM:
                String popularMovieId = uri.getLastPathSegment();
                updateRows = db.update(MovieEntry.TABLE_POPULAR_MOVIE,
                        values,
                        MovieEntry.COLUMN_NAME_MOVIE_ID + " = ?",
                        new String[]{popularMovieId});
                break;
            case RATED_MOVIE_DIR:
                updateRows = db.update(MovieEntry.TABLE_TOP_RATED_MOVIE, values, selection, selectionArgs);
                break;
            case RATED_MOVIE_ITEM:
                String ratedMovieId = uri.getLastPathSegment();
                updateRows = db.update(MovieEntry.TABLE_TOP_RATED_MOVIE,
                        values,
                        MovieEntry.COLUMN_NAME_MOVIE_ID + " = ?",
                        new String[]{ratedMovieId});
                break;
            case FAVORITE_MOVIE_DIR:
                updateRows = db.update(MovieEntry.TABLE_FAVORITE_MOVIE, values, selection, selectionArgs);
                break;
            case FAVORITE_MOVIE_ITEM:
                String favoriteMovieId = uri.getLastPathSegment();
                updateRows = db.update(MovieEntry.TABLE_FAVORITE_MOVIE,
                        values,
                        MovieEntry.COLUMN_NAME_MOVIE_ID + " = ?",
                        new String[]{favoriteMovieId});
                break;
            default:
                break;
        }
        return updateRows;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = 0;
        switch (uriMatcher.match(uri)) {
            case POPULAR_MOVIE_DIR:
                deletedRows = db.delete(MovieEntry.TABLE_POPULAR_MOVIE, selection, selectionArgs);
                break;
            case POPULAR_MOVIE_ITEM:
                String popularMovieId = uri.getLastPathSegment();
                deletedRows = db.delete(MovieEntry.TABLE_POPULAR_MOVIE,
                        MovieEntry.COLUMN_NAME_MOVIE_ID + " = ?",
                        new String[]{popularMovieId});
                break;
            case RATED_MOVIE_DIR:
                deletedRows = db.delete(MovieEntry.TABLE_TOP_RATED_MOVIE, selection, selectionArgs);
                break;
            case RATED_MOVIE_ITEM:
                String ratedMovieId = uri.getPathSegments().get(1);
                deletedRows = db.delete(MovieEntry.TABLE_TOP_RATED_MOVIE,
                        MovieEntry.COLUMN_NAME_MOVIE_ID + " = ?",
                        new String[]{ratedMovieId});
                break;
            case FAVORITE_MOVIE_DIR:
                deletedRows = db.delete(MovieEntry.TABLE_FAVORITE_MOVIE, selection, selectionArgs);
                break;
            case FAVORITE_MOVIE_ITEM:
                String favoriteMovieId = uri.getLastPathSegment();
                deletedRows = db.delete(MovieEntry.TABLE_FAVORITE_MOVIE,
                        MovieEntry.COLUMN_NAME_MOVIE_ID + " = ?",
                        new String[]{favoriteMovieId});
                break;
            default:
                break;
        }
        return deletedRows;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case POPULAR_MOVIE_DIR:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.com.example.android.myhotmovies.popular_movie";
            case POPULAR_MOVIE_ITEM:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.example.android.myhotmovies.popular_movie";
            case RATED_MOVIE_DIR:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.com.example.android.myhotmovies.rated_movie";
            case RATED_MOVIE_ITEM:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.example.android.myhotmovies.rated_movie";
            case FAVORITE_MOVIE_DIR:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.com.example.android.myhotmovies.favorite_movie";
            case FAVORITE_MOVIE_ITEM:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.example.android.myhotmovies.favorite_movie";
        }
        return null;
    }
}
