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

package com.goforer.fyber_challenge_android.ui.view.drawer.model.interfaces.drawer;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.mikepenz.fastadapter.IIdentifyable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;

public interface Menuable<T> extends IIdentifyable<T> {
    T withMenu(String menu);

    T withMenu(@StringRes int menuRes);

    T withMenu(StringHolder menu);

    StringHolder getMenu();

    T withMenuSub1(String menuSub1);

    T withMenuSub1(@StringRes int menuSub1Res);

    T withMenuSub1(StringHolder menuSub1);

    StringHolder getMenuSub1();

    T withMenuSub2(String menuSub2);

    T withMenuSub2(@StringRes int menuSub2Res);

    T withMenuSub2(StringHolder menuSub2);

    StringHolder getMenuSub2();

    T withDescription(String description);

    T withDescription(@StringRes int descriptionRes);

    StringHolder getDescription();

    T withIcon(Drawable icon);

    T withIcon(Bitmap bitmap);

    T withIcon(@DrawableRes int iconRes);

    T withIcon(String url);

    T withIcon(Uri uri);

    T withIcon(IIcon icon);

    ImageHolder getIcon();

    T withSelectable(boolean selectable);

    boolean isSelectable();
}
