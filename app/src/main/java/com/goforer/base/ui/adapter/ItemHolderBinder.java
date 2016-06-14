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

import android.support.annotation.NonNull;

/**
 * A ItemHolderBinder describes an item view and metadata about its place within the RecyclerView.
 *
 * <p>All ViewHolder extended {@link BaseViewHolder} implementations should subclass ItemHolderBinder
 * and implements bindItemHolder method to to display the item at the specified position.</p>
 *
 */
public interface ItemHolderBinder<T> {
    /**
     * Called by RecyclerView to display the item at the specified position. This method
     * should update the contents of the item's view to reflect the item at the given position.
     *
     * @param item The ItemHolder which should be updated to represent the contents of the
     *        item at the given position in the data set
     * @param position The position of the item within the adapter's data set.
     *
     */
    void bindItemHolder(@NonNull T item, int position);
}
