package com.amrendra.popularmovies.app.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.Swatch;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amrendra.popularmovies.R;
import com.amrendra.popularmovies.adapter.TrailerViewAdapter;
import com.amrendra.popularmovies.adapter.TrailerViewAdapter.TrailerCallback;
import com.amrendra.popularmovies.app.views.MovieTaglineTextView;
import com.amrendra.popularmovies.app.views.MovieTitleTextView;
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
import com.amrendra.popularmovies.utils.GraphicsUtils;
import com.amrendra.popularmovies.utils.MoviesConstants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
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

    boolean isAlreadyFavouriteMovie = false;
    private int mStatusBarColor = Color.BLUE;
    private int reviewContentColor = Color.WHITE;
    private int reviewAuthorColor = Color.YELLOW;

    TrailerViewAdapter trailerAdapter;
    boolean tinted = true;
    boolean toolbarShown = false;

    /*
    Todo: Resolve the error
    There is some issue with ButterKnife binding, throwing random NPE for tablets
    Reporting the issue to Butterknife github page

    Issue is totally random in nature & happens with different views & not always.
    Issue doesnt exists if I use findViewById
     */

    FloatingActionButton floatingFavouriteActionButton;
    FloatingActionButton floatingShareActionButton;
    LinearLayout fullContainer;
    CoordinatorLayout mDetailFragmentCoordinatorLayout;
    CollapsingToolbarLayout mCollapsingToolbar;
    AppBarLayout mAppBarLayout;
    Toolbar mToolbar;
    TitleTaglineView toolbarHeaderView;
    ImageView posterImageView;
    NestedScrollView detailContainer;


    // Overview Card
    MovieTitleTextView overviewTitleTv;
    MovieTaglineTextView overviewYearTv;
    TextView movieRatingsTv;
    TextView movieOverviewContentTv;
    TextView movieTimeTv;
    TextView movieGenreTv;
    // End Overview Card


    // Reviews Card
    ProgressBar reviewsProgressbar;
    TextView noReviewsTv;
    LinearLayout reviewsContainer;
    // End : Reviews Card


    // Trailers Card
    ProgressBar trailerProgressbar;
    TextView noTrailerTv;
    RecyclerView trailerRecyclerView;
    // End : Trailer Card


    // Lists
    @Nullable
    @Bind({R.id.overview_title, R.id.overview_year, R.id
            .review_content_heading, R.id
            .trailers_content_heading})
    List<TextView> titleLists;


    @Bind({R.id.detail_content_heading_genre_tv, R.id.detail_content_heading_rating_tv, R.id.detail_content_heading_runtime_tv, R.id.detail_overview_card_ratings, R.id
            .detail_genre_tv, R.id.detail_time_tv})
    List<TextView> extraDetailsList;


    @Nullable
    @Bind({R.id.detail_overview_card_content, R.id.review_content, R.id.review_by,
            R.id.no_trailers_tv, R.id.no_reviews_tv})
    List<TextView> contentLists;


    @Nullable
    @Bind({R.id.divider_content, R.id.divider_view, R.id.overview_divider_view_left, R.id
            .overview_divider_view_right, R.id.review_divider_view_left, R.id
            .review_divider_view_right, R.id.trailers_divider_view_left, R.id
            .trailers_divider_view_right, R.id.preview_details_container})
    List<View> dividerLists;
    // End Lists


    Movie mMovie = null;
    List<Review> mReviewList = null;
    List<Trailer> mTrailerList = null;


    private boolean isHideToolbarView = false;

    private FavouriteQueryHandler mFavouriteQueryHandler;

    public DetailFragment() {
        Debug.c();
    }


    @Override
    public void onAttach(Context context) {
        mFavouriteQueryHandler = new FavouriteQueryHandler(context.getContentResolver(), this);
        super.onAttach(context);
    }

    public void selectMovie() {
        Debug.c();
        fullContainer.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);
        floatingFavouriteActionButton.setVisibility(View.GONE);
        floatingShareActionButton.setVisibility(View.GONE);
        mDetailFragmentCoordinatorLayout.setBackgroundColor(ContextCompat.getColor(getActivity(),
                R.color.colorPrimary));
        Snackbar snackbar = Snackbar.make(mDetailFragmentCoordinatorLayout, getResources()
                        .getString(R.string.select_movie),
                Snackbar
                        .LENGTH_LONG);
        snackbar.show();
    }

    private void setFavouriteButtonImage() {
        if (isAlreadyFavouriteMovie) {
            floatingFavouriteActionButton.setImageResource(R.drawable.fav_selected);
        } else {
            floatingFavouriteActionButton.setImageResource(R.drawable.favourite);
        }
    }


    private void addFavourite() {
        if (mMovie != null) {
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

        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbarFragment);
        floatingFavouriteActionButton = (FloatingActionButton) rootView.findViewById(R.id.favourite_float_button);
        floatingShareActionButton = (FloatingActionButton) rootView.findViewById(R.id.share_float_button);
        floatingFavouriteActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavourite();
            }
        });
        floatingShareActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareMovie();
            }
        });

        fullContainer = (LinearLayout) rootView.findViewById(R.id.full_content_detail_fragment);
        mDetailFragmentCoordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.detail_fragment_coordinator_layout);
        mCollapsingToolbar = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);

        mAppBarLayout = (AppBarLayout) rootView.findViewById(R.id.app_bar_layout);

        toolbarHeaderView = (TitleTaglineView) rootView.findViewById(R.id.toolbar_header_view);

        posterImageView = (ImageView) rootView.findViewById(R.id.movie_poster_image);

        detailContainer = (NestedScrollView) rootView.findViewById(R.id.detail_content_fragment_nested_scroll);

        overviewTitleTv = (MovieTitleTextView) rootView.findViewById(R.id.overview_title);
        overviewYearTv = (MovieTaglineTextView) rootView.findViewById(R.id.overview_year);
        movieRatingsTv = (TextView) rootView.findViewById(R.id.detail_overview_card_ratings);
        movieOverviewContentTv = (TextView) rootView.findViewById(R.id.detail_overview_card_content);

        movieTimeTv = (TextView) rootView.findViewById(R.id.detail_time_tv);
        movieGenreTv = (TextView) rootView.findViewById(R.id.detail_genre_tv);

        reviewsProgressbar = (ProgressBar) rootView.findViewById(R.id.review_progressbar);

        noReviewsTv = (TextView) rootView.findViewById(R.id.no_reviews_tv);

        reviewsContainer = (LinearLayout) rootView.findViewById(R.id.reviews_container);

        trailerProgressbar = (ProgressBar) rootView.findViewById(R.id.trailers_progressbar);

        noTrailerTv = (TextView) rootView.findViewById(R.id.no_trailers_tv);

        trailerRecyclerView = (RecyclerView) rootView.findViewById(R.id.trailer_recyvlerview);

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void setupPosterImage(Bundle bundle) {
        if (mMovie != null) {
            Bitmap bitmap = (Bitmap) bundle.get(AppConstants
                    .MOVIE_BITMAP_SHARE);
            if (bitmap != null) {
                posterImageView.setImageBitmap(bitmap);
                setupDynamicColor(bitmap);
            }

            String posterUrl = MoviesConstants.API_IMAGE_BASE_URL + MoviesConstants
                    .IMAGE_SIZE_LARGE + mMovie.posterPath;

            Glide.with(getActivity())
                    .load(posterUrl).asBitmap()
                    .placeholder(posterImageView.getDrawable())
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            setupDynamicColor(resource);
                            return false;
                        }
                    })
                    .into(posterImageView);
        }

    }


    private void setupDynamicColor(Bitmap resource) {
        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Swatch darkVibrantSwatch = palette.getDarkVibrantSwatch();
                Swatch darkMutedSwatch = palette.getDarkMutedSwatch();
                Swatch lightVibrantSwatch = palette.getLightVibrantSwatch();
                Swatch lightMutedSwatch = palette.getLightMutedSwatch();

                Swatch backgroundAndContentColors = darkVibrantSwatch;
                if (backgroundAndContentColors == null) {
                    backgroundAndContentColors = darkMutedSwatch;
                }
                Swatch titleAndFabColors = lightVibrantSwatch;

                if (titleAndFabColors == null) {
                    titleAndFabColors = lightMutedSwatch;
                }
                setDarkColorWork(backgroundAndContentColors);
                setLightColorWork(titleAndFabColors);
            }
        });
    }

    private void setDarkColorWork(Swatch swatch) {
        if (swatch != null) {
            int color = swatch.getRgb();

            mStatusBarColor = color;

            // butterknife is throwing NPEs, so have to perform null check
            if (titleLists != null) {
                ButterKnife.apply(titleLists, titleTextColorChange, color);
            }
            if (dividerLists != null) {
                ButterKnife.apply(dividerLists, dividerColorChange, color);

            }
            floatingFavouriteActionButton.setBackgroundTintList(ColorStateList.valueOf(color));
            floatingShareActionButton.setBackgroundTintList(ColorStateList.valueOf(color));
            mToolbar.setBackgroundColor(color);
            mCollapsingToolbar.setStatusBarScrimColor(color);
        }

    }


    private void setLightColorWork(Swatch swatch) {
        if (swatch != null) {
            int color = swatch.getRgb();
            toolbarHeaderView.getTitle().setTextColor(color);
            toolbarHeaderView.getSubTitle().setTextColor(color);
            detailContainer.setBackgroundColor(color);
            changeBackgroundColorEvent(color);
            floatingFavouriteActionButton.setRippleColor(color);
            floatingShareActionButton.setRippleColor(color);
            reviewAuthorColor = swatch.getBodyTextColor();
            reviewContentColor = swatch.getBodyTextColor();

            if (extraDetailsList != null) {
                ButterKnife.apply(extraDetailsList, titleTextColorChange, color);
            }
            if (contentLists != null) {
                ButterKnife.apply(contentLists, titleTextColorChange, reviewContentColor);
            }
            paintAllReviews(reviewsContainer);
        }

    }


    public static final ButterKnife.Setter<TextView, Integer> titleTextColorChange = new ButterKnife
            .Setter<TextView, Integer>() {
        @Override
        public void set(TextView view, Integer value, int index) {
            view.setTextColor(value);
        }
    };

    public static final ButterKnife.Setter<View, Integer> dividerColorChange = new ButterKnife
            .Setter<View, Integer>() {
        @Override
        public void set(View view, Integer value, int index) {
            view.setBackgroundColor(value);
        }
    };

    private void setupDetails() {
        if (mMovie != null) {
            movieOverviewContentTv.setText(mMovie.overview);
            movieRatingsTv.setText(Double.toString(mMovie.averageVote));

            movieGenreTv.setText(MoviesConstants.getGenresList(mMovie.genreIds));

            overviewTitleTv.setText(mMovie.title);
            overviewYearTv.setText(mMovie.releaseDate);
            setUpFineDetails();
        }
    }

    private void setUpFineDetails() {
        if (mMovie != null) {
            int movieTime = mMovie.runtime;
            String toSet = "-";
            if (movieTime != 0) {
                toSet = Integer.toString(movieTime) + " mins";
            }
            movieTimeTv.setText(toSet);
            toolbarHeaderView.bindTo(mMovie.title, mMovie.tagline);
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mStatusBarColor = ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark);
        Bundle passedBundle = getArguments();
        if (passedBundle == null) {
            Debug.c();
            selectMovie();
            return;
        } else {
            mMovie = (Movie) passedBundle.get(AppConstants.MOVIE_SHARE);
            if (mMovie != null) {
                Debug.e("Movie detail : " + mMovie.title, false);
                setupPosterImage(passedBundle);
                setupDetails();
            } else {
                selectMovie();
                return;
            }
        }
        mAppBarLayout.addOnOffsetChangedListener(this);
        setFavouriteButtonImage();
        trailerAdapter = new TrailerViewAdapter(new ArrayList<Trailer>(),
                getActivity(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager
                .HORIZONTAL, false);
        trailerRecyclerView.setLayoutManager(layoutManager);
        trailerRecyclerView.setAdapter(trailerAdapter);
        trailerRecyclerView.setNestedScrollingEnabled(false);
    }


    @Override
    public void onResume() {
        super.onResume();
        Debug.c();
        if (mMovie != null) {
            if (toolbarShown) {
                showToolbar();
            } else {
                hideToolbar();
            }
            initLoaders();
        }
    }

    private void changeBackgroundColorEvent(int backgroundColor) {
        BusProvider.getInstance().post(new DetailBackgroundColorChangeEvent(backgroundColor));
    }

    private void initLoaders() {
        if (mMovie != null) {
/*            isAlreadyFavouriteMovie = MoviesConstants.isFavouriteMovie(mMovie.id, getActivity());
            setFavouriteButtonImage();*/
            // Todo : check why below implementation causes lag, though its not on ui thread, but
            // above one is.
            Uri uri = MovieContract.MovieEntry.buildMovieWithId(mMovie.id);
            Debug.e("check fav uri: " + uri, false);
            mFavouriteQueryHandler.startQuery(
                    0,
                    0,
                    uri,
                    MovieContract.MovieEntry.MOVIE_PROJECTION_GRID,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? LIMIT 1",
                    new String[]{Long.toString(mMovie.id)},
                    null
            );


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
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
                    reviewAuthor.setText(getResources().getString(R.string.review_by_author,
                            review.author));
                    reviewAuthor.setTextColor(reviewAuthorColor);
                    reviewContent.setText(review.content);
                    reviewContent.setTextColor(reviewContentColor);
                    reviewsContainer.addView(reviewView);
                    if (i < len - 1) {
                        final View dividerView = inflater.inflate(R.layout.divider_content,
                                reviewsContainer, false);
                        View divider = ButterKnife.findById(dividerView, R.id.divider_content);
                        divider.setBackgroundColor(mStatusBarColor);
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
        reviewsProgressbar.setVisibility(View.GONE);
    }

    private void paintAllReviews(ViewGroup viewGroup) {
        if (viewGroup != null) {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup) {
                    paintAllReviews((ViewGroup) view);
                } else if (view instanceof TextView) {
                    ((TextView) view).setTextColor(reviewContentColor);
                } else if (view != null) {
                    //divider
                    view.setBackgroundColor(mStatusBarColor);
                }
            }
        }

    }

    private void addTrailers(TrailerList result) {
        if (result.getError() == Error.SUCCESS) {
            List<Trailer> trailerList = result.results;
            mTrailerList = trailerList;
            if (!(trailerList == null || trailerList.isEmpty())) {
                noTrailerTv.setVisibility(View.GONE);
                trailerAdapter.addTrailer(trailerList);
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
        trailerProgressbar.setVisibility(View.GONE);
    }

    private void playTrailer(String key) {
        Snackbar snackbar = Snackbar.make(mDetailFragmentCoordinatorLayout, getResources()
                        .getString(R.string.playing_trailer),
                Snackbar
                        .LENGTH_LONG);
        snackbar.show();
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
            intent.putExtra("force_fullscreen", true);
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(MoviesConstants.TRAILER_VIDEO_URL + key));
            intent.putExtra("force_fullscreen", true);
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
        setFavouriteButtonImage();
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
            BusProvider.getInstance().post(new FavouriteMovieAddEvent(mMovie));
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
            BusProvider.getInstance().post(new FavouriteMovieDeleteEvent());
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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {

        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            toolbarShown = true;
            showToolbar();
        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarShown = false;
            hideToolbar();
        }
    }

    private void showToolbar() {
        mToolbar.setVisibility(View.VISIBLE);
        overviewTitleTv.setVisibility(View.INVISIBLE);
        toolbarHeaderView.setVisibility(View.VISIBLE);
        isHideToolbarView = !isHideToolbarView;
        floatingFavouriteActionButton.setVisibility(View.INVISIBLE);
        floatingShareActionButton.setVisibility(View.INVISIBLE);
        if (tinted) {
            tinted = false;
            GraphicsUtils.statusBarRemoveTint(getActivity());
        }
    }

    private void hideToolbar() {
        mToolbar.setVisibility(View.INVISIBLE);
        floatingFavouriteActionButton.setVisibility(View.VISIBLE);
        floatingShareActionButton.setVisibility(View.VISIBLE);
        overviewTitleTv.setVisibility(View.VISIBLE);
        toolbarHeaderView.setVisibility(View.INVISIBLE);
        isHideToolbarView = !isHideToolbarView;
        if (!tinted) {
            GraphicsUtils.statusBarTinted(getActivity());
            tinted = true;
        }
    }
}