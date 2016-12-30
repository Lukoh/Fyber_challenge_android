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

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.goforer.base.ui.view.ThreeTwoImageView;
import com.goforer.fyber_challenge.R;
import com.goforer.fyber_challenge.model.data.Gallery;
import com.goforer.fyber_challenge.ui.transition.ImageBrowseSharedElementEnterCallback;
import com.goforer.fyber_challenge.utility.ImageSize;

import java.util.List;

import static com.goforer.fyber_challenge.utility.CommonUtils.TRANSITION_NAME_FOR_IMAGE;
import static com.goforer.fyber_challenge.utility.CommonUtils.TRANSITION_NAME_FOR_TITLE;

public class OffersImageBrowseAdapter extends PagerAdapter {
    private Context mContext;
    private List<Gallery> mImageList;

    private ImageBrowseSharedElementEnterCallback mSharedElementCallback;

    public OffersImageBrowseAdapter(Context context, List<Gallery> imageList,
                                    @NonNull ImageBrowseSharedElementEnterCallback callback) {
        mContext = context;
        mImageList = imageList;
        mSharedElementCallback = callback;
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int pos) {
        final View view = LayoutInflater.from(mContext.getApplicationContext())
                .inflate(R.layout.view_gallery_image, container, false);
        Glide.with(mContext.getApplicationContext()).load(mImageList.get(pos).getThumbnail()
                .getHires()).asBitmap().override(ImageSize.NORMAL[0], ImageSize.NORMAL[1])
                .into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                ((ImageView)view.findViewById(R.id.iv_image)).setImageBitmap(resource);
            }
        });

        ((TextView)view.findViewById(R.id.tv_title)).setText(mImageList.get(pos).getTitle());
        container.addView(view);
        return view;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (object instanceof FrameLayout) {
            FrameLayout layout = (FrameLayout)object;
            ThreeTwoImageView imageView = (ThreeTwoImageView)layout.findViewById(R.id.iv_image);
            TextView titleView = (TextView)layout.findViewById(R.id.tv_title);
            if (isNullCheck(imageView, titleView)) {
                imageView.setTransitionName(
                        TRANSITION_NAME_FOR_IMAGE + position);
                titleView.setTransitionName(
                        TRANSITION_NAME_FOR_TITLE + position);
                mSharedElementCallback.setViewBinding(imageView, titleView);
            }
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private boolean isNullCheck(ThreeTwoImageView imageView, TextView titleView) {
        return imageView.getTransitionName() == null || titleView.getTransitionName() == null;
    }
}
