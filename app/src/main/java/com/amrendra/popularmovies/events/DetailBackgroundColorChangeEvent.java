package com.amrendra.popularmovies.events;

/**
 * Created by Amrendra Kumar on 18/12/15.
 */
public final class DetailBackgroundColorChangeEvent {

    int bgColor;

    public DetailBackgroundColorChangeEvent(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getBgColor() {
        return bgColor;
    }
}
