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

package com.goforer.fyber_challenge_android.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.goforer.fyber_challenge_android.model.data.Event;
import com.goforer.fyber_challenge_android.model.data.Gallery;
import com.goforer.fyber_challenge_android.model.data.Offers;
import com.goforer.fyber_challenge_android.model.data.Profile;
import com.goforer.fyber_challenge_android.ui.activity.OffersGalleryActivity;
import com.goforer.fyber_challenge_android.ui.activity.OffersImageBrowseActivity;
import com.goforer.fyber_challenge_android.ui.activity.OffersInfoActivity;
import com.goforer.fyber_challenge_android.ui.activity.OffersListActivity;

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

    public static final int FROM_OFFERS_LIST = 0;
    public static final int FROM_PROFILE_BOOKMARK = 1;
    public static final int FROM_PROFILE_SUBSCRIPTION = 2;

    public static final int SELECTED_ITEM_POSITION = 1000;

    public Intent createIntent(Context context, Class<?> cls, boolean isNewTask) {
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

    public void callOffersList(Activity activity, Profile profile) {
        Intent intent = createIntent(activity, OffersListActivity.class, true);
        intent.putExtra(EXTRA_PROFILE, profile);
        activity.startActivity(intent);
    }

    public void callInfo(Activity activity, List<Offers> items, int position, int from,
                             int requestCode) {
        Intent intent = createIntent(activity, OffersInfoActivity.class, true);
        intent.putExtra(EXTRA_FROM, from);
        intent.putParcelableArrayListExtra(EXTRA_OFFERS_LIST, (ArrayList<Offers>)items);
        intent.putExtra(EXTRA_OFFERS_ITEM_POSITION, position);
        activity.startActivityForResult(intent, requestCode);
    }

    public void callItem(Activity activity, List<Offers> items, int position, int from) {
        callInfo(activity, items, position, from, ActivityCaller.SELECTED_ITEM_POSITION);
    }

    public void callEvents(Activity activity, Event event) {
        Intent intent = createIntent(activity, OffersInfoActivity.class, true);
        String eventInfo = Offers.gson().toJson(event);
        intent.putExtra(Offers.class.getName(), eventInfo);
        activity.startActivity(intent);
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

    public void callImageBrowse(Activity activity, List<Gallery> items, int position,
                                int requestCode) {
        Intent intent = createIntent(activity, OffersImageBrowseActivity.class, true);
        intent.putExtra(EXTRA_GALLERY_IMAGE_POSITION, position);
        intent.putParcelableArrayListExtra(EXTRA_GALLERY_IMAGE_LIST, (ArrayList<Gallery>) items);

        activity.startActivityForResult(intent, requestCode);
    }
}

