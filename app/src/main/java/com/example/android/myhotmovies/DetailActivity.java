package com.example.android.myhotmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myhotmovies.data.MovieDetail;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private TextView mTitleDisplay;
    private ImageView mPosterDisplay;
    private TextView mOverviewDisplay;
    private TextView mVoteAverageDisplay;
    private TextView mReleaseDateDisplay;
    private MovieDetail movieDetail;

    final String TRANS_DETAIL = "movie_detail";
    final String IMG_LOAD_PATH = "http://image.tmdb.org/t/p/w185/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mTitleDisplay = findViewById(R.id.tv_display_movie_title);
        mPosterDisplay = findViewById(R.id.iv_movie_poster);
        mOverviewDisplay = findViewById(R.id.tv_display_movie_overview);
        mVoteAverageDisplay = findViewById(R.id.tv_display_vote_average);
        mReleaseDateDisplay = findViewById(R.id.tv_display_release_date);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(TRANS_DETAIL)) {
                movieDetail = (MovieDetail) intent.getSerializableExtra(TRANS_DETAIL);
                mTitleDisplay.setText(movieDetail.get_title());
                Picasso.with(this).load(Uri.parse(IMG_LOAD_PATH + movieDetail.getPosterPath())).into(mPosterDisplay);
                mOverviewDisplay.setText(movieDetail.getOverview());
                mVoteAverageDisplay.setText(movieDetail.getVoteAverage());
                mReleaseDateDisplay.setText(movieDetail.getReleaseDate());
            }
        }
    }
}
