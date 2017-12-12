package com.example.android.myhotmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.myhotmovies.R;
import com.example.android.myhotmovies.data.MovieReview;

import java.util.ArrayList;

/**
 * Created by xie on 2017/12/6.
 */

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewAdapterViewHolder> {

    private ArrayList<MovieReview> mMovieReviews;

    public class MovieReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mReviewAuthorTextView;
        public final TextView mReviewContentTextView;

        public MovieReviewAdapterViewHolder(View itemView) {
            super(itemView);
            mReviewAuthorTextView = itemView.findViewById(R.id.tv_display_review_author);
            mReviewContentTextView = itemView.findViewById(R.id.tv_display_review_content);
        }
    }

    @Override
    public MovieReviewAdapter.MovieReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.movie_review_list_item, parent, false);
        return new MovieReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewAdapter.MovieReviewAdapterViewHolder holder, int position) {
        holder.mReviewAuthorTextView.setText(mMovieReviews.get(position).getAuthor());
        holder.mReviewContentTextView.setText(mMovieReviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        if (null == mMovieReviews) return 0;
        return mMovieReviews.size();
    }

    public void setMovieReviews(ArrayList<MovieReview> movieReviews) {
        mMovieReviews = movieReviews;
        notifyDataSetChanged();
    }
}
