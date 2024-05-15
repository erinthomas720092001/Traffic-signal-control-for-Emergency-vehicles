package com.project.boatnavigation.customview.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.boatnavigation.R;
import com.project.boatnavigation.customview.textview.RipplesButtonTextView;


//http://developer.android.com/training/custom-views/create-view.html
//http://kevindion.com/2011/01/custom-xml-attributes-for-android-widgets/
public class RipplesButton extends RelativeLayout {
    Context context;

    //CardView cardView;
    PaperButton paperButton;
    RippleView rippleView;

    ImageView imageView;

    RipplesButtonTextView textView;
    View v;

    public RipplesButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public RipplesButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.custom_ripples_button, this);
        setImageView((ImageView) v.findViewById(R.id.imageView));
        setTextView((RipplesButtonTextView) v.findViewById(R.id.textView));
        setRippleView((RippleView) findViewById(R.id.rippleView));
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RipplesButton);
        int drwable = R.drawable.loading;
        int color = 0xffffff;
        int text_color = 0xffffff;
        int textSize=1;
        //cardView = (CardView) v.findViewById(R.id.card_view_inripple);
        paperButton = (PaperButton) v.findViewById(R.id.paper_button);
        String text;
        int rippleShape = 0xffffff;
        try {
            drwable = a.getResourceId(R.styleable.RipplesButton_bg_src, 0);
            color = a.getInteger(R.styleable.RipplesButton_bg_color, 0xffffff);
            text = a.getString(R.styleable.RipplesButton_text);
            rippleShape = a.getInteger(R.styleable.RipplesButton_ripple_shape, 1);
            text_color = a.getInteger(R.styleable.RipplesButton_text_color, 0xffffff);
            textSize=a.getInteger(R.styleable.RipplesButton_text_size,18);
        } finally {
            a.recycle();
        }
        //paperButton.setTextColor(text_color);
        textView.setTextColor(text_color);
        textView.setTextSize(textSize);
        //imageView.setBackgroundColor(color);
        paperButton.setColor(color);
        imageView.setImageResource(drwable);
        if (text != null) {
            textView.setText(text);
            // paperButton.setText(text);
        }
        if (rippleShape == 0) {
            rippleView.setRippleType(RippleView.RippleType.DOUBLE);
            paperButton.setVisibility(INVISIBLE);
            // cardView.setCardBackgroundColor(Color.TRANSPARENT);
            // cardView.setCardElevation(0);


        } else {
            rippleView.setVisibility(INVISIBLE);
        }

    }

    public void setRippleImage(int drawable) {
        imageView.setImageResource(drawable);
    }

    public void setOnClickListener(final OnClickListener onClickListener) {
        rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClick(v);
            }
        });
        paperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClick(v);
            }
        });
    }

    public static interface OnClickListener {
        public void onClick(View view);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }


    public RippleView getRippleView() {
        return rippleView;
    }

    public void setRippleView(RippleView rippleView) {
        this.rippleView = rippleView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(RipplesButtonTextView textView) {
        this.textView = textView;
    }
}
