/*
 * Copyright (C) 2016 Lukoh Nam, goForer
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

package com.goforer.fyber_challenge_android.ui.activity;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.goforer.base.ui.activity.BaseActivity;
import com.goforer.fyber_challenge_android.R;
import com.goforer.fyber_challenge_android.utility.ActivityCaller;

import butterknife.BindView;

public class  ImageViewActivity extends BaseActivity {
    private static final int ANIM_DURATION = 500;

    private String mImageUrl;

    @BindView(R.id.content_holder)
    RelativeLayout mHolder;
    @BindView(R.id.iv_bg)
    ImageView mBg;
    @BindView(R.id.iv_image)
    ImageView mImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mImageUrl = getIntent().getStringExtra(ActivityCaller.EXTRA_OFFERS_IMAGE);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_image_view);
    }

    @Override
    public void setViews(Bundle onSavedInstanceState) {
        Animation animationImage = AnimationUtils.loadAnimation(this, R.anim.scale_up_gallery);
        mImage.startAnimation(animationImage);
        Animation animationHolder = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        mBg.startAnimation(animationHolder);

        Glide.with(this).load(mImageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mImage.setImageBitmap(resource);
            }
        });

        runEnterAnimation();
    }

    public void runEnterAnimation() {
        final long duration = (long) (ANIM_DURATION );

        // Fade in the black background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(mHolder, "alpha", 0, 255);
        bgAnim.setDuration(duration);
        bgAnim.start();

        // Animate a color filter to take the image from grayscale to full color.
        // This happens in parallel with the image scaling and moving into place.
        ObjectAnimator colorizer = ObjectAnimator.ofFloat(mHolder, "saturation", 0, 1);
        colorizer.setDuration(duration);
        colorizer.start();
    }
}
