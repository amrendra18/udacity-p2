package com.amrendra.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amrendra.popularmovies.R;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.Trailer;
import com.amrendra.popularmovies.utils.MoviesConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Amrendra Kumar on 05/12/15.
 */
public class TrailerViewAdapter extends RecyclerView.Adapter<TrailerViewAdapter.ViewHolder> {

    private List<Trailer> mTrailerList = new ArrayList<>();
    private Context mContext;

    private TrailerCallback mTrailerCallback;


    public TrailerViewAdapter(List<Trailer> trailers, Context context,
            TrailerCallback callback) {
        mTrailerList = trailers;
        mContext = context;
        mTrailerCallback = callback;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Debug.c();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailerview_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Debug.c();
        final Trailer trailer = mTrailerList.get(position);

        String imageUrl = String.format(MoviesConstants.TRAILER_IMAGE_URL, trailer.key);

        Picasso.with(mContext)
                .load(imageUrl)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.place_holder)
                .into(holder.trailerImage);

        // set the image
        // set the onclick listener

        holder.trailerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTrailerCallback.onClickTrailerThumbnail(trailer.key);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ((mTrailerList == null) ? (0) : (mTrailerList.size()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.trailerview_container)
        CardView trailerCardView;

        @Bind(R.id.trailerview_image)
        ImageView trailerImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface TrailerCallback {
        void onClickTrailerThumbnail(String key);
    }
}

