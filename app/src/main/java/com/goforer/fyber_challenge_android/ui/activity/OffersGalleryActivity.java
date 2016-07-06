package com.goforer.fyber_challenge_android.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.goforer.base.ui.activity.BaseActivity;
import com.goforer.fyber_challenge_android.R;
import com.goforer.fyber_challenge_android.ui.fragment.OffersGalleryFragment;
import com.goforer.fyber_challenge_android.utility.ActivityCaller;

public class OffersGalleryActivity extends BaseActivity {
    private long mOffersId;

    private String mOffersTitle;


    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        mOffersId = getIntent().getLongExtra(ActivityCaller.EXTRA_OFFERS_ID, -1);
        mOffersTitle = getIntent().getStringExtra(ActivityCaller.EXTRA_OFFERS_TITLE);

        super.onCreate(onSavedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_gallery);
    }

    @Override
    public void setViews(Bundle onSavedInstanceState) {
        transactFragment(OffersGalleryFragment.class, R.id.content_holder, null);
    }

    @Override
    protected void setActionBar() {
        super.setActionBar();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_USE_LOGO);
            actionBar.setTitle(
                    mOffersTitle + "'s " + getResources().getString(R.string.gallery));
            actionBar.setElevation(0);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public long getOffersId() {
        return mOffersId;
    }
}
