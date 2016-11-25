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

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
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
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.goforer.base.ui.activity.BaseActivity;
import com.goforer.base.ui.view.SwipeViewPager;
import com.goforer.fyber_challenge.R;
import com.goforer.fyber_challenge.model.action.BookmarkChangeAction;
import com.goforer.fyber_challenge.model.action.CommentLikeAction;
import com.goforer.fyber_challenge.model.action.SubscriptionChangeAction;
import com.goforer.fyber_challenge.model.data.Comment;
import com.goforer.fyber_challenge.model.data.Offers;
import com.goforer.fyber_challenge.model.event.LikeCommentEvent;
import com.goforer.fyber_challenge.ui.adapter.OffersInfoAdapter;
import com.goforer.fyber_challenge.ui.view.drawer.SlidingDrawer;
import com.goforer.fyber_challenge.utility.ActivityCaller;
import com.goforer.fyber_challenge.utility.CommonUtils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OffersInfoActivity extends BaseActivity {
    private static final String TAG = "OffersInfoActivity";
    private static final String TRANSITION_IMAGE = "OffersInfoActivity:image";

    private static final int PAGE_MARGIN_VALUE = 40;

    private List<Offers> mOffersItems;
    private SlidingDrawer mSlidingDrawer;
    private Menu mMenu;

    private ShareDialog mShareDialog;

    private int mItemPosition;
    private int mFrom;

    @BindView(R.id.pager_flip)
    SwipeViewPager mSwipePager;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar)
    AppBarLayout mAppBar;
    @BindView(R.id.backdrop)
    ImageView mBackdrop;
    @BindView(R.id.backdrop_new)
    ImageView mNewBackdrop;
    @BindView(R.id.fab_star)
    FloatingActionButton mFabStar;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
            showToastMessage(getString(R.string.toast_no_data));
        }

        mSlidingDrawer = new SlidingDrawer(this, SlidingDrawer.DRAWER_INFO_COMMENT_TYPE,
                R.id.container_info, savedInstanceState);

        super.onCreate(savedInstanceState);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
            /**
             *  In case this project, I put below 3 line code, programmatically at this time
             *  because there was no provided the data of {@link Comment} from Fyber server.
             *
             *  In real project, the data should be put automatically.
             *  It means the server have to provide comments data to App as the client.
             */
            mOffersItems.get(mItemPosition).setBookmarkedCount(306);
            mOffersItems.get(mItemPosition).setSubscribedCount(500);
            mOffersItems.get(mItemPosition).setGalleryCount(30);

            OffersInfoAdapter mAdapter = new OffersInfoAdapter(getSupportFragmentManager(), mOffersItems);
            mSwipePager.setAdapter(mAdapter);
            ViewCompat.setTransitionName(mSwipePager, TRANSITION_IMAGE);
            mSwipePager.setPageMargin(PAGE_MARGIN_VALUE);

            mCollapsingToolbarLayout.setTitle(mOffersItems.get(mItemPosition).getTitle());

            handleSwipePager();
            Glide.with(getApplicationContext()).load(mOffersItems.get(mItemPosition).getThumbnail()
                    .getHires()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    mBackdrop.setImageBitmap(resource);
                }
            });

            AppBarLayout.OnOffsetChangedListener listener
                    = new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    mCollapsingToolbarLayout.setTitle(mOffersItems.get(mItemPosition).getTitle());
                    if (mCollapsingToolbarLayout.getHeight() +
                            verticalOffset < 2 * ViewCompat
                            .getMinimumHeight(mCollapsingToolbarLayout)) {
                        mBackdrop.animate().alpha(0.3f).setDuration(600);
                    } else {
                        mBackdrop.animate().alpha(1f).setDuration(600);    // 1.0f means opaque
                    }
                }
            };

            mAppBar.addOnOffsetChangedListener(listener);

            FacebookSdk.sdkInitialize(getApplicationContext());
            mShareDialog = new ShareDialog(this);
        }
    }

    @Override
    protected void setActionBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        mCollapsingToolbarLayout.setTitle(mOffersItems.get(mItemPosition).getTitle());
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_USE_LOGO);
            actionBar.setTitle(getResources().getString(R.string.app_name));
            actionBar.setElevation(0);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);

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
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return super.onPrepareOptionsPanel(view, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.view_comments:
                if (mSlidingDrawer != null) {
                    mSlidingDrawer.getCommentsDrawer().openDrawer();
                }
                return true;
            case R.id.subscription:
                SubscriptionChangeAction action = new SubscriptionChangeAction();
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                    mOffersItems.get(mItemPosition).setSubscribed(false);
                    mOffersItems.get(mItemPosition).setSubscribedCount(
                            mOffersItems.get(mItemPosition).getSubscribedCount() - 1);
                    mSlidingDrawer.subtractSubscribedCount();
                    menuItem.setIcon(R.drawable.ic_menu_cart);
                    action.setSubscribed(false);
                    action.setPosition(mItemPosition);
                    showToastMessage(getString(R.string.toast_unsubscribed));
                } else {
                    menuItem.setChecked(true);
                    mOffersItems.get(mItemPosition).setSubscribed(true);
                    mOffersItems.get(mItemPosition).setSubscribedCount(
                            mOffersItems.get(mItemPosition).getSubscribedCount() + 1);
                    mSlidingDrawer.addSubscribedCount();
                    action.setSubscribed(true);
                    action.setPosition(mItemPosition);
                    menuItem.setIcon(R.drawable.ic_menu_subscribed);
                    showToastMessage(getString(R.string.toast_subscribed));
                }

                EventBus.getDefault().post(action);
                return true;
            case R.id.share_others:
                showAppListToShare();
                return true;
            case R.id.share_facebook:
                shareToFb();
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
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = mSlidingDrawer.getDrawer().saveInstanceState(outState);
        outState = mSlidingDrawer.getCommentsDrawer().saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    private void showSubscription() {
        if ((mFrom == ActivityCaller.FROM_OFFERS_LIST)
                || (mFrom == ActivityCaller.FROM_PROFILE_SUBSCRIPTION)) {
            mMenu.getItem(0).setVisible(true);
            if (mOffersItems.get(mItemPosition).isSubscribed()) {
                mMenu.getItem(0).setChecked(true);
                mMenu.getItem(0).setIcon(R.drawable.ic_menu_subscribed);
            } else {
                mMenu.getItem(0).setChecked(false);
                mMenu.getItem(0).setIcon(R.drawable.ic_menu_cart);
            }
        } else {
            mMenu.getItem(0).setVisible(false);
        }
    }

    private void shareToFb() {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(mOffersItems.get(mItemPosition).getTitle())
                    .setContentDescription(mOffersItems.get(mItemPosition).getTeaser())
                    .setContentUrl(Uri.parse(mOffersItems.get(mItemPosition).getLink()))
                    .setImageUrl(Uri.parse(mOffersItems.get(mItemPosition).getThumbnail().getHires()))
                    .build();

            mShareDialog.show(linkContent);
        }

    }

    private void showAppListToShare() {
        List<Intent> targetedShareIntents = new ArrayList<>();

        Intent googlePlusIntent = getShareIntent("com.google.android.apps.plus",
                mOffersItems.get(mItemPosition).getTitle(),
                mOffersItems.get(mItemPosition).getTitle() + "\n\n" +
                        mOffersItems.get(mItemPosition).getTeaser() + "\n\n" +
                        mOffersItems.get(mItemPosition).getThumbnail().getHires() + "\n\n" +
                        mOffersItems.get(mItemPosition).getLink());
        if (googlePlusIntent != null) {
            targetedShareIntents.add(googlePlusIntent);
        }

        Intent twitterIntent = getShareIntent("twitter",
                mOffersItems.get(mItemPosition).getTitle(),
                mOffersItems.get(mItemPosition).getTeaser() + "\n\n" +
                        mOffersItems.get(mItemPosition).getThumbnail().getHires() + "\n\n" +
                        mOffersItems.get(mItemPosition).getLink());
        if (twitterIntent != null) {
            targetedShareIntents.add(twitterIntent);
        }

        Intent whatsAppIntent = getShareIntent("com.whatsapp",
                mOffersItems.get(mItemPosition).getTitle(),
                mOffersItems.get(mItemPosition).getTeaser() + "\n\n" +
                        mOffersItems.get(mItemPosition).getThumbnail().getHires() + "\n\n" +
                        mOffersItems.get(mItemPosition).getLink());
        if (whatsAppIntent != null) {
            targetedShareIntents.add(whatsAppIntent);
        }

        Intent hangoutIntent = getShareIntent("com.google.android.talk",
                mOffersItems.get(mItemPosition).getTitle(),
                mOffersItems.get(mItemPosition).getTeaser() + "\n\n" +
                        mOffersItems.get(mItemPosition).getThumbnail().getHires() + "\n\n" +
                        mOffersItems.get(mItemPosition).getLink());
        if (hangoutIntent != null) {
            targetedShareIntents.add(hangoutIntent);
        }

        Intent kakaotalkIntent = getShareIntent("com.kakao.talk",
                mOffersItems.get(mItemPosition).getTitle(), "\n\n" +
                        mOffersItems.get(mItemPosition).getTeaser() + "\n\n" +
                        mOffersItems.get(mItemPosition).getThumbnail().getHires() + "\n\n" +
                        mOffersItems.get(mItemPosition).getLink());
        if (kakaotalkIntent != null) {
            targetedShareIntents.add(kakaotalkIntent);
        }

        Intent lineIntent = getShareIntent("jp.naver.line.android",
                mOffersItems.get(mItemPosition).getTitle(),
                mOffersItems.get(mItemPosition).getTeaser() + "\n\n" +
                        mOffersItems.get(mItemPosition).getThumbnail().getHires() + "\n\n" +
                        mOffersItems.get(mItemPosition).getLink());
        if (lineIntent != null) {
            targetedShareIntents.add(lineIntent);
        }

        Intent gmailIntent = getShareIntent("gmail",
                mOffersItems.get(mItemPosition).getTitle(),
                mOffersItems.get(mItemPosition).getTeaser() + "\n\n" +
                        mOffersItems.get(mItemPosition).getThumbnail().getHires() + "\n\n" +
                        mOffersItems.get(mItemPosition).getLink());
        if (gmailIntent != null) {
            targetedShareIntents.add(gmailIntent);
        }

        Intent chooser = Intent.createChooser(targetedShareIntents.remove(0),
                getResources().getString(R.string.offer_info_view_share));
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                targetedShareIntents.toArray(new Parcelable[]{}));

        chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(chooser);
    }

    private Intent getShareIntent(String type, String subject, String text) {
        boolean found = false;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");

        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type) ||
                        info.activityInfo.name.toLowerCase().contains(type)) {
                    ActivityInfo activity = info.activityInfo;
                    ComponentName name = new ComponentName(
                            activity.applicationInfo.packageName, activity.name);
                    share.setComponent(name);
                    share.addCategory(Intent.CATEGORY_DEFAULT);
                    share.putExtra(Intent.EXTRA_SUBJECT, subject);
                    share.putExtra(Intent.EXTRA_TEXT, text);
                    found = true;
                    break;
                }
            }

            if (!found) {
                return null;
            }

            return share;
        }
        return null;
    }

    private void showNewBackDropImage(int position) {
        Glide.with(getApplicationContext()).load(mOffersItems.get(position).getThumbnail()
                .getHires()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mNewBackdrop.setImageBitmap(resource);
            }
        });
    }

    private void setImage(int position) {
        Glide.with(getApplicationContext()).load(mOffersItems.get(position).getThumbnail()
                .getHires()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mBackdrop.setImageBitmap(resource);
            }
        });

        if (position == mOffersItems.size() - 1) {
            showNewBackDropImage(position);
        } else {
            showNewBackDropImage(position + 1);
        }
    }

    private void setTitle(final int position, final float positionOffset) {
        if (positionOffset > 0.5) {
            if (position == mOffersItems.size() - 1) {
                mCollapsingToolbarLayout.setTitle(mOffersItems.get(position).getTitle());
            } else {
                mCollapsingToolbarLayout.setTitle(mOffersItems.get(position + 1).getTitle());
            }
        } else {
            mCollapsingToolbarLayout.setTitle(mOffersItems.get(position).getTitle());
        }
    }

    private void showToastMessage(String phrase) {
        Toast.makeText(getApplicationContext(), phrase, Toast.LENGTH_SHORT).show();
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
                        mOffersItems.get(mItemPosition).setBookmarkedCount(
                                mOffersItems.get(mItemPosition).getBookmarkedCount() - 1);
                        mSlidingDrawer.subtractBookmarkedCount();
                        action.setBookmarked(false);
                        action.setPosition(mItemPosition);
                        mFabStar.setImageDrawable(new IconicsDrawable(v.getContext(),
                                GoogleMaterial.Icon.gmd_favorite).actionBar().color(Color.WHITE));
                        showToastMessage(getString(R.string.toast_bookmark_removed));

                    } else {
                        mOffersItems.get(mItemPosition).setBookmarked(true);
                        mOffersItems.get(mItemPosition).setBookmarkedCount(
                                mOffersItems.get(mItemPosition).getBookmarkedCount() + 1);
                        mSlidingDrawer.addBookmarkedCount();
                        action.setBookmarked(true);
                        action.setPosition(mItemPosition);
                        mFabStar.setImageDrawable(new IconicsDrawable(v.getContext(),
                                GoogleMaterial.Icon.gmd_favorite).actionBar().color(Color.RED));
                        showToastMessage(getString(R.string.toast_bookmarked));
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

        //mActionBar.setTitle(mOffersItems.get(mItemPosition).getTitle());
        mSwipePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int displace = -positionOffsetPixels;
                int displaceNew = -positionOffsetPixels + mSwipePager.getWidth();

                if (positionOffset > 0.999) {
                    mBackdrop.setX(0);
                    mNewBackdrop.setX(0);
                } else {
                    setTitle(position, positionOffset);
                    mBackdrop.setX(displace);
                    mNewBackdrop.setX(displaceNew);
                }

                setImage(position);
            }

            @Override
            public void onPageSelected(int position) {
                mItemPosition = position;
                showSubscription();
                if (mOffersItems.get(mItemPosition).isBookmarked()) {
                    mFabStar.setImageDrawable(new IconicsDrawable(getBaseContext(),
                            GoogleMaterial.Icon.gmd_favorite).actionBar().color(Color.RED));
                } else {
                    mFabStar.setImageDrawable(new IconicsDrawable(getBaseContext(),
                            GoogleMaterial.Icon.gmd_favorite).actionBar().color(Color.WHITE));
                }

                /**
                 *  In case this project, I put below 3 line code, programmatically at this time
                 *  because there was no provided the data of {@link Comment} from Fyber server.
                 *
                 *  In real project, the data should be put automatically.
                 *  It means the server have to provide comments data to App as the client.
                 */
                mOffersItems.get(mItemPosition).setBookmarkedCount(306);
                mOffersItems.get(mItemPosition).setSubscribedCount(500);
                mOffersItems.get(mItemPosition).setGalleryCount(30);

                mSwipePager.setCurrentItem(position, false);
                mSlidingDrawer.setDrawerInfo(mOffersItems.get(mItemPosition));

                mCollapsingToolbarLayout.setTitle(mOffersItems.get(mItemPosition).getTitle());
                mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources()
                        .getColor(R.color.md_white_1000));
                mCollapsingToolbarLayout.setExpandedTitleColor(getResources()
                        .getColor(R.color.md_white_1000));
                Log.d(TAG, "called onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void changeComment() {
        List<Comment> comments = new ArrayList<>();
        /**
         *  In case this project, I put the data of {@link Comment} into the list, List<Comment> mComments
         *  in {@link Offers} class, programmatically at this time because there was no provided
         *  the data of {@link Comment} from Fyber server.
         *
         *  In real project, the data of {@link Comment} should be put into to the list,
         *  List<Comment> mComments in {@link Offers} class, automatically.
         *  It means the server have to provide comments data to App as the client.
         *  It's very important to display the data of Comments on the list of Drawer's comments.
         */
        for (int i = 0; i < 10; i++) {
            Comment comment = new Comment();
            comment.setCommentId(i);
            comment.setOfferId(mOffersItems.get(mItemPosition).getOfferId());
            comment.setDate(CommonUtils.getCurrentDateTime());
            comment.setCommentId(123456789 + i);
            if (comment.getPicture() == null) {
                Comment.Picture picture = new Comment.Picture();
                if (i % 2 == 0) {
                    comment.setCommenterId(414343978);
                    comment.setCommenterName("Paul");
                    comment.setComment("I love it. It has good shape and color. I recommend you to use it.");
                    comment.setCommentLikeCount(16);
                    picture.setCommenterPictureUrl("https://github.com/Lukoh/Fyber_challenge_android/blob/master/profile.jpg?raw");
                } else {
                    comment.setCommenterId(115681534);
                    comment.setCommenterName("Lukoh");
                    comment.setComment("I like it. It's fit in me and comfortable.");
                    comment.setCommentLikeCount(12);
                    picture.setCommenterPictureUrl("https://raw.githubusercontent.com/Lukoh/Fyber_challenge_android/master/profile.jpg");
                }

                comment.setPicture(picture);
            }

            comments.add(comment);
        }

        mOffersItems.get(mItemPosition).setComments(comments);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAction(CommentLikeAction action) {
        Toast.makeText(getApplicationContext(), getString(R.string.like_phrase),
                Toast.LENGTH_SHORT).show();

        changeComment();
        // To update comments
        mSlidingDrawer.setDrawerType(SlidingDrawer.DRAWER_INFO_COMMENT_TYPE);
        mSlidingDrawer.setDrawerInfo(mOffersItems.get(mItemPosition));
        /**
         * In this case, Fyber server does not support Like function. So the below code is commented.
         * Remove the commented code in the real project.
         */
        /*
        LikeCommentEvent event = new LikeCommentEvent(true);
        Intermediary.INSTANCE.postLikeComment(this, mOffersItems.get(mItemPosition).getOfferId(),
                action.getCommenterId(), action.getCommentId(), event);
        */
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(LikeCommentEvent event) {
        //TODO:: In case of real project, Parsing json data and put into the object of Comment class
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("OffersInfo Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
}
