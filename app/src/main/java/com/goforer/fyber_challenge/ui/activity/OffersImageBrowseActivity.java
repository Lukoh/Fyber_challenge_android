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
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.goforer.base.ui.activity.BaseActivity;
import com.goforer.base.ui.view.SwipeViewPager;
import com.goforer.fyber_challenge.R;
import com.goforer.fyber_challenge.model.data.Gallery;
import com.goforer.fyber_challenge.ui.adapter.OffersImageBrowseAdapter;
import com.goforer.fyber_challenge.utility.ActivityCaller;

import java.util.List;

import butterknife.BindView;

public class OffersImageBrowseActivity extends BaseActivity {
    private static final int ALPHA_ANIMATION_DURATION = 700;

    private int mImagePosition;

    private List<Gallery> mImageList;

    @BindView(R.id.pager_browse)
    SwipeViewPager mSwipePager;
    @BindView(R.id.tv_number)
    TextView mNumber;
    @BindView(R.id.bg_mask)
    View mBgMask;
    @BindView(R.id.iv_image)
    ImageView mImage;

    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        mImageList = getIntent().getParcelableArrayListExtra(ActivityCaller.EXTRA_GALLERY_IMAGE_LIST);
        mImagePosition = getIntent().getIntExtra(ActivityCaller.EXTRA_GALLERY_IMAGE_POSITION, -1);

        super.onCreate(onSavedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_offers_image_browse);
    }

    @Override
    public void setViews(Bundle onSavedInstanceState) {
        runEnterAnimation();

        mNumber.setText((mImagePosition + 1) + "/" + mImageList.size());

        final OffersImageBrowseAdapter adapter = new OffersImageBrowseAdapter(this, mImageList);

        mSwipePager.setAdapter(adapter);

        mSwipePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mImagePosition = position;
                mNumber.setText((position + 1) + "/" + mImageList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //set current position
        mSwipePager.setCurrentItem(mImagePosition, false);
    }

    @Override
    public void onBackPressed() {
        runExitAnimation(mBgMask);

        Intent intent = new Intent();
        intent.putExtra(ActivityCaller.EXTRA_SELECTED_ITEM_POSITION, mImagePosition);
        this.setResult(RESULT_OK, intent);

        super.onBackPressed();
    }

    private void runEnterAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(ALPHA_ANIMATION_DURATION);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());
        mBgMask.startAnimation(alphaAnimation);
    }

    public void runExitAnimation(final View view) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(ALPHA_ANIMATION_DURATION);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBgMask.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mBgMask.startAnimation(alphaAnimation);
    }
}
