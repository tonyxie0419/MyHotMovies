package com.example.android.myhotmovies.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.myhotmovies.AsyncTaskCompleteListener;
import com.example.android.myhotmovies.R;
import com.example.android.myhotmovies.data.MovieDetail;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by xie on 2017/11/7.
 */

public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<MovieDetail>> {

    private AsyncTaskCompleteListener mListener;

    public FetchMovieTask(AsyncTaskCompleteListener listener) {
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
        URL movieRequestUrl = NetworkUtils.buildUrl(queryCategory);

        try {
            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

            ArrayList<MovieDetail> mMovieDetails = OpenMovieJsonUtils.getSimpleMovieStringsFromJson(jsonMovieResponse);

            return mMovieDetails;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<MovieDetail> movieDetails) {
        mListener.onTaskComplete(movieDetails);
    }
}
