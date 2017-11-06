package com.example.android.myhotmovies.utilities;

import com.example.android.myhotmovies.data.MovieDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xie on 2017/11/3.
 */

public class OpenMovieJsonUtils {

    private static final String TAG = OpenMovieJsonUtils.class.getSimpleName();

    public static ArrayList<MovieDetail> getSimpleMovieStringsFromJson(String movieJsonStr) throws JSONException {

        final String QUERY_RESULTS = "results";
        final String QUERY_ID = "id";
        final String QUERY_POSTER_PATH = "poster_path";
        final String QUERY_VOTE_AVERAGE = "vote_average";
        final String QUERY_TITLE = "title";
        final String QUERY_OVERVIEW = "overview";
        final String QUERY_RELEASE_DATE = "release_date";

        ArrayList<MovieDetail> mMovieDetailList = new ArrayList();

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray(QUERY_RESULTS);

        for (int i = 0; i < movieArray.length(); i++) {
            //TODO id以后有用
            MovieDetail movieDetail = new MovieDetail();
            String id;
            String posterPath;
            String voteAverage;
            String overview;
            String releaseDate;
            String title;
            JSONObject movieData = movieArray.getJSONObject(i);
            id = movieData.getString(QUERY_ID);
            posterPath = movieData.getString(QUERY_POSTER_PATH);
            voteAverage = movieData.getString(QUERY_VOTE_AVERAGE);
            overview = movieData.getString(QUERY_OVERVIEW);
            releaseDate = movieData.getString(QUERY_RELEASE_DATE);
            title = movieData.getString(QUERY_TITLE);
            movieDetail.set_id(id);
            movieDetail.set_title(title);
            movieDetail.setOverview(overview);
            movieDetail.setPosterPath(posterPath);
            movieDetail.setReleaseDate(releaseDate);
            movieDetail.setVoteAverage(voteAverage);
            mMovieDetailList.add(movieDetail);
        }
        return mMovieDetailList;
    }
}
