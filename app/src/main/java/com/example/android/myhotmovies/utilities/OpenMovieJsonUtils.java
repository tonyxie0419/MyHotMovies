package com.example.android.myhotmovies.utilities;


import android.content.Context;
import com.example.android.myhotmovies.data.MovieDetail;
import com.example.android.myhotmovies.data.MovieReview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by xie on 2017/11/3.
 */

public class OpenMovieJsonUtils {

    private static final String TAG = OpenMovieJsonUtils.class.getSimpleName();

    private static final String QUERY_MOVIE_ID = "id";
    private static final String QUERY_POSTER_PATH = "poster_path";
    private static final String QUERY_VOTE_AVERAGE = "vote_average";
    private static final String QUERY_MOVIE_TITLE = "title";
    private static final String QUERY_OVERVIEW = "overview";
    private static final String QUERY_RELEASE_DATE = "release_date";

    public static ArrayList<MovieDetail> getSimpleMovieStringsFromJson(Context context, String movieJsonStr) throws JSONException {

        final String QUERY_RESULTS = "results";

        String movieId;
        String posterPath;
        String voteAverage;
        String overview;
        String releaseDate;
        String movieTitle;

        ArrayList<MovieDetail> mMovieDetailList = new ArrayList();

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray(QUERY_RESULTS);

        for (int i = 0; i < movieArray.length(); i++) {
            MovieDetail movieDetail = new MovieDetail();
            JSONObject movieData = movieArray.getJSONObject(i);
            movieId = movieData.getString(QUERY_MOVIE_ID);
            posterPath = movieData.getString(QUERY_POSTER_PATH);
            voteAverage = movieData.getString(QUERY_VOTE_AVERAGE);
            overview = movieData.getString(QUERY_OVERVIEW);
            releaseDate = movieData.getString(QUERY_RELEASE_DATE);
            movieTitle = movieData.getString(QUERY_MOVIE_TITLE);
            movieDetail.setMovieId(movieId);
            movieDetail.setMovieTitle(movieTitle);
            movieDetail.setOverview(overview);
            movieDetail.setPosterPath(posterPath);
            movieDetail.setReleaseDate(releaseDate);
            movieDetail.setVoteAverage(voteAverage);
            mMovieDetailList.add(movieDetail);
        }
        return mMovieDetailList;
    }

    public static ArrayList<String> getSimpleMovieTrailersKeyStringsFromJson(String movieTrailersKeysJsonStr) throws JSONException {

        final String QUERY_RESULTS = "results";
        final String QUERY_TRAILERS_KEY = "key";

        ArrayList<String> mMovieTrailersKeys = new ArrayList<>();

        JSONObject movieTrailersKeysJson = new JSONObject(movieTrailersKeysJsonStr);

        JSONArray movieTrailersKeysArray = movieTrailersKeysJson.getJSONArray(QUERY_RESULTS);

        for (int i = 0; i < movieTrailersKeysArray.length(); i++) {
            String trailersKey;
            JSONObject movieTrailersData = movieTrailersKeysArray.getJSONObject(i);
            trailersKey = movieTrailersData.getString(QUERY_TRAILERS_KEY);
            mMovieTrailersKeys.add(trailersKey);
        }
        return mMovieTrailersKeys;
    }

    public static ArrayList<MovieReview> getSimpleMovieReviewStringsFromJson(String movieReviewsJsonStr) throws JSONException {

        final String QUERY_RESULTS = "results";
        final String QUERY_AUTHOR = "author";
        final String QUERY_CONTENT = "content";

        ArrayList<MovieReview> mMovieReviews = new ArrayList<>();

        JSONObject movieReviewsJson = new JSONObject(movieReviewsJsonStr);

        JSONArray movieReviewsArray = movieReviewsJson.getJSONArray(QUERY_RESULTS);

        for (int i = 0; i < movieReviewsArray.length(); i++) {
            String author;
            String content;
            MovieReview movieReview = new MovieReview();
            JSONObject movieReviewsData = movieReviewsArray.getJSONObject(i);
            author = movieReviewsData.getString(QUERY_AUTHOR);
            content = movieReviewsData.getString(QUERY_CONTENT);
            movieReview.setAuthor(author);
            movieReview.setContent(content);
            mMovieReviews.add(movieReview);
        }
        return mMovieReviews;
    }
}
