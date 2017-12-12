package com.example.android.myhotmovies;

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

import com.example.android.myhotmovies.adapter.MovieAdapter;
import com.example.android.myhotmovies.data.MovieDetail;
import com.example.android.myhotmovies.utilities.NetworkUtils;
import com.example.android.myhotmovies.utilities.SyncTaskUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

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

    public static final String TYPE_POPULAR = "popular";
    public static final String TYPE_TOP_RATED = "top_rated";
    public static final String TYPE_DEFAULT = TYPE_POPULAR;
    public static final String TYPE_FAVORITE = "favorite";

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
        String type = sharedPreferences.getString("now", "default");
        switch (type) {
            case TYPE_POPULAR:
                loadMovieData(TYPE_POPULAR);
                break;
            case TYPE_TOP_RATED:
                loadMovieData(TYPE_TOP_RATED);
                break;
            case TYPE_FAVORITE:
                loadMovieData(TYPE_FAVORITE);
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
            case TYPE_POPULAR:
                mNoFavoriteMoviesDisplay.setVisibility(View.INVISIBLE);
                SyncTaskUtils.syncMovie(this, NetworkUtils.QUERY_POPULAR_MOVIES_URL, SyncTaskUtils.CODE_MOVIE_DETAIL);
                setTitle(R.string.popular_movies);
                break;
            case TYPE_TOP_RATED:
                mNoFavoriteMoviesDisplay.setVisibility(View.INVISIBLE);
                SyncTaskUtils.syncMovie(this, NetworkUtils.QUERY_TOP_RATED_MOVIES_URL, SyncTaskUtils.CODE_MOVIE_DETAIL);
                setTitle(R.string.rated_movies);
                break;
            case TYPE_FAVORITE:
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                List<MovieDetail> favoriteMovies = DataSupport.findAll(MovieDetail.class);
                if (favoriteMovies.size() == 0) {
                    mNoFavoriteMoviesDisplay.setVisibility(View.VISIBLE);
                    mMovieAdapter.setMovieData(null);
                    mMovieAdapter.notifyDataSetChanged();
                } else {
                    mNoFavoriteMoviesDisplay.setVisibility(View.INVISIBLE);
                    ArrayList<MovieDetail> arrFavoriteMovies = new ArrayList<>();
                    arrFavoriteMovies.addAll(favoriteMovies);
                    mMovieAdapter.setMovieData(arrFavoriteMovies);
                    mMovieAdapter.notifyDataSetChanged();
                }
                setTitle(R.string.favorite_movie);
                break;
            default:
                mNoFavoriteMoviesDisplay.setVisibility(View.INVISIBLE);
                SyncTaskUtils.syncMovie(this, NetworkUtils.QUERY_POPULAR_MOVIES_URL, SyncTaskUtils.CODE_MOVIE_DETAIL);
                setTitle(R.string.popular_movies);
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
                mMovieAdapter.setMovieData(null);
                loadMovieData(TYPE_POPULAR);
                editor.putString("now", TYPE_POPULAR);
                editor.apply();
                return true;
            case R.id.action_top_rated_refresh:
                mMovieAdapter.setMovieData(null);
                loadMovieData(TYPE_TOP_RATED);
                editor.putString("now", TYPE_TOP_RATED);
                editor.apply();
                return true;
            case R.id.action_favorite_movie:
                mMovieAdapter.setMovieData(null);
                loadMovieData(TYPE_FAVORITE);
                editor.putString("now", TYPE_FAVORITE);
                editor.apply();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(ArrayList result, int requestCode) {
        if (requestCode == SyncTaskUtils.CODE_MOVIE_DETAIL) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (result != null) {
                showMovieDataView();
                mMovieAdapter.setMovieData(result);
                scrollToPosition();
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果在电影详情页修改了收藏状态，并且主页面在收藏电影页面，刷新数据并显示
        if (DetailActivity.CHECKED_CHANGED && TextUtils.equals(getTitle(), getResources().getString(R.string.favorite_movie))) {
            List<MovieDetail> favoriteMovies = DataSupport.findAll(MovieDetail.class);
            if (favoriteMovies.size() == 0) {
                mNoFavoriteMoviesDisplay.setVisibility(View.VISIBLE);
                mMovieAdapter.setMovieData(null);
                mMovieAdapter.notifyDataSetChanged();
            } else {
                mNoFavoriteMoviesDisplay.setVisibility(View.INVISIBLE);
                ArrayList<MovieDetail> arrFavoriteMovies = new ArrayList<>();
                arrFavoriteMovies.addAll(favoriteMovies);
                mMovieAdapter.setMovieData(arrFavoriteMovies);
                mMovieAdapter.notifyDataSetChanged();
            }
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
