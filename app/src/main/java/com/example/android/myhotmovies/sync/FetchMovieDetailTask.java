package com.example.android.myhotmovies.sync;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.example.android.myhotmovies.data.MovieDetail;
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
 * Created by xie on 2017/11/7.
 */

public class FetchMovieDetailTask extends AsyncTask<String, Void, ArrayList<MovieDetail>> {

    private AsyncTaskCompleteListener mListener;
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private String type;

    public FetchMovieDetailTask(Context context, AsyncTaskCompleteListener listener) {
        mContext = context;
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

        type = strings[0];
        URL movieRequestUrl = null;
        switch (type) {
            case DatabaseContract.TYPE_POPULAR:
                movieRequestUrl = NetworkUtils.buildQueryUrl(NetworkUtils.QUERY_POPULAR_MOVIES_URL);
                break;
            case DatabaseContract.TYPE_TOP_RATED:
                movieRequestUrl = NetworkUtils.buildQueryUrl(NetworkUtils.QUERY_TOP_RATED_MOVIES_URL);
                break;
        }

        if (movieRequestUrl != null) {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(movieRequestUrl)
                    .build();

            Call call = okHttpClient.newCall(request);

            try {
                Response response = call.execute();
                String jsonMovieResponse = response.body().string();

                return OpenMovieJsonUtils.getSimpleMovieStringsFromJson(mContext, jsonMovieResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieDetail> movieDetails) {
        mListener.onTaskComplete(movieDetails, type);
    }
}
