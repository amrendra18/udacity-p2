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
public class ContentHeadingTextView extends TextView {

    public ContentHeadingTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public ContentHeadingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public ContentHeadingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontsCache.getTypeface(context, MoviesConstants.CONTENT_HEADING_FONT);
        setTypeface(customFont);
    }
}
