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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.goforer.base.ui.activity.BaseActivity;
import com.goforer.base.ui.view.SwipeViewPager;
import com.goforer.fyber_challenge_android.R;
import com.goforer.fyber_challenge_android.model.data.Offers;
import com.goforer.fyber_challenge_android.ui.adapter.OffersInfoAdapter;
import com.goforer.fyber_challenge_android.ui.view.SlidingDrawer;
import com.goforer.fyber_challenge_android.utility.ActivityCaller;

import java.util.List;

import butterknife.BindView;

public class OffersInfoActivity extends BaseActivity {
    private static final String TAG = "OffersInfoActivity";
    private static final String TRANSITION_IMAGE = "OffersInfoActivity:image";

    private static final int PAGE_MARGIN_VALUE = 40;

    private Offers mOffers;
    private List<Offers> mOffersItems;
    private ActionBar mActionBar;
    private SlidingDrawer mSlidingDrawer;

    private int mItemPosition;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.backdrop)
    ImageView mBackDrop;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.pager_flip)
    SwipeViewPager mSwipePager;
    @BindView(R.id.tv_title)
    TextView mTitleView;
    @BindView(R.id.tv_offer_id)
    TextView mOfferIdView;
    @BindView(R.id.tv_payout)
    TextView mPayoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * For using ViewPager in OffersInfoActivity, the list of Offers and
         * the position of an item have been passed into OffersInfoActivity.
         * It means that I'm going to put ViewPager and implement some module to allow a user to see
         * each Offers's information by flipping left and right through pages of data.
         */
        mItemPosition = getIntent().getIntExtra(ActivityCaller.EXTRA_OFFERS_ITEM_POSITION, -1);
        mOffersItems = getIntent().getExtras().getParcelableArrayList(
                ActivityCaller.EXTRA_OFFERS_LIST);

        /*
        // To set Offers's information using GSon from the String to  of an object of Offers.
        String infoString = getIntent().getStringExtra(Offers.class.getName());
        if (!TextUtils.isEmpty(infoString)) {
            mOffers = Offers.gson().fromJson(infoString, Offers.class);
        }
        */

        if (mOffersItems != null && mItemPosition != -1) {
            mOffers = mOffersItems.get(mItemPosition);
        } else {
            Toast.makeText(this, getString(R.string.toast_no_offers), Toast.LENGTH_SHORT).show();
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
    protected void setViews(Bundle savedInstanceState) {
        if (mOffersItems != null && mItemPosition != -1) {
            OffersInfoAdapter adapter = new OffersInfoAdapter(getSupportFragmentManager(), mOffersItems);
            mSwipePager.setAdapter(adapter);
            ViewCompat.setTransitionName(mSwipePager, TRANSITION_IMAGE);
            mSwipePager.setPageMargin(PAGE_MARGIN_VALUE);

            handleSwipePager();

            mSlidingDrawer = new SlidingDrawer(this, SlidingDrawer.DRAWER_INFO_TYPE,
                    R.id.content_holder, savedInstanceState);
            Glide.with(this).load(mOffersItems.get(mItemPosition).getThumbnail().getHires())
                    .centerCrop().into(mBackDrop);
        }
    }

    @Override
    protected void setActionBar() {
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mCollapsingToolbarLayout.setTitle(mOffersItems.get(mItemPosition).getTitle());
        if (mActionBar != null) {
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_USE_LOGO);
            mActionBar.setTitle(getResources().getString(R.string.app_name));
            mActionBar.setElevation(0);
            mActionBar.setDisplayShowTitleEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(false);
            mActionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_offer_info_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.back:
                onBackPressed();
                return true;
            case R.id.star:
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                    menuItem.setIcon(R.drawable.ic_menu_star);
                } else {
                    menuItem.setChecked(true);
                    menuItem.setIcon(R.drawable.ic_menu_starred);
                }

                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
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
        Intent intent = new Intent();
        intent.putExtra(ActivityCaller.EXTRA_SELECTED_ITEM_POSITION, mItemPosition);
        this.setResult(RESULT_OK, intent);

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = mSlidingDrawer.getDrawer().saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    private void handleSwipePager() {
        mSwipePager.setCurrentItem(mItemPosition, false);
        mSwipePager.setOnSwipeOutListener(new SwipeViewPager.OnSwipeOutListener() {
            @Override
            public void onSwipeOutAtStart() {
            }

            @Override
            public void onSwipeOutAtEnd() {
            }
        });

        mActionBar.setTitle(mOffersItems.get(mItemPosition).getTitle());
        mTitleView.setText(mOffersItems.get(mItemPosition).getTitle());

        mSwipePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mItemPosition = position;
                mActionBar.setTitle(mOffersItems.get(mItemPosition).getTitle());
                mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.gray));
                Glide.with(mCurrentActivity).load(mOffersItems.get(mItemPosition).getThumbnail().getLowres())
                        .centerCrop().into(mBackDrop);
                mTitleView.setText(mOffersItems.get(mItemPosition).getTitle());
                mOfferIdView.setText(String.valueOf(mOffersItems.get(mItemPosition).getOfferId()));
                mPayoutView.setText(String.valueOf(mOffersItems.get(mItemPosition).getPayout()));
                Log.d(TAG, "called onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
