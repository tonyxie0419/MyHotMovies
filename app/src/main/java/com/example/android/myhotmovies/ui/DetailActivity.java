package com.example.android.myhotmovies.ui;

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

import com.example.android.myhotmovies.sync.AsyncTaskCompleteListener;
import com.example.android.myhotmovies.R;
import com.example.android.myhotmovies.adapter.MovieReviewAdapter;
import com.example.android.myhotmovies.adapter.MovieTrailerAdapter;
import com.example.android.myhotmovies.data.MovieDetail;
import com.example.android.myhotmovies.databinding.ActivityDetailBinding;
import com.example.android.myhotmovies.provider.DatabaseContract;
import com.example.android.myhotmovies.utilities.DatabaseUtils;
import com.example.android.myhotmovies.utilities.SyncTaskUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements AsyncTaskCompleteListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = DetailActivity.class.getSimpleName();

    ActivityDetailBinding mBinding;

    private MovieDetail movieDetail;
    private String movieId;
    private MovieDetail queryMovieDetail;

    private RecyclerView mTrailerRecyclerView;
    private MovieTrailerAdapter mMovieTrailerAdapter;

    private RecyclerView mReviewRecyclerView;
    private MovieReviewAdapter mMovieReviewAdapter;

    final String TRANS_DETAIL = "movie_detail";
    final String IMG_LOAD_PATH = "http://image.tmdb.org/t/p/w185/";

    public static boolean CHECKED_CHANGED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Intent intentWithMovieDetail = getIntent();
        if (intentWithMovieDetail != null) {
            if (intentWithMovieDetail.hasExtra(TRANS_DETAIL)) {
                //设置详情页
                movieDetail = intentWithMovieDetail.getParcelableExtra(TRANS_DETAIL);
                mBinding.tvDisplayMovieTitle.setText(movieDetail.getMovieTitle());
                Picasso.with(this).load(Uri.parse(IMG_LOAD_PATH + movieDetail.getPosterPath())).into(mBinding.ivMoviePoster);
                mBinding.overviewInfo.tvDisplayMovieOverview.setText(movieDetail.getOverview());
                mBinding.extraDetailInfo.tvDisplayVoteAverage.setText(movieDetail.getVoteAverage());
                mBinding.extraDetailInfo.tvDisplayReleaseDate.setText(movieDetail.getReleaseDate());

                //加载完成前不显示列表
                hideViews();

                movieId = movieDetail.getMovieId();
                Log.d(TAG, "movieId: " + movieId);
                queryMovieDetail = queryDataById(movieId, DatabaseContract.TYPE_FAVORITE);
                //从数据库中根据ID查询TYPE为TYPE_FAVORITE的电影
                if (queryMovieDetail != null) {
                    Log.d(TAG, "queryMovieDetail: " + queryMovieDetail.getMovieId());
                    mBinding.cbFavorite.setChecked(true);
                }
                mBinding.cbFavorite.setOnCheckedChangeListener(this);

                SyncTaskUtils.syncMovieTrailer(this, movieId);
                SyncTaskUtils.syncMovieReview(this, movieId);

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
            if (queryMovieDetail == null) {
                Log.d(TAG, "onCheckedChanged: saveMovieToDatabase");
                DatabaseUtils.saveMovieToDatabase(this, movieDetail, DatabaseContract.TYPE_FAVORITE);
            }
        } else {
            DatabaseUtils.deleteMovieByIdFromDatabase(this, movieId, DatabaseContract.TYPE_FAVORITE);
        }
        CHECKED_CHANGED = true;
    }

    @Override
    public void onTaskComplete(ArrayList result, String type) {
        switch (type) {
            case DatabaseContract.TYPE_TRAILER:
                if (result != null) {
                    mMovieTrailerAdapter.setMovieTrailersKeys(result);
                    mBinding.trailersInfo.rvMovieTrailers.setVisibility(View.VISIBLE);
                }
                break;
            case DatabaseContract.TYPE_REVIEW:
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

    private MovieDetail queryDataById(String movieId, String type) {
        return DatabaseUtils.queryMovieByIdFromDatabase(this, movieId, type);
    }
}
