package com.example.android.myhotmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.myhotmovies.data.MovieDetail;
import com.example.android.myhotmovies.utilities.NetworkUtils;
import com.example.android.myhotmovies.utilities.OpenMovieJsonUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private final String TYPE_POPULAR = "popular";
    private final String TYPE_TOP_RATED = "top_rated";
    private final String TYPE_DEFAULT = TYPE_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.rv_movies);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(layoutManager);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieAdapter);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        loadMovieData(TYPE_DEFAULT);
    }

    /**
     * 通知AsyncTask获取电影数据
     */
    private void loadMovieData(String type) {
        showMovieDataView();
        switch (type) {
            case TYPE_POPULAR:
                new FetchMovieTask().execute(NetworkUtils.QUERY_POPULAR_MOVIES_URL);
                break;
            case TYPE_TOP_RATED:
                new FetchMovieTask().execute(NetworkUtils.QUERY_TOP_RATED_MOVIES_URL);
                break;
            default:
                new FetchMovieTask().execute(NetworkUtils.QUERY_POPULAR_MOVIES_URL);
        }
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<MovieDetail>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
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
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieDetails != null) {
                showMovieDataView();
                mMovieAdapter.setMovieData(movieDetails);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_popular_refresh:
                mMovieAdapter.setMovieData(null);
                loadMovieData(TYPE_POPULAR);
                return true;
            case R.id.action_top_rated_refresh:
                mMovieAdapter.setMovieData(null);
                loadMovieData(TYPE_TOP_RATED);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
