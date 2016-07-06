/*
 * Copyright (C) 2016 Lukoh Nam, goForer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
