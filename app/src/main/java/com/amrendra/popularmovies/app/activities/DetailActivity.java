package com.amrendra.popularmovies.app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amrendra.popularmovies.R;
import com.amrendra.popularmovies.app.fragments.DetailFragment;
import com.amrendra.popularmovies.logger.Debug;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Debug.c();
        if (savedInstanceState == null) {
            Bundle bundle = new Bundle(getIntent().getExtras());
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_activity_container, detailFragment)
                    .commit();
        }
    }

}
