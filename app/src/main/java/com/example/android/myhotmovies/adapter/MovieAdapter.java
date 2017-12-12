package com.example.android.myhotmovies.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.myhotmovies.DetailActivity;
import com.example.android.myhotmovies.R;
import com.example.android.myhotmovies.data.MovieDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by xie on 2017/11/2.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private ArrayList<MovieDetail> mMovieDetails;
    private Context mContext;
    final String IMG_LOAD_PATH = "http://image.tmdb.org/t/p/w500/";
    final String TRANS_DETAIL = "movie_detail";

    public MovieAdapter(Context context) {
        mContext = context;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMovieImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieImageView = itemView.findViewById(R.id.iv_movies_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Class destinationClass = DetailActivity.class;
            MovieDetail movieDetail = mMovieDetails.get(adapterPosition);

            Intent intent = new Intent(mContext, destinationClass);
            intent.putExtra(TRANS_DETAIL, movieDetail);
            mContext.startActivity(intent);
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Picasso.with(mContext)
                .load(Uri.parse(IMG_LOAD_PATH + mMovieDetails.get(position).getPosterPath()))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.mMovieImageView);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieDetails) return 0;
        return mMovieDetails.size();
    }

    /**
     * 创建好adapter以后，设置电影相关数据
     *
     * @param movieDetails 用来存放查询获得的电影数据
     */
    public void setMovieData(ArrayList<MovieDetail> movieDetails) {
        mMovieDetails = movieDetails;
        notifyDataSetChanged();
    }
}
