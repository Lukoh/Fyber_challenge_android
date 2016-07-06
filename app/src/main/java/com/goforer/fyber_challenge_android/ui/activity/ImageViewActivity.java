package com.goforer.fyber_challenge_android.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.goforer.base.ui.activity.BaseActivity;
import com.goforer.fyber_challenge_android.R;
import com.goforer.fyber_challenge_android.utility.ActivityCaller;

import butterknife.BindView;

public class ImageViewActivity extends BaseActivity {
    private String mImageUrl;

    @BindView(R.id.pv_image)
    PhotoView mPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mImageUrl = getIntent().getStringExtra(ActivityCaller.EXTRA_OFFERS_IMAGE);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_image_view);
    }

    @Override
    protected void setViews(Bundle savedInstanceState) {
        mPhotoView.enable();
        mPhotoView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Glide.with(this).load(mImageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mPhotoView.setImageBitmap(resource);
            }
        });
    }
}
