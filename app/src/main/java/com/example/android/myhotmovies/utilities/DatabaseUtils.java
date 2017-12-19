package com.example.android.myhotmovies.utilities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.android.myhotmovies.data.MovieDetail;
import com.example.android.myhotmovies.provider.DatabaseContract;

import java.util.ArrayList;

/**
 * Created by xie on 2017/12/17.
 */

public class DatabaseUtils {

    private static final String TAG = "DatabaseUtils";

    public static boolean saveMovieToDatabase(Context context, MovieDetail movieDetail, String type) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        Uri insertUri = null;
        switch (type) {
            case DatabaseContract.TYPE_POPULAR:
                insertUri = DatabaseContract.MovieEntry.TABLE_POPULAR_MOVIE_CONTENT_URI;
                break;
            case DatabaseContract.TYPE_TOP_RATED:
                insertUri = DatabaseContract.MovieEntry.TABLE_TOP_RATED_MOVIE_CONTENT_URI;
                break;
            case DatabaseContract.TYPE_FAVORITE:
                insertUri = DatabaseContract.MovieEntry.TABLE_FAVORITE_MOVIE_CONTENT_URI;
                break;
        }
        values.put(DatabaseContract.MovieEntry.COLUMN_NAME_MOVIE_ID, movieDetail.getMovieId());
        values.put(DatabaseContract.MovieEntry.COLUMN_NAME_POSTER_PATH, movieDetail.getPosterPath());
        values.put(DatabaseContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE, movieDetail.getVoteAverage());
        values.put(DatabaseContract.MovieEntry.COLUMN_NAME_MOVIE_TITLE, movieDetail.getMovieTitle());
        values.put(DatabaseContract.MovieEntry.COLUMN_NAME_OVERVIEW, movieDetail.getOverview());
        values.put(DatabaseContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, movieDetail.getReleaseDate());
        Uri uri = null;
        if (insertUri != null) {
            Log.d(TAG, "saveMovieToDatabase: " + insertUri);
            uri = contentResolver.insert(insertUri, values);
        }
        return uri != null;
    }

    public static MovieDetail queryMovieByIdFromDatabase(Context context, String queryMovieId, String type) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri queryUri = null;
        switch (type) {
            case DatabaseContract.TYPE_POPULAR:
                queryUri = Uri.parse(DatabaseContract.MovieEntry.TABLE_POPULAR_MOVIE_CONTENT_URI + "/" + queryMovieId);
                Log.d(TAG, "queryMovieByIdFromDatabase: " + queryUri);
                break;
            case DatabaseContract.TYPE_TOP_RATED:
                queryUri = Uri.parse(DatabaseContract.MovieEntry.TABLE_TOP_RATED_MOVIE_CONTENT_URI + "/" + queryMovieId);
                Log.d(TAG, "queryMovieByIdFromDatabase: " + queryUri);
                break;
            case DatabaseContract.TYPE_FAVORITE:
                queryUri = Uri.parse(DatabaseContract.MovieEntry.TABLE_FAVORITE_MOVIE_CONTENT_URI + "/" + queryMovieId);
                Log.d(TAG, "queryMovieByIdFromDatabase: " + queryUri);
                break;
        }
        if (queryUri != null) {
            Cursor cursor = contentResolver.query(
                    queryUri,
                    null,
                    DatabaseContract.MovieEntry.COLUMN_NAME_MOVIE_ID + " = ?",
                    new String[]{queryMovieId},
                    null);
            if (cursor != null) {
                MovieDetail movieDetail = new MovieDetail();
                while (cursor.moveToNext()) {
                    String movieId = cursor.getString(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_NAME_MOVIE_ID));
                    String posterPath = cursor.getString(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_NAME_POSTER_PATH));
                    String voteAverage = cursor.getString(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE));
                    String overview = cursor.getString(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_NAME_OVERVIEW));
                    String releaseDate = cursor.getString(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_NAME_RELEASE_DATE));
                    String movieTitle = cursor.getString(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_NAME_MOVIE_TITLE));
                    movieDetail.setMovieId(movieId);
                    movieDetail.setMovieTitle(movieTitle);
                    movieDetail.setOverview(overview);
                    movieDetail.setPosterPath(posterPath);
                    movieDetail.setReleaseDate(releaseDate);
                    movieDetail.setVoteAverage(voteAverage);
                }
                cursor.close();
                return movieDetail;
            }
            return null;
        }
        return null;
    }

    public static ArrayList<MovieDetail> queryAllMovieFromDatabase(Context context, String type) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri queryUri = null;
        switch (type) {
            case DatabaseContract.TYPE_POPULAR:
                queryUri = DatabaseContract.MovieEntry.TABLE_POPULAR_MOVIE_CONTENT_URI;
                Log.d(TAG, "queryAllMovieFromDatabase: " + queryUri);
                break;
            case DatabaseContract.TYPE_TOP_RATED:
                queryUri = DatabaseContract.MovieEntry.TABLE_TOP_RATED_MOVIE_CONTENT_URI;
                Log.d(TAG, "queryAllMovieFromDatabase: " + queryUri);
                break;
            case DatabaseContract.TYPE_FAVORITE:
                queryUri = DatabaseContract.MovieEntry.TABLE_FAVORITE_MOVIE_CONTENT_URI;
                Log.d(TAG, "queryAllMovieFromDatabase: " + queryUri);
                break;
        }
        if (queryUri != null) {
            ArrayList<MovieDetail> movieDetails = new ArrayList<>();
            Cursor cursor = contentResolver.query(
                    queryUri,
                    null,
                    null,
                    null,
                    null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    MovieDetail movieDetail = new MovieDetail();
                    String movieId = cursor.getString(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_NAME_MOVIE_ID));
                    String posterPath = cursor.getString(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_NAME_POSTER_PATH));
                    String voteAverage = cursor.getString(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE));
                    String overview = cursor.getString(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_NAME_OVERVIEW));
                    String releaseDate = cursor.getString(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_NAME_RELEASE_DATE));
                    String movieTitle = cursor.getString(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_NAME_MOVIE_TITLE));
                    movieDetail.setMovieId(movieId);
                    movieDetail.setMovieTitle(movieTitle);
                    movieDetail.setOverview(overview);
                    movieDetail.setPosterPath(posterPath);
                    movieDetail.setReleaseDate(releaseDate);
                    movieDetail.setVoteAverage(voteAverage);
                    movieDetails.add(movieDetail);
                }
                cursor.close();
            }
            return movieDetails;
        }
        return null;
    }

    public static int deleteMovieByIdFromDatabase(Context context, String deleteId, String type) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri deleteUri = null;
        int deleteRows = 0;
        switch (type) {
            case DatabaseContract.TYPE_POPULAR:
                deleteUri = Uri.parse(DatabaseContract.MovieEntry.TABLE_POPULAR_MOVIE_CONTENT_URI + "/" + deleteId);
                break;
            case DatabaseContract.TYPE_TOP_RATED:
                deleteUri = Uri.parse(DatabaseContract.MovieEntry.TABLE_TOP_RATED_MOVIE_CONTENT_URI + "/" + deleteId);
                break;
            case DatabaseContract.TYPE_FAVORITE:
                deleteUri = Uri.parse(DatabaseContract.MovieEntry.TABLE_FAVORITE_MOVIE_CONTENT_URI + "/" + deleteId);
                break;
        }
        if (deleteUri != null) {
            Log.d(TAG, "deleteMovieByIdFromDatabase: " + deleteUri);
            deleteRows = contentResolver.delete(
                    deleteUri,
                    DatabaseContract.MovieEntry.COLUMN_NAME_MOVIE_ID + " = ?",
                    new String[]{deleteId});
        }
        return deleteRows;
    }
}
