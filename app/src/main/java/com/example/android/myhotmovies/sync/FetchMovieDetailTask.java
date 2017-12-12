package com.example.android.myhotmovies.sync;

import android.os.AsyncTask;

import com.example.android.myhotmovies.AsyncTaskCompleteListener;
import com.example.android.myhotmovies.data.MovieDetail;
import com.example.android.myhotmovies.utilities.NetworkUtils;
import com.example.android.myhotmovies.utilities.OpenMovieJsonUtils;
import com.example.android.myhotmovies.utilities.SyncTaskUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xie on 2017/11/7.
 */

public class FetchMovieDetailTask extends AsyncTask<String, Void, ArrayList<MovieDetail>> {

    private AsyncTaskCompleteListener mListener;

    public FetchMovieDetailTask(AsyncTaskCompleteListener listener) {
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<MovieDetail> doInBackground(String... strings) {

        /*如果没有字符串数组传进来，说明没有要查询的，返回空*/
        if (strings.length == 0) {
            return null;
        }

        String queryCategory = strings[0];
        URL movieRequestUrl = NetworkUtils.buildQueryUrl(queryCategory);

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(movieRequestUrl)
                .build();

        Call call = okHttpClient.newCall(request);

        try {
            Response response = call.execute();
            String jsonMovieResponse = response.body().string();

            return OpenMovieJsonUtils.getSimpleMovieStringsFromJson(jsonMovieResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<MovieDetail> movieDetails) {
        mListener.onTaskComplete(movieDetails, SyncTaskUtils.CODE_MOVIE_DETAIL);
    }
}
