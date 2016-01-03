package com.amrendra.popularmovies.app.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.amrendra.popularmovies.R;
import com.amrendra.popularmovies.app.fragments.DetailFragment;
import com.amrendra.popularmovies.bus.BusProvider;
import com.amrendra.popularmovies.events.MovieThumbnailClickEvent;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.Movie;
import com.amrendra.popularmovies.utils.AppConstants;
import com.amrendra.popularmovies.utils.MoviesConstants;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_activity_main);
        setSupportActionBar(toolbar);
        TextView titleTextView = (TextView) toolbar.getChildAt(0);
        titleTextView.setTypeface(Typeface.createFromAsset(getAssets(), MoviesConstants.MOVIE_TITLE_FONT));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setIcon(R.mipmap.ic_launcher);
        }

        BusProvider.getInstance().register(this);

        if (findViewById(R.id.detail_activity_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_activity_container, new DetailFragment(), DetailFragment.TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
        Debug.e("TABLET : " + mTwoPane, false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Debug.c();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Debug.c();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Debug.c();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
        Debug.c();
    }

    @Subscribe
    public void onClickMovieThumbnail(MovieThumbnailClickEvent event) {
        Debug.c();
        Movie movie = event.getMovie();
        Bitmap bitmap = event.getBitmap();
        Debug.e("Movie clicked : " + movie.title, false);
        if (mTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppConstants.MOVIE_SHARE, movie);
            bundle.putParcelable(AppConstants.MOVIE_BITMAP_SHARE, bitmap);
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_activity_container, detailFragment, DetailFragment.TAG).commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(AppConstants.MOVIE_SHARE, movie);
            intent.putExtra(AppConstants.MOVIE_BITMAP_SHARE, bitmap);
            startActivity(intent);
        }
    }

}
