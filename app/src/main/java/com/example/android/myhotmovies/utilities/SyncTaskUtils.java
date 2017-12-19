package com.example.android.myhotmovies.utilities;

import android.content.Context;

import com.example.android.myhotmovies.sync.AsyncTaskCompleteListener;
import com.example.android.myhotmovies.sync.FetchMovieDetailTask;
import com.example.android.myhotmovies.sync.FetchMovieReviewTask;
import com.example.android.myhotmovies.sync.FetchMovieTrailerTask;

/**
 * Created by xie on 2017/12/7.
 */

public class SyncTaskUtils {

    public static void syncMovieDetail(Context context, AsyncTaskCompleteListener listener, String type) {
        new FetchMovieDetailTask(context, listener).execute(type);
    }

    public static void syncMovieTrailer(AsyncTaskCompleteListener listener, String movieId) {
        new FetchMovieTrailerTask(listener).execute(movieId);
    }

    public static void syncMovieReview(AsyncTaskCompleteListener listener, String movieId) {
        new FetchMovieReviewTask(listener).execute(movieId);
    }
}
