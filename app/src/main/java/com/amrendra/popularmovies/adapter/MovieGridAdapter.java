package com.amrendra.popularmovies.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amrendra.popularmovies.R;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.Movie;
import com.amrendra.popularmovies.utils.MoviesConstants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Amrendra Kumar on 28/11/15.
 */
public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.ViewHolder> {

    private List<Movie> movieList = new ArrayList<>();

    private int defaultColor;
    private Context mContext;

    public interface OnMovieViewClickListener {
        void onClickMovieThumbnail(Movie movie, Bitmap bitmap);
    }

    private OnMovieViewClickListener onMovieViewClickListener;


    public MovieGridAdapter(@NonNull List<Movie> movieList, int defaultColor, @NonNull Context context,
                            OnMovieViewClickListener listener) {
        this.movieList = movieList;
        this.defaultColor = defaultColor;
        this.mContext = context;
        this.onMovieViewClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.moviegrid_column_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Movie movie = movieList.get(position);

        String imageUrl = MoviesConstants.API_IMAGE_BASE_URL + MoviesConstants.IMAGE_SIZE_SMALL +
                movie.posterPath;
        Glide.with(mContext)
                .load(imageUrl).asBitmap()
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.place_holder)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        movie.setLoaded(false);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        movie.setLoaded(true);
                        return false;
                    }
                })
                .into(holder.gridMoviePosterImage);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void resetMovieList(@NonNull List<Movie> data) {
        movieList = data;
        notifyDataSetChanged();
    }

    @NonNull
    public List<Movie> getItemList() {
        return movieList;
    }

    public void addMovies(@NonNull List<Movie> data) {
        if (!data.isEmpty()) {
            int currentSize = movieList.size();
            int added = data.size();
            movieList.addAll(data);
            notifyItemRangeInserted(currentSize, added);
        }

    }

    public Movie firstMovie(){
        if (!movieList.isEmpty()) {
            return movieList.get(0);
        }
        return null;
    }

    public void clearMovies() {
        if (!movieList.isEmpty()) {
            movieList.clear();
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.grid_item_movie_poster_image)
        ImageView gridMoviePosterImage;

        @Bind(R.id.grid_item_container)
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Debug.e("OnClick : " + pos, false);
                    Movie movie = movieList.get(pos);
                    Bitmap posterBitmap = null;
                    if (movie.isLoaded()) {
                        posterBitmap = ((BitmapDrawable) gridMoviePosterImage.getDrawable()).getBitmap();
                    }
                    onMovieViewClickListener.onClickMovieThumbnail(movieList.get(pos),
                            posterBitmap);

                }
            });
        }
    }
}
