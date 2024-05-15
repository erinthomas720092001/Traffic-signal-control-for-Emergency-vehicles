package com.project.boatnavigation.customview.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.project.boatnavigation.R;


public class TextViewBase extends AppCompatTextView {
    TextView textViewBase;
    int fontStyle=Typeface.NORMAL;
    private Typeface tf;
    public TextViewBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public TextViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

//    public TextViewBase(Context context) {
//        super(context);
//        //init(context, null);
//    }

    private void init(Context context,AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TextViewBase);
        try {
            fontStyle=a.getInteger(R.styleable.TextViewBase_font_style, Typeface.NORMAL);
        } finally {
            a.recycle();
        }
       /* LayoutInflater inflater = LayoutInflater.from(context);
        TextViewBase.this = (TextViewBase) inflater.inflate(R.layout.custom_text_view_base,null);*/
        setFontStyle(fontStyle);
    }

    public void setFontStyle(int fontStyle) {
        switch(fontStyle) {
            case Typeface.BOLD:
                tf  = Typeface.createFromAsset(getContext().getAssets(),
                        getContext().getResources().getString(R.string.app_font_path_Bold));
                break;
            case Typeface.BOLD_ITALIC:
                tf  = Typeface.createFromAsset(getContext().getAssets(),
                        getContext().getResources().getString(R.string.app_font_path_BoldItalic));
                break;
            case Typeface.ITALIC:
                tf  = Typeface.createFromAsset(getContext().getAssets(),
                        getContext().getResources().getString(R.string.app_font_path_Italic));
                break;
            default:
                tf  = Typeface.createFromAsset(getContext().getAssets(),
                        getContext().getResources().getString(R.string.app_font_path));
        }
        super.setTypeface(tf);
    }
    public TextView getTextView(){
        return  textViewBase;
    }
}
