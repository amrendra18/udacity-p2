package com.amrendra.popularmovies.app.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.amrendra.popularmovies.utils.FontsCache;
import com.amrendra.popularmovies.utils.MoviesConstants;

/**
 * Created by Amrendra Kumar on 21/12/15.
 */
public class MovieTaglineTextView extends TextView {

    public MovieTaglineTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public MovieTaglineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public MovieTaglineTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontsCache.getTypeface(context, MoviesConstants.MOVIE_SUBTITLE_FONT);
        setTypeface(customFont);
    }
}
