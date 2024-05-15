package com.project.boatnavigation.customview.textview;

import android.content.Context;
import android.util.AttributeSet;

import com.project.boatnavigation.R;


public class MediumTextView extends TextViewBase {

    public MediumTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MediumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

//    public MediumTextView(Context context) {
//        super(context);
//        init();
//    }

    private void init() {
        setTextColor(getResources().getColor(R.color.app_label_color));


       // setTextSize(17);
    }
}
