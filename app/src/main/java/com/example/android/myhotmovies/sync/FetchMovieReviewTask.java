package com.example.android.myhotmovies.sync;

import android.os.AsyncTask;

import com.example.android.myhotmovies.data.MovieReview;
import com.example.android.myhotmovies.provider.DatabaseContract;
import com.example.android.myhotmovies.utilities.NetworkUtils;
import com.example.android.myhotmovies.utilities.OpenMovieJsonUtils;

import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xie on 2017/12/7.
 */

public class FetchMovieReviewTask extends AsyncTask<String, Void, ArrayList<MovieReview>> {

    private AsyncTaskCompleteListener mListener;

    public FetchMovieReviewTask(AsyncTaskCompleteListener listener) {
        mListener = listener;
    }

    @Override
    protected ArrayList<MovieReview> doInBackground(String... strings) {
        /*如果没有字符串数组传进来，说明没有要查询的，返回空*/
        if (strings.length == 0) {
            return null;
        }
        String movieId = strings[0];
        URL movieReviewRequestUrl = NetworkUtils.buildQueryReviewUrl(movieId);

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(movieReviewRequestUrl)
                .build();

        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String jsonMovieReviewResponse = response.body().string();

            return OpenMovieJsonUtils.getSimpleMovieReviewStringsFromJson(jsonMovieReviewResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<MovieReview> movieReviews) {
        mListener.onTaskComplete(movieReviews, DatabaseContract.TYPE_REVIEW);
    }
}
