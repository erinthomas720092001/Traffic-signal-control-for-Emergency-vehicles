package com.project.boatnavigation.customview.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.material.textfield.TextInputLayout;
import com.project.boatnavigation.R;
import com.project.boatnavigation.manager.Utils;


public class FloatingEditTextExt extends RelativeLayout {
    Context context;
    View v;

    public FloatingEditTextExt(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public FloatingEditTextExt(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    AppCompatEditText appCompatEditText;
    TextInputLayout textInputLayout;
    float textSize = 1;
    float textPaddingRight = 1;
    int infoTextSize = 1;
    String infoText = "";
    String inputType = "text";
    boolean counterEnabled = false;
    int counterMax = 0;
    boolean requestFocus = false;
    private OnFocusChangeListener onFocusChangeListener;
    public static final int TEXT_GRAVITY_LEFT = 1;
    public static final int TEXT_GRAVITY_CENTER = 2;
    private int textGravity;

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.vp_custom_floating_edit_text, this);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.FloatingEditTextExt);
        setAppCompatEditText((AppCompatEditText) findViewById(R.id.floating_ext_app_comp_edit_id));
        setTextInputLayout((TextInputLayout) findViewById(R.id.floating_ext_text_input_layout));

        // getAppCompatEditText().getBackground().setColorFilter(Color.parseColor("#696969"), PorterDuff.Mode.SRC_ATOP);

        try {

            infoText = a.getString(R.styleable.FloatingEditTextExt_float_textinfo);
            textSize = a.getDimension(R.styleable.FloatingEditTextExt_float_text_size_float, 16);
            textPaddingRight = a.getDimension(R.styleable.FloatingEditTextExt_text_padding_right, 0);
            infoTextSize = a.getInteger(R.styleable.FloatingEditTextExt_float_info_text_size_float, 16);
            inputType = a.getString(R.styleable.FloatingEditTextExt_float_text_input_type);
            counterEnabled = a.getBoolean(R.styleable.FloatingEditTextExt_float_counter_enabled, false);
            counterMax = a.getInteger(R.styleable.FloatingEditTextExt_float_counter_max, 0);
            requestFocus = a.getBoolean(R.styleable.FloatingEditTextExt_float_requestFocus, false);
            switch (inputType) {
                case "text":
                    getAppCompatEditText().setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case "textCapWords":
                    getAppCompatEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                    break;
                case "numberSigned":
                    getAppCompatEditText().setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_FLAG_SIGNED);
                    break;
                case "unSignedNumber":
                    getAppCompatEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case "numberDecimal":
                    getAppCompatEditText().setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    break;
                case "unSignedDecimal":
                    getAppCompatEditText().setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    break;
                case "textCapSentences":
                    getAppCompatEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                    break;
                case "emailAddress":
                    getAppCompatEditText().setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    break;
                case "textPassword":
                    getAppCompatEditText().setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    break;
                case "phone":
                    getAppCompatEditText().setInputType(InputType.TYPE_CLASS_PHONE);
                    //counterMax = counterMax > 0 ? counterMax : MOBILE_NO_MAX_LENGTH;
                    break;
                case "textMultiLine":
                    getAppCompatEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES |
                            InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
                    getAppCompatEditText().setSingleLine(false);
                    getAppCompatEditText().setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                    getAppCompatEditText().setMaxLines(8);
                    break;
                default:
                    getAppCompatEditText().setInputType(InputType.TYPE_CLASS_TEXT);

            }
            if (requestFocus) {
                getAppCompatEditText().requestFocus();
            }

