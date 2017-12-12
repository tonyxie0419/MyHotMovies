package com.example.android.myhotmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.example.android.myhotmovies.adapter.MovieReviewAdapter;
import com.example.android.myhotmovies.adapter.MovieTrailerAdapter;
import com.example.android.myhotmovies.data.MovieDetail;
import com.example.android.myhotmovies.databinding.ActivityDetailBinding;
import com.example.android.myhotmovies.utilities.SyncTaskUtils;
import com.squareup.picasso.Picasso;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements AsyncTaskCompleteListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = DetailActivity.class.getSimpleName();

    ActivityDetailBinding mBinding;

    private MovieDetail movieDetail;
    private String movieId;

    private RecyclerView mTrailerRecyclerView;
    private MovieTrailerAdapter mMovieTrailerAdapter;

    private RecyclerView mReviewRecyclerView;
    private MovieReviewAdapter mMovieReviewAdapter;

    final String TRANS_DETAIL = "movie_detail";
    final String IMG_LOAD_PATH = "http://image.tmdb.org/t/p/w185/";

    final String DATABASE_QUERY_CONDITIONS = "movieId = ?";
    public static boolean CHECKED_CHANGED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        mBinding.cbFavorite.setOnCheckedChangeListener(this);
        Intent intentWithMovieDetail = getIntent();
        if (intentWithMovieDetail != null) {
            if (intentWithMovieDetail.hasExtra(TRANS_DETAIL)) {
                //设置详情页
                movieDetail = intentWithMovieDetail.getParcelableExtra(TRANS_DETAIL);
                mBinding.tvDisplayMovieTitle.setText(movieDetail.get_title());
                Picasso.with(this).load(Uri.parse(IMG_LOAD_PATH + movieDetail.getPosterPath())).into(mBinding.ivMoviePoster);
                mBinding.overviewInfo.tvDisplayMovieOverview.setText(movieDetail.getOverview());
                mBinding.extraDetailInfo.tvDisplayVoteAverage.setText(movieDetail.getVoteAverage());
                mBinding.extraDetailInfo.tvDisplayReleaseDate.setText(movieDetail.getReleaseDate());

                //加载完成前不显示列表
                hideViews();

                movieId = movieDetail.getMovieId();
                if (queryDataById(movieId).size() != 0) {
                    mBinding.cbFavorite.setChecked(true);
                }
                SyncTaskUtils.syncMovie(this, movieId, SyncTaskUtils.CODE_MOVIE_TRAILER);
                SyncTaskUtils.syncMovie(this, movieId, SyncTaskUtils.CODE_MOVIE_REVIEW);

                //设置预告片
                mTrailerRecyclerView = findViewById(R.id.rv_movie_trailers);
                GridLayoutManager trailerLayoutManager = new GridLayoutManager(this, 2);
                mTrailerRecyclerView.setLayoutManager(trailerLayoutManager);
                mMovieTrailerAdapter = new MovieTrailerAdapter(this);
                mTrailerRecyclerView.setHasFixedSize(true);
                mTrailerRecyclerView.setAdapter(mMovieTrailerAdapter);

                //设置评论
                mReviewRecyclerView = findViewById(R.id.rv_movie_reviews);
                LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this);
                mReviewRecyclerView.setLayoutManager(reviewLayoutManager);
                mMovieReviewAdapter = new MovieReviewAdapter();
                mReviewRecyclerView.setHasFixedSize(true);
                mReviewRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
                mReviewRecyclerView.setAdapter(mMovieReviewAdapter);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (queryDataById(movieId).size() == 0) {
                movieDetail.save();
            }
        } else {
            DataSupport.deleteAll(MovieDetail.class, DATABASE_QUERY_CONDITIONS, movieId);
            movieDetail.clearSavedState();
        }
        CHECKED_CHANGED = true;
    }

    @Override
    public void onTaskComplete(ArrayList result, int requestCode) {
        switch (requestCode) {
            case SyncTaskUtils.CODE_MOVIE_TRAILER:
                if (result != null) {
                    mMovieTrailerAdapter.setMovieTrailersKeys(result);
                    mBinding.trailersInfo.rvMovieTrailers.setVisibility(View.VISIBLE);
                }
                break;
            case SyncTaskUtils.CODE_MOVIE_REVIEW:
                if (result != null) {
                    mMovieReviewAdapter.setMovieReviews(result);
                    mBinding.reviewsInfo.rvMovieReviews.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void hideViews() {
        mBinding.trailersInfo.rvMovieTrailers.setVisibility(View.INVISIBLE);
        mBinding.reviewsInfo.rvMovieReviews.setVisibility(View.INVISIBLE);
    }

    private List<MovieDetail> queryDataById(String movieId) {
        return DataSupport.where(DATABASE_QUERY_CONDITIONS, movieId).find(MovieDetail.class);
    }
}
