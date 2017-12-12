package com.example.android.myhotmovies.utilities;

import com.example.android.myhotmovies.AsyncTaskCompleteListener;
import com.example.android.myhotmovies.sync.FetchMovieDetailTask;
import com.example.android.myhotmovies.sync.FetchMovieReviewTask;
import com.example.android.myhotmovies.sync.FetchMovieTrailerTask;

/**
 * Created by xie on 2017/12/7.
 */

public class SyncTaskUtils {
    public static final int CODE_MOVIE_DETAIL = 1;
    public static final int CODE_MOVIE_TRAILER = 2;
    public static final int CODE_MOVIE_REVIEW = 3;

    public static void syncMovie(AsyncTaskCompleteListener listener, String queryCategory, int requestCode) {
        switch (requestCode) {
            case CODE_MOVIE_DETAIL:
                new FetchMovieDetailTask(listener).execute(queryCategory);
                break;
            case CODE_MOVIE_TRAILER:
                new FetchMovieTrailerTask(listener).execute(queryCategory);
                break;
            case CODE_MOVIE_REVIEW:
                new FetchMovieReviewTask(listener).execute(queryCategory);
                break;
        }
    }
}
