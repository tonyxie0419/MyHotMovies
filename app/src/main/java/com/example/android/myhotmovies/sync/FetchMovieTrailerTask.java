package com.example.android.myhotmovies.sync;

import android.os.AsyncTask;

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
 * Created by xie on 2017/12/5.
 */

public class FetchMovieTrailerTask extends AsyncTask<String, Void, ArrayList<String>> {

    private AsyncTaskCompleteListener mListener;

    public FetchMovieTrailerTask(AsyncTaskCompleteListener listener) {
        mListener = listener;
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        /*如果没有字符串数组传进来，说明没有要查询的，返回空*/
        if (strings.length == 0) {
            return null;
        }
        String movieId = strings[0];
        URL movieTrailerKeyRequestUrl = NetworkUtils.buildQueryTrailerKeyUrl(movieId);

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(movieTrailerKeyRequestUrl)
                .build();

        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String jsonMovieTrailerResponse = response.body().string();

            return OpenMovieJsonUtils.getSimpleMovieTrailersKeyStringsFromJson(jsonMovieTrailerResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        mListener.onTaskComplete(result, DatabaseContract.TYPE_TRAILER);
    }
}
