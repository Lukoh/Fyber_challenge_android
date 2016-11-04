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

package com.goforer.fyber_challenge.ui.view.drawer.model.interfaces.drawer;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.mikepenz.fastadapter.IIdentifyable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;

public interface Commentable<T> extends IIdentifyable<T> {
    T withCommenter(String commenter);

    T withCommenter(@StringRes int commenterRes);

    T withCommenter(StringHolder commenter);

    StringHolder getCommenter();

    T withComment(String comment);

    T withComment(@StringRes int commentRes);

    T withComment(StringHolder comment);

    StringHolder getComment();

    T withDate(String date);

    T withDate(@StringRes int dateRes);

    T withDate(StringHolder date);

    StringHolder getDate();

    T withLikeCount(String likeCount);

    T withLikeCount(StringHolder likeCount);

    T withLikeCount(@StringRes int likeCountRes);

    T withCommentId(long id);

    T withCommenterId(long id);

    StringHolder getLikeCount();

    T withPicture(Drawable picture);

    T withPicture(Bitmap bitmap);

    T withPicture(@DrawableRes int pictureRes);

    T withPicture(String url);

    T withPicture(Uri uri);

    T withPicture(IIcon icon);

    ImageHolder getPicture();

    T withSelectable(boolean selectable);

    boolean isSelectable();
}
