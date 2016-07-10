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

package com.goforer.base.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ItemAnimator;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.goforer.base.model.event.ResponseListEvent;
import com.goforer.base.ui.adapter.BaseListAdapter;
import com.goforer.base.ui.adapter.DividerItemDecoration;
import com.goforer.fyber_challenge_android.R;
import com.google.gson.JsonElement;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A {@link BaseFragment}'s subclass with {@link SwipyRefreshLayout} and {@link RecyclerView}.
 */
public abstract class RecyclerFragment<T> extends BaseFragment {
    private static final String TAG = "RecyclerFragment";

    private BaseListAdapter mBaseArrayAdapter;
    private OnProcessListener mListener;

    protected List<T> mItems = new ArrayList<>();
    protected RecyclerView.OnScrollListener mOnScrollListener;

    protected boolean mIsLoading = false;
    protected boolean mIsReachToLast = false;
    protected boolean mIsUpdated = false;

    protected int mCurrentPage = 0;

    @BindView(R.id.swipe_layout)
    protected SwipyRefreshLayout mSwipeLayout;
    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_default_recycler, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        setScrollListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        removeScrollListener();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        EventBus.getDefault().register(this);

    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);

        super.onDetach();
    }

    private void setViews() {
        if (mSwipeLayout != null) {
            setupSwipeLayout();
        }

        mRecyclerView.setLayoutManager(createLayoutManager());
        if (isItemDecorationVisible()) {
            addItemDecorations();
        }

        addItemTouchListener();
        mRecyclerView.setItemAnimator(createItemAnimator());
        Adapter adapter = createAdapter();

        setScrollListener();

        if (adapter instanceof BaseListAdapter) {
            mBaseArrayAdapter = (BaseListAdapter)adapter;
        }

        Log.i(TAG, "Initialize views");
    }

    private void request(boolean isRefreshed) {
        if (isLastPage(mCurrentPage)) {
            doneRefreshing();
            if (mBaseArrayAdapter != null) {
                mBaseArrayAdapter.setReachedToLastPage(true);
            }
        }

        if (!mIsLoading) {
            mIsLoading = true;
            if (mBaseArrayAdapter != null) {
                mBaseArrayAdapter.setLoadingItems(true);
            }

            mCurrentPage = 1;
            if (isRefreshed) {
                requestData(false);
            } else {
                requestData(true);
            }
        }
    }

    private void requestUpdate() {
        if (!mIsLoading) {
            mIsLoading = true;
            if (mBaseArrayAdapter != null) {
                mBaseArrayAdapter.setLoadingItems(true);
            }

            mIsUpdated = true;

            updateData();
        }
    }

    /**
     * An OnScrollListener can be set on a RecyclerView to receive messages
     * when a scrolling event has occurred on that RecyclerView.
     *
     * If you are planning to have several listeners at the same time, use
     * RecyclerView#addOnScrollListener. If there will be only one listener at the time and you
     * want your components to be able to easily replace the listener use
     * RecyclerView#setOnScrollListener.
     */
    private void setScrollListener() {
        removeScrollListener();

        mOnScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!mIsLoading && !mBaseArrayAdapter.isReachedToLastPage() && dy >= 0) {
                    int lastVisibleItemPosition = getLastVisibleItem();
                    int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                    if (lastVisibleItemPosition >= totalItemCount - 1) {
                        mBaseArrayAdapter.setReachedToLastItem(true);
                        if (isLastPage(mCurrentPage)) {
                            doneRefreshing();
                            mBaseArrayAdapter.setReachedToLastPage(true);

                            return;
                        }

                        scrolledReachToLast();
                        requestData(false);
                        mListener.onScrolledToLast(recyclerView, dx, dy);
                    } else {
                        mBaseArrayAdapter.setReachedToLastItem(false);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mListener.onScrolling();
                } else {
                    mListener.onScrolled();
                }
            }
        };

        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    /**
     * Remove a listener that was notified of any changes in scroll state or position.
     */
    private void removeScrollListener() {
        if (mOnScrollListener != null) {
            mRecyclerView.removeOnScrollListener(mOnScrollListener);
        }
    }

    /**
     * RecyclerView can perform several optimizations if it can know in advance that changes in
     * adapter content cannot change the size of the RecyclerView itself.
     * If your use of RecyclerView falls into this category, set this to true.
     *
     * @param hasFixedSize true if adapter changes cannot affect the size of the RecyclerView.
     *                     setItemHasFixedSize(true) means the RecyclerView has children (items)
     *                     that has fixed width and
     */
    protected void setItemHasFixedSize(boolean hasFixedSize) {
        mRecyclerView.setHasFixedSize(hasFixedSize);
    }

    /**
     * Add an {@link ItemDecoration} to this RecyclerView. Item decorations can
     * affect both measurement and drawing of individual item views.
     *
     * <p>Item decorations are ordered. Decorations placed earlier in the list will
     * be run/queried/drawn first for their effects on item views. Padding added to views
     * will be nested; a padding added by an earlier decoration will mean further
     * item decorations in the list will be asked to draw/pad within the previous decoration's
     * given area.</p>
     *
     */
    protected void addItemDecorations() {
        mRecyclerView.addItemDecoration(createItemDecoration());
    }

    protected void addItemTouchListener(){
    }

    /**
     * Sets the {@link SwipyRefreshLayout}'s setting that this RecyclerFragment will use.
     *
     * <p>
     * To set the {@link SwipyRefreshLayout}'s more setting, you must override
     * </p>
     *
     */
    protected void setupSwipeLayout() {
        mSwipeLayout.setColorSchemeResources(R.color.swipeBar);
        mSwipeLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                requestUpdate();
            }
        });
    }

    /**
     * Create the {@link LayoutManager} that this RecyclerFragment will use.
     *
     * <p>
     * To set the {@link LayoutManager} to provide RecyclerFragment, you must override
     * </p>
     *
     * <p>In contrast to other adapter-backed views such as {@link android.widget.ListView}
     * or {@link android.widget.GridView}, RecyclerFragment allows client code to provide custom
     * layout arrangements for child views. These arrangements are controlled by the
     * {@link LayoutManager}. A LayoutManager must be provided for RecyclerFragment to function.</p>
     *
     * <p>Several default strategies are provided for common uses such as lists and grids.</p>
     *
     * @return LayoutManager to use
     *
     * If you'd like to use GridLayout, you have to override this setLayoutManager method
     * in your fragment as below example:
     *
     * Example
     * @@Override
     * protected RecyclerView.LayoutManager setLayoutManager() {
     *     return new GridLayoutManager(activity, 1);
     * }
     * or to use LinearLayout, you have to override this setLayoutManager method
     * in your fragment as below:
     *
     * @@Override
     * protected RecyclerView.LayoutManager setLayoutManager() {
     *     return new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
     * }
     *
     */
    protected abstract LayoutManager createLayoutManager();

    protected int getLastVisibleItem() {
        LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }

        return 0;
    }

    /**
     * Create the DividerItemDecoration.
     *
     * An ItemDecoration allows the application to add a special drawing and layout offset
     * to specific item views from the adapter's data set. This can be useful for drawing dividers
     * between items, highlights, visual grouping boundaries and more.
     *
     * <p>All ItemDecorations are drawn in the order item were added. </p>
     *
     */
    protected ItemDecoration createItemDecoration() {
        return new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST);
    }

    /**
     * This implementation of {@link RecyclerView.ItemAnimator} provides basic
     * animations on remove, add, and move events that happen to the items in
     * a RecyclerView. RecyclerView uses a DefaultItemAnimator by default.
     *
     * @see RecyclerView#setItemAnimator(RecyclerView.ItemAnimator)
     */
    protected ItemAnimator createItemAnimator() {
        return new DefaultItemAnimator();
    }

    /**
     * Create a new adapter to provide child views on demand.
     * <p>
     * To set a new adapter to provide child views on demand, you must override
     * </p>
     * <p>
     * When adapter is changed, all existing views are recycled back to the pool. If the pool has
     * only one adapter, it will be cleared.
     *
     * @return The new adapter to set, or null to set no adapter.
     */
    protected abstract Adapter createAdapter();

    /**
     * RequestClient to get the information or images from server.
     * <p>
     * To request information or data to Web server, you must override
     * This method is called whenever the adapter position of the last visible view is to last item on the list.
     * </p>
     *
     * @param isNew set to true to request new information or images, or false
     *
     */
    protected abstract void requestData(boolean isNew);

    /**
     * RequestClient to get the updated information or images from server.
     * <p>
     * To request information or data to Web server, you must override
     * This method is called whenever the swipe gesture triggers a refresh.
     * </p>
     *
     */
    protected abstract void updateData();

    /**
     * Check if the given page is the last page.
     * Return true if the given page is the last page
     * <p>
     * To check if the given page is the last page, you must override
     * This method is called whenever the swipe gesture triggers a refresh.
     * </p>
     *
     * @param pageNum the current page number to check if given page is the last page
     *
     */
    protected abstract boolean isLastPage(int pageNum);

    /**
     * Check if the ItemDecoration is visible.
     *
     * <p>
     * To check if the given page is the last page, you must override
     * This method is called in {@link #onViewCreated}.
     * </p>
     *
     * @return true if the ItemDecoration in {@link RecyclerView} must be visible
     */
    protected abstract boolean isItemDecorationVisible();

    /**
     * The information should be refreshed whenever the RecyclerFragment is created or
     * the user refresh the contents of a view via a vertical swipe gesture.
     *
     * <P> Must.
     * This method have to be called in override onViewCreated{@link #onViewCreated} method
     * to get the information from server.
     * </P>
     *
     * <p>
     * Be refreshed as a result of the gesture in case a vertical swipe gesture.
     * The information must be provided to allow refresh of the content wherever this gesture
     * is used in case of a vertical swipe gesture.
     * </p>
     */
    protected void refresh() {
        Log.i(TAG, "refresh");

        if (mSwipeLayout != null) {
            mSwipeLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeLayout.setRefreshing(true);
                    request(true);
                }
            });
        } else {
            request(false);
        }
    }

    protected void scrolledReachToLast() {
        Log.i(TAG, "scrolledReachToLast");

        if (!mIsLoading) {
            mIsLoading = true;
            if (mBaseArrayAdapter != null) {
                mBaseArrayAdapter.setLoadingItems(true);
            }

            mCurrentPage++;
        }
    }

    protected void clear() {
        mIsReachToLast = false;
        mCurrentPage = 1;

        if (mItems != null && mItems.size() > 0) {
            mItems.clear();
        }
    }

    /**
     * Notify that data-parsing processing is completed.
     */
    public void doneRefreshing() {
        mIsLoading = false;
        mIsUpdated = false;

        if (mBaseArrayAdapter != null) mBaseArrayAdapter.setLoadingItems(false);

        if (mSwipeLayout != null) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    /**
     * Handle the event which is got from the web server and parse it.
     *
     * <p>
     * This process is processed on AsyncTask.
     * The parsed item is added into a {@link List}
     * and notify the widget that refresh state has changed.
     * </p>
     *
     * @param event the event with responseClient from Web server
     * @see ResponseListEvent
     *
     */
    protected void handleEvent(final ResponseListEvent event) {
        Log.i(TAG, "handleEvent");

        if (event.getResponseClient() != null && event.getResponseClient().isSuccessful()) {
            new handleTask(this, event).execute();
        } else {
            mListener.onCompleted(OnProcessListener.RESULT_ERROR);
        }
    }

    /**
     * Add the items to a {@link List}
     *
     * @param items the item to add to a {@link List}.
     */
    protected void addItems(List<T> items) {
        Log.i(TAG, "addItems");

        if (items != null && !items.isEmpty()) {
            if (mIsUpdated) {
                mItems.addAll(0, items);
                mRecyclerView.setAdapter(mBaseArrayAdapter);
            } else {
                int startIndex = mItems.size() - 1;
                mItems.addAll(items);
                if (mCurrentPage == 1) {
                    mRecyclerView.setAdapter(mBaseArrayAdapter);
                } else {
                    mBaseArrayAdapter.notifyItemRangeChanged(startIndex, items.size() + 1);
                }
            }
        } else {
            mBaseArrayAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Parses an element of Json.
     *
     * <p>
     * You must override to parse an element of Json
     * </p>
     *
     * @param json an element of Json
     * @see JsonElement
     */
    protected abstract List<T> parseItems(JsonElement json);

    /**
     * Sets the listener to be notified when a process is completed
     */
    public void setOnProcessListener(OnProcessListener listener) {
        mListener = listener;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * This class carry out the parsing job to put some data, which be got from the server, to the list.
     *
     * <p>
     * Declared this class as inner static class to prevent the memory leak and use a
     * WeakReference to get a hold of the parent fragment.
     * </p>
     *
     */
    private static class handleTask extends AsyncTask<Void, Void, List> {
        private RecyclerFragment mFragment;
        private ResponseListEvent mEvent;
        private WeakReference<RecyclerFragment> mFragmentWeakRef;

        private handleTask(RecyclerFragment fragment, ResponseListEvent event) {
            mFragment = fragment;
            mEvent = event;

            mFragmentWeakRef = new WeakReference<>(fragment);
        }

        @Override
        protected List doInBackground(Void... params) {
            return mFragment.parseItems(mEvent.getResponseClient().getOffers());
        }

        @Override
        protected void onPostExecute(List items) {
            super.onPostExecute(items);

            RecyclerFragment fragment = mFragmentWeakRef.get();
            // Make sure the fragment is not null before doing anything on it because the destroyed
            // activity could be null.
            if (fragment != null) {
                if (items.size() == 0) {
                    Toast.makeText(mFragment.getContext(), R.string.toast_no_offers,
                            Toast.LENGTH_SHORT).show();
                }

                if (mFragment.isAdded()) {
                    if (mEvent.isNew()) {
                        mFragment.clear();
                    }

                    mFragment.addItems(items);
                    mFragment.doneRefreshing();
                    mFragment.mListener.onCompleted(OnProcessListener.RESULT_SUCCESS);
                }
            }
        }
    }

    /**
     * Classes that wish to be notified when a process is completed should implement this interfaces.
     * An OnProcessListener allows the application to intercept the process events.
     *
     * <p>This can be useful for applications that wish to implement some module
     * after the process is done.
     * /p>
     */
    public interface OnProcessListener {
        int RESULT_ERROR = 0;
        int RESULT_SUCCESS = 1;

        /**
         * This listener method to be invoked after the process is done.
         *
         * @param result the result parameter has RESULT_SUCCESS if there is no error in the process
         */
        void onCompleted(int result);

        /**
         * This listener method to be invoked when the RecyclerView has been scrolled to the last.
         * This will be called after the scroll has completed.
         * <p>
         * This listener method will also be called if visible item range changes after a layout
         * calculation. In that case, dx and dy will be 0.
         *
         * @param recyclerView The RecyclerView which scrolled.
         * @param dx The amount of horizontal scroll.
         * @param dy The amount of vertical scroll.
         */
        void onScrolledToLast(RecyclerView recyclerView, int dx, int dy);

        /**
         * This listener method to be invoked on scrolling.
         */
        void onScrolling();

        /**
         * This listener method to be invoked when scrolling is done.
         */
        void onScrolled();
    }
}

