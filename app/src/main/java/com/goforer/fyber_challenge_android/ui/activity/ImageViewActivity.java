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
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.goforer.fyber_challenge_android.R;
import com.goforer.fyber_challenge_android.utility.ActivityCaller;

public class  ImageViewActivity extends Activity {

    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();
    private static final int ANIM_DURATION = 500;

    ColorDrawable mBackground;
    private ImageView mImageView;

    private String mImageUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mImageUrl = getIntent().getStringExtra(ActivityCaller.EXTRA_OFFERS_IMAGE);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_view);
        mImageView = (ImageView) findViewById(R.id.imageView);

        Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.scale_up_gallery);
        mImageView.startAnimation(animationFadeIn);


        Glide.with(this).load(mImageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mImageView.setImageBitmap(resource);
            }
        });

        runEnterAnimation();

    }

    /**
     * The enter animation scales the picture in from its previous imageUrl
     * size/location, colorizing it in parallel. In parallel, the background of the
     * activity is fading in. When the pictue is in place, the text description
     * drops down.
     */
    public void runEnterAnimation() {
        final long duration = (long) (ANIM_DURATION );

        // Fade in the black background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(mBackground, "alpha", 0, 150);
        bgAnim.setDuration(duration);
        bgAnim.start();

        // Animate a color filter to take the image from grayscale to full color.
        // This happens in parallel with the image scaling and moving into place.
        ObjectAnimator colorizer = ObjectAnimator.ofFloat(ImageViewActivity.this,
                "saturation", 0, 1);
        colorizer.setDuration(duration);
        colorizer.start();
    }
}
