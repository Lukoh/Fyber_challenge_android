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

package com.goforer.fyber_challenge_android.ui.view.drawer;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.goforer.base.ui.activity.BaseActivity;
import com.goforer.fyber_challenge_android.R;
import com.goforer.fyber_challenge_android.model.data.Event;
import com.goforer.fyber_challenge_android.model.data.Offers;
import com.goforer.fyber_challenge_android.model.data.Profile;
import com.goforer.fyber_challenge_android.ui.view.drawer.model.drawer.CustomCountPanelDrawableItem;
import com.goforer.fyber_challenge_android.ui.view.drawer.model.drawer.MenuDrawerItem;
import com.goforer.fyber_challenge_android.ui.view.drawer.model.drawer.SecondaryIconDrawerItem;
import com.goforer.fyber_challenge_android.utility.ActivityCaller;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;

public class SlidingDrawer {
    public static final int DRAWER_PROFILE_TYPE = 0;
    public static final int DRAWER_INFO_TYPE = 1;

    private static final int CUSTOM_ITEM_BOOKMARK_TYPE = 0;
    private static final int CUSTOM_ITEM_SUBSCRIPTION_TYPE = 1;
    private static final int CUSTOM_ITEM_EVENT_TYPE = 2;

    private static final int DRAWER_PROFILE_ITEM_IDENTIFIER_ID = 1;
    private static final int DRAWER_PROFILE_ITEM_BOOKMARK_ID = DRAWER_PROFILE_ITEM_IDENTIFIER_ID;
    private static final int DRAWER_PROFILE_ITEM_SUBSCRIPTION_ID
            = DRAWER_PROFILE_ITEM_IDENTIFIER_ID + 1;

    private static final int DRAWER_INFO_ITEM_FIRST_IDENTIFIER_ID = 100;
    private static final int DRAWER_INFO_ITEM_FIRST_EVENT_ID = DRAWER_INFO_ITEM_FIRST_IDENTIFIER_ID;
    private static final int DRAWER_INFO_ITEM_FIRST_GALLERY_ID
            = DRAWER_INFO_ITEM_FIRST_IDENTIFIER_ID + 1;

    private static final int DRAWER_INFO_SECONDARY_FIRST_DRAWER_ITEM_ID = 2000;
    private static final int DRAWER_INFO_SECONDARY_SECOND_DRAWER_ITEM_ID = 3000;
    private static final int DRAWER_PROFILE_SECONDARY_FIRST_DRAWER_ITEM_ID = 4000;
    private static final int DRAWER_STICKY_FIRST_ITEM_ID = 5000;
    private static final int DRAWER_INFO_STICKY_LINK_ID = DRAWER_STICKY_FIRST_ITEM_ID;
    private static final int DRAWER_INFO_STICKY_HELP_ID = DRAWER_STICKY_FIRST_ITEM_ID + 1;

    private static final int SECONDARY_DRAWER_LEVEL = 2;

    private static final String HELP_URL = "https://github.com/Lukoh/Fyber_challenge_android";


    private AccountHeader mHeader = null;
    private Drawer mDrawer = null;
    private Profile mProfile;
    private Offers mOffers;
    private BaseActivity mActivity;
    private Bundle mBundle;

    private int mType;
    private int mRootViewRes;

    public SlidingDrawer(final BaseActivity activity, final int type, int rootViewRes,
                         @Nullable Bundle savedInstanceState) {
        mActivity = activity;
        mType = type;
        mRootViewRes = rootViewRes;
        mBundle = savedInstanceState;
    }

    public Drawer getDrawer() {
        return mDrawer;
    }

    public AccountHeader getDrawerHeader() {
        return mHeader;
    }

    public void setDrawerInfo(Object info) {
        if (info instanceof Profile) {
            mProfile = (Profile) info;
        } else if (info instanceof Offers) {
            mOffers = (Offers) info;
        }

        setDrawer(mType);
    }

    private void setDrawer(int type) {
        switch(type) {
            case DRAWER_PROFILE_TYPE:
                mDrawer = createProfileDrawer(mActivity, mRootViewRes, mBundle);
                break;
            case DRAWER_INFO_TYPE:
                mDrawer = createInfoDrawer(mActivity, mRootViewRes, mBundle);
                break;
            default:
        }
    }

    private void buildHeader(final Activity activity, IProfile profile, Bundle savedInstanceState) {
        // Create the AccountHeader
        mHeader = new AccountHeaderBuilder()
                .withActivity(activity)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        profile
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .withProfileImagesVisible(true)
                .withSavedInstance(savedInstanceState)
                .build();
    }

    private Drawer createProfileDrawer(final BaseActivity activity, @IdRes int rootViewRes,
                                       @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);

