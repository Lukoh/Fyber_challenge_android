/*
 * Copyright (C) 2015-2016 Lukoh Nam, goForer
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

package com.goforer.fyber_challenge.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.goforer.base.ui.activity.BaseActivity;
import com.goforer.fyber_challenge.R;
import com.goforer.fyber_challenge.model.action.MoveImageAction;
import com.goforer.fyber_challenge.ui.fragment.OffersGalleryFragment;
import com.goforer.fyber_challenge.utility.ActivityCaller;

import org.greenrobot.eventbus.EventBus;

public class OffersGalleryActivity extends BaseActivity {
    private String mOffersTitle;


    @Override
    public void onCreate(Bundle onSavedInstanceState) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case ActivityCaller.SELECTED_ITEM_POSITION:
                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra(
                            ActivityCaller.EXTRA_SELECTED_ITEM_POSITION, -1);
                    if (position != -1) {
                        MoveImageAction action = new MoveImageAction();
                        action.setPosition(position);
                        EventBus.getDefault().post(action);
                    }
                }
                break;
            default:
        }
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
}
