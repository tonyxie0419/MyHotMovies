package com.example.android.myhotmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myhotmovies.R;
import com.example.android.myhotmovies.utilities.NetworkUtils;

import java.util.ArrayList;

/**
 * Created by xie on 2017/12/5.
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailersAdapterViewHolder> {

    private ArrayList<String> mMovieTrailersKeys;
    private Context mContext;

    public MovieTrailerAdapter(Context context) {
        mContext = context;
    }

    public class MovieTrailersAdapterViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mPlayTrailerImageView;
        public final TextView mTrailerTextView;


        public MovieTrailersAdapterViewHolder(View itemView) {
            super(itemView);
            mPlayTrailerImageView = itemView.findViewById(R.id.iv_play_trailer);
            mTrailerTextView = itemView.findViewById(R.id.tv_display_trailer_name);
        }
    }

    @Override
    public MovieTrailerAdapter.MovieTrailersAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.movie_trailer_list_item, parent, false);
        return new MovieTrailersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailerAdapter.MovieTrailersAdapterViewHolder holder, final int position) {
        holder.mPlayTrailerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri destinationUri = NetworkUtils.buildYoutubeUri(mMovieTrailersKeys.get(position));

                Intent intent = new Intent(Intent.ACTION_VIEW, destinationUri);
                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intent);
                }
            }
        });
        holder.mTrailerTextView.setText(mContext.getString(R.string.trailers_text) + " " + (position + 1));
    }

    @Override
    public int getItemCount() {
        if (null == mMovieTrailersKeys) return 0;
        return mMovieTrailersKeys.size();
    }

    public void setMovieTrailersKeys(ArrayList<String> movieTrailersKeys) {
        mMovieTrailersKeys = movieTrailersKeys;
        notifyDataSetChanged();
    }
}
