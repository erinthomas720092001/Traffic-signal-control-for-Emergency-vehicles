package com.project.boatnavigation.customview.edittext;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;

import com.project.boatnavigation.R;


public class EditTextBase extends androidx.appcompat.widget.AppCompatEditText {
    float TEXT_SIZE=20;
    public EditTextBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init( context);
    }
    public EditTextBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init( context);
    }
    public EditTextBase(Context context) {
        super(context);
        init( context);
    }

    private void init(Context context){
        Typeface tf = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.app_font_path));
        setTypeface(tf, Typeface.NORMAL);

        //this.setTextSize(TEXT_SIZE);
    }

    public float getTextSize(){
        return  TEXT_SIZE;
    }

    /**
     * You can  set the focus on your EditText
     * */
    public void setFocus(){
        this.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
    }
}
