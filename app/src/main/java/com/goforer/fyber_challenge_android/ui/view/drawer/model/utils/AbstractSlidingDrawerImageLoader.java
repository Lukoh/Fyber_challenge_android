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

package com.goforer.fyber_challenge_android.ui.view.drawer.model.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.mikepenz.materialdrawer.util.DrawerUIUtils;

public class AbstractSlidingDrawerImageLoader implements
        SlidingDrawerImageLoader.IDrawerImageLoader {
    @Override
    public void set(final ImageView imageView, Uri uri, Drawable placeholder) {
        //this won't do anything
        Log.i("MaterialDrawer", "you have not specified a ImageLoader implementation through " +
                "the DrawerImageLoader.init(IDrawerImageLoader) method");
    }

    @Override
    public void cancel(ImageView imageView) {
    }

    @Override
    public Drawable placeholder(Context context) {
        return DrawerUIUtils.getPlaceHolder(context);
    }

    @Override
    public Drawable placeholder(Context context, String tag) {
        return placeholder(context);
    }
}