        IProfile profile = new ProfileDrawerItem()
                .withName(mProfile.getName())
                .withEmail(mProfile.getEmail())
                .withIcon(mProfile.getPictureURL())
                .withIdentifier(DRAWER_PROFILE_ITEM_IDENTIFIER_ID);

        buildHeader(activity, profile, savedInstanceState);

        assert toolbar != null;
        mDrawer = new DrawerBuilder()
                .withActivity(activity)
                .withRootView(rootViewRes)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withActionBarDrawerToggleAnimated(true)
                .withSavedInstance(savedInstanceState)
                .withAccountHeader(mHeader) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_point)
                                .withDescription(String.valueOf(mProfile.getPoints()))
                                .withIcon(R.drawable.ic_drawer_point)
                                .withSelectable(false),
                        createExpandableDrawerItem(R.drawable.ic_drawer_star,
                                CUSTOM_ITEM_BOOKMARK_TYPE,
                                activity.getResources().getString(R.string.drawer_item_bookmark),
                                mProfile.getBookmarks(), SECONDARY_DRAWER_LEVEL),
                        createExpandableDrawerItem(R.drawable.ic_drawer_subscription,
                                CUSTOM_ITEM_SUBSCRIPTION_TYPE,
                                activity.getResources().getString(R.string.drawer_item_subscription),
                                mProfile.getSubscriptions(), SECONDARY_DRAWER_LEVEL),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_virtual_currency)
                                .withDescription(mProfile.getVirtualCurrenccy())
                                .withIcon(R.drawable.ic_drawer_currency)
                                .withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_country)
                                .withDescription(mProfile.getCountry())
                                .withIcon(R.drawable.ic_drawer_country)
                                .withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_language)
                                .withDescription(mProfile.getLanguage())
                                .withIcon(R.drawable.ic_drawer_language)
                                .withSelectable(false)
                ) // Add the items we want to use with our SlidingDrawer
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {
                        // This method is only called if the Arrow icon is shown.
                        // The hamburger is automatically managed by the MaterialDrawer if the back
                        // arrow is shown. Close the activity
                        activity.finish();
                        // Return true if we have consumed the event
                        return true;
                    }
                })
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }
                })
                .withSavedInstance(savedInstanceState)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        return false;
                    }
                })
                .buildForFragment();

        if (savedInstanceState == null) {
            mHeader.setActiveProfile(profile);
        }

        mDrawer.getDrawerLayout().setFitsSystemWindows(false);
        mDrawer.getSlider().setFitsSystemWindows(false);

        return mDrawer;
    }

    private Drawer createInfoDrawer(final BaseActivity activity, @IdRes int rootViewRes,
                                    @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);

        /**
         *  In case this project, I put the data of {@link Event} into the list, List<Event> mEvents
         *  in {@link Offers} class, programmatically at this time because there was no provided
         *  the data of {@link Event} from Fyber server.
         *  And I also put the bookmarked count and subscribed count into the list of {@link Offers}.
         *
         *  In real project, the data of {@link Event} should be put into to the list,
         *  List<Event> mEvents in {@link Offers} class, automatically.
         *  It means the server have to provide events data to App as the client.
         *  It's very important to display the data of Events on the list of Drawer's events.
         */
        if (mOffers.getEvents() == null || mOffers.getEvents().size() == 0) {
            List<Event> events = new ArrayList<>();
            for(int i = 0; i < 10; i++) {
                Event event = new Event();
                event.setId("Event ID " + String.valueOf(i));
                event.setName("Event Name " + String.valueOf(i));
                event.setTitle("Event Title " + String.valueOf(i));
                event.setLink("Event Link " + String.valueOf(i));
                event.setContent("Event Content " + String.valueOf(i));
                if (event.getImages() == null) {
                    Event.Images images = new Event.Images();
                    images.setThumbnail("Event Thumbnail Image URL " + String.valueOf(i));
                    images.setOriginal("Event Original Image URL " + String.valueOf(i));
                    event.setImages(images);
                }

                events.add(event);

                mOffers.setBookmarkedCount(306);
                mOffers.setSubscribedCount(500);
                mOffers.setGalleryCount(30);
            }

            mOffers.setEvents(events);
        }

        assert toolbar != null;
        mDrawer = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withActionBarDrawerToggleAnimated(true)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        new MenuDrawerItem()
                                .withIcon(mOffers.getThumbnail().getLowres())
                                .withMenu(mOffers.getTitle())
                                .withMenuSub2(activity.getResources()
                                        .getString(R.string.drawer_item_offer_type) + "  "
                                        + String.valueOf(mOffers.getOfferTypes()
                                        .get(0).getOfferTypeId()))
                                .withDescription(mOffers.getTeaser())
                                .withSelectable(false)
                                .withSelectedTextColorRes(R.color.md_white_1000)
                                .withSelectedColorRes(R.color.material_drawer_menu_selected)
                                .withIdentifier(3000),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_offer_id)
                                .withDescription(String.valueOf(mOffers.getOfferId()))
                                .withIcon(R.drawable.ic_drawer_id)
                                .withTextColor(mActivity.getResources()
                                        .getColor(R.color.colorPrimaryDark))
                                .withDescriptionTextColor(mActivity.getResources()
                                        .getColor(R.color.material_drawable_offer_id_text))

                                .withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_payout)
                                .withDescription(String.valueOf(mOffers.getPayout()))
                                .withIcon(R.drawable.ic_drawer_payout)
                                .withTextColor(mActivity.getResources()
                                        .getColor(R.color.colorPrimaryDark))
                                .withDescriptionTextColor(mActivity.getResources()
                                        .getColor(R.color.material_drawable_offer_id_text))
                                .withSelectable(false),
                        createExpandableDrawerItem(R.drawable.ic_drawer_event,
                                CUSTOM_ITEM_EVENT_TYPE,
                                activity.getResources().getString(R.string.drawer_item_event),
                                mOffers.getEvents(), SECONDARY_DRAWER_LEVEL),
                        new CustomCountPanelDrawableItem()
                                .withName(activity.getResources().getString(
                                        R.string.drawer_item_bookmarked_count))
                                .withCount(String.valueOf(mOffers.getBookmarkedCount()))
                                .withCountTextColor(mActivity.getResources()
                                        .getColor(R.color.material_drawable_bookmark_count_text))
                                .withIcon(R.drawable.ic_drawer_star)
                                .withArrowVisible(false)
                                .withSelectable(false),
                        new CustomCountPanelDrawableItem()
                                .withName(activity.getResources().getString(
                                        R.string.drawer_item_subscribed_count))
                                .withCount(String.valueOf(mOffers.getSubscribedCount()))
                                .withCountTextColor(mActivity.getResources()
                                        .getColor(R.color.material_drawable_bookmark_count_text))
                                .withIcon(R.drawable.ic_drawer_subscription)
                                .withArrowVisible(false)
                                .withSelectable(false),
                        new CustomCountPanelDrawableItem()
                                .withName(activity.getResources().getString(
                                        R.string.drawer_item_gallery))
                                .withCount(String.valueOf(mOffers.getGalleryCount()))
                                .withCountTextColor(mActivity.getResources()
                                        .getColor(R.color.material_drawable_bookmark_count_text))
                                .withIcon(R.drawable.ic_drawer_gallery)
                                .withIdentifier(DRAWER_INFO_ITEM_FIRST_GALLERY_ID)
                                .withArrowVisible(false)
                                .withSelectable(true)

                        //here we use a customPrimaryDrawerItem we defined in our sample app
                        //this custom DrawerItem extends the PrimaryDrawerItem so it just overwrites some methods
                ) // add the items we want to use with our Drawer
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {
                        //this method is only called if the Arrow icon is shown. The hamburger is automatically managed by the MaterialDrawer
                        //if the back arrow is shown. close the activity
                        activity.finish();
                        //return true if we have consumed the event
                        return true;
                    }
                })
                .addStickyDrawerItems(
                        new SecondaryDrawerItem().withName(R.string.drawer_item_link)
                                .withIcon(R.drawable.ic_drawer_link)
                                .withIdentifier(DRAWER_INFO_STICKY_LINK_ID),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_help)
                                .withIcon(R.drawable.ic_drawer_help)
                                .withIdentifier(DRAWER_INFO_STICKY_HELP_ID)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem
                        if (drawerItem != null) {
                            if (drawerItem.getIdentifier() == DRAWER_INFO_STICKY_LINK_ID) {
                                ActivityCaller.INSTANCE.callLink(mActivity, mOffers.getLink());
                            } else if (drawerItem.getIdentifier() == DRAWER_INFO_STICKY_HELP_ID) {
                                ActivityCaller.INSTANCE.callLink(mActivity, HELP_URL);
                            }
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        return mDrawer;
    }

    private CustomCountPanelDrawableItem createExpandableDrawerItem(
            int  iconRes, int type, String itemName, List<?> items, int level) {
        Drawable drawable;

        CustomCountPanelDrawableItem drawableItem = new CustomCountPanelDrawableItem();
        switch (type) {
            case CUSTOM_ITEM_BOOKMARK_TYPE:
                drawable = mActivity.getResources()
                        .getDrawable(R.drawable.ic_drawer_bookmark);
                drawableItem.withName(itemName)
                        .withCount(String.valueOf(items.size()))
                        .withCountTextColor(mActivity.getResources()
                                .getColor(R.color.material_drawable_bookmark_count_text))
                        .withIcon(iconRes)
                        .withIdentifier(DRAWER_PROFILE_ITEM_BOOKMARK_ID)
                        .withSelectable(false)
                        .withSubItems(
                                createSecondaryDrawerItem(drawable, items, type, level)
                        );
                break;
            case CUSTOM_ITEM_SUBSCRIPTION_TYPE:
                drawable = mActivity.getResources()
                        .getDrawable(R.drawable.ic_drawer_subscription);
                drawableItem.withName(itemName)
                        .withCount(String.valueOf(items.size()))
                        .withCountTextColor(mActivity.getResources()
                                .getColor(R.color.material_drawable_subscription_count_text))
                        .withIcon(iconRes)
                        .withIdentifier(DRAWER_PROFILE_ITEM_SUBSCRIPTION_ID)
                        .withSelectable(false)
                        .withSubItems(
                                createSecondaryDrawerItem(drawable, items, type, level)
                        );
                break;
            case CUSTOM_ITEM_EVENT_TYPE:
                drawable = mActivity.getResources()
                        .getDrawable(R.drawable.ic_drawer_events);
                drawableItem.withName(itemName)
                        .withCount(String.valueOf(items.size()))
                        .withCountTextColor(mActivity.getResources()
                                .getColor(R.color.material_drawable_events_count_text))
                        .withIcon(iconRes)
                        .withIdentifier(DRAWER_INFO_ITEM_FIRST_EVENT_ID)
                        .withSelectable(false)
                        .withSubItems(
                                createSecondaryDrawerItem(drawable, items, type, level)
                        );
                break;
            default:
        }

        return drawableItem;
    }

    private List<IDrawerItem> createSecondaryDrawerItem(Drawable drawable, List<?> items,
                                                        int type, int level) {
        switch (type) {
            case CUSTOM_ITEM_BOOKMARK_TYPE:
                List<IDrawerItem> bookmarkItems = new ArrayList<>();
                int bookmarkIdentifier = DRAWER_INFO_SECONDARY_FIRST_DRAWER_ITEM_ID;
                final List<Offers> itemsForBookmark = (List<Offers>) items;
                for (final Offers offers : itemsForBookmark) {
                    SecondaryIconDrawerItem item = new SecondaryIconDrawerItem();
                    item.withName(offers.getTitle())
                            .withLevel(level)
                            .withIcon(offers.getThumbnail().getLowres())
                            .withIdentifier(bookmarkIdentifier)
                            .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                @Override
                                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                    if (drawerItem != null) {
                                        ActivityCaller.INSTANCE.callBookmarkItem(
                                                mActivity, itemsForBookmark,
                                                itemsForBookmark.indexOf(offers));
                                    }

                                    return false;
                                }
                            });

                    bookmarkItems.add(item);
                    bookmarkIdentifier++;
                }

                return bookmarkItems;
            case CUSTOM_ITEM_SUBSCRIPTION_TYPE:
                List<IDrawerItem> subscriptionItems = new ArrayList<>();
                int subscriptionIdentifier = DRAWER_INFO_SECONDARY_SECOND_DRAWER_ITEM_ID;
                final List<Offers> itemsForSubscription = (List<Offers>) items;
                for (final Offers offers : itemsForSubscription) {
                    SecondaryIconDrawerItem item = new SecondaryIconDrawerItem();
                    item.withName(offers.getTitle())
                            .withLevel(level)
                            .withIcon(offers.getThumbnail().getLowres())
                            .withIdentifier(subscriptionIdentifier)
                            .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                @Override
                                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                    if (drawerItem != null) {
                                        ActivityCaller.INSTANCE.callBookmarkItem(
                                                mActivity, itemsForSubscription,
                                                itemsForSubscription.indexOf(offers));
                                    }

                                    return false;
                                }
                            });

                    subscriptionItems.add(item);
                    subscriptionIdentifier++;
                }

                return subscriptionItems;
            case CUSTOM_ITEM_EVENT_TYPE:
                List<IDrawerItem> eventItems = new ArrayList<>();
                int eventIdentifier = DRAWER_PROFILE_SECONDARY_FIRST_DRAWER_ITEM_ID;
                List<Event> itemsForEvent = (List<Event>) items;
                for (final Event event : itemsForEvent) {
                    SecondaryDrawerItem item = new SecondaryDrawerItem();
                    item.withName(event.getTitle())
                            .withLevel(level)
                            .withIcon(drawable)
                            .withIdentifier(eventIdentifier)
                            .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                @Override
                                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                    if (drawerItem != null) {
                                        //ActivityCaller.INSTANCE.CallEvent(mActivity, event);
                                    }

                                    return false;
                                }
                            });

                    eventItems.add(item);
                    eventIdentifier++;
                }

                return eventItems;
            default:
                List<IDrawerItem> defaultItems = new ArrayList<>();
                return defaultItems;

        }
    }
}