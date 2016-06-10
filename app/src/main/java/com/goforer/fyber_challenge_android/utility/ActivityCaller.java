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

import com.goforer.fyber_challenge_android.model.data.Offers;
import com.goforer.fyber_challenge_android.ui.activity.OffersInfoActivity;

import java.util.ArrayList;
import java.util.List;

public enum  ActivityCaller {
    INSTANCE;

    public static final String EXTRA_OFFERS_LIST = "fyber:offers_list";
    public static final String EXTRA_OFFERS_ITEMS_POSITION = "fyber:offers_items_position";

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


    public void callItemInfo(Context context, Offers offers, List<Offers> items, int position) {
        Intent intent = createIntent(context, OffersInfoActivity.class, true);
        String offersInfo = Offers.gson().toJson(offers);
        intent.putExtra(Offers.class.getName(), offersInfo);
        intent.putParcelableArrayListExtra(EXTRA_OFFERS_LIST, (ArrayList)items);
        intent.putExtra(EXTRA_OFFERS_ITEMS_POSITION, position);
        context.startActivity(intent);
    }

    public void callLink(Context context,  String url) {
        Intent intent = createIntent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

        context.startActivity(intent);
    }
}

