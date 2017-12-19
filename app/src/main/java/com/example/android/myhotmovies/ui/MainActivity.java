package com.example.android.myhotmovies.ui;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.myhotmovies.sync.AsyncTaskCompleteListener;
import com.example.android.myhotmovies.R;
import com.example.android.myhotmovies.adapter.MovieAdapter;
import com.example.android.myhotmovies.data.MovieDetail;
import com.example.android.myhotmovies.provider.DatabaseContract;
import com.example.android.myhotmovies.utilities.DatabaseUtils;
import com.example.android.myhotmovies.utilities.NetworkUtils;
import com.example.android.myhotmovies.utilities.SyncTaskUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AsyncTaskCompleteListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private GridLayoutManager layoutManager;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private TextView mNoFavoriteMoviesDisplay;

    private int lastOffset = 0;
    private int lastPosition = 0;

    public static final String TYPE_DEFAULT = DatabaseContract.TYPE_POPULAR;

    public static final String PREF_KEY_NOW = "now";
    public static final String PREF_VALUE_DEFAULT = "default";

    final String SAVED_LAST_POSITION = "saved_last_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movies);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mNoFavoriteMoviesDisplay = findViewById(R.id.tv_no_favorite_movies_display);

        //横屏一行显示3个图片，竖屏一行显示2个图片
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(this, 3);
        } else {
            layoutManager = new GridLayoutManager(this, 2);
        }
        mRecyclerView.setLayoutManager(layoutManager);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieAdapter);
        if (savedInstanceState != null) {
            lastPosition = savedInstanceState.getInt(SAVED_LAST_POSITION);
        }
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                getPositionAndOffset();
            }
        });
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String type = sharedPreferences.getString(PREF_KEY_NOW, PREF_VALUE_DEFAULT);
        switch (type) {
            case DatabaseContract.TYPE_POPULAR:
                loadMovieData(DatabaseContract.TYPE_POPULAR);
                break;
            case DatabaseContract.TYPE_TOP_RATED:
                loadMovieData(DatabaseContract.TYPE_TOP_RATED);
                break;
            case DatabaseContract.TYPE_FAVORITE:
                loadMovieData(DatabaseContract.TYPE_FAVORITE);
                break;
            default:
                loadMovieData(TYPE_DEFAULT);
                break;
        }
    }

    /**
     * 记录RecyclerView当前位置
     */
    private void getPositionAndOffset() {
        GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        //获取可视的第一个view
        View topView = layoutManager.getChildAt(0);
        if (topView != null) {
            //获取与该view的顶部的偏移量
            lastOffset = topView.getTop();
            //得到该View的数组位置
            lastPosition = layoutManager.getPosition(topView);
        }
    }

    /**
     * 让RecyclerView滚动到指定位置
     */
    private void scrollToPosition() {
        if (mRecyclerView.getLayoutManager() != null && lastPosition >= 0) {
            ((GridLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(lastPosition, lastOffset);
        }
    }

    /**
     * 通知AsyncTask获取电影数据
     */
    private void loadMovieData(String type) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        switch (type) {
            case DatabaseContract.TYPE_POPULAR:
                if (NetworkUtils.isNetworkConnected(this)) {
                    mNoFavoriteMoviesDisplay.setVisibility(View.INVISIBLE);
                    SyncTaskUtils.syncMovieDetail(this, this, type);
                } else {
                    ArrayList<MovieDetail> popularMovies =
                            DatabaseUtils.queryAllMovieFromDatabase(
                                    this, DatabaseContract.TYPE_POPULAR);
                    mMovieAdapter.setMovieData(popularMovies, DatabaseContract.TYPE_POPULAR);
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                    Log.d(TAG, "无网络");
                }
                setTitle(R.string.popular_movies);
                break;
            case DatabaseContract.TYPE_TOP_RATED:
                if (NetworkUtils.isNetworkConnected(this)) {
                    mNoFavoriteMoviesDisplay.setVisibility(View.INVISIBLE);
                    SyncTaskUtils.syncMovieDetail(this, this, type);
                } else {
                    ArrayList<MovieDetail> topRatedMovies =
                            DatabaseUtils.queryAllMovieFromDatabase(
                                    this, DatabaseContract.TYPE_TOP_RATED);
                    mMovieAdapter.setMovieData(topRatedMovies, DatabaseContract.TYPE_TOP_RATED);
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                    Log.d(TAG, "无网络");
                }
                setTitle(R.string.rated_movies);
                break;
            case DatabaseContract.TYPE_FAVORITE:
                Log.d(TAG, "loadMovieData: " + type);
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                ArrayList<MovieDetail> favoriteMovies = DatabaseUtils.queryAllMovieFromDatabase(
                        this, DatabaseContract.TYPE_FAVORITE);
                if (favoriteMovies != null) {
                    if (favoriteMovies.size() == 0) {
                        mNoFavoriteMoviesDisplay.setVisibility(View.VISIBLE);
                        mMovieAdapter.setMovieData(null, null);
                        mMovieAdapter.notifyDataSetChanged();
                    } else {
                        mNoFavoriteMoviesDisplay.setVisibility(View.INVISIBLE);
                        mMovieAdapter.setMovieData(favoriteMovies, type);
                        mMovieAdapter.notifyDataSetChanged();
                    }
                }
                setTitle(R.string.favorite_movie);
                break;
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (id) {
            case R.id.action_popular_refresh:
                mMovieAdapter.setMovieData(null, null);
                loadMovieData(DatabaseContract.TYPE_POPULAR);
                editor.putString(PREF_KEY_NOW, DatabaseContract.TYPE_POPULAR);
                editor.apply();
                return true;
            case R.id.action_top_rated_refresh:
                mMovieAdapter.setMovieData(null, null);
                loadMovieData(DatabaseContract.TYPE_TOP_RATED);
                editor.putString(PREF_KEY_NOW, DatabaseContract.TYPE_TOP_RATED);
                editor.apply();
                return true;
            case R.id.action_favorite_movie:
                mMovieAdapter.setMovieData(null, null);
                loadMovieData(DatabaseContract.TYPE_FAVORITE);
                editor.putString(PREF_KEY_NOW, DatabaseContract.TYPE_FAVORITE);
                editor.apply();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(ArrayList result, String type) {
        Log.d(TAG, "onTaskComplete: " + type);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (result != null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String prefType = sharedPreferences.getString(PREF_KEY_NOW, PREF_VALUE_DEFAULT);
            showMovieDataView();
            mMovieAdapter.setMovieData(result, prefType);
            scrollToPosition();
            for (Object movieDetail : result) {
                DatabaseUtils.saveMovieToDatabase(this, (MovieDetail) movieDetail, type);
            }
            Log.d(TAG, "onTaskComplete: " + DatabaseUtils.queryAllMovieFromDatabase(this, type).size());
        } else {
            showErrorMessage();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果在电影详情页修改了收藏状态，并且主页面在收藏电影页面，刷新数据并显示
        if (DetailActivity.CHECKED_CHANGED && TextUtils.equals(getTitle(), getResources().getString(R.string.favorite_movie))) {
            Log.d(TAG, "onResume: " + DetailActivity.CHECKED_CHANGED);
            loadMovieData(DatabaseContract.TYPE_FAVORITE);
            DetailActivity.CHECKED_CHANGED = false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int savedLastPosition = lastPosition;
        outState.putInt(SAVED_LAST_POSITION, savedLastPosition);
    }
}
