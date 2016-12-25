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

package com.goforer.base.ui.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.goforer.base.ui.adapter.BaseAdapter;

import butterknife.ButterKnife;

/**
 * A BaseViewHolder describes an item view and metadata about its place within the RecyclerView.
 *
 * <p>{@link BaseAdapter} implementations should subclass ViewHolder and add fields for caching
 * potentially expensive {@link View#findViewById(int)} results.</p>
 *
 * <p>See {@link RecyclerView.ViewHolder} if you'd like to get more.</p>
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements ItemHolderBinder<T> {
    private Context mContext;
    private View mView;


    public BaseViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mView = itemView;
        ButterKnife.bind(this, itemView);
    }

    public Context getContext() {
        return mContext;
    }

    public View getView() {
        return mView;
    }
}
