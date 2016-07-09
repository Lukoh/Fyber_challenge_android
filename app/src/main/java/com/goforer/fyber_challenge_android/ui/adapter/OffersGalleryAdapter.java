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

package com.goforer.fyber_challenge_android.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.goforer.base.ui.activity.BaseActivity;
import com.goforer.base.ui.adapter.BaseListAdapter;
import com.goforer.base.ui.adapter.BaseViewHolder;
import com.goforer.base.ui.adapter.DefaultViewHolder;
import com.goforer.fyber_challenge_android.R;
import com.goforer.fyber_challenge_android.model.data.Gallery;
import com.goforer.fyber_challenge_android.utility.ActivityCaller;

import java.util.List;

import butterknife.BindView;

public class OffersGalleryAdapter extends BaseListAdapter<Gallery> {
    private Context mContext;

    public OffersGalleryAdapter(Context context, List<Gallery> items, int layoutResId,
                                boolean usedLoadingImage) {
        super(items, layoutResId);

        setUsedLoadingImage(usedLoadingImage);
        mContext = context;
    }

    @Override
    public int getItemCount() {
        int count  = super.getItemCount();

        if (isReachedToLastPage()) {
            return count + 1;
        }

        return count;
    }

    @Override
    public int getItemViewType(int position) {
        int itemCount = getItemCount() - 1;

        if (isReachedToLastPage() && position == itemCount) {
            return VIEW_TYPE_FOOTER;
        } else if (position == itemCount) {
            return VIEW_TYPE_LOADING;
        }

        return VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view;

        switch (type) {
            case VIEW_TYPE_FOOTER:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_last_item,
                        viewGroup, false);
                return new DefaultViewHolder(view);
            case VIEW_TYPE_LOADING:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.list_loading_item, viewGroup, false);
                return new DefaultViewHolder(view);
            default:
                return super.onCreateViewHolder(viewGroup, type);
        }
    }

    @Override
    protected RecyclerView.ViewHolder createViewHolder(View view, int type) {
        return new GalleryContentViewHolder(view, mItems, ((BaseActivity)mContext).resumed());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)){
            case VIEW_TYPE_FOOTER:
            case VIEW_TYPE_LOADING:
                return;
            default:
                super.onBindViewHolder(viewHolder, position);
        }
    }

    public static class GalleryContentViewHolder extends BaseViewHolder<Gallery> {
        private Gallery mGallery;
        private List<Gallery> mGalleryItems;

        private boolean mIsResumed;

        @BindView(R.id.iv_content)
        ImageView mContentImageView;

        public GalleryContentViewHolder(View itemView, List<Gallery> items, boolean isResumed) {
            super(itemView);

            mGalleryItems = items;
            mIsResumed = isResumed;
        }

        @Override
        public void bindItemHolder(@NonNull final Gallery gallery, final int position) {
            mGallery = gallery;

            Glide.with(mContext.getApplicationContext()).load(mGallery.getThumbnail()
                    .getHires()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    mContentImageView.setImageBitmap(resource);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCaller.INSTANCE.callImageBrowse(mContext, mGalleryItems, position,
                            ActivityCaller.SELECTED_ITEM_POSITION);
                }
            });
        }
    }
}
