package com.project.boatnavigation.customview.textview;

import android.content.Context;
import android.util.AttributeSet;

import com.project.boatnavigation.R;


public class MediumTextViewSecondary extends TextViewBase {

    public MediumTextViewSecondary(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MediumTextViewSecondary(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
//
//    public MediumTextViewSecondary(Context context) {
//        super(context);
//        init();
//    }

    private void init() {
        setTextColor(getResources().getColor(R.color.app_label_color_secondary));
        // setTextSize(17);
    }
}

