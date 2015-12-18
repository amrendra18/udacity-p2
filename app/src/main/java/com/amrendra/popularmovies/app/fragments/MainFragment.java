package com.amrendra.popularmovies.app.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.amrendra.popularmovies.bus.BusProvider;
import com.amrendra.popularmovies.events.DetailBackgroundColorChangeEvent;
import com.amrendra.popularmovies.events.FavouriteMovieAddEvent;
import com.amrendra.popularmovies.events.FavouriteMovieDeleteEvent;
import com.amrendra.popularmovies.events.MovieThumbnailClickEvent;
import com.amrendra.popularmovies.listener.EndlessScrollListener;
import com.amrendra.popularmovies.loaders.FavouriteLoader;
import com.amrendra.popularmovies.loaders.MoviesLoader;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.Movie;
import com.amrendra.popularmovies.model.MovieList;
import com.amrendra.popularmovies.utils.AppConstants;
import com.amrendra.popularmovies.utils.Error;
import com.amrendra.popularmovies.utils.MoviesConstants;
import com.amrendra.popularmovies.utils.PreferenceManager;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements AdapterView
        .OnItemSelectedListener, MovieGridAdapter.OnMovieViewClickListener {

    public static final String TAG_MAIN_FRAGMENT = "main_fragment";

    private static final int MOVIE_LOADER = 0;
    private static final int FAVOURITE_LOADER = MOVIE_LOADER + 1;

    private int mSelectedPosition = -1;
    private int lastClickedMovieIndex = -1;
    private int mCurrentPage = 1;
    private int pageToBeLoaded = -1;
    private MovieGridAdapter mMovieGridAdapter;


    @Bind(R.id.movies_gridlist)
    public RecyclerView movieGridRecyleView;

    @Bind(R.id.swipe_refresh_layout)
    public SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.fragment_main_framelayout)
    FrameLayout mainFragmentFrameLayout;

    int navColor;

    String currentSortingBy;
    //private MovieClickCallback movieClickCallback;


    private EndlessScrollListener endlessScrollListener;

    @Override
    public void onClickMovieThumbnail(Movie movie, Bitmap bitmap, View view, int position) {
        Debug.c();
        lastClickedMovieIndex = position;
        // Notify to activity that a thumbnail has been clicked
        BusProvider.getInstance().post(new MovieThumbnailClickEvent(movie, bitmap, view, position));
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
            //movieClickCallback = (MovieClickCallback) context;
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
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Debug.c();
        outState.putInt(AppConstants.CURRENT_PAGE, mCurrentPage);
        outState.putInt(AppConstants.SCROLL_POSITION, mSelectedPosition);
        outState.putParcelableArrayList(AppConstants.MOVIE_LIST_PARCEL, new ArrayList<>(mMovieGridAdapter
                .getItemList()));
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
        // movieGridRecyleView.addOnScrollListener(endlessScrollListener);

        Spinner spinner = (Spinner) getActivity().findViewById(R.id.toolbar_spinner);
        final CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(getActivity());
        String[] sortOptions = getResources().getStringArray(R.array.string_sort_by);
        spinnerAdapter.addItems(Arrays.asList(sortOptions));
        spinner.setAdapter(spinnerAdapter);

        currentSortingBy = MoviesConstants.getSortOrder(getActivity());
        Debug.e("Initial Sorting : " + currentSortingBy, false);
        spinner.setSelection(0, false);
        spinner.setOnItemSelectedListener(this);

        if (!restored) {
            initLoadersOnCategory(null);
        }
    }

    private void initLoadersOnCategory(Bundle bundle) {
        Debug.c();
        Debug.bundle(bundle);
        if (bundle == null) {
            bundle = new Bundle();
        }
        String sortBy = MoviesConstants.getSortOrder(getActivity());

        bundle.putString(AppConstants.GRID_VIEW_SORTING_TYPE, sortBy);
        if (sortBy.equals(MoviesConstants.SORT_BY_FAVOURITES)) {
            Debug.e("will start favourite loader", false);
            initFavouriteLoader(bundle);
        } else {
            Debug.e("will start movie loader", false);
            initMovieLoader(bundle);
        }
    }

    private void initMovieLoader(Bundle bundle) {
        if (getLoaderManager().getLoader(MOVIE_LOADER) == null) {
            getLoaderManager().initLoader(MOVIE_LOADER, bundle, movieLoaderCallbacks);
        } else {
            getLoaderManager().restartLoader(MOVIE_LOADER, bundle, movieLoaderCallbacks);
        }
    }

    private void initFavouriteLoader(Bundle bundle) {
        if (getLoaderManager().getLoader(FAVOURITE_LOADER) == null) {
            getLoaderManager().initLoader(FAVOURITE_LOADER, bundle, favouriteLoaderCallbacks);
        } else {
            getLoaderManager().restartLoader(FAVOURITE_LOADER, bundle, favouriteLoaderCallbacks);
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

        MoviesConstants.saveSortOrder(getActivity(), nextSortingBy);
        // if (nextSortingBy.equals(MoviesConstants.SORT_BY_FAVOURITES) == false) {
        if (!nextSortingBy.equals(currentSortingBy)) {
            restartLoader(null);
            movieGridRecyleView.scrollToPosition(0);
        }
        currentSortingBy = nextSortingBy;
/*        } else {

            Debug.showToastShort("Coming Up. Part of Stage-2: PopularMovies :)", getActivity(),
                    true);
            parent.setSelection(lastSelection);*/
        // }
        lastSelection = position;
    }


    // spinner
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Subscribe
    public void changeBackgroundColor(DetailBackgroundColorChangeEvent event) {
        Debug.c();
        mainFragmentFrameLayout.setBackgroundColor(event.getBgColor());
    }

    public void restartLoader(Bundle bundle) {
        Debug.c();
        initLoadersOnCategory(bundle);
    }


    private LoaderManager.LoaderCallbacks<MovieList> movieLoaderCallbacks
            = new LoaderManager.LoaderCallbacks<MovieList>() {
        @Override
        public Loader<MovieList> onCreateLoader(int id, Bundle args) {
            Debug.c();
            mSwipeRefreshLayout.setRefreshing(true);
            int page = 1;
            String sortBy = MoviesConstants.SORT_BY_POPULARITY;
            if (args != null) {
                page = args.getInt(AppConstants.CURRENT_PAGE, page);
                sortBy = args.getString(AppConstants.GRID_VIEW_SORTING_TYPE);
            }
            pageToBeLoaded = page;
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
                if (data.page > 1) {
                    mMovieGridAdapter.addMovies(list);
                } else {
                    mMovieGridAdapter.resetMovieList(list);
                }
            } else {
                String errorMessage;
                if (pageToBeLoaded > 1) {
                    errorMessage = getResources().getString(R.string.error_movie_list_more, data.getError
                            ().getDescription());
                } else {
                    errorMessage = getResources().getString(R.string.error_movie_list, data.getError
                            ().getDescription());
                }
                Snackbar snackbar = Snackbar
                        .make(getActivity().findViewById(R.id.main_activity_coordinator_layout), errorMessage,
                                Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(AppConstants.CURRENT_PAGE, pageToBeLoaded);
                                restartLoader(bundle);
                            }
                        });
                snackbar.show();
            }
            // hide refresh at last
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onLoaderReset(Loader<MovieList> loader) {
            Debug.c();
            mMovieGridAdapter.clearMovies();
        }
    };


    private LoaderManager.LoaderCallbacks<List<Movie>> favouriteLoaderCallbacks
            = new LoaderManager.LoaderCallbacks<List<Movie>>() {

        @Override
        public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
            Debug.c();
            mSwipeRefreshLayout.setRefreshing(true);
            return new FavouriteLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
            Debug.c();
            if (data != null) {
                mMovieGridAdapter.resetMovieList(data);
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {
            Debug.c();
            mMovieGridAdapter.clearMovies();
        }
    };

    @Subscribe
    public void onFavouriteMovieAdd(FavouriteMovieAddEvent event) {
        Debug.c();
        Movie movie = event.getMovie();
        String currentSortBy = MoviesConstants.getSortOrder(getActivity());
        if (currentSortBy.equals(MoviesConstants.SORT_BY_FAVOURITES)) {
            Debug.c();
            mMovieGridAdapter.addMovie(movie);
        }

    }

    @Subscribe
    public void onFavouriteMovieDelete(FavouriteMovieDeleteEvent event) {
        Debug.c();
        int idx = event.getIdx();
        String currentSortBy = MoviesConstants.getSortOrder(getActivity());
        if (currentSortBy.equals(MoviesConstants.SORT_BY_FAVOURITES)) {
            Debug.e("Deleting movie : " + idx, false);
            mMovieGridAdapter.deleteMovie(idx);
        }
    }


}
