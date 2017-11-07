package com.example.android.myhotmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.android.myhotmovies.MyApiKey;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by xie on 2017/11/2.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static final String QUERY_POPULAR_MOVIES_URL = "http://api.themoviedb.org/3/movie/popular";

    public static final String QUERY_TOP_RATED_MOVIES_URL = "https://api.themoviedb.org/3/movie/top_rated";

    final static String QUERY_API_KEY = "api_key";

    public static URL buildUrl(String queryCategory) {
        Uri builtUri = Uri.parse(queryCategory).buildUpon()
                .appendQueryParameter(QUERY_API_KEY, MyApiKey.API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "buildUrl: " + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(5000);
        urlConnection.setReadTimeout(10000);
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
