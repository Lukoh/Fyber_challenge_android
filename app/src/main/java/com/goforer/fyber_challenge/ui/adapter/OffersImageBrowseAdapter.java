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
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.goforer.fyber_challenge.R;
import com.goforer.fyber_challenge.model.data.Gallery;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OffersImageBrowseAdapter extends PagerAdapter {
    private static final int ANIMATION_DURATION = 600;

    private Context mContext;
    private List<Gallery> mImageList;

    @BindView(R.id.iv_image)
    ImageView mImage;

    public OffersImageBrowseAdapter(Context context, List<Gallery> imageList) {
        mContext = context;
        mImageList = imageList;
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int pos) {
        View view = LayoutInflater.from(mContext.getApplicationContext())
                .inflate(R.layout.view_gallery_image, container, false);
        ButterKnife.bind(this, view);
        Glide.with(mContext.getApplicationContext()).load(mImageList.get(pos).getThumbnail()
                .getHires()).asBitmap().thumbnail(0.1f).dontAnimate().into(mImage);
        container.addView(view);

        startAnimation();

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void startAnimation() {
        if (mImage != null) {
            Animation animationImage = AnimationUtils.loadAnimation(mContext.getApplicationContext(),
                    R.anim.scale_up_gallery);
            animationImage.setDuration(ANIMATION_DURATION);
            mImage.startAnimation(animationImage);
        }
    }
}
