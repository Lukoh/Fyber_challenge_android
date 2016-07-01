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

package com.goforer.fyber_challenge_android.ui.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.goforer.base.ui.activity.BaseActivity;
import com.goforer.fyber_challenge_android.R;
import com.goforer.fyber_challenge_android.ui.activity.OffersListActivity;
import com.goforer.fyber_challenge_android.utility.ActivityCaller;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class SlidingDrawer {
    public static final int DRAWER_PROFILE_TYPE = 0;
    public static final int DRAWER_INFO_TYPE = 1;

    private AccountHeader mHeader = null;
    private Drawer mDrawer = null;

    public SlidingDrawer(final BaseActivity activity, final int type, int rootViewRes,
                                                           @Nullable Bundle savedInstanceState) {
        switch(type) {
            case DRAWER_PROFILE_TYPE:
                mDrawer = createProfileDrawer(activity, rootViewRes, savedInstanceState);
                break;
            case DRAWER_INFO_TYPE:
                mDrawer = createInfoDrawer(activity, rootViewRes, savedInstanceState);
                break;
            default:
        }
    }

    public Drawer getDrawer() {
        return mDrawer;
    }

    private void buildHeader(final Activity activity, IProfile profile, boolean compact,
                             Bundle savedInstanceState) {
        // Create the AccountHeader
        mHeader = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .withCompactStyle(compact)
                .addProfiles(
                        profile
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }

    private Drawer createProfileDrawer(final BaseActivity activity, @IdRes int rootViewRes,
                                       @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        IProfile profile = new ProfileDrawerItem().withName("Lukoh Nam").withEmail("lukoh.nam@gmail.com")
                .withIcon(activity.getResources().getDrawable(R.drawable.profile));

        buildHeader(activity, profile, false, savedInstanceState);

        assert toolbar != null;
        mDrawer = new DrawerBuilder()
                .withActivity(activity)
                .withRootView(rootViewRes)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withSavedInstance(savedInstanceState)
                .withAccountHeader(mHeader) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).
                                withIcon(FontAwesome.Icon.faw_home).withIdentifier(1)
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
                .withSavedInstance(savedInstanceState)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            if (drawerItem.getIdentifier() == 1) {
                                ActivityCaller.INSTANCE.callLink(view.getContext(),
                                        OffersListActivity.HOME_URL);
                            }
                        }

                        return false;
                    }
                })
                .buildForFragment();

        mDrawer.getDrawerLayout().setFitsSystemWindows(false);
        mDrawer.getSlider().setFitsSystemWindows(false);

        return mDrawer;
    }

    private Drawer createInfoDrawer(final BaseActivity activity, @IdRes int rootViewRes,
                                    @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);

        mDrawer = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withFullscreen(true)
                .withActionBarDrawerToggleAnimated(true)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home)
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
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog).withIdentifier(10),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question)
                )
                .withSavedInstance(savedInstanceState)
                .build();

        return mDrawer;

    }
}
