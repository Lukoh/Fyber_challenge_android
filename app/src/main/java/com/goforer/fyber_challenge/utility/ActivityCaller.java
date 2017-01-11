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

package com.goforer.fyber_challenge.utility;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.goforer.base.ui.activity.BaseActivity;
import com.goforer.fyber_challenge.model.data.Event;
import com.goforer.fyber_challenge.model.data.Gallery;
import com.goforer.fyber_challenge.model.data.Offers;
import com.goforer.fyber_challenge.model.data.Profile;
import com.goforer.fyber_challenge.ui.activity.OffersActivity;
import com.goforer.fyber_challenge.ui.activity.OffersGalleryActivity;
import com.goforer.fyber_challenge.ui.activity.OffersImageBrowseActivity;
import com.goforer.fyber_challenge.ui.activity.OffersInfoActivity;

import java.util.ArrayList;
import java.util.List;

public enum  ActivityCaller {
    INSTANCE;

    public static final String EXTRA_OFFERS_LIST = "fyber:offers_list";
    public static final String EXTRA_OFFERS_ITEM_POSITION = "fyber:offers_items_position";
    public static final String EXTRA_SELECTED_ITEM_POSITION = "fyber:selected_item_position";
    public static final String EXTRA_PROFILE = "fyber:profile";
    public static final String EXTRA_FROM = "fyber:from";
    public static final String EXTRA_OFFERS_ID = "fyber:offers_id";
    public static final String EXTRA_OFFERS_TITLE = "fyber:offers_title";
    public static final String EXTRA_GALLERY_IMAGE_LIST = "fyber:gallery_images";
    public static final String EXTRA_GALLERY_IMAGE_POSITION= "fyber:gallery_image_position";

    public static final String FONT_SIZE = "fontSize";
    public static final String PADDING = "padding";
    public static final String TEXT_COLOR = "color";

    public static final int FROM_OFFERS_LIST = 0;
    public static final int FROM_PROFILE_BOOKMARK = 1;
    public static final int FROM_PROFILE_SUBSCRIPTION = 2;

    public static final int SELECTED_ITEM_POSITION = 1000;

    public static final String HELP_URL = "https://github.com/Lukoh/Fyber_challenge_android";
    public static final String APP_LINK_URL = "goforer://github_challenge?user=jakewharton";

    private Intent createIntent(Context context, Class<?> cls, boolean isNewTask) {
        Intent intent = new Intent(context, cls);

        if (isNewTask && !(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        return intent;
    }

    private Intent createIntent(String action) {
        Intent intent = new Intent(action);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }

    public void callOffersList(Context context, Profile profile) {
        Intent intent = createIntent(context, OffersActivity.class, true);
        intent.putExtra(EXTRA_PROFILE, profile);
        context.startActivity(intent);
    }

    public void callInfo(Context context, List<Offers> items, int position, int from,
                             int requestCode) {
        Intent intent = createIntent(context, OffersInfoActivity.class, true);
        intent.putExtra(EXTRA_FROM, from);
        intent.putParcelableArrayListExtra(EXTRA_OFFERS_LIST, (ArrayList<Offers>)items);
        intent.putExtra(EXTRA_OFFERS_ITEM_POSITION, position);
        ((BaseActivity)context).startActivityForResult(intent, requestCode);
    }

    public void callItem(Context context, List<Offers> items, int position, int from) {
        callInfo(context, items, position, from, ActivityCaller.SELECTED_ITEM_POSITION);
    }

    public void callEvents(Context context, Event event) {
        Intent intent = createIntent(context, OffersInfoActivity.class, true);
        String eventInfo = Offers.gson().toJson(event);
        intent.putExtra(Offers.class.getName(), eventInfo);
        context.startActivity(intent);
    }

    public void callLink(Context context,  String url) {
        Intent intent = createIntent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

        context.startActivity(intent);
    }

    public void callOffersGallery(Context context, long offersId, String offersTitle) {
        Intent intent = createIntent(context, OffersGalleryActivity.class, true);
        intent.putExtra(EXTRA_OFFERS_ID, offersId);
        intent.putExtra(EXTRA_OFFERS_TITLE, offersTitle);

        context.startActivity(intent);
    }

    public void callImageBrowse(Context context, View imageView, View textView,
                                List<Gallery> items, int position, int requestCode) {
        Intent intent = createIntent(context, OffersImageBrowseActivity.class, true);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra(EXTRA_GALLERY_IMAGE_POSITION, position);
        intent.putParcelableArrayListExtra(EXTRA_GALLERY_IMAGE_LIST, (ArrayList<Gallery>) items);
        intent.putExtra(FONT_SIZE, ((TextView)textView).getTextSize());
        intent.putExtra(PADDING,
                new Rect(textView.getPaddingLeft(),
                        textView.getPaddingTop(),
                        textView.getPaddingRight(),
                        textView.getPaddingBottom()));
        intent.putExtra(TEXT_COLOR, ((TextView)textView).getCurrentTextColor());

        final ActivityOptions activityOptions = getActivityOptions(context, imageView, textView);
        ((BaseActivity)context).startActivityForResult(intent, requestCode,
                activityOptions.toBundle());
    }

    /**
     * Checks if all extras are present in an intent.
     *
     * @param intent The intent to check.
     * @param extras The extras to check for.
     * @return <code>true</code> if all extras are present, else <code>false</code>.
     */
    public boolean hasAll(Intent intent, String... extras) {
        for (String extra : extras) {
            if (!intent.hasExtra(extra)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if any extra is present in an intent.
     *
     * @param intent The intent to check.
     * @param extras The extras to check for.
     * @return <code>true</code> if any checked extra is present, else <code>false</code>.
     */
    public boolean hasAny(Intent intent, String... extras) {
        for (String extra : extras) {
            if (intent.hasExtra(extra)) {
                return true;
            }
        }
        return false;
    }

    private ActivityOptions getActivityOptions(Context context, View imageView, View textView) {
        Pair titlePair = Pair.create(textView, textView.getTransitionName());
        Pair imagePair = Pair.create(imageView, imageView.getTransitionName());
        View decorView = ((BaseActivity)context).getWindow().getDecorView();
        View statusBackground = decorView.findViewById(android.R.id.statusBarBackground);
        View navBackground = decorView.findViewById(android.R.id.navigationBarBackground);
        Pair statusPair = Pair.create(statusBackground,
                statusBackground.getTransitionName());

        final ActivityOptions options;
        if (navBackground == null) {
            options = ActivityOptions.makeSceneTransitionAnimation(((BaseActivity)context),
                    titlePair, imagePair, statusPair);
        } else {
            Pair navPair = Pair.create(navBackground, navBackground.getTransitionName());
            options = ActivityOptions.makeSceneTransitionAnimation((BaseActivity)context,
                    titlePair, imagePair, statusPair, navPair);
        }
        return options;
    }
}

