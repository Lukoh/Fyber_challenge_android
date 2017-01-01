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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.transition.Transition;
import android.view.ViewTreeObserver;

import com.goforer.base.ui.activity.BaseActivity;
import com.goforer.base.ui.holder.DefaultViewHolder;
import com.goforer.fyber_challenge.R;
import com.goforer.fyber_challenge.ui.adapter.OffersGalleryAdapter;
import com.goforer.fyber_challenge.ui.fragment.OffersGalleryFragment;
import com.goforer.fyber_challenge.ui.transition.ImageBrowseSharedElementEnterCallback;
import com.goforer.fyber_challenge.ui.transition.TransitionCallback;
import com.goforer.fyber_challenge.utility.ActivityCaller;

public class OffersGalleryActivity extends BaseActivity {
    private String mOffersTitle;

    private final Transition.TransitionListener sharedExitListener =
            new TransitionCallback() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    ActivityCompat.setExitSharedElementCallback(OffersGalleryActivity.this, null);
                }
            };

    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        mOffersTitle = getIntent().getStringExtra(ActivityCaller.EXTRA_OFFERS_TITLE);

        super.onCreate(onSavedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_gallery);
        postponeEnterTransition();
        // Listener to reset shared element exit transition callbacks.
        getWindow().getSharedElementExitTransition().addListener(sharedExitListener);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
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

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        postponeEnterTransition();

        final OffersGalleryFragment fragment =
                (OffersGalleryFragment)getFragment(OffersGalleryFragment.class);

        // Start the postponed transition when the recycler view is ready to be drawn.
        fragment.getRecyclerView().getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                fragment.getRecyclerView().getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return true;
            }
        });

        if (data == null) {
            return;
        }

        int position = data.getIntExtra(ActivityCaller.EXTRA_SELECTED_ITEM_POSITION, 0);

        fragment.getRecyclerView().scrollToPosition(position);

        OffersGalleryAdapter.GalleryContentViewHolder holder;

        if (fragment.getRecyclerView().findViewHolderForAdapterPosition(position)
                instanceof DefaultViewHolder) {
            return;
        } else {
            holder = (OffersGalleryAdapter.GalleryContentViewHolder) fragment.getRecyclerView().
                            findViewHolderForAdapterPosition(position);
        }

        if (holder == null) {
            return;
        }

        ImageBrowseSharedElementEnterCallback callback =
                new ImageBrowseSharedElementEnterCallback(getIntent());
        callback.setViewBinding(holder.getView().findViewById(R.id.iv_content),
                holder.getView().findViewById(R.id.tv_title));
        setExitSharedElementCallback(callback);

        /*
        if (position == fragment.getAdapter().getItems().size() - 1) {
            //: do something...
        }
        */
    }
}
