package com.project.boatnavigation.customview.textview;

import android.content.Context;
import android.util.AttributeSet;

import com.project.boatnavigation.R;


public class SmallTextView extends TextViewBase {
    public SmallTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SmallTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

//    public SmallTextView(Context context) {
//        super(context);
//        init();
//    }

    private void init(){
        setTextColor(getResources().getColor(R.color.app_small_label_color));
    }
}
