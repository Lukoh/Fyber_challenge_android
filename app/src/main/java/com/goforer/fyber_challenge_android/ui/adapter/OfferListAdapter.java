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

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goforer.base.ui.activity.BaseActivity;
import com.goforer.base.ui.adapter.BaseListAdapter;
import com.goforer.base.ui.adapter.BaseViewHolder;
import com.goforer.base.ui.adapter.DefaultViewHolder;
import com.goforer.base.ui.view.SquircleImageView;
import com.goforer.fyber_challenge_android.R;
import com.goforer.fyber_challenge_android.model.action.SelectAction;
import com.goforer.fyber_challenge_android.model.data.Offers;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

public class OfferListAdapter extends BaseListAdapter<Offers> {
    private static BaseActivity mActivity;

    private static final String PAY_OUT = "PayOut : ";

    public OfferListAdapter(BaseActivity activity, List<Offers> items, int layoutResId,
                            boolean usedLoadingImage) {
        super(items, layoutResId);

        setUsedLoadingImage(usedLoadingImage);
        mActivity = activity;
    }

    @Override
    public int getItemCount() {
        int count  = super.getItemCount();

        if (isReachedToLastPage() && count >= 0) {
            count++;
            return count;
        } else if (isReachedToLastItem() && count > 1) {
            count++;
        }

        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (isReachedToLastPage() && position == getItemCount() - 1) {
            return VIEW_TYPE_FOOTER;
        } else if (position > 1 && position == getItemCount() - 1) {
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
        return new OfferListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)){
            case VIEW_TYPE_FOOTER:
            case VIEW_TYPE_LOADING:
            default:
                super.onBindViewHolder(viewHolder, position);
        }
    }

    static class OfferListViewHolder extends BaseViewHolder<Offers> {
        private Offers mOffers;

        @BindView(R.id.iv_hires)
        SquircleImageView mHiresView;
        @BindView(R.id.tv_title)
        TextView mTitleView;
        @BindView(R.id.tv_teaser)
        TextView mTeaserView;
        @BindView(R.id.tv_payout)
        TextView mPayoutView;

        public OfferListViewHolder(View itemView) {
            super(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bindItem(@NonNull final Offers offers) {
            mOffers = offers;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mActivity.resumed()) {
                        SelectAction action = new SelectAction();
                        action.setOffers(mOffers);
                        EventBus.getDefault().post(action);
                    }
                }
            });


            mHiresView.setImage(mOffers.getThumbnail().getHires());
            mTitleView.setText(mOffers.getTitle());
            mTeaserView.setText(mOffers.getTeaser());
            mPayoutView.setText(PAY_OUT + String.valueOf(mOffers.getPayout()).toString());


        }
    }
}
