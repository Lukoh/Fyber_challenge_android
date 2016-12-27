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
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.goforer.base.ui.view.SwipeViewPager;
import com.goforer.fyber_challenge.R;
import com.goforer.fyber_challenge.model.data.Gallery;
import com.goforer.fyber_challenge.ui.adapter.OffersImageBrowseAdapter;
import com.goforer.fyber_challenge.ui.transition.ImageBrowseSharedElementEnterCallback;
import com.goforer.fyber_challenge.utility.ActivityCaller;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OffersImageBrowseActivity extends AppCompatActivity {
    private int mImagePosition;
    private int mInitialPosition;

    private List<Gallery> mImageList;

    private final View.OnClickListener navigationOnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishAfterTransition();
                }
            };
    private ImageBrowseSharedElementEnterCallback sharedElementCallback;

    @BindView(R.id.pager_browse)
    SwipeViewPager mSwipePager;
    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    @BindView(R.id.tv_number)
    TextView mNumber;

    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        setContentView(R.layout.activity_offers_image_browse);

        postponeEnterTransition();

        TransitionSet transitions = new TransitionSet();
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.setInterpolator(AnimationUtils.loadInterpolator(this,
                android.R.interpolator.linear_out_slow_in));
        slide.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
        transitions.addTransition(slide);
        transitions.addTransition(new Fade());
        getWindow().setEnterTransition(transitions);

        sharedElementCallback = new ImageBrowseSharedElementEnterCallback(getIntent());
        setEnterSharedElementCallback(sharedElementCallback);

        mImageList = getIntent().getParcelableArrayListExtra(ActivityCaller.EXTRA_GALLERY_IMAGE_LIST);
        mInitialPosition = getIntent().getIntExtra(ActivityCaller.EXTRA_GALLERY_IMAGE_POSITION, -1);
        mImagePosition = mInitialPosition;

        super.onCreate(onSavedInstanceState);
        ButterKnife.bind(this);

        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_USE_LOGO);
            actionBar.setElevation(0);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        mToolBar.setNavigationOnClickListener(navigationOnClickListener);

        setUpViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mNumber.setText((mImagePosition + 1) + "/" + mImageList.size());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(ActivityCaller.EXTRA_SELECTED_ITEM_POSITION, mInitialPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mInitialPosition = savedInstanceState.getInt(
                ActivityCaller.EXTRA_SELECTED_ITEM_POSITION, 0);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        setActivityResult();

        super.onBackPressed();
    }

    @Override
    public void finishAfterTransition() {
        setActivityResult();

        super.finishAfterTransition();
    }

    private void setUpViewPager() {
        mSwipePager.setAdapter(new OffersImageBrowseAdapter(this, mImageList,
                sharedElementCallback));
        //set current position
        mSwipePager.setCurrentItem(mImagePosition, false);
        mSwipePager.setPageScrolled(false);
        mSwipePager.setOnSwipeOutListener(new SwipeViewPager.OnSwipeOutListener() {
            @Override
            public void onSwipeOutAtStart() {
            }

            @Override
            public void onSwipeOutAtEnd() {
            }

            @Override
            public void onSwipeLeft(float x, float y) {

            }

            @Override
            public void onSwipeRight(float x, float y) {

            }

            @Override
            public void onSwipeDown(float x, float y) {
                finishAfterTransition();
            }

            @Override
            public void onSwipeUp(float x, float y) {
            }
        });

        mSwipePager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mSwipePager.getChildCount() > 0) {
                    mSwipePager.removeOnLayoutChangeListener(this);
                    startPostponedEnterTransition();
                }
            }
        });

        mSwipePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mSwipePager.setPageScrolled(true);
            }

            @Override
            public void onPageSelected(int position) {
                mImagePosition = position;
                mSwipePager.setCurrentItem(position, false);
                mNumber.setText((position + 1) + "/" + mImageList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mSwipePager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.padding_mini));
        mSwipePager.setPageMarginDrawable(R.drawable.page_margin);
    }

    private void setActivityResult() {
        if (mInitialPosition == mSwipePager.getCurrentItem()) {
            setResult(RESULT_OK);
            return;
        }

        Intent intent = new Intent();
        int item = mSwipePager.getCurrentItem();
        intent.putExtra(ActivityCaller.EXTRA_SELECTED_ITEM_POSITION, item);
        setResult(RESULT_OK, intent);
    }
}
