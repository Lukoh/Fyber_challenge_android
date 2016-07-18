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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.goforer.base.ui.activity.BaseActivity;
import com.goforer.base.ui.view.SwipeViewPager;
import com.goforer.fyber_challenge_android.R;
import com.goforer.fyber_challenge_android.model.action.BookmarkChangeAction;
import com.goforer.fyber_challenge_android.model.action.SubscriptionChangeAction;
import com.goforer.fyber_challenge_android.model.data.Offers;
import com.goforer.fyber_challenge_android.ui.adapter.OffersInfoAdapter;
import com.goforer.fyber_challenge_android.ui.view.drawer.SlidingDrawer;
import com.goforer.fyber_challenge_android.utility.ActivityCaller;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

public class OffersInfoActivity extends BaseActivity {
    private static final String TAG = "OffersInfoActivity";
    private static final String TRANSITION_IMAGE = "OffersInfoActivity:image";

    private static final int PAGE_MARGIN_VALUE = 40;

    private List<Offers> mOffersItems;
    private ActionBar mActionBar;
    private SlidingDrawer mSlidingDrawer;
    private Menu mMenu;

    private int mItemPosition;
    private int mFrom;

    @BindView(R.id.pager_flip)
    SwipeViewPager mSwipePager;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.backdrop)
    ImageView mBackdrop;
    @BindView(R.id.backdrop_new)
    ImageView mNewBackdrop;
    @BindView(R.id.fab_star)
    FloatingActionButton mFabStar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * For using ViewPager in OffersInfoActivity, the list of Offers and
         * the position of an item have been passed into OffersInfoActivity.
         * It means that I'm going to put ViewPager and implement some module to allow a user to see
         * each Offers's information by flipping left and right through pages of data.
         */
        mFrom = getIntent().getIntExtra(ActivityCaller.EXTRA_FROM, -1);
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

        if (mOffersItems == null && mItemPosition == -1) {
            Toast.makeText(this, getString(R.string.toast_no_offers), Toast.LENGTH_SHORT).show();
        }

        mSlidingDrawer = new SlidingDrawer(this, SlidingDrawer.DRAWER_INFO_TYPE,
                R.id.container_info, savedInstanceState);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSlidingDrawer.setDrawerInfo(mOffersItems.get(mItemPosition));
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
            OffersInfoAdapter mAdapter = new OffersInfoAdapter(getSupportFragmentManager(), mOffersItems);
            mSwipePager.setAdapter(mAdapter);
            ViewCompat.setTransitionName(mSwipePager, TRANSITION_IMAGE);
            mSwipePager.setPageMargin(PAGE_MARGIN_VALUE);

            handleSwipePager();
            Glide.with(getApplicationContext()).load(mOffersItems.get(mItemPosition).getThumbnail()
                    .getHires()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    mBackdrop.setImageBitmap(resource);
                }
            });

            mSlidingDrawer = new SlidingDrawer(this, SlidingDrawer.DRAWER_INFO_TYPE,
                    R.id.content_holder, savedInstanceState);
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
        mMenu = menu;
        showSubscription();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.back:
                onBackPressed();
                return true;
            case R.id.subscription:
                SubscriptionChangeAction action = new SubscriptionChangeAction();
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                    mOffersItems.get(mItemPosition).setSubscribed(false);
                    menuItem.setIcon(R.drawable.ic_menu_subscribe);
                    action.setSubscribed(false);
                    action.setPosition(mItemPosition);
                    Toast.makeText(this, getString(R.string.toast_unsubscribed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    menuItem.setChecked(true);
                    mOffersItems.get(mItemPosition).setSubscribed(true);
                    action.setSubscribed(true);
                    action.setPosition(mItemPosition);
                    menuItem.setIcon(R.drawable.ic_menu_subscribed);
                    Toast.makeText(this, getString(R.string.toast_subscribed),
                            Toast.LENGTH_SHORT).show();
                }

                EventBus.getDefault().post(action);
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


    private void showSubscription() {
        if ((mFrom == ActivityCaller.FROM_OFFERS_LIST)
            || (mFrom == ActivityCaller.FROM_PROFILE_SUBSCRIPTION)) {
            mMenu.getItem(1).setVisible(true);
            if (mOffersItems.get(mItemPosition).isSubscribed()) {
                mMenu.getItem(1).setChecked(true);
                mMenu.getItem(1).setIcon(R.drawable.ic_menu_subscribed) ;
            } else {
                mMenu.getItem(1).setChecked(false);
                mMenu.getItem(1).setIcon(R.drawable.ic_menu_subscribe) ;
            }
        } else {
            mMenu.getItem(1).setVisible(false);
        }
    }

    private void setImage(int position) {
        Glide.with(getApplicationContext()).load(mOffersItems.get(position).getThumbnail()
                .getHires()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mBackdrop.setImageBitmap(resource);
            }
        });

        Glide.with(getApplicationContext()).load(mOffersItems.get(position + 1).getThumbnail()
                .getHires()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mNewBackdrop.setImageBitmap(resource);
            }
        });
    }

    private void handleSwipePager() {
        if ((mFrom == ActivityCaller.FROM_OFFERS_LIST)
                || (mFrom == ActivityCaller.FROM_PROFILE_BOOKMARK)) {
            mFabStar.setVisibility(View.VISIBLE);
            if (mOffersItems.get(mItemPosition).isBookmarked()) {
                mFabStar.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_favorite)
                        .actionBar().color(Color.RED));
            } else {
                mFabStar.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_favorite)
                        .actionBar().color(Color.WHITE));
            }

            mFabStar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    BookmarkChangeAction action = new BookmarkChangeAction();
                    if (mOffersItems.get(mItemPosition).isBookmarked()) {
                        mOffersItems.get(mItemPosition).setBookmarked(false);
                        action.setBookmarked(false);
                        action.setPosition(mItemPosition);
                        mFabStar.setImageDrawable(new IconicsDrawable(v.getContext(),
                                GoogleMaterial.Icon.gmd_favorite).actionBar().color(Color.WHITE));
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.toast_bookmark_removed),
                                Toast.LENGTH_SHORT).show();

                    } else {
                        mOffersItems.get(mItemPosition).setBookmarked(true);
                        action.setBookmarked(true);
                        action.setPosition(mItemPosition);
                        mFabStar.setImageDrawable(new IconicsDrawable(v.getContext(),
                                GoogleMaterial.Icon.gmd_favorite).actionBar().color(Color.RED));
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_bookmarked),
                                Toast.LENGTH_SHORT).show();
                    }

                    EventBus.getDefault().post(action);
                }
            });
        } else {
            mFabStar.setVisibility(View.GONE);
        }

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
        mSwipePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int displace = -positionOffsetPixels;
                int displaceNew = -positionOffsetPixels + mSwipePager.getWidth();

                if (positionOffset > 0.99) {
                    mBackdrop.setX(0);
                    mNewBackdrop.setX(0);
                } else {
                    mBackdrop.setX(displace);
                    mNewBackdrop.setX(displaceNew);

                }

                setImage(position);
            }

            @Override
            public void onPageSelected(int position) {
                mItemPosition = position;
                mCollapsingToolbarLayout.setTitle(mOffersItems.get(mItemPosition).getTitle());
                mCollapsingToolbarLayout.setExpandedTitleColor(getResources()
                        .getColor(R.color.md_white_1000));
                showSubscription();
                if (mOffersItems.get(mItemPosition).isBookmarked()) {
                    mFabStar.setImageDrawable(new IconicsDrawable(getBaseContext(),
                            GoogleMaterial.Icon.gmd_favorite).actionBar().color(Color.RED));
                } else {
                    mFabStar.setImageDrawable(new IconicsDrawable(getBaseContext(),
                            GoogleMaterial.Icon.gmd_favorite).actionBar().color(Color.WHITE));
                }

                mSlidingDrawer.setDrawerInfo(mOffersItems.get(mItemPosition));

                mSwipePager.setCurrentItem(position, false);

                Log.d(TAG, "called onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
