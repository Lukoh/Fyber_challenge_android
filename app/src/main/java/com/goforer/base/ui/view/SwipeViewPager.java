/*
 * Copyright (C) 2013 The Android Open Source Project
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
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * SwipeViewPager detect when user is trying to swipe out of bounds
 */
public class SwipeViewPager extends ViewPager {
    private static final int SWIPE_MIN_DISTANCE = 180;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private float mStartDragX;
    private float mStartDragY;

    private OnSwipeOutListener mListener;

    private boolean mIsPageScrolled;

    public SwipeViewPager(Context context) {
        super(context);
    }

    public SwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnSwipeOutListener(OnSwipeOutListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartDragX = ev.getX();
                mStartDragY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                float distanceY = y - mStartDragY;
                float distanceX = x - mStartDragX;
                if (Math.abs(distanceY) > Math.abs(distanceX)) {
                    if (y - mStartDragY > SWIPE_MIN_DISTANCE && Math.abs(y) > SWIPE_THRESHOLD_VELOCITY) {
                        mListener.onSwipeDown(x, y);
                    } else if (mStartDragY - y > SWIPE_MIN_DISTANCE && Math.abs(y) > SWIPE_THRESHOLD_VELOCITY) {
                        mListener.onSwipeUp(x, y);
                    }
                } else {
                    if (x - mStartDragX > SWIPE_MIN_DISTANCE && Math.abs(x) > SWIPE_THRESHOLD_VELOCITY) {
                        mListener.onSwipeRight(x, y);
                    } else if (mStartDragX - x > SWIPE_MIN_DISTANCE && Math.abs(x) > SWIPE_THRESHOLD_VELOCITY) {
                        mListener.onSwipeLeft(x, y);
                    }
                }
                break;
        }

        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartDragX = ev.getX();
                mStartDragY = ev.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (mStartDragX < x && getCurrentItem() == 0) {
                    mListener.onSwipeOutAtStart();
                } else if (mStartDragX > x && getCurrentItem() == getAdapter().getCount() - 1) {
                    mListener.onSwipeOutAtEnd();
                }
                break;
        }

        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setPageScrolled(boolean isPageScrolled) {
        mIsPageScrolled  = isPageScrolled;
    }

    public interface OnSwipeOutListener {
        void onSwipeOutAtStart();

        void onSwipeOutAtEnd();

        void onSwipeLeft(float x, float y);

        void onSwipeRight(float x, float y);

        void onSwipeDown(float x, float y);

        void onSwipeUp(float x, float y);
    }
}

