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

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Extended {@link BaseAdapter} that is the bridge between a {@link RecyclerView}
 * and the data that backs the list. Frequently that data comes from a Cursor,
 * but that is not required. The ListView can display any data provided that it is wrapped in a
 * BaseListAdapter.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
    private boolean mIsReachedToLastItem = false;
    private boolean mIsReachedToLastPage = false;
    private boolean mIsEmptyItems = false;
    private boolean mIsLoadingItems = false;
    private boolean mUsedLoadingImage = false;

    protected List<T> mItems;

    public BaseListAdapter(List<T> items, int layoutResId) {
        super(layoutResId);

        mItems = items;
    }

    @Override
    public int getItemCount() {
        if (mItems != null) {
            return mItems.size();
        }

        return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemBindable) {
            if (position >= mItems.size()) {
                return;
            }

            T item = mItems.get(position);
            if (item != null) {
                ((ItemBindable<T>) holder).bindItem(item);
            }
        }
    }

    /**
     * Set true if the item is reached to the last.
     *
     * @param isReachedToLast true if the item is reached to the last
     */
    public void setReachedToLastItem(boolean isReachedToLast) {
        mIsReachedToLastItem = isReachedToLast;
    }

    /**
     * Set true if the page is reached to the last.
     *
     * @param isReachedToLast true if the page is reached to the last
     */
    public void setReachedToLastPage(boolean isReachedToLast) {
        mIsReachedToLastPage = isReachedToLast;
    }

    /**
     * Set true if the items is empty.
     *
     * @param isEmptyItems true if the items is empty
     */
    public void setEmptyItems(boolean isEmptyItems) {
        mIsEmptyItems = isEmptyItems;
    }

    /**
     * Set true if the items is loading.
     *
     * @param isLoadingItems true if the items is loading
     */
    public void setLoadingItems(boolean isLoadingItems) {
        mIsLoadingItems = isLoadingItems;
    }

    /**
     * Set true if the loading image is used.
     *
     * @param usedLoadingImage true if the loading image is used
     */
    public void setUsedLoadingImage(boolean usedLoadingImage) {
        mUsedLoadingImage = usedLoadingImage;
    }

    /**
     * Check if the item is reached to the last.
     *
     * @return true if the item or page is reached to the last
     */
    public boolean isReachedToLastItem() { return mIsReachedToLastItem; }

    /**
     * Check if the page is reached to the last.
     *
     * @return true if the item or page is reached to the last
     */
    public boolean isReachedToLastPage() {
        return mIsReachedToLastPage;
    }

    /**
     * Check if the items is empty.
     *
     * @return true if the items is empty
     */
    public boolean isEmptyItems() { return mIsEmptyItems; }

    /**
     * Check if the items is loading.
     *
     * @return true if the items is loading
     */
    public boolean isLoadingItems() { return mIsLoadingItems; }

    /**
     * Check if the loading image is used to the list as an item.
     *
     * @return true if the loading image is used
     */
    public boolean usedLoadImage() { return mUsedLoadingImage; }
}
