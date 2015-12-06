package com.amrendra.popularmovies.app.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.amrendra.popularmovies.R;
import com.amrendra.popularmovies.app.fragments.DetailFragment;
import com.amrendra.popularmovies.app.fragments.MainFragment;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.Movie;
import com.amrendra.popularmovies.utils.AppConstants;

import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MainFragment.MovieClickCallback,
        DetailFragment.ColorCallback {

    private static final String TAG_MAIN_FRAGMENT = "main_fragment";

    MainFragment mMainFragment;

    boolean tablet = false;
    int movieSelection = 0;
    public int backgroundColor = Color.WHITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMainFragment =
                (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);

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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClickMovieThumbnail(Movie movie, Bitmap bitmap, View view) {
        Debug.e("Movie clicked : " + movie.title, false);
        if (tablet) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppConstants.MOVIE_SHARE, movie);
            bundle.putParcelable(AppConstants.MOVIE_BITMAP_SHARE, bitmap);
            DetailFragment detailFragment = DetailFragment.getInstance(bundle, true);
            fragmentTransaction.replace(R.id.detail_activity_container, detailFragment, DetailFragment.TAG).commit();
        } else {
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Debug.e("Animation", false);
                options = ActivityOptions.
                        makeSceneTransitionAnimation(this, view, AppConstants.SHARED_IMAGE_VIEW);
            }
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(AppConstants.MOVIE_SHARE, movie);
            intent.putExtra(AppConstants.MOVIE_BITMAP_SHARE, bitmap);
            if (options != null) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        }
    }

    @Override
    public void onBackgroundChange(int color) {
        this.backgroundColor = color;
        if (tablet && mMainFragment != null) {
            mMainFragment.changeBackgroundColor(color);
        }
    }

}
