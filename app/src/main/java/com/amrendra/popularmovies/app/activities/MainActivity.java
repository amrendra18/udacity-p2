package com.amrendra.popularmovies.app.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amrendra.popularmovies.R;
import com.amrendra.popularmovies.app.fragments.DetailFragment;
import com.amrendra.popularmovies.app.fragments.MainFragment;
import com.amrendra.popularmovies.bus.BusProvider;
import com.amrendra.popularmovies.events.MovieThumbnailClickEvent;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.Movie;
import com.amrendra.popularmovies.utils.AppConstants;
import com.amrendra.popularmovies.utils.MoviesConstants;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    MainFragment mMainFragment;

    boolean tablet = false;
    int movieSelection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView titleTextView = (TextView) toolbar.getChildAt(0);
        titleTextView.setTypeface(Typeface.createFromAsset(getAssets(), MoviesConstants.MOVIE_TITLE_FONT));

        BusProvider.getInstance().register(this);

/*        mMainFragment =
                (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);*/

        getSupportActionBar().setIcon(R.mipmap.ic_launcher);


        if (findViewById(R.id.detail_activity_container) != null) {
            tablet = true;
            movieSelection = 0;
            DetailFragment detailFragment = DetailFragment.getInstance(savedInstanceState, true);
            addDetailFragment(detailFragment);
        } else {
            tablet = false;
        }
        Debug.e("TABLET : " + tablet, false);
    }

    private void addDetailFragment(DetailFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.detail_activity_container, fragment, DetailFragment.TAG).commit();
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
        View view = event.getView();
        int positionInAdapter = event.getPosition();
        Debug.e("Movie clicked : " + movie.title, false);
        if (tablet) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppConstants.MOVIE_SHARE, movie);
            bundle.putParcelable(AppConstants.MOVIE_BITMAP_SHARE, bitmap);
            bundle.putInt(AppConstants.MOVIE_IDX_SHARE, positionInAdapter);
            DetailFragment detailFragment = DetailFragment.getInstance(bundle, true);
            fragmentTransaction.replace(R.id.detail_activity_container, detailFragment, DetailFragment.TAG).commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(AppConstants.MOVIE_SHARE, movie);
            intent.putExtra(AppConstants.MOVIE_BITMAP_SHARE, bitmap);
            intent.putExtra(AppConstants.MOVIE_IDX_SHARE, positionInAdapter);
            startActivity(intent);
        }
    }
}
