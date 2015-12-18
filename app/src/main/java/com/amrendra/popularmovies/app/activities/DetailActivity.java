package com.amrendra.popularmovies.app.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.amrendra.popularmovies.R;
import com.amrendra.popularmovies.app.fragments.DetailFragment;
import com.amrendra.popularmovies.bus.BusProvider;
import com.amrendra.popularmovies.events.FavouriteMovieAddEvent;
import com.amrendra.popularmovies.events.FavouriteMovieDeleteEvent;
import com.amrendra.popularmovies.logger.Debug;
import com.squareup.otto.Subscribe;

public class DetailActivity extends AppCompatActivity {

    boolean toSupportBackTransition = true;
    Fragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Debug.c();
        BusProvider.getInstance().register(this);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle(getIntent().getExtras());
            detailFragment = DetailFragment.getInstance(bundle, false);
            fragmentTransaction.add(R.id.detail_activity_container, detailFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                Debug.c();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Debug.c();
        supportFinishAfterTransition();
    }

    @Override
    public void supportFinishAfterTransition() {
        //toSupportBackTransition = true;
        if (toSupportBackTransition) {
            super.supportFinishAfterTransition();
        } else {
            finish();
        }
    }

    @Subscribe
    public void onFavouriteMovieAdd(FavouriteMovieAddEvent event) {
        Debug.c();
        //Todo : Fix this bug
        //toSupportBackTransition = true;
    }

    @Subscribe
    public void onFavouriteMovieDelete(FavouriteMovieDeleteEvent event) {
        Debug.c();
        toSupportBackTransition = false;
    }
}
