package com.project.boatnavigation.customview.textview;

import android.content.Context;
import android.util.AttributeSet;

import com.project.boatnavigation.R;


public class TitleTextView extends TextViewBase {

    public TitleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

//    public TitleTextView(Context context) {
//        super(context);
//        init();
//    }

    private void init() {
        setTextColor(getResources().getColor(R.color.app_title_bar_text_color));
        // setTextSize(17);
    }
}
