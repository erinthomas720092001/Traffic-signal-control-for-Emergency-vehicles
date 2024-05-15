package com.project.boatnavigation.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.project.boatnavigation.appconstant.Constant;
import com.project.boatnavigation.customview.actionbar.CustomActionBar;


public abstract class BaseActivity extends AppCompatActivity implements  Constant {
    CustomActionBar actionBar;
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.v("CLASS", "CLASS " +this.getClass().getCanonicalName());
        setContentView(getLayoutResourceId());
        setUpActionBar();
    }

    public CustomActionBar  getCustomActionBar(){
        return actionBar;
    }

    int _ToolIcons[];
    private void setToolBarButton() {
        _ToolIcons = setToolBarIcons();
    }

    protected void setActionBarIcons(Integer... icons){
        int icn[] = new int[4];
        for(int i=0;i<4;i++) {
            if(icons.length > i)
                icn[i] = icons[i];
        }
        actionBar.setIcons(icn[0], icn[1], icn[2], icn[3]);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                actionBar.onHomeButtonClicked();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
    private void setUpActionBar() {

        actionBar = new CustomActionBar(this);
        if (actionBar.getActionBar() == null)
            return;
        setToolBarButton();

        actionBar.setIcons(_ToolIcons[0], _ToolIcons[1], _ToolIcons[2], _ToolIcons[3]);
        actionBar.setActionBarTitle(setActionBarTitle());


        actionBar.setOnActionListener(new CustomActionBar.OnActionBarClickListener() {
            @Override
            public void onBackButtonCLick() {

                BaseActivity.this.onBackButtonCLick();
            }

            @Override
            public void onSearchButtonClick(String key) {
                BaseActivity.this.onSearchButtonClick(key);

            }

            @Override
            public void onMenuItemButtnCalick() {
                BaseActivity.this.onMenuItemButtnCalick();

            }

            @Override
            public void onMenuMainButtonClick() {
                BaseActivity.this.onMenuMainButtonClick();

            }
        });
        actionBar.setOnSearchClickListenet(new CustomActionBar.SearchClickListener() {
            @Override
            public void searchInitialClicked() {
                BaseActivity.this.searchInitialClicked();

            }

            @Override
            public void searchBackClicked() {
                BaseActivity.this.searchBackClicked();
            }
        });

    }




    protected abstract void searchInitialClicked();

    protected abstract void searchBackClicked();

    protected abstract void onBackButtonCLick();

    protected abstract void onSearchButtonClick(String key);

    protected abstract void onMenuItemButtnCalick();

    protected abstract void onMenuMainButtonClick();

    protected abstract int getLayoutResourceId();

    protected abstract String getActivityId();

    protected abstract int setActionBarTitle();

    protected abstract int[] setToolBarIcons();
}
