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

package com.goforer.fyber_challenge.ui.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.goforer.base.ui.adapter.BaseListAdapter;
import com.goforer.base.ui.helper.ItemTouchHelperListener;
import com.goforer.base.ui.holder.BaseViewHolder;
import com.goforer.base.ui.holder.DefaultViewHolder;
import com.goforer.fyber_challenge.R;
import com.goforer.fyber_challenge.model.action.MoveItemAction;
import com.goforer.fyber_challenge.model.data.Gallery;
import com.goforer.fyber_challenge.utility.ActivityCaller;
import com.goforer.fyber_challenge.utility.ImageSize;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

import static com.goforer.fyber_challenge.utility.CommonUtils.TRANSITION_NAME_FOR_IMAGE;
import static com.goforer.fyber_challenge.utility.CommonUtils.TRANSITION_NAME_FOR_TITLE;

public class OffersGalleryAdapter extends BaseListAdapter<Gallery> implements ItemTouchHelperListener {
    private GalleryContentViewHolder mViewHolder;

    public OffersGalleryAdapter(List<Gallery> items, int layoutResId,
                                boolean usedLoadingImage) {
        super(items, layoutResId);

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
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
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
    protected GalleryContentViewHolder createViewHolder(ViewGroup viewGroup, View view, int type) {
        return mViewHolder = new GalleryContentViewHolder(view, getItems());
    }

    @Override
    public void onBindViewHolder(BaseViewHolder viewHolder, int position) {
        switch (getItemViewType(position)){
            case VIEW_TYPE_FOOTER:
            case VIEW_TYPE_LOADING:
                return;
            default:
                super.onBindViewHolder(viewHolder, position);
        }
    }

    @Override
    public void onItemDismiss(int position) {
        getItems().remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(getItems(), fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        if ((fromPosition - toPosition) > 0) {
            notifyItemRangeChanged(toPosition, fromPosition);
        } else {
            notifyItemRangeChanged(fromPosition, toPosition);
        }

        return true;
    }

    @Override
    public void onItemDrag(int actionState) {
        MoveItemAction action = new MoveItemAction();
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            action.setType(MoveItemAction.ITEM_MOVED_START);
        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE){
            action.setType(MoveItemAction.ITEM_MOVED_END);
        }

        EventBus.getDefault().post(action);
    }

    public GalleryContentViewHolder getViewHolder() {
        return mViewHolder;
    }

    public final static class GalleryContentViewHolder extends BaseViewHolder<Gallery> {
        private Gallery mGallery;
        private List<Gallery> mGalleryItems;

        GalleryContentViewHolder(View itemView, List<Gallery> items) {
            super(itemView);


            mGalleryItems = items;
        }

        @Override
        public void bindItemHolder(final BaseViewHolder holder, @NonNull final Gallery gallery,
                                   final int position) {
            mGallery = gallery;

            holder.getView().findViewById(R.id.iv_content)
                    .setTransitionName(TRANSITION_NAME_FOR_IMAGE + position);
            holder.getView().findViewById(R.id.tv_title)
                    .setTransitionName(TRANSITION_NAME_FOR_TITLE + position);

            Glide.with(getContext().getApplicationContext()).load(gallery.getThumbnail()
                    .getHires()).asBitmap().override(ImageSize.NORMAL[0], ImageSize.NORMAL[1])
                    .into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    ((ImageView)holder.getView().findViewById(R.id.iv_content)).setImageBitmap(resource);
                }
            });

            ((TextView)holder.getView().findViewById(R.id.tv_title)).setText(mGallery.getTitle());
            holder.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCaller.INSTANCE.callImageBrowse(getContext(),
                            holder.getView().findViewById(R.id.iv_content),
                            holder.getView().findViewById(R.id.tv_title), mGalleryItems,
                            position, ActivityCaller.SELECTED_ITEM_POSITION);
                }
            });
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
