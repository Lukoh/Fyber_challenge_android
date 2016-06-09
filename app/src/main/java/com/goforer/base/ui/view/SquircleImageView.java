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

package com.goforer.base.ui.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;

import com.goforer.fyber_challenge_android.R;

import java.util.UUID;

/**
 * Custom ImageView for Squircle(rounded rectangular) images in Android while maintaining the
 * best draw performance and supporting custom borders & selectors.
 */
public class SquircleImageView extends ImageView {
    private static final int TILE_MODE_UNDEFINED = -2;
    private static final int TILE_MODE_CLAMP = 0;
    private static final int TILE_MODE_REPEAT = 1;
    private static final int TILE_MODE_MIRROR = 2;

    public static final float DEFAULT_RADIUS = 0f;
    public static final float DEFAULT_BORDER_WIDTH = 0f;

    public static final Shader.TileMode DEFAULT_TILE_MODE = Shader.TileMode.CLAMP;

    private static final ScaleType[] SCALE_TYPES = {
            ScaleType.MATRIX,
            ScaleType.FIT_XY,
            ScaleType.FIT_START,
            ScaleType.FIT_CENTER,
            ScaleType.FIT_END,
            ScaleType.CENTER,
            ScaleType.CENTER_CROP,
            ScaleType.CENTER_INSIDE
    };

    private float mCornerRadius = DEFAULT_RADIUS;
    private float mBorderWidth = DEFAULT_BORDER_WIDTH;

    private ColorStateList mBorderColor = ColorStateList.valueOf(RoundDrawable.DEFAULT_BORDER_COLOR);

    private boolean mIsOval = false;
    private boolean mMutateBackground = false;
    private boolean mOnlyTopCorner = false;
    private boolean mHasColorFilter = false;
    private boolean mColorMod = false;

    private Shader.TileMode mTileModeX = DEFAULT_TILE_MODE;
    private Shader.TileMode mTileModeY = DEFAULT_TILE_MODE;

    private ColorFilter mColorFilter = null;

    private int mResource;
    private Drawable mDrawable;
    private Drawable mBackgroundDrawable;

    private ScaleType mScaleType;
    private Drawable mDefaultImage;

    public SquircleImageView(Context context) {
        super(context);
    }

