package com.example.android.myhotmovies;

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

import com.example.android.myhotmovies.utilities.FetchMovieTask;
import com.example.android.myhotmovies.utilities.NetworkUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AsyncTaskCompleteListener {

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
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(layoutManager);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieAdapter);

        loadMovieData(TYPE_DEFAULT);
    }

    /**
     * 通知AsyncTask获取电影数据
     */
    private void loadMovieData(String type) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
//        showMovieDataView();
        switch (type) {
            case TYPE_POPULAR:
                new FetchMovieTask(this).execute(NetworkUtils.QUERY_POPULAR_MOVIES_URL);
                break;
            case TYPE_TOP_RATED:
                new FetchMovieTask(this).execute(NetworkUtils.QUERY_TOP_RATED_MOVIES_URL);
                break;
            default:
                new FetchMovieTask(this).execute(NetworkUtils.QUERY_POPULAR_MOVIES_URL);
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

    @Override
    public void onTaskComplete(ArrayList result) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (result != null) {
            showMovieDataView();
            mMovieAdapter.setMovieData(result);
        } else {
            showErrorMessage();
        }
    }
}
