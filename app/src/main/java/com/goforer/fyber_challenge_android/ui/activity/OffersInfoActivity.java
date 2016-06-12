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
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;

import com.goforer.base.ui.activity.BaseActivity;
import com.goforer.base.ui.view.SwipeViewPager;
import com.goforer.fyber_challenge_android.R;
import com.goforer.fyber_challenge_android.model.data.Offers;
import com.goforer.fyber_challenge_android.ui.adapter.OffersInfoAdapter;
import com.goforer.fyber_challenge_android.utility.ActivityCaller;
import com.sembozdemir.viewpagerarrowindicator.library.ViewPagerArrowIndicator;

import java.util.List;

import butterknife.BindView;

public class OffersInfoActivity extends BaseActivity {
    private static final String TAG = "OffersInfoActivity";
    private static final String TRANSITION_IMAGE = "OffersInfoActivity:image";

    private static final int PAGE_MARGIN_VALUE = 40;

    private Offers mOffers;
    private List<Offers> mItems;
    private ActionBar mActionBar;

    private int mPosition;

    @BindView(R.id.pager_flip)
    SwipeViewPager mSwipePager;
    @BindView(R.id.viewPagerArrowIndicator)
    ViewPagerArrowIndicator mViewPagerArrowIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String infoString = getIntent().getStringExtra(Offers.class.getName());

        /**
         * For using ViewPager in OffersInfoActivity, the list of Offers and
         * the position of an item have been passed into OffersInfoActivity.
         * It means that I'm going to put ViewPager and implement some module to allow a user to see
         * each Offers's information by flipping left and right through pages of data.
         */
        mPosition = getIntent().getIntExtra(ActivityCaller.EXTRA_OFFERS_ITEMS_POSITION, -1);
        mItems = this.getIntent().getExtras().getParcelableArrayList(
                ActivityCaller.EXTRA_OFFERS_LIST);

        if (!TextUtils.isEmpty(infoString)) {
            mOffers = Offers.gson().fromJson(infoString, Offers.class);
        }

        if (mItems != null && mPosition != -1) {
            mOffers = mItems.get(mPosition);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_offers_info);
    }

    @Override
    protected void setViews() {
        OffersInfoAdapter adapter = new OffersInfoAdapter(getSupportFragmentManager(), mItems);
        mSwipePager.setAdapter(adapter);
        ViewCompat.setTransitionName(mSwipePager, TRANSITION_IMAGE);
        mSwipePager.setPageMargin(PAGE_MARGIN_VALUE);

        handleSwipePager();
    }

    @Override
    protected void setActionBar() {
        super.setActionBar();

        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_USE_LOGO);
            mActionBar.setTitle(mOffers.getTitle());
            mActionBar.setElevation(0);
            mActionBar.setDisplayShowTitleEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void setEffectIn() {
        Log.i(TAG, "setEffectIn");

        overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.scale_down_exit);
    }

    @Override
    protected void setEffectOut() {
        Log.i(TAG, "setEffectOut");

        overridePendingTransition(R.anim.scale_up_enter, R.anim.slide_out_to_bottom);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void handleSwipePager() {
        mSwipePager.setCurrentItem(mPosition, false);
        mViewPagerArrowIndicator.bind(mSwipePager);
        mViewPagerArrowIndicator.setArrowIndicatorRes(R.drawable.arrowleft,
                R.drawable.arrowright);
        mSwipePager.setOnSwipeOutListener(new SwipeViewPager.OnSwipeOutListener() {
            @Override
            public void onSwipeOutAtStart() {
            }

            @Override
            public void onSwipeOutAtEnd() {
            }
        });

        mActionBar.setTitle(mItems.get(mPosition).getTitle());

        mSwipePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                mActionBar.setTitle(mItems.get(position).getTitle());
                Log.d(TAG, "called onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