            textGravity = a.getInteger(R.styleable.FloatingEditTextExt_float_text_gravity, TEXT_GRAVITY_LEFT);
            setTextGravity(textGravity);
//             getAppCompatEditText().setHint(infoText);
            getTextInputLayout().setHint(infoText);
            getTextInputLayout().setCounterEnabled(counterEnabled);
            getTextInputLayout().setCounterMaxLength(counterMax);
            int dp = (int) (textSize / getResources().getDisplayMetrics().density);
            getAppCompatEditText().setTextSize(dp);
            int paddingRight = (int) (textPaddingRight/ getResources().getDisplayMetrics().density);
            getAppCompatEditText().setPadding(
                    getAppCompatEditText().getPaddingLeft(),
                    getAppCompatEditText().getPaddingTop(),
                    getAppCompatEditText().getPaddingRight() + paddingRight,
                    getAppCompatEditText().getPaddingBottom());
            Typeface tf  = Typeface.createFromAsset(context.getAssets(),
                    context.getResources().getString(R.string.app_font_path));
            getAppCompatEditText().setTypeface(tf);
            getTextInputLayout().setTypeface(tf);


        } finally {
//            if(counterEnabled && counterMax>0) {      // Changing condition to apply filters without showing the counter
            if(counterMax>0) {
                getAppCompatEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(counterMax)});
            }
            a.recycle();
        }
        setAction();

    }

    public void setInputFilter(final String blockCharacterSet ){
        InputFilter filter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                if (source != null && blockCharacterSet.contains(("" + source))) {
                    return "";
                }
                return null;
            }
        };
        appCompatEditText.setFilters(new InputFilter[] { filter });
    }

    public void setInputType(int type) {
        getAppCompatEditText().setInputType(type);
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }

    public String getText() {
        return getAppCompatEditText().getText().toString();
    }

    public void setText(String text) {
        getAppCompatEditText().setText(text);
    }

    public AppCompatEditText getEditText() {
        return getAppCompatEditText();
    }

    public void counterTextAppearance(int max) {
        getTextInputLayout().setCounterMaxLength(max);
    }

    public void setCounterEnabled(boolean enable) {
        getTextInputLayout().setCounterEnabled(enable);
    }

    public void setErrorEnabled(boolean errorEnabled) {
        getTextInputLayout().setErrorEnabled(errorEnabled);
    }

    public void setHintTextAppearance(int resId) {
        getTextInputLayout().setHintTextAppearance(resId);
    }

    public void setHintTextAppearance(String resId) {
        getTextInputLayout().setHint(resId);
    }

    public String getHintText() {
        return getTextInputLayout().getHint() == null ? "" : getTextInputLayout().getHint().toString();
    }

    public void setError(CharSequence error) {
//        Log.v("LOG", "02Jan2016 " + " setError error " + error);
        getTextInputLayout().setError(error);
//        int ecolor = Color.parseColor("#00FF00"); // whatever color you want
//
//        ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
//        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(error);
//        ssbuilder.setSpan(fgcspan, 0, error.length(), 0);
//        getEditText().setError(Html.fromHtml("<font color='red'>Password can't be empty</font>"));

    }

    public void setValidateResult(boolean flag, String error) {
        setError(error);
    }

    public boolean isTextShowError() {
        return getTextInputLayout().isErrorEnabled();
    }

    private void setAction() {
        getAppCompatEditText().setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(onFocusChangeListener != null) {
                    onFocusChangeListener.onFocusChange(v,hasFocus);
                }
            }
        });
        getAppCompatEditText().addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (textWatcher != null) {
                    textWatcher.beforeTextChanged(s, start, count, after);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.v("LOG", "02Jan2016 " + " count " + count);
                if (isTextShowError() && count > 0) {
                    getAppCompatEditText().getBackground().setColorFilter
                            (Utils.getColor(context, R.color.app_label_color_secondary), PorterDuff.Mode.SRC_ATOP);
                    getTextInputLayout().setError("");
                    //getTextInputLayout().setErrorEnabled(false);
                    Log.v("LOG", "02Jan2016 " + " setError empty ");
                } else {
//                    getAppCompatEditText().getBackground().setColorFilter
//                            (Utils.getColor(context,R.color.app_label_color_secondary), PorterDuff.Mode.SRC_ATOP);
                }

                if (textWatcher != null) {
                    textWatcher.onTextChanged(s, start, before, count);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                getTextInputLayout().setError("");
                if (textWatcher != null) {
                    textWatcher.afterTextChanged(s);
                }
            }
        });
    }
    TextWatcher textWatcher;


    public void addTextChangedListener(TextWatcher textWatcher) {
        this.textWatcher = textWatcher;
    }

    public interface TextWatcher {
        void beforeTextChanged(CharSequence s, int start, int count, int after);

        void onTextChanged(CharSequence s, int start, int before, int count);

        void afterTextChanged(Editable s);

    }

    public AppCompatEditText getAppCompatEditText() {
        return appCompatEditText;
    }

    public void setAppCompatEditText(AppCompatEditText appCompatEditText) {
        this.appCompatEditText = appCompatEditText;
    }

    public TextInputLayout getTextInputLayout() {
        return textInputLayout;
    }

    public void setTextInputLayout(TextInputLayout textInputLayout) {
        this.textInputLayout = textInputLayout;
    }

    public void setEnabled(boolean enabled) {
        int color;
        if(enabled) {
            color = R.color.app_label_color_secondary;
        } else {
            color = R.color.floating_edit_text_disabled_color;
        }
        getAppCompatEditText().setEnabled(enabled);
        getAppCompatEditText().setTextColor(Utils.getColor(getContext(), color));
    }

    public void setEditable(boolean editable) {
        getAppCompatEditText().setEnabled(editable);
    }

    public void clearFocus() {
        getAppCompatEditText().clearFocus();
    }

    public void setSelection(int index) {
        getAppCompatEditText().setSelection(index);
    }
    public void setHint(CharSequence hint) {
        getAppCompatEditText().setHint(hint);
    }

    public void setTextGravity(int textGravity) {
        int gravity, alignment;
        switch (textGravity) {
            case TEXT_GRAVITY_CENTER:
                gravity = Gravity.CENTER;
                alignment = TEXT_ALIGNMENT_CENTER;
                break;
            case TEXT_GRAVITY_LEFT:
            default:
                gravity = Gravity.LEFT;
                alignment = TEXT_ALIGNMENT_TEXT_START;
        }

        getAppCompatEditText().setGravity(gravity);
        getAppCompatEditText().setTextAlignment(alignment);
    }
}
