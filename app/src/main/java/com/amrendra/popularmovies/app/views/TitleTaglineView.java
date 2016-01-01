package com.amrendra.popularmovies.app.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amrendra.popularmovies.R;
import com.amrendra.popularmovies.logger.Debug;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Amrendra Kumar on 21/12/15.
 */
public class TitleTaglineView extends LinearLayout {

    @Bind(R.id.header_view_title)
    TextView title;

    @Bind(R.id.header_view_sub_title)
    TextView subTitle;


    public TitleTaglineView(Context context) {
        super(context);
    }

    public TitleTaglineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleTaglineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TitleTaglineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void bindTo(String title) {
        bindTo(title, "");
    }

    public void bindTo(String title, String subTitle) {
        hideOrSetText(this.title, title);
        hideOrSetText(this.subTitle, subTitle);
    }

    private void hideOrSetText(TextView tv, String text) {
        if (text == null || text.equals("")) {
            tv.setVisibility(INVISIBLE);
        } else {
            tv.setText(text);
            if (tv.getVisibility() != VISIBLE) {
                tv.setVisibility(VISIBLE);
            }
        }
        Debug.e("TAG : " + text, false);
    }
}

