package com.project.boatnavigation.customview.textview;

import android.content.Context;
import android.util.AttributeSet;

import com.project.boatnavigation.R;


public class MediumTextViewTertiary  extends TextViewBase{
    public MediumTextViewTertiary(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MediumTextViewTertiary(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        setTextColor(getResources().getColor(R.color.app_label_medium_color_tertiary));
        // setTextSize(17);
    }
}
