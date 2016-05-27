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

package goforer.com.base.ui.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Extended {@link BaseAdapter} that is the bridge between a {@link RecyclerView}
 * and the data that backs the list. Frequently that data comes from a Cursor,
 * but that is not required. The ListView can display any data provided that it is wrapped in a
 * BaseListAdapter.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
    private boolean mIsReachToLast = false;
    private boolean mIsEmptyItems = false;
    private boolean mIsLoadingItems = false;

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
            T item = mItems.get(position);
            if (item != null) {
                ((ItemBindable<T>) holder).bindItem(item);
            }
        }
    }

    /**
     * Set true if the item or page is reached to the last.
     *
     * @param isReachToLast true if the item or page is reached to the last
     */
    public void setReachToLast(boolean isReachToLast) {
        mIsReachToLast = isReachToLast;
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
     * Check if the item or page is reached to the last.
     *
     * @return true if the item or page is reached to the last
     */
    public boolean isReachedToLast() { return mIsReachToLast; }

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
}