    public SquircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SquircleImageView, defStyle, 0);

        int index = a.getInt(R.styleable.SquircleImageView_android_scaleType, -1);
        if (index >= 0) {
            setScaleType(SCALE_TYPES[index]);
        } else {
            setScaleType(ScaleType.FIT_CENTER);
        }

        mCornerRadius = a.getDimensionPixelSize(R.styleable.SquircleImageView_riv_corner_radius, -1);
        mBorderWidth = a.getDimensionPixelSize(R.styleable.SquircleImageView_riv_border_width, -1);

        if (mCornerRadius < 0) {
            mCornerRadius = DEFAULT_RADIUS;
        }
        if (mBorderWidth < 0) {
            mBorderWidth = DEFAULT_BORDER_WIDTH;
        }

        mBorderColor = a.getColorStateList(R.styleable.SquircleImageView_riv_border_color);
        if (mBorderColor == null) {
            mBorderColor = ColorStateList.valueOf(RoundDrawable.DEFAULT_BORDER_COLOR);
        }

        mMutateBackground = a.getBoolean(R.styleable.SquircleImageView_riv_mutate_background, false);
        mIsOval = a.getBoolean(R.styleable.SquircleImageView_riv_oval, false);

        mOnlyTopCorner = a.getBoolean(R.styleable.SquircleImageView_only_top_corner, false);

        final int tileMode = a.getInt(R.styleable.SquircleImageView_riv_tile_mode, TILE_MODE_UNDEFINED);
        if (tileMode != TILE_MODE_UNDEFINED) {
            setTileModeX(parseTileMode(tileMode));
            setTileModeY(parseTileMode(tileMode));
        }

        final int tileModeX = a.getInt(R.styleable.SquircleImageView_riv_tile_mode_x, TILE_MODE_UNDEFINED);
        if (tileModeX != TILE_MODE_UNDEFINED) {
            setTileModeX(parseTileMode(tileModeX));
        }

        final int tileModeY = a.getInt(R.styleable.SquircleImageView_riv_tile_mode_y, TILE_MODE_UNDEFINED);
        if (tileModeY != TILE_MODE_UNDEFINED) {
            setTileModeY(parseTileMode(tileModeY));
        }

        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(true);

        a.recycle();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    @Override
    public ScaleType getScaleType() {
        return mScaleType;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        assert scaleType != null;

        if (mScaleType != scaleType) {
            mScaleType = scaleType;

            switch (scaleType) {
                case CENTER:
                case CENTER_CROP:
                case CENTER_INSIDE:
                case FIT_CENTER:
                case FIT_START:
                case FIT_END:
                case FIT_XY:
                    super.setScaleType(ScaleType.FIT_XY);
                    break;
                default:
                    super.setScaleType(scaleType);
                    break;
            }

            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }
    }

    private static Shader.TileMode parseTileMode(int tileMode) {
        switch (tileMode) {
            case TILE_MODE_CLAMP:
                return Shader.TileMode.CLAMP;
            case TILE_MODE_REPEAT:
                return Shader.TileMode.REPEAT;
            case TILE_MODE_MIRROR:
                return Shader.TileMode.MIRROR;
            default:
                return null;
        }
    }

    /**
     * Sets the content of this ImageView to the default bitmap image
     *
     * @param defaultImage the Drawable to set, or {@code null} to clear the content
     */
    public void setDefaultImage(Drawable defaultImage) {
        mDefaultImage = defaultImage;
    }

    public void loadImage(final String url) {
        DrawableRequestBuilder builder = Glide.with(getContext())
                .load(url)
                .placeholder(mDefaultImage)
                .error(mDefaultImage);
        builder.into(this);
    }

    /**
     * Sets the content of this ImageView to the specified Image.
     *
     * <p>
     * Set the target the resource will be loaded into.
     * </p>
     *
     * @param imageUrl the desired Image URL
     */
    public void setImage(String imageUrl) {
        if (imageUrl != null) {
            Glide.with(getContext()).load(imageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    setImageBitmap(resource);
                }
            });
        } else {
            setImageDrawable(mDefaultImage);
        }
    }

    /**
     * Sets the content of this ImageView to the specified Image.
     *
     * <p>
     * Sets some additional data to be mixed in to the memory and disk cache keys allowing
     * the caller more control over when cached data is invalidated.
     * </p>
     *
     * <p>
     * Note - The signature does not replace the cache key, it is purely additive.
     * </p>
     *
     * @param imageUrl the desired Image URL
     */
    public void setImageNewCache(String imageUrl) {
        if (imageUrl != null) {
            Glide.with(getContext()).load(imageUrl).asBitmap().signature(
                    new StringSignature(UUID.randomUUID().toString())).into(
                    new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource,
                                                    GlideAnimation<? super Bitmap> glideAnimation) {
                            setImageBitmap(resource);
                        }
                    });
        } else {
            setImageDrawable(mDefaultImage);
        }
    }

    /**
     * Sets the content of this ImageView to the specified Url.
     *
     * @param url the Url of an image
     */
    public void setImageUrl(String url) {
        if (url.length() > 0) {
            Glide.with(getContext()).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    setImageBitmap(resource);
                }
            });
        }
    }

    /**
     * Sets the content of this ImageView to the specified Image.
     *
     * @param imageUrl the desired Image URL
     * @param resizeWidth the width to resize
     * @param resizeHeight the height to resize
     * @param defaultImage the default image
     */
    public void setImage(String imageUrl, int resizeWidth, int resizeHeight, Drawable defaultImage) {
        setDefaultImage(defaultImage);
        setImage(imageUrl, resizeWidth, resizeHeight);
    }

    /**
     * Sets the content of this ImageView to the specified Image.
     *
     * @param imageUrl the desired Image URL
     * @param resizeWidth the width to resize
     * @param resizeHeight the height to resize
     */
    public void setImage(String imageUrl, int resizeWidth, int resizeHeight) {
        if (imageUrl != null) {
            Glide.with(getContext()).load(imageUrl).asBitmap().override(resizeWidth, resizeHeight).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    if (resource != null) {
                        float scale = 1f;
                        int width = resource.getWidth();
                        int height = resource.getHeight();
                        float max = Math.max(width, height);
                        if (max > 1024) {
                            scale = 1024 / max;
                        }
                        if (scale < 1f) {
                            Matrix matrix = new Matrix();
                            matrix.postScale(scale, scale);
                            resource = Bitmap.createBitmap(resource, 0, 0, width, height, matrix, true);
                        }
                    }

                    setImageBitmap(resource);
                }
            });
        } else {
            setImageDrawable(mDefaultImage);
        }
    }

    /**
     * Sets the content of this ImageView to the specified Image.
     *
     * @param imageUrl the desired Image URL
     * @param resId the resource identifier of the drawable
     */
    public void setImage(final String imageUrl, @DrawableRes int resId) {
        setImageResource(resId);

        new Handler(Looper.myLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (imageUrl != null) {
                    Glide.with(getContext()).load(imageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            setImageBitmap(resource);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        mResource = 0;
        mDrawable = RoundDrawable.fromDrawable(drawable);
        updateDrawableAttrs();
        super.setImageDrawable(mDrawable);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        mResource = 0;
        mDrawable = RoundDrawable.fromBitmap(bm);
        updateDrawableAttrs();
        super.setImageDrawable(mDrawable);
    }

    @Override
    public void setImageResource(int resId) {
        if (mResource != resId) {
            mResource = resId;
            mDrawable = resolveResource();
            updateDrawableAttrs();
            super.setImageDrawable(mDrawable);
        }
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setImageDrawable(getDrawable());
    }

    private Drawable resolveResource() {
        Resources rsrc = getResources();
        if (rsrc == null) {
            return null;
        }

        Drawable d = null;

        if (mResource != 0) {
            try {
                d = rsrc.getDrawable(mResource);
            } catch (Exception e) {
                mResource = 0;
            }
        }
        return RoundDrawable.fromDrawable(d);
    }

    @Override
    public void setBackground(Drawable background) {
        setBackgroundDrawable(background);
    }

    /**
     * Re-initializes the Drawable-attribute used to fill in
     * the square upon drawing.
     */
    private void updateDrawableAttrs() {
        updateAttrs(mDrawable);
    }

    /**
     * Re-initializes the Drawable-attribute of background used to fill in
     * the square upon drawing.
     *
     * @param convert get the background-drawable from RoundDrawable class if the convert is true
     */
    private void updateBackgroundDrawableAttrs(boolean convert) {
        if (mMutateBackground) {
            if (convert) {
                mBackgroundDrawable = RoundDrawable.fromDrawable(mBackgroundDrawable);
            }
            updateAttrs(mBackgroundDrawable);
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (mColorFilter != cf) {
            mColorFilter = cf;
            mHasColorFilter = true;
            mColorMod = true;
            applyColorMod();
            invalidate();
        }
    }

    private void applyColorMod() {
        if (mDrawable != null && mColorMod) {
            mDrawable = mDrawable.mutate();
            if (mHasColorFilter) {
                mDrawable.setColorFilter(mColorFilter);
            }
        }
    }

    private void updateAttrs(Drawable drawable) {
        if (drawable == null) {
            return;
        }

        if (drawable instanceof RoundDrawable) {
            ((RoundDrawable) drawable)
                    .setScaleType(mScaleType)
                    .setCornerRadius(mCornerRadius)
                    .setBorderWidth(mBorderWidth)
                    .setBorderColor(mBorderColor)
                    .setOval(mIsOval)
                    .setOval(mIsOval)
                    .setTileModeX(mTileModeX)
                    .setTileModeY(mTileModeY)
                    .setOnlyTopCorner(mOnlyTopCorner);
            applyColorMod();
        } else if (drawable instanceof LayerDrawable) {
            LayerDrawable ld = ((LayerDrawable) drawable);
            for (int i = 0, layers = ld.getNumberOfLayers(); i < layers; i++) {
                updateAttrs(ld.getDrawable(i));
            }
        }
    }

    @Override
    @Deprecated
    public void setBackgroundDrawable(Drawable background) {
        mBackgroundDrawable = background;
        updateBackgroundDrawableAttrs(true);
        super.setBackgroundDrawable(mBackgroundDrawable);
    }

    public float getCornerRadius() {
        return mCornerRadius;
    }

    public void setCornerRadiusDimension(int resId) {
        setCornerRadius(getResources().getDimension(resId));
    }

    public void setCornerRadius(float radius) {
        if (mCornerRadius == radius) {
            return;
        }

        mCornerRadius = radius;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int resId) {
        setBorderWidth(getResources().getDimension(resId));
    }

    public void setBorderWidth(float width) {
        if (mBorderWidth == width) {
            return;
        }

        mBorderWidth = width;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    /**
     * Gets the SquircleImageView's basic border color
     *
     * @return the the SquircleImageView's basic border color
     */
    public int getBorderColor() {
        return mBorderColor.getDefaultColor();
    }

    /**
     * Sets the SquircleImageView's basic border color.
     *
     * @param color The new color (including alpha) to set the border.
     */
    public void setBorderColor(int color) {
        setBorderColor(ColorStateList.valueOf(color));
    }

    /**
     * Gets a set of state spec / color pairs
     *
     * @return a set of state spec / color pairs
     * @see ColorStateList
     */
    public ColorStateList getBorderColors() {
        return mBorderColor;
    }

    /**
     * Sets the CircularImageView's basic border color.
     *
     * @param colors a set of state spec / color pairs
     * @see ColorStateList
     */
    public void setBorderColor(ColorStateList colors) {
        if (mBorderColor.equals(colors)) {
            return;
        }

        mBorderColor = (colors != null) ? colors : ColorStateList.valueOf(RoundDrawable.DEFAULT_BORDER_COLOR);
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        if (mBorderWidth > 0) {
            invalidate();
        }
    }

    public boolean isOval() {
        return mIsOval;
    }

    public void setOval(boolean oval) {
        mIsOval = oval;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    /**
     * Gets the shader mode for X coordinate
     *
     * @return the shader mode
     * @see Shader.TileMode
     */
    public Shader.TileMode getTileModeX() {
        return mTileModeX;
    }

    /**
     * Sets the shader mode to draw for X coordinate
     *
     * @param tileModeX the shader mode to draw
     *                  @see Shader.TileMode
     */
    public void setTileModeX(Shader.TileMode tileModeX) {
        if (this.mTileModeX == tileModeX) {
            return;
        }

        this.mTileModeX = tileModeX;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    /**
     * Gets the shader mode for Y coordinate
     *
     * @return the shader mode
     * @see Shader.TileMode
     */
    public Shader.TileMode getTileModeY() {
        return mTileModeY;
    }

    /**
     * Sets the shader mode to draw for Y coordinate
     *
     * @param tileModeY the shader mode to draw
     *                  @see Shader.TileMode
     */
    public void setTileModeY(Shader.TileMode tileModeY) {
        if (this.mTileModeY == tileModeY) {
            return;
        }

        this.mTileModeY = tileModeY;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }
}

