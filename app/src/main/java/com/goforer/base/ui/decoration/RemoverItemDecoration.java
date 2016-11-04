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

package com.goforer.base.ui.decoration;

import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RemoverItemDecoration extends RecyclerView.ItemDecoration {
    private int mBgColor;
    // we want to cache this and not allocate anything repeatedly in the onDraw method
    private Drawable mBackground;

    private boolean mIsInitiated;

    public RemoverItemDecoration(int bgColor) {
        mBgColor = bgColor;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {

        if (!mIsInitiated) {
            init();
        }

        // only if animation is in progress
        if (parent.getItemAnimator().isRunning()) {

            // some items might be animating down and some items might be animating up to close the gap left by the removed item
            // this is not exclusive, both movement can be happening at the same time
            // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
            // then remove one from the middle

            // find first child with translationY > 0
            // and last one with translationY < 0
            // we're after a rect that is not covered in recycler-view views at this point in time
            View lastViewComingDown = null;
            View firstViewComingUp = null;

            // this is fixed
            int left = 0;
            int right = parent.getWidth();

            // this we need to find out
            int top = 0;
            int bottom = 0;

            // find relevant translating views
            int childCount = parent.getLayoutManager().getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getLayoutManager().getChildAt(i);
                if (child.getTranslationY() < 0) {
                    // view is coming down
                    lastViewComingDown = child;
                } else if (child.getTranslationY() > 0) {
                    // view is coming up
                    if (firstViewComingUp == null) {
                        firstViewComingUp = child;
                    }
                }
            }

            if (lastViewComingDown != null && firstViewComingUp != null) {
                // views are coming down AND going up to fill the void
                top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
            } else if (lastViewComingDown != null) {
                // views are going down to fill the void
                top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                bottom = lastViewComingDown.getBottom();
            } else if (firstViewComingUp != null) {
                // views are coming up to fill the void
                top = firstViewComingUp.getTop();
                bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
            }

            mBackground.setBounds(left, top, right, bottom);
            mBackground.draw(canvas);

        }

        super.onDraw(canvas, parent, state);
    }

    private void init() {
        mBackground = new ColorDrawable(mBgColor);
        mIsInitiated = true;
    }
}
