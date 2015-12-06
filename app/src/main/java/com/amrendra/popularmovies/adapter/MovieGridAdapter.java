package com.amrendra.popularmovies.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.amrendra.popularmovies.R;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.Movie;
import com.amrendra.popularmovies.utils.MoviesConstants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
        void onClickMovieThumbnail(Movie movie, Bitmap bitmap, View view);
    }

    private OnMovieViewClickListener onMovieViewClickListener;


    public MovieGridAdapter(List<Movie> movieList, int defaultColor, Context context,
                            OnMovieViewClickListener listener) {
        this.movieList = movieList;
        this.defaultColor = defaultColor;
        this.mContext = context;
        this.onMovieViewClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Debug.c();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.moviegrid_column_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Debug.c();
        final Movie movie = movieList.get(position);
        //holder.gridMovieNameTv.setText(movie.title);
        String imageUrl = MoviesConstants.API_IMAGE_BASE_URL + MoviesConstants.IMAGE_SIZE_SMALL +
                movie.posterPath;
        holder.ratingBar.setRating(((float) movie.averageVote) / 2.0f);
/*        LayerDrawable bgDrawable = (LayerDrawable) holder.gridMovieNameTv.getBackground();
        final GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.shape_id);*/

        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.place_holder)
                .into(holder.gridMoviePosterImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap posterBitmap = ((BitmapDrawable) holder.gridMoviePosterImage.getDrawable()).getBitmap();
                        Palette.from(posterBitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                Palette.Swatch vibrant = palette.getVibrantSwatch();
                                if (vibrant != null) {
                                    // following code causing flicker, need to fix it.
/*                                    shape.setColor(vibrant.getRgb());
                                    shape.setAlpha(210);
                                    holder.gridMovieNameTv.setTextColor(vibrant.getTitleTextColor());*/
/*                                    shape.setColor(palette.getDarkMutedColor(ContextCompat
                                            .getColor(mContext, R.color.colorPrimaryTransparentNav)));
                                    shape.setAlpha(190);
                                    holder.gridMovieNameTv.setTextColor(palette.getLightVibrantColor(ContextCompat
                                            .getColor(mContext, R.color.white)));*/
                                }
                            }
                        });
                    }

                    @Override
                    public void onError() {
                    }
                });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Debug.showToastShort("" + movie.title + " clicked", holder.gridMoviePosterImage.getContext());
                Bitmap posterBitmap = ((BitmapDrawable) holder.gridMoviePosterImage.getDrawable()).getBitmap();
                onMovieViewClickListener.onClickMovieThumbnail(movieList.get(position), posterBitmap, v);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ((movieList == null) ? (0) : (movieList.size()));
    }

    public void resetMovieList(List<Movie> data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        movieList = data;
        notifyDataSetChanged();
    }

    public void addMovies(List<Movie> data) {
        if (data == null) {
            return;
        }
        if (movieList == null) {
            movieList = new ArrayList<>();
        }
        movieList.addAll(data);
        notifyDataSetChanged();
    }

    public void clearMovies() {
        if (movieList != null) {
            movieList.clear();
        }
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener */ {
        @Bind(R.id.grid_item_movie_poster_image)
        ImageView gridMoviePosterImage;

/*        @Bind(R.id.grid_item_movie_name_text_view)
        TextView gridMovieNameTv;*/

        @Bind(R.id.rating)
        RatingBar ratingBar;

        @Bind(R.id.grid_item_container)
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            //itemView.setOnClickListener(this);
        }

/*        @Override
        public void onClick(View v) {
            Debug.e("onClick " + get() + " " + mItem););
        }*/
    }
}
