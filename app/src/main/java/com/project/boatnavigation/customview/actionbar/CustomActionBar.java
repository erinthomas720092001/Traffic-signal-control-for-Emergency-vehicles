package com.project.boatnavigation.customview.actionbar;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.project.boatnavigation.R;
import com.project.boatnavigation.controller.BaseActivity;
import com.project.boatnavigation.customview.button.RipplesButton;
import com.project.boatnavigation.customview.edittext.EditTextBase;
import com.project.boatnavigation.customview.textview.TextViewBase;
import com.project.boatnavigation.customview.textview.TitleTextView;
import com.project.boatnavigation.manager.Utils;


public class CustomActionBar {
    BaseActivity mActivity;
    int iconBack;

    public CustomActionBar(BaseActivity activity) {
        this.mActivity = activity;
        setUpActionBar(activity);
    }

    ActionBar actionBar;

    public ActionBar getActionBar() {
        return actionBar;
    }

    public void setActionBar(ActionBar actionBar) {
        this.actionBar = actionBar;
    }

    private void setUpActionBar(Activity activity) {
        // android.support.v7.app.ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();  //for higher version
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        ((AppCompatActivity) activity).setSupportActionBar(toolbar);
        actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        if (actionBar == null)
            return;
        actionBar.setDisplayShowCustomEnabled(true);
        View customView = activity.getLayoutInflater().inflate(R.layout.custom_action_bar_view, null);
        actionBar.setCustomView(customView);
        setViews(customView);
        activity.getWindow().getDecorView().findViewById(android.R.id.home);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setHomeAsUpIndicator(R.drawable.logo_back);
    }

    SearchClickListener onSearchInitialClickListenet;
    OnActionBarClickListener onActionBarClickListener;
    RelativeLayout rippleButtonBackLayout;
    RelativeLayout rippleSearchLayout;
    //RelativeLayout progressViewLayout;
    RelativeLayout rippleMenuItemLayout;
    RelativeLayout rippleMenuLayout;
    RelativeLayout titleEditTextRL;
    TitleTextView textViewTitleInActionBar;
    EditTextBase editTextSearch;
    //RipplesButton rippleButtonBackIcon;
    RipplesButton rippleSearch;
    RipplesButton rippleMenuItem;
    RipplesButton rippleMenu;
    EditorAction editorAction;
    TextViewBase textViewHintText;

    private void setViews(View customView) {
        rippleButtonBackLayout = (RelativeLayout) customView.findViewById(R.id.rippleButtonBackLayout);
        rippleSearchLayout = (RelativeLayout) customView.findViewById(R.id.rippleSearchLayout);
       // progressViewLayout = (RelativeLayout) customView.findViewById(R.id.progressViewLayout);
        rippleMenuItemLayout = (RelativeLayout) customView.findViewById(R.id.rippleMenuItemLayout);
        rippleMenuLayout = (RelativeLayout) customView.findViewById(R.id.rippleMenuLayout);
        titleEditTextRL = (RelativeLayout) customView.findViewById(R.id.titleEditTextRL);
        //progressViewLayout.setVisibility(View.GONE);

        textViewTitleInActionBar = (TitleTextView) customView.findViewById(R.id.textViewTitleInActionBar);

        textViewHintText = (TextViewBase) customView.findViewById(R.id.textViewHintText);
        editTextSearch = (EditTextBase) customView.findViewById(R.id.editTextSearch);
        editTextSearch.setTextColor(Color.WHITE);
        editTextSearch.setTextSize(16);
        editTextSearch.setVisibility(View.GONE);
        textViewHintText.setVisibility(View.GONE);
        //rippleButtonBackIcon = (RipplesButton) customView.findViewById(R.id.rippleButtonBackIcon);
        rippleSearch = (RipplesButton) customView.findViewById(R.id.rippleSearch);
        rippleMenuItem = (RipplesButton) customView.findViewById(R.id.rippleMenuItem);
        rippleMenu = (RipplesButton) customView.findViewById(R.id.rippleMenu);
        editorAction = new EditorAction();

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextSearch.getText().length() == 0) {
                    if(editTextSearch.getVisibility()==View.VISIBLE){
                        textViewHintText.setVisibility(View.VISIBLE);
                    }else{
                        textViewHintText.setVisibility(View.GONE);
                    }


                   // Log.v("LOG","08Nov2016 "+ " 2 textViewHintText VISIBLE");
                }else{
                    textViewHintText.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        editTextSearch.setTextWatchListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(fragment != null){
//                    if(fragment.getSelectedFragment() instanceof TrackersFragment){
//                        ((TrackersFragment)fragment.getSelectedFragment()).onSearchButtonClick(getSearchText());
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

    }

    public void setSearchHint(String searchHint) {
        textViewHintText.setText(searchHint);
    }

    public void setActionBarTitle(int title) {
        try {
            if (textViewTitleInActionBar != null)
                textViewTitleInActionBar.setText(mActivity.getResources().getString(title));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setActionBarTitle(String title) {
        textViewTitleInActionBar.setText(title);
    }

    public TitleTextView getActionBarTitleText() {
        return textViewTitleInActionBar;
    }

    public void setOnSearchClickListenet(SearchClickListener onSearchInitialClickListenet) {
        this.onSearchInitialClickListenet = onSearchInitialClickListenet;
    }

    public static interface SearchClickListener {
        public void searchInitialClicked();

        public void searchBackClicked();
    }

    public RipplesButton getRippleSearchView() {
        return rippleSearch;
    }


    public void onHomeButtonClicked() {
        Utils.hideSoftKeyboard(mActivity);
        if (editTextSearch.getVisibility() == View.VISIBLE) {
            restoreActionBarToNormal();
            if (onSearchInitialClickListenet != null)
                onSearchInitialClickListenet.searchBackClicked();
        } else {
//            editTextSearch.setOnEditorActionListener(null);
//            editTextSearch.setText("");
//            editTextSearch.setOnEditorActionListener(editorAction);
            onActionBarClickListener.onBackButtonCLick();
        }
    }

    public void restoreActionBarToNormal() {

        editTextSearch.setVisibility(View.GONE);
        textViewHintText.setVisibility(View.GONE);
        textViewTitleInActionBar.setVisibility(View.VISIBLE);
        editTextSearch.setText("");
        //rippleButtonBackIcon.setRippleImage(iconBack);
        if (actionBar != null)
            actionBar.setHomeAsUpIndicator(iconBack);
    }

    public class EditorAction implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                    (actionId == EditorInfo.IME_ACTION_DONE)) {
                doSearch();
            }
            return false;
        }
    }

