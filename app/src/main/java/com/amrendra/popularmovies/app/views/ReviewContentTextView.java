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
public class ReviewContentTextView extends TextView {

    public ReviewContentTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public ReviewContentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public ReviewContentTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontsCache.getTypeface(context, MoviesConstants.OVERVIEW_CONTENT_FONT);
        setTypeface(customFont);
    }
}
