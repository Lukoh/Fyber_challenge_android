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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.goforer.base.ui.activity.BaseActivity;
import com.goforer.base.ui.adapter.BaseListAdapter;
import com.goforer.base.ui.adapter.BaseViewHolder;
import com.goforer.base.ui.adapter.DefaultViewHolder;
import com.goforer.fyber_challenge_android.R;
import com.goforer.fyber_challenge_android.model.action.SelectAction;
import com.goforer.fyber_challenge_android.model.data.Offers;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

public class OfferGridAdapter extends BaseListAdapter<Offers> {
    private Context mContext;

    public OfferGridAdapter(Context context, List<Offers> items, int layoutResId,
                            boolean usedLoadingImage) {
        super(items, layoutResId);

        mContext = context;

        setUsedLoadingImage(usedLoadingImage);
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
        return new OffersGridViewHolder(view, mItems, ((BaseActivity)mContext).resumed());
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

    public static class OffersGridViewHolder extends BaseViewHolder<Offers> {
        private Offers mOffers;
        private List<Offers> mOffersItems;

        private boolean mIsResumed;

        @BindView(R.id.iv_content)
        ImageView mContentImageView;
        @BindView(R.id.tv_title)
        TextView mTitleView;

        public OffersGridViewHolder(View itemView, List<Offers> items, boolean isResumed) {
            super(itemView);

            mOffersItems = items;
            mIsResumed = isResumed;
        }

        @Override
        public void bindItemHolder(@NonNull final Offers offers, final int position) {
            mOffers = offers;

            Glide.with(mContext.getApplicationContext()).load(mOffers.getThumbnail()
                    .getHires()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    mContentImageView.setImageBitmap(resource);
                }
            });

            mTitleView.setText(mOffers.getTitle());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mIsResumed) {
                        SelectAction action = new SelectAction();
                        /**
                         * For using ViewPager in OffersInfoActivity, the list of Offers and
                         * the position of an item have been passed into OffersInfoActivity.
                         * It means that I'm going to put ViewPager and implement some module
                         * to allow a user to see each Offers's information by flipping left
                         * and right through pages of data.
                         */
                        action.setOffers(offers);
                        action.setOffersList(mOffersItems);
                        action.setPosition(position);
                        EventBus.getDefault().post(action);
                    }
                }
            });
        }
    }
}