    public void setOnActionListener(final OnActionBarClickListener onActionBarClickListener) {

        this.onActionBarClickListener = onActionBarClickListener;


        rippleSearch.setOnClickListener(new RipplesButton.OnClickListener() {
            @Override
            public void onClick(View view) {

                doSearch();
            }
        });
        editTextSearch.setOnEditorActionListener(editorAction);

        rippleMenuItem.setOnClickListener(new RipplesButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionBarClickListener.onMenuItemButtnCalick();
            }
        });
        rippleMenu.setOnClickListener(new RipplesButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionBarClickListener.onMenuMainButtonClick();
            }
        });
    }

    public void setTextWatcher(TextWatcher textWatcher) {
        editTextSearch.addTextChangedListener(textWatcher);
    }

    public String getSearchText() {
        return editTextSearch.getText().toString();
    }

    private void doSearch() {
        if (editTextSearch.getVisibility() == View.VISIBLE) {
            if (editTextSearch.getText().toString().length() > 0) {

                onActionBarClickListener.onSearchButtonClick(editTextSearch.getText().toString().replace(" ", "%20"));
                //editTextSearch.setText("");
                Utils.hideSoftKeyboard(mActivity);
            }


        } else {
            if (onSearchInitialClickListenet != null)
                onSearchInitialClickListenet.searchInitialClicked();
            editTextSearch.setVisibility(View.VISIBLE);
            textViewHintText.setVisibility(View.VISIBLE);
            //Log.v("LOG","08Nov2016 "+ " 1 textViewHintText VISIBLE");
            textViewTitleInActionBar.setVisibility(View.GONE);
            if (actionBar != null)
                actionBar.setHomeAsUpIndicator(R.drawable.logo_back);
            editTextSearch.requestFocus();
            Utils.showSoftKey(mActivity);

        }
    }

    public static interface OnActionBarClickListener {
        public void onBackButtonCLick();

        public void onSearchButtonClick(String key);

        public void onMenuItemButtnCalick();

        public void onMenuMainButtonClick();
    }

    public void setIcons(int icon_1, int icon_2, int icon_3, int icon_4) {
        if (actionBar == null)
            return;
        if (icon_1 == 0) {
            rippleButtonBackLayout.setVisibility(View.GONE);
            actionBar.setDisplayHomeAsUpEnabled(false);
        } else {
            iconBack = icon_1;
            actionBar.setHomeAsUpIndicator(icon_1);
            rippleButtonBackLayout.setVisibility(View.VISIBLE);
        }
        if (icon_2 == 0) {
            rippleSearchLayout.setVisibility(View.GONE);
            //Utils.startGoneAnimation(mActivity, rippleSearchLayout);
        } else {
            rippleSearch.setRippleImage(icon_2);
            rippleSearchLayout.setVisibility(View.VISIBLE);
        }
        if (icon_3 == 0) {
            rippleMenuItemLayout.setVisibility(View.GONE);
            //Utils.startGoneAnimation(mActivity, rippleMenuItemLayout);
        } else {
            rippleMenuItem.setRippleImage(icon_3);
            rippleMenuItemLayout.setVisibility(View.VISIBLE);
        }
        if (icon_4 == 0) {
            rippleMenuLayout.setVisibility(View.GONE);
            //Utils.startGoneAnimation(mActivity, rippleMenuLayout);
        } else {
            rippleMenu.setRippleImage(icon_4);
            rippleMenuLayout.setVisibility(View.VISIBLE);
        }
    }
}
