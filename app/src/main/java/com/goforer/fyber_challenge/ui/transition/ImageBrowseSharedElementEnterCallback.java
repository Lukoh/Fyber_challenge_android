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

package com.goforer.fyber_challenge.ui.transition;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.app.SharedElementCallback;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.goforer.fyber_challenge.utility.ActivityCaller.FONT_SIZE;
import static com.goforer.fyber_challenge.utility.ActivityCaller.INSTANCE;
import static com.goforer.fyber_challenge.utility.ActivityCaller.PADDING;
import static com.goforer.fyber_challenge.utility.ActivityCaller.TEXT_COLOR;

public class ImageBrowseSharedElementEnterCallback extends SharedElementCallback {
    private final Intent intent;

    private float targetTextSize;

    private ColorStateList targetTextColors;

    private TextView mTitleView;
    private ImageView mImageView;

    private Rect targetPadding;

    public ImageBrowseSharedElementEnterCallback(Intent intent) {
        this.intent = intent;
    }

    @Override
    public void onSharedElementStart(List<String> sharedElementNames,
                                     List<View> sharedElements,
                                     List<View> sharedElementSnapshots) {
        TextView title = getTitle();
        targetTextSize = title.getTextSize();
        targetTextColors = title.getTextColors();
        targetPadding = new Rect(title.getPaddingLeft(),
                title.getPaddingTop(),
                title.getPaddingRight(),
                title.getPaddingBottom());
        if (INSTANCE.hasAll(intent, TEXT_COLOR, FONT_SIZE, PADDING)) {
            title.setTextColor(intent.getIntExtra(TEXT_COLOR, Color.BLACK));
            float textSize = intent.getFloatExtra(FONT_SIZE, targetTextSize);
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            Rect padding = intent.getParcelableExtra(PADDING);
            title.setPadding(padding.left, padding.top, padding.right, padding.bottom);
        }
    }

    @Override
    public void onSharedElementEnd(List<String> sharedElementNames,
                                   List<View> sharedElements,
                                   List<View> sharedElementSnapshots) {
        TextView title = getTitle();
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, targetTextSize);
        if (targetTextColors != null) {
            title.setTextColor(targetTextColors);
        }
        if (targetPadding != null) {
            title.setPadding(targetPadding.left, targetPadding.top,
                    targetPadding.right, targetPadding.bottom);
        }
    }

    @Override
    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
        removeObsoleteElements(names, sharedElements, mapObsoleteElements(names));
        mapSharedElement(names, sharedElements, getTitle());
        mapSharedElement(names, sharedElements, getImage());
    }

    public void setViewBinding(View image, View title) {
        if (title instanceof TextView) {
            mTitleView = (TextView)title;
        }

        if (image instanceof ImageView) {
            mImageView = (ImageView)image;
        }
    }

    @Deprecated
    public void setTitle(TextView textView) {
        mTitleView = textView;
    }

    @Deprecated
    public void setImage(ImageView imageView) {
        mImageView = imageView;
    }

    private TextView getTitle() {
        return mTitleView;
    }

    private ImageView getImage() {
        return mImageView;
    }

    /**
     * Maps all views that don't start with "android" namespace.
     *
     * @param names All shared element names.
     * @return The obsolete shared element names.
     */
    @NonNull
    private List<String> mapObsoleteElements(List<String> names) {
        List<String> elementsToRemove = new ArrayList<>(names.size());
        for (String name : names) {
            if (name.startsWith("android")) continue;
            elementsToRemove.add(name);
        }
        return elementsToRemove;
    }

    /**
     * Removes obsolete elements from names and shared elements.
     *
     * @param names Shared element names.
     * @param sharedElements Shared elements.
     * @param elementsToRemove The elements that should be removed.
     */
    private void removeObsoleteElements(List<String> names,
                                        Map<String, View> sharedElements,
                                        List<String> elementsToRemove) {
        if (elementsToRemove.size() > 0) {
            names.removeAll(elementsToRemove);
            for (String elementToRemove : elementsToRemove) {
                sharedElements.remove(elementToRemove);
            }
        }
    }

    /**
     * Puts a shared element to transitions and names.
     *
     * @param names The names for this transition.
     * @param sharedElements The elements for this transition.
     * @param view The view to add.
     */
    private void mapSharedElement(List<String> names, Map<String, View> sharedElements, View view) {
        String transitionName = view.getTransitionName();
        names.add(transitionName);
        sharedElements.put(transitionName, view);
    }

    private void forceSharedElementLayout(View view) {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(view.getWidth(),
                View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(view.getHeight(),
                View.MeasureSpec.EXACTLY);
        view.measure(widthSpec, heightSpec);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    }

}