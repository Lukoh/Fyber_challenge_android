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

package com.goforer.base.ui.adapter;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.view.ViewPropertyAnimatorCompatSet;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

/**
 * Base class for an Adapter
 *
 * <p>
 * Adapters provide a binding from an app-specific data set to views that are displayed
 * within a {@link RecyclerView}.
 * </p>
 */
public abstract class BaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final float VIEW_VERTICAL_POSITION = 100;
    private static final float ANIMATED_VALUE = 0;
    private static final long ANIMATION_DURATION = 250;
    private static final float ANIMATION_DEGREE = 1.0f;

    public static final int VIEW_TYPE_HEADER = 10000;
    public static final int VIEW_TYPE_FOOTER = 10001;
    public static final int VIEW_TYPE_LOADING = 10002;
    public static final int VIEW_TYPE_ITEM = 10003;
    public static final int VIEW_TYPE_NONE_COMMENT = 10004;

    protected int mLayoutResId;
    private int mLastAnimatedPosition = -1;

    public BaseAdapter() {
        super();
    }

    public BaseAdapter(int layoutResId) {
        super();
        this.mLayoutResId = layoutResId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(mLayoutResId, viewGroup, false);
        return createViewHolder(view, type);
    }

    /**
     * Create a ViewHolder when BaseAdapter needs a new ViewHolder of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     *
     * @param view The view this LayoutManager is bound to
     * @param type The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type
     */
    protected abstract RecyclerView.ViewHolder createViewHolder(View view, int type);

    public void resetAnimation(){
        mLastAnimatedPosition = -1;
    }

    /**
     * Set animation ViewHolder's item if need be.
     *
     * @param viewHolder the ViewHolder to set animation ViewHolder's item
     */
    public void setAnimateViewIfNecessary(final RecyclerView.ViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition ();

        if (position <= mLastAnimatedPosition) return;

        final View itemView = viewHolder.itemView;

        ViewCompat.setPivotX(itemView, 0);
        ViewCompat.setPivotY(itemView, 0);
        ViewCompat.setTranslationY(itemView, VIEW_VERTICAL_POSITION);
        ViewCompat.postOnAnimation(itemView, new Runnable() {
            @Override
            public void run() {
                ViewPropertyAnimatorCompat translation = ViewCompat.animate(itemView).translationY(ANIMATED_VALUE);
                ViewPropertyAnimatorCompatSet set = new ViewPropertyAnimatorCompatSet();
                set.play(translation);
                DecelerateInterpolator interpolator = new DecelerateInterpolator(ANIMATION_DEGREE);
                set.setInterpolator(interpolator)
                        .setDuration(ANIMATION_DURATION)
                        .setListener(new ViewPropertyAnimatorListener() {
                            @Override
                            public void onAnimationStart(View view) {
                            }

                            @Override
                            public void onAnimationEnd(View view) {
                                mLastAnimatedPosition = position;
                            }

                            @Override
                            public void onAnimationCancel(View view) {
                            }
                        }).start();
            }
        });
    }
}
