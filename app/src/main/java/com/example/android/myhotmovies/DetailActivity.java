package com.example.android.myhotmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myhotmovies.data.MovieDetail;
import com.example.android.myhotmovies.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding mBinding;

    private ImageView mPosterDisplay;
    private MovieDetail movieDetail;

    final String TRANS_DETAIL = "movie_detail";
    final String IMG_LOAD_PATH = "http://image.tmdb.org/t/p/w185/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        mPosterDisplay = findViewById(R.id.iv_movie_poster);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(TRANS_DETAIL)) {
                movieDetail = intent.getParcelableExtra(TRANS_DETAIL);
                mBinding.tvDisplayMovieTitle.setText(movieDetail.get_title());
                Picasso.with(this).load(Uri.parse(IMG_LOAD_PATH + movieDetail.getPosterPath())).into(mPosterDisplay);
                mBinding.tvDisplayMovieOverview.setText(movieDetail.getOverview());
                mBinding.tvDisplayVoteAverage.setText(movieDetail.getVoteAverage());
                mBinding.tvDisplayReleaseDate.setText(movieDetail.getReleaseDate());
            }
        }
    }
}
