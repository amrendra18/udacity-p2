package com.amrendra.popularmovies.app.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amrendra.popularmovies.R;
import com.amrendra.popularmovies.adapter.TrailerViewAdapter;
import com.amrendra.popularmovies.adapter.TrailerViewAdapter.TrailerCallback;
import com.amrendra.popularmovies.app.views.TitleTaglineView;
import com.amrendra.popularmovies.bus.BusProvider;
import com.amrendra.popularmovies.db.MovieContract;
import com.amrendra.popularmovies.events.DetailBackgroundColorChangeEvent;
import com.amrendra.popularmovies.events.FavouriteMovieAddEvent;
import com.amrendra.popularmovies.events.FavouriteMovieDeleteEvent;
import com.amrendra.popularmovies.handler.FavouriteQueryHandler;
import com.amrendra.popularmovies.loaders.MovieDetailLoader;
import com.amrendra.popularmovies.loaders.ReviewsLoader;
import com.amrendra.popularmovies.loaders.TrailersLoader;
import com.amrendra.popularmovies.logger.Debug;
import com.amrendra.popularmovies.model.Movie;
import com.amrendra.popularmovies.model.Review;
import com.amrendra.popularmovies.model.ReviewList;
import com.amrendra.popularmovies.model.Trailer;
import com.amrendra.popularmovies.model.TrailerList;
import com.amrendra.popularmovies.utils.AppConstants;
import com.amrendra.popularmovies.utils.Error;
import com.amrendra.popularmovies.utils.MoviesConstants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements TrailerCallback, FavouriteQueryHandler.OnQueryCompleteListener, AppBarLayout.OnOffsetChangedListener {

    public static final String TAG = "detailFragment";

    private static final int DETAIL_LOADER = 0;
    private static final int REVIEWS_LOADER = DETAIL_LOADER + 1;
    private static final int TRAILER_LOADER = REVIEWS_LOADER + 1;

    private int idx = -1;
    boolean isAlreadyFavouriteMovie = false;

    @Bind(R.id.full_content_detail_fragment)
    LinearLayout fullContainer;

    @Bind(R.id.detail_fragment_coordinator_layout)
    CoordinatorLayout mDetailFragmentCoordinatorLayout;

    @Nullable
    @Bind((R.id.collapsing_toolbar))
    CollapsingToolbarLayout mCollapsingToolbar;

    @Bind(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.toolbar_header_view)
    TitleTaglineView toolbarHeaderView;

    @Bind(R.id.float_header_view)
    TitleTaglineView floatHeaderView;

    @Bind(R.id.movie_poster_image)
    ImageView posterImageView;

    @Bind(R.id.detail_content_fragment_nested_scroll)
    NestedScrollView detailContainer;


    // Overview Card
    @Bind(R.id.detail_overview_card_title)
    TextView movieOverviewTitleTv;

    @Bind(R.id.detail_overview_card_ratings)
    TextView movieRatingsTv;

    @Bind(R.id.detail_overview_card_content)
    TextView movieOverviewContentTv;


    @Bind(R.id.detail_orginal_title_tv)
    TextView movieOriginalTitleTv;

    @Bind(R.id.detail_release_date_tv)
    TextView movieReleaseDateTv;

    @Bind(R.id.detail_orginal_language_tv)
    TextView movieOriginalLanguageTv;

    @Bind(R.id.detail_imdb_tv)
    TextView movieImdbTv;

    @Bind(R.id.detail_homepage_tv)
    TextView movieHomepageTv;

    @Bind(R.id.detail_revenue_tv)
    TextView movieRevenueTv;

    @Bind(R.id.detail_time_tv)
    TextView movieTimeTv;

    @Bind(R.id.detail_tagline_tv)
    TextView movieTaglineTv;

    @Bind(R.id.detail_genre_tv)
    TextView movieGenreTv;

    // End Overview Card


    // Reviews Card
    @Bind(R.id.review_progressbar)
    ProgressBar reviewsProgressbar;

    @Bind(R.id.no_reviews_tv)
    TextView noReviewsTv;

    @Bind(R.id.reviews_container)
    LinearLayout reviewsContainer;
    // End : Reviews Card


    // Trailers Card
    @Bind(R.id.trailer_progressbar)
    ProgressBar trailerProgressbar;

    @Bind(R.id.no_trailers_tv)
    TextView noTrailerTv;

    @Bind(R.id.trailer_recyvlerview)
    RecyclerView trailerRecyclerView;
    // End : Trailer Card


    MenuItem favMenu = null;


    Movie mMovie = null;
    List<Review> mReviewList = null;
    List<Trailer> mTrailerList = null;

    private boolean isTablet = false;

    private boolean isHideToolbarView = false;

    private FavouriteQueryHandler mFavouriteQueryHandler;

    public DetailFragment() {
        Debug.c();
    }


    public void setTablet(boolean isTablet) {
        this.isTablet = isTablet;
    }


    public static DetailFragment getInstance(Bundle bundle, boolean tablet) {
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(bundle);
        fragment.setTablet(tablet);
        Debug.c();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        mFavouriteQueryHandler = new FavouriteQueryHandler(context.getContentResolver(), this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    public void selectMovie() {
        fullContainer.setVisibility(View.GONE);
        mCollapsingToolbar.setTitle("Select a movie. #Filmie");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Debug.c();
        inflater.inflate(R.menu.menu_detail, menu);
        favMenu = menu.findItem(R.id.detail_favourite);
        setFavouriteButtonImage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.detail_share:
                shareMovie();
                return true;

            case R.id.detail_favourite:
                addFavourite(item);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFavouriteButtonImage() {
        if (isAlreadyFavouriteMovie) {
            favMenu.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.fav_selected));
        } else {
            favMenu.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.favourite));
        }
    }


    private void addFavourite(MenuItem menu) {
        if (isAlreadyFavouriteMovie) {
            mFavouriteQueryHandler.startDelete(
                    0,
                    null,
                    MovieContract.MovieEntry.CONTENT_URI,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{Long.toString(mMovie.id)}
            );
        } else {
            mFavouriteQueryHandler.startInsert(
                    0,
                    null,
                    MovieContract.MovieEntry.CONTENT_URI, mMovie.movieToContentValue()
            );
        }


    }

    private void shareMovie() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shareIntent.setType("text/plain");

        String movieText = "Checkout this movie.\n";
        if (mMovie != null) {
            movieText += mMovie.title + "\n";
        }
        movieText += "Shared via #" + getResources().getString(R.string.app_name) + "\n";
        if (mTrailerList != null && mTrailerList.size() > 0) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, movieText + MoviesConstants.TRAILER_VIDEO_URL +
                    mTrailerList.get(0).key);
        }

        //shareIntent.putExtra(Intent.EXTRA_SUBJECT, movieText);

        startActivity(Intent.createChooser(shareIntent, getActivity().getString(R.string.action_share)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Debug.c();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);
        Bundle passedBundle = getArguments();
        if (passedBundle == null) {
            selectMovie();
        } else {
            mMovie = (Movie) passedBundle.get(AppConstants.MOVIE_SHARE);
            idx = (int) passedBundle.get(AppConstants.MOVIE_IDX_SHARE);
            Debug.e("Movie with : " + idx, false);
            if (mMovie != null) {
                setupPosterImage(passedBundle);
                setupDetails();
            } else {
                selectMovie();
            }
        }
        addBackHomeArrow(rootView);
        return rootView;
    }

    private void setupPosterImage(Bundle bundle) {
        // fetch the movie
        mMovie = (Movie) bundle.get(AppConstants.MOVIE_SHARE);

        if (mMovie != null) {
            // set the poster for now
            // later get high resolution pic and replace silently ;-)
            posterImageView.setImageBitmap((Bitmap) bundle.get(AppConstants
                    .MOVIE_BITMAP_SHARE));
        }

    }

    private void setupDetails() {
        if (mMovie != null) {
            // set the title in the toolbar
            //mCollapsingToolbar.setTitle(mMovie.title);
            movieOverviewContentTv.setText(mMovie.overview);
            movieRatingsTv.setText(Double.toString(mMovie.averageVote) + "/10");

            movieOriginalTitleTv.setText(mMovie.originalTitle);
            movieReleaseDateTv.setText(mMovie.releaseDate);
            movieOriginalLanguageTv.setText(mMovie.originalLanguage);


            movieImdbTv.setText(mMovie.imdbid);
            movieHomepageTv.setText(mMovie.homepage);
            movieRevenueTv.setText(Long.toString(mMovie.revenue));
            movieTimeTv.setText(Integer.toString(mMovie.runtime));
            movieTaglineTv.setText(mMovie.tagline);
            movieGenreTv.setText(MoviesConstants.getGenresList(mMovie.genreIds));
        }
    }

    private void setUpFineDetails() {
        if (mMovie != null) {
            movieImdbTv.setText(mMovie.imdbid);
            movieHomepageTv.setText(mMovie.homepage);
            movieRevenueTv.setText(Long.toString(mMovie.revenue));
            movieTimeTv.setText(Integer.toString(mMovie.runtime));
            movieTaglineTv.setText(mMovie.tagline);
            toolbarHeaderView.bindTo(mMovie.title, mMovie.tagline);
        }
    }

    private void addBackHomeArrow(View rootView) {
/*        final Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);*/
        toolbarHeaderView.bindTo(mMovie.title, "");
        floatHeaderView.bindTo(mMovie.title, "");
        mAppBarLayout.addOnOffsetChangedListener(this);
        if (!isTablet) {
            //for creating home button
            //activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        trailerRecyclerView.setNestedScrollingEnabled(false);


    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Debug.c();

        initLoaders();
        changeToDynamicColor();

    }

    private void changeToDynamicColor() {
        if (mMovie != null) {
            Bitmap posterBitmap = ((BitmapDrawable) posterImageView.getDrawable()).getBitmap();
            Palette.from(posterBitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Debug.e("Changing colors", false);
                    int currentColor = ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark);
                    int whiteColor = ContextCompat.getColor(getActivity(), R.color.background);
                    int navLightColor = palette.getVibrantColor(currentColor);
                    int navDarkColor = palette.getDarkVibrantColor(currentColor);
                    mCollapsingToolbar.setContentScrimColor(navLightColor);
                    mCollapsingToolbar.setStatusBarScrimColor(navDarkColor);
                    int backgroundColor = palette.getLightVibrantColor(whiteColor);
                    detailContainer.setBackgroundColor(backgroundColor);

                    if (!isTablet && Build.VERSION.SDK_INT >= Build.VERSION_CODES
                            .LOLLIPOP) {
                        getActivity().getWindow().setNavigationBarColor(navLightColor);
                    }
                    Debug.e("Is tablet : " + isTablet, false);
                    if (isTablet) {
                        BusProvider.getInstance().post(new DetailBackgroundColorChangeEvent(backgroundColor));
                    }
                }
            });
        }
    }

    private void initLoaders() {
        if (mMovie != null) {
            isAlreadyFavouriteMovie = MoviesConstants.isFavouriteMovie(mMovie.id, getActivity());
            // Todo : check why below implementation causes lag, though its not on ui thread, but
            // above one is.
/*            Uri uri = MovieContract.MovieEntry.buildMovieWithId(mMovie.id);
            Debug.e("check fav uri: " + uri, false);
            mFavouriteQueryHandler.startQuery(
                    0,
                    0,
                    uri,
                    MovieContract.MovieEntry.MOVIE_PROJECTION_GRID,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? LIMIT 1",
                    new String[]{Long.toString(mMovie.id)},
                    null
            );*/


            if (mMovie.tagline == null && mMovie.runtime == 0 && mMovie.revenue == 0) {
                Debug.e("detail loader init", false);
                getLoaderManager().initLoader(DETAIL_LOADER, null, movieDetailsLoaderCallbacks);
            }

            if (mReviewList == null || mReviewList.size() == 0) {
                Debug.e("Requesting for reviews", false);
                getLoaderManager().initLoader(REVIEWS_LOADER, null, reviewListLoaderCallbacks);
            }
            if (mTrailerList == null || mTrailerList.size() == 0) {
                Debug.e("Requesting for trailers", false);
                getLoaderManager().initLoader(TRAILER_LOADER, null, trailerListLoaderCallbacks);
            }
        }
    }


    @Override
    public void onPause() {
        Debug.c();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private LoaderManager.LoaderCallbacks<Movie> movieDetailsLoaderCallbacks
            = new LoaderManager.LoaderCallbacks<Movie>() {

        @Override
        public Loader<Movie> onCreateLoader(int id, Bundle args) {
            Debug.c();
            return new MovieDetailLoader(getActivity(), mMovie.id);
        }

        @Override
        public void onLoadFinished(Loader<Movie> loader, Movie data) {
            Debug.c();
            if (data != null) {
                Debug.e("DETAILED MOVIE : " + data.toString(), false);
                mMovie.imdbid = data.imdbid;
                mMovie.homepage = data.homepage;
                mMovie.revenue = data.revenue;
                mMovie.runtime = data.runtime;
                mMovie.tagline = data.tagline;
                setUpFineDetails();
                if (isAlreadyFavouriteMovie) {
                    Debug.e("Already favourite but not in db, so updating db", false);
                    mFavouriteQueryHandler.startUpdate(
                            0,
                            null,
                            MovieContract.MovieEntry.CONTENT_URI,
                            mMovie.movieToContentValue(),
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{Long.toString(mMovie.id)}
                    );
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Movie> loader) {
            Debug.c();
        }
    };

    private LoaderManager.LoaderCallbacks<ReviewList> reviewListLoaderCallbacks
            = new LoaderManager.LoaderCallbacks<ReviewList>() {
        @Override
        public Loader<ReviewList> onCreateLoader(int id, Bundle args) {
            Debug.c();
            reviewsProgressbar.setVisibility(ProgressBar.VISIBLE);
            return new ReviewsLoader(getActivity(), mMovie.id);
        }

        @Override
        public void onLoadFinished(Loader<ReviewList> loader, ReviewList data) {
            Debug.c();
            addReviews(data);
        }

        @Override
        public void onLoaderReset(Loader<ReviewList> loader) {
            Debug.c();
        }
    };

    private LoaderManager.LoaderCallbacks<TrailerList> trailerListLoaderCallbacks
            = new LoaderManager.LoaderCallbacks<TrailerList>() {

        @Override
        public Loader<TrailerList> onCreateLoader(int id, Bundle args) {
            Debug.c();
            trailerProgressbar.setVisibility(ProgressBar.VISIBLE);
            return new TrailersLoader(getActivity(), mMovie.id);
        }

        @Override
        public void onLoadFinished(Loader<TrailerList> loader, TrailerList data) {
            Debug.c();
            addTrailers(data);
        }

        @Override
        public void onLoaderReset(Loader<TrailerList> loader) {
            Debug.c();
        }
    };


    private void addReviews(ReviewList result) {
        if (result.getError() == Error.SUCCESS) {
            final LayoutInflater inflater = LayoutInflater.from(getActivity());
            List<Review> reviewList = result.results;
            if (!(reviewList == null || reviewList.isEmpty())) {
                int len = reviewList.size();
                mReviewList = reviewList;
                for (int i = 0; i < len; i++) {
                    Review review = reviewList.get(i);
                    final View reviewView = inflater.inflate(R.layout.review_layout, reviewsContainer, false);
                    TextView reviewAuthor = ButterKnife.findById(reviewView, R.id.review_by);
                    TextView reviewContent = ButterKnife.findById(reviewView, R.id.review_content);
                    reviewAuthor.setText(review.author);
                    reviewContent.setText(review.content);
                    reviewsContainer.addView(reviewView);
                    if (i < len - 1) {
                        final View dividerView = inflater.inflate(R.layout.divider_content,
                                reviewsContainer, false);
                        View divider = ButterKnife.findById(dividerView, R.id.divider_content);
                        reviewsContainer.addView(divider);
                    }
                }
                noReviewsTv.setVisibility(View.GONE);
            } else {
                noReviewsTv.setText(getResources().getText(R.string.no_detail_reviews));
                noReviewsTv.setVisibility(View.VISIBLE);
            }
        } else {
            noReviewsTv.setText(getResources().getString(R.string
                    .error_detail_reviews, result.getError().getDescription()));
            noReviewsTv.setVisibility(View.VISIBLE);
        }
        reviewsProgressbar.setVisibility(ProgressBar.INVISIBLE);
    }


    private void addTrailers(TrailerList result) {
        if (result.getError() == Error.SUCCESS) {
            List<Trailer> trailerList = result.results;
            mTrailerList = trailerList;
            if (!(trailerList == null || trailerList.isEmpty())) {
                noTrailerTv.setVisibility(View.GONE);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager
                        .HORIZONTAL, false);
                trailerRecyclerView.setLayoutManager(layoutManager);
                TrailerViewAdapter adapter = new TrailerViewAdapter(trailerList, getActivity(), this);
                trailerRecyclerView.setAdapter(adapter);
            } else {
                noTrailerTv.setText(getResources().getText(R.string.no_detail_trailers));
                noTrailerTv.setVisibility(View.VISIBLE);
                trailerRecyclerView.setVisibility(View.GONE);
            }
        } else {
            noTrailerTv.setText(getResources().getString(R.string.error_detail_trailers, result
                    .getError().getDescription()));
            noTrailerTv.setVisibility(View.VISIBLE);
            trailerRecyclerView.setVisibility(View.GONE);
        }
        trailerProgressbar.setVisibility(ProgressBar.INVISIBLE);
    }

    private void playTrailer(String key) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
            //intent.putExtra("force_fullscreen", true);
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(MoviesConstants.TRAILER_VIDEO_URL + key));
            //intent.putExtra("force_fullscreen", true);
            startActivity(intent);
        }
    }

    @Override
    public void onClickTrailerThumbnail(String key) {
        Debug.e(key, false);
        playTrailer(key);
    }

    // START FavouriteQueryHandler.OnQueryCompleteListener
    @Override
    public void onQueryComplete(Cursor cursor) {
        Debug.e("Check fav : ", false);
        boolean found = false;
        if (cursor != null) {
            if (cursor.moveToNext()) {
                found = true;
            }
            cursor.close();
        }
        isAlreadyFavouriteMovie = found;
        Debug.e("result : " + found, false);
    }

    @Override
    public void onInsertComplete(Uri retUri) {
        Debug.e("Add fav : " + retUri, false);
        String message = "";
        if (retUri == null) {
            message = "Error in adding " + mMovie.title + " from favourites!";
        } else {
            isAlreadyFavouriteMovie = true;
            message = mMovie.title + " added to favourites!";
            BusProvider.getInstance().post(new FavouriteMovieAddEvent(mMovie, idx));
        }
        setFavouriteButtonImage();
        Snackbar snackbar = Snackbar.make(mDetailFragmentCoordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onDeleteComplete(int result) {
        Debug.e("Del fav : " + result, false);
        String message;
        if (result == 1) {
            message = mMovie.title + " removed from favourites!";
            isAlreadyFavouriteMovie = false;
            BusProvider.getInstance().post(new FavouriteMovieDeleteEvent(idx));
        } else {
            message = "Error in removing " + mMovie.title + " from favourites!";
        }
        setFavouriteButtonImage();
        Snackbar snackbar = Snackbar.make(mDetailFragmentCoordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onUpdateComplete(int result) {
        Debug.e("favourite movie details updates : " + result, false);
    }
    // END

    public void onBackPressed() {
        Debug.c();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {

        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;
            floatHeaderView.setVisibility(View.INVISIBLE);
        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.INVISIBLE);
            isHideToolbarView = !isHideToolbarView;
            floatHeaderView.setVisibility(View.VISIBLE);
        }
    }
}