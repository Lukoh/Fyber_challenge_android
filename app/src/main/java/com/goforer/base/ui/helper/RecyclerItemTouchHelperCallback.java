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

package com.goforer.base.ui.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.goforer.base.ui.holder.ItemHolderBinder;
import com.goforer.fyber_challenge.R;

public class RecyclerItemTouchHelperCallback extends ItemTouchHelper.Callback {

    public static final float ALPHA_FULL = 1.0f;

    private Context mContext;

    private final ItemTouchHelperListener mHelperListener;

    private Drawable mBackground;
    private Drawable mXMark;

    private int mXMarkMargin;
    private int mBgColor;

    private boolean mInitiated;

    public RecyclerItemTouchHelperCallback(Context context, ItemTouchHelperListener listener) {
        this(context, listener, Color.BLUE);
    }

    public RecyclerItemTouchHelperCallback(Context context, ItemTouchHelperListener listener,
                                           int bgColor) {
        mContext = context;
        mHelperListener = listener;
        mBgColor = bgColor;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // Set movement flags based on the layout manager
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = ItemTouchHelper.START;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        // Notify the adapter of the move
        mHelperListener.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // Notify the adapter of the dismissal
        mHelperListener.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;

            // not sure why, but this method get's called for viewholder that are already swiped away
            if (viewHolder.getAdapterPosition() == -1) {
                // not interested in those
                return;
            }

            if (!mInitiated) {
                init();
            }

            // draw red background
            mBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            mBackground.draw(canvas);

            // draw x mark
            int itemHeight = itemView.getBottom() - itemView.getTop();
            int intrinsicWidth = mXMark.getIntrinsicWidth();
            int intrinsicHeight = mXMark.getIntrinsicWidth();

            int xMarkLeft = itemView.getRight() - mXMarkMargin - intrinsicWidth;
            int xMarkRight = itemView.getRight() - mXMarkMargin;
            int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
            int xMarkBottom = xMarkTop + intrinsicHeight;
            mXMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

            mXMark.draw(canvas);


            // Fade out the view as it is swiped out of the parent's bounds
            final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);

        } else {
            super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // We only want the active item to change
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ItemHolderBinder) {
                // Let the view holder know that this item is being moved or dragged
                ItemHolderBinder itemViewHolder = (ItemHolderBinder) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }

        if ((actionState == ItemTouchHelper.ACTION_STATE_DRAG) || (actionState == ItemTouchHelper.ACTION_STATE_IDLE)) {
            mHelperListener.onItemDrag(actionState);
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        viewHolder.itemView.setAlpha(ALPHA_FULL);

        if (viewHolder instanceof ItemHolderBinder) {
            // Tell the view holder it's time to restore the idle state
            ItemHolderBinder itemViewHolder = (ItemHolderBinder) viewHolder;
            itemViewHolder.onItemClear();
        }
    }

    private void init() {
        mBackground = new ColorDrawable(mBgColor);
        mXMark = ContextCompat.getDrawable(mContext, R.drawable.ic_clear_24dp);
        mXMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        mXMarkMargin = (int) mContext.getResources().getDimension(R.dimen.helper_icon_clear_margin);
        mInitiated = true;
    }
}
