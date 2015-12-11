package com.amrendra.popularmovies.app.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.amrendra.popularmovies.R;
import com.amrendra.popularmovies.adapter.CustomSpinnerAdapter;
import com.amrendra.popularmovies.adapter.MovieGridAdapter;
import com.amrendra.popularmovies.utils.AppConstants;
import com.amrendra.popularmovies.utils.Error;
import com.amrendra.popularmovies.listener.EndlessScrollListener;
import com.amrendra.popularmovies.loaders.MoviesLoader;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.Movie;
import com.amrendra.popularmovies.model.MovieList;
import com.amrendra.popularmovies.utils.MoviesConstants;
import com.amrendra.popularmovies.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements LoaderManager
        .LoaderCallbacks<MovieList>, /*SwipeRefreshLayout.OnRefreshListener,*/ AdapterView
        .OnItemSelectedListener, MovieGridAdapter.OnMovieViewClickListener {

    public static final String TAG_MAIN_FRAGMENT = "main_fragment";

    private static final int MOVIE_LOADER = 0;

    private int mSelectedPosition = -1;
    private int mCurrentPage = 1;
    private MovieGridAdapter mMovieGridAdapter;


    @Bind(R.id.movies_gridlist)
    public RecyclerView movieGridRecyleView;

    @Bind(R.id.swipe_refresh_layout)
    public SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.fragment_main_framelayout)
    FrameLayout mainFragmentFrameLayout;

    int navColor;

    String currentSortingBy;
    private MovieClickCallback movieClickCallback;


    private EndlessScrollListener endlessScrollListener;

    @Override
    public void onClickMovieThumbnail(Movie movie, Bitmap bitmap, View view) {
        movieClickCallback.onClickMovieThumbnail(movie, bitmap, view);

    }


    public interface MovieClickCallback {
        void onClickMovieThumbnail(Movie movie, Bitmap bitmap, View view);
    }

        /*
    Lifecycle of a fragment
    1. onInflate
    2. onAttach()
    3. onCreate()
    4. onCreateView()
    5. onViewCreated()
       Activity.onCreate()
    6. onActivityCreated()
    7. onStart()
    8. onResume() Fragment is visible now
    9. onPause()
    10. onStop()
    11. onDestroyView();
    12. onDestroy()
    13. onDetach
     */


    public MainFragment() {
        Debug.c();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            movieClickCallback = (MovieClickCallback) context;
        } catch (ClassCastException ex) {
            throw new IllegalStateException("Any Activity having Main Fragment must implement " +
                    "MainFragment.MovieClickCallback");
        }
        Debug.c();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Debug.c();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Debug.c();
        outState.putInt(AppConstants.CURRENT_PAGE, mCurrentPage);
        outState.putInt(AppConstants.SCROLL_POSITION, mSelectedPosition);
        outState.putParcelableArrayList(AppConstants.MOVIE_LIST_PARCEL, new ArrayList<>(mMovieGridAdapter
                .getItemList()));
        Debug.bundle(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Debug.c();
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Debug.c();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Debug.c();

        int gridColumns = getResources().getInteger(R.integer.grid_columns);
        navColor = ContextCompat.getColor(getActivity(), (R.color.colorPrimaryTransparentNav));

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setProgressViewOffset(true, 200, 500);
        mSwipeRefreshLayout.setEnabled(false);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), gridColumns);


        movieGridRecyleView.setLayoutManager(mGridLayoutManager);
        movieGridRecyleView.setHasFixedSize(true);

        // restore the list and scroll position
        List<Movie> restoredMovies;
        boolean restored = false;
        if (savedInstanceState != null) {

            mSelectedPosition = savedInstanceState.getInt(AppConstants.SCROLL_POSITION);
            restoredMovies = savedInstanceState.getParcelableArrayList(AppConstants
                    .MOVIE_LIST_PARCEL);
            mCurrentPage = savedInstanceState.getInt(AppConstants.CURRENT_PAGE);
            Debug.e("restoring state : " + mSelectedPosition + " size : " + restoredMovies.size()
                    + " page : " + mCurrentPage, false);
            restored = true;
        } else {
            restoredMovies = new ArrayList<>();
            mSelectedPosition = -1;
            mCurrentPage = 1;
        }
        endlessScrollListener = new EndlessScrollListener(mGridLayoutManager, mCurrentPage) {
            @Override
            public void onLoadMore(int current_page) {
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstants.CURRENT_PAGE, current_page);
                restartLoader(bundle);
            }
        };
        mMovieGridAdapter = new MovieGridAdapter(restoredMovies, navColor, getActivity(),
                this);
        movieGridRecyleView.setAdapter(mMovieGridAdapter);

        // Will add later :P
        movieGridRecyleView.addOnScrollListener(endlessScrollListener);

        Spinner spinner = (Spinner) getActivity().findViewById(R.id.toolbar_spinner);
        final CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(getActivity());
        String[] sortOptions = getResources().getStringArray(R.array.string_sort_by);
        spinnerAdapter.addItems(Arrays.asList(sortOptions));
        spinner.setAdapter(spinnerAdapter);


        currentSortingBy = MoviesConstants.SORT_BY_POPULARITY;
        PreferenceManager.getInstance(getActivity()).writeValue(MoviesConstants.SORT_BY,
                currentSortingBy);
        spinner.setSelection(0, false);
        spinner.setOnItemSelectedListener(this);
        Debug.c();
        if (!restored) {
            // Calling LoaderManager.init in onCreate makes call to onLoadingFinished twice.
            // hence check if loader already exists
            if (getLoaderManager().getLoader(MOVIE_LOADER) == null) {
                getLoaderManager().initLoader(MOVIE_LOADER, null, this);
            } else {
                getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Debug.c();
    }

    @Override
    public void onResume() {
        super.onResume();
        Debug.c();
    }

    @Override
    public void onPause() {
        super.onPause();
        Debug.c();
    }

    @Override
    public void onStop() {
        super.onStop();
        Debug.c();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Debug.c();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Debug.c();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Debug.c();
        ButterKnife.unbind(this);
    }


    @Override
    public Loader<MovieList> onCreateLoader(int id, Bundle args) {
        Debug.c();
        String sortBy = PreferenceManager.getInstance(getActivity()).readValue(MoviesConstants
                .SORT_BY, MoviesConstants.SORT_BY_POPULARITY);
        if (sortBy.equals(MoviesConstants.SORT_BY_FAVOURITES)) {
            Debug.showToastShort("Coming Up. Part of Stage-2: PopularMovies :)", getActivity(),
                    true);
            mMovieGridAdapter.clearMovies();
            mSwipeRefreshLayout.setRefreshing(false);
            return null;
        }
        mSwipeRefreshLayout.setRefreshing(true);
        int page = 1;
        if (args != null) {
            page = args.getInt(AppConstants.CURRENT_PAGE);
        }
        Debug.e("REQUESTING  :" + page, false);
        return new MoviesLoader(getActivity(), sortBy, page);
    }

    @Override
    public void onLoadFinished(Loader<MovieList> loader, MovieList data) {
        Debug.c();
        if (data.getError() == Error.SUCCESS) {
            List<Movie> list = data.results;
            mCurrentPage = data.page;
            Debug.e("LOADED page : " + mCurrentPage, false);
            Debug.e(data.toString(), false);
            mMovieGridAdapter.addMovies(list);
        } else {
            Debug.showSnackbarLong(getActivity().findViewById(R.id.main_activity_coordinator_layout), getResources().getString(R.string.error_movie_list, data.getError().getDescription()));
        }
        // hide refresh at last
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<MovieList> loader) {
        Debug.c();
        mMovieGridAdapter.clearMovies();
    }

    public void restartLoader(Bundle bundle) {
        Debug.c();
        getLoaderManager().restartLoader(MOVIE_LOADER, bundle, this);
    }

    int lastSelection = 0;

    // spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Debug.c();
        String selection = parent.getItemAtPosition(position).toString();
        //Debug.showToastShort(selection, getActivity());
        Debug.e(selection, false);
        PreferenceManager.getInstance(getActivity()).debug();
        String nextSortingBy;
        switch (position) {
            case 0: // popularity
                nextSortingBy = MoviesConstants.SORT_BY_POPULARITY;
                break;
            case 1: // ratings
                nextSortingBy = MoviesConstants.SORT_BY_RATINGS;
                break;
            case 2: //favourite
                nextSortingBy = MoviesConstants.SORT_BY_FAVOURITES;
                break;
            default: //popularity
                nextSortingBy = MoviesConstants.SORT_BY_POPULARITY;
                break;
        }
        PreferenceManager.getInstance(getActivity()).writeValue(MoviesConstants.SORT_BY,
                nextSortingBy);
        if (nextSortingBy.equals(MoviesConstants.SORT_BY_FAVOURITES) == false) {
            if (nextSortingBy != currentSortingBy) {
                restartLoader(null);
                movieGridRecyleView.scrollToPosition(0);
            }
            currentSortingBy = nextSortingBy;
        } else {
            Debug.showToastShort("Coming Up. Part of Stage-2: PopularMovies :)", getActivity(),
                    true);
            parent.setSelection(lastSelection);
        }
        lastSelection = position;
    }


    // spinner
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void changeBackgroundColor(int color) {
        mainFragmentFrameLayout.setBackgroundColor(color);
    }

}
