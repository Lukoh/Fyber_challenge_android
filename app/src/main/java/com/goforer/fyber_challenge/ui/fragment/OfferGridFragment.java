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

package com.goforer.fyber_challenge.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.goforer.base.model.ListModel;
import com.goforer.base.model.data.ResponseBase;
import com.goforer.base.ui.decoration.GapItemDecoration;
import com.goforer.base.ui.fragment.RecyclerFragment;
import com.goforer.base.ui.helper.RecyclerItemTouchHelperCallback;
import com.goforer.fyber_challenge.FyberChallenge;
import com.goforer.fyber_challenge.R;
import com.goforer.fyber_challenge.model.action.BookmarkChangeAction;
import com.goforer.fyber_challenge.model.action.FinishAction;
import com.goforer.fyber_challenge.model.action.FocusItemAction;
import com.goforer.fyber_challenge.model.action.MoveItemAction;
import com.goforer.fyber_challenge.model.action.SubscriptionChangeAction;
import com.goforer.fyber_challenge.model.data.Offers;
import com.goforer.fyber_challenge.model.data.ResponseOffer;
import com.goforer.fyber_challenge.model.event.OffersDataEvent;
import com.goforer.fyber_challenge.ui.activity.OffersActivity;
import com.goforer.fyber_challenge.ui.adapter.OfferGridAdapter;
import com.goforer.fyber_challenge.ui.view.drawer.SlidingDrawer;
import com.goforer.fyber_challenge.utility.ActivityCaller;
import com.goforer.fyber_challenge.utility.CommonUtils;
import com.goforer.fyber_challenge.utility.DisplayUtils;
import com.goforer.fyber_challenge.web.Intermediary;
import com.goforer.fyber_challenge.web.communicator.RequestClient;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OfferGridFragment extends RecyclerFragment<Offers> {
    private static final String TAG = "OfferListFragment";

    private static final int SPAN_COUNT = 3;
    private static final int SPAN_NUMBER_ONE = 1;

    private OfferGridAdapter mAdapter;
    private SlidingDrawer mSlidingDrawer;

    @BindView(R.id.fam_menu)
    FloatingActionMenu mMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offers_grid, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMenu.hideMenu(false);
        setItemHasFixedSize(true);
        refresh(true);
        mSlidingDrawer = new SlidingDrawer(getBaseActivity(), SlidingDrawer.DRAWER_PROFILE_TYPE,
                R.id.drawer_container,
                savedInstanceState);
        mSlidingDrawer.setDrawerInfo(((OffersActivity)mActivity).getProfile());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = mSlidingDrawer.getDrawer().saveInstanceState(outState);
        outState = mSlidingDrawer.getDrawerHeader().saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();

        mSlidingDrawer.setDrawerInfo(((OffersActivity)mActivity).getProfile());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        super.setOnProcessListener(new RecyclerFragment.OnProcessListener() {
            @Override
            public void onCompleted(int result) {
                Log.i(TAG, "onCompleted");

                if (result == OnProcessListener.RESULT_ERROR) {
                    CommonUtils.showToastMessage(mContext, getString(R.string.toast_process_error), Toast.LENGTH_SHORT);
                    FyberChallenge.closeApplication();
                }

                mMenu.showMenuButton(true);
                mMenu.setClosedOnTouchOutside(true);
                mMenu.showMenu(true);
            }

            @Override
            public void onScrolledToLast(RecyclerView recyclerView, int dx, int dy) {
                Log.i(TAG, "onScrolledToLast");
            }

            @Override
            public void onScrolling() {
                Log.i(TAG, "onScrolling");
            }

            @Override
            public void onScrolled() {
                Log.i(TAG, "onScrolled");
            }

            @Override
            public void onError(String message) {
                CommonUtils.showToastMessage(mContext, message, Toast.LENGTH_SHORT);
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, SPAN_COUNT,
                GridLayoutManager.VERTICAL, false);
        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return SPAN_NUMBER_ONE;
            }
        };

        spanSizeLookup.setSpanIndexCacheEnabled(true);
        gridLayoutManager.setSpanSizeLookup(spanSizeLookup);
        return gridLayoutManager;
    }

    @Override
    protected RecyclerView.ItemDecoration createItemDecoration() {
        int gap = DisplayUtils.INSTANCE.dpToPx(mContext, 2);
        return new GapItemDecoration(GapItemDecoration.VERTICAL_LIST, gap) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                       RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);

                if ((position + 1) % SPAN_COUNT == 1) {
                    outRect.set(mGap, mGap, 0, 0);
                } else if ((position + 1) % SPAN_COUNT == 2) {
                    outRect.set(mGap, mGap, 0, 0);
                } else if ((position + 1) % SPAN_COUNT == 0) {
                    outRect.set(mGap, mGap, mGap, 0);
                }
            }
        };
    }

    @Override
    protected RecyclerView.Adapter createAdapter() {
        return mAdapter = new OfferGridAdapter(mContext, getListItems(), R.layout.grid_offer_item,
                true);
    }

    @Override
    protected ItemTouchHelper.Callback createItemTouchHelper() {
        return new RecyclerItemTouchHelperCallback(mContext, mAdapter);
    }

    @Override
    protected boolean isItemDecorationVisible() {
        return false;
    }

    @Override
    protected void requestData(boolean isNew) {
        try {
            requestOfferList(isNew);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "requestData");
    }

    @Override
    protected void updateData() {
        /**
         * Please put some module to update new data here, instead of doneRefreshing() method if
         * there is some data to be updated in Server side.
         * I just put doneRefreshing() method because there is no data to be updated from Sever side.
         */
        doneRefreshing();

        Log.i(TAG, "updateData");
    }

    @Override
    protected List<Offers> parseItems(Object object) {
        return new ListModel<>(Offers.class).fromJson(((ResponseOffer)object).getOffers());
    }

    @Override
    protected boolean isLastPage(int pageNum) {
        return (getTotalPageCount() == pageNum) && (getTotalPageCount() >= 1);

    }

    @SuppressWarnings("")
    @OnClick(R.id.fam_menu)
    void onMenuToggle() {
        mMenu.toggle(true);
    }

    @SuppressWarnings("")
    @OnClick(R.id.fab_help)
    void onCallHelp() {
        ActivityCaller.INSTANCE.callLink(mContext, ActivityCaller.HELP_URL);
    }

    private void requestOfferList(boolean isNew) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        OffersDataEvent<ResponseOffer> event = new OffersDataEvent<>(isNew);
        String advertisingId = CommonUtils.getGoogleAID();
        long timestamp = System.currentTimeMillis() / 1000L;

        String hashKey = CommonUtils.getHashKey(advertisingId, timestamp, getCurrentPageNumber());
        hashKey = hashKey.toLowerCase();

        Intermediary.INSTANCE.getOffers(mContext.getApplicationContext(), RequestClient.APP_ID,
                advertisingId, RequestClient.IP, RequestClient.LOCALE, RequestClient.OFFER_TYPES,
                getCurrentPageNumber(), timestamp, RequestClient.UID, hashKey, event);
    }

    @SuppressWarnings("")
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(OffersDataEvent<ResponseBase> event) {
        switch(event.getResponseClient().getStatus()) {
            case ResponseBase.GENERAL_ERROR:
                CommonUtils.showToastMessage(mContext, getString(R.string.toast_server_error_phrase), Toast.LENGTH_SHORT);
                break;
            case ResponseBase.NETWORK_ERROR:
                CommonUtils.showToastMessage(mContext, getString(R.string.toast_disconnect_phrase), Toast.LENGTH_SHORT);
                break;
            case ResponseBase.RESPONSE_SIGNATURE_NOT_MATCH:
                CommonUtils.showToastMessage(mContext, getString(R.string.toast_response_signature_mismatch_phrase), Toast.LENGTH_SHORT);
                break;
            case ResponseBase.SUCCESSFUL:
                if (event.getResponseClient().getCount() == 0) {
                    CommonUtils.showToastMessage(mContext, getString(R.string.toast_no_data), Toast.LENGTH_SHORT);
                    return;
                }

                handleEvent(event);

                break;
        }
    }

    @SuppressWarnings("")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAction(FocusItemAction action) {
        mAdapter.moveSelectedPosition(getRecyclerView().getLayoutManager(), action.getPosition());
    }

    @SuppressWarnings("")
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onAction(BookmarkChangeAction action) {
        if (!action.isBookmarked()) {
            ((OffersActivity)mActivity).getProfile().getBookmarks()
                    .remove(action.getPosition());
        }

        getListItems().get(action.getPosition()).setBookmarked(action.isBookmarked());
    }

    @SuppressWarnings("")
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onAction(SubscriptionChangeAction action) {
        if (!action.isSubscribed()) {
            ((OffersActivity)mActivity).getProfile().getSubscriptions()
                    .remove(action.getPosition());
        }

        getListItems().get(action.getPosition()).setSubscribed(action.isSubscribed());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAction(FinishAction action){
        doneRefreshing();
        ((OffersActivity)mActivity).showDialog(action);
    }

    @SuppressWarnings("")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAction(MoveItemAction action) {
        if (action.getType() == MoveItemAction.ITEM_MOVED_START) {
            getRefreshLayout().setRefreshing(false);
            getRefreshLayout().setEnabled(false);
        } else {
            getRefreshLayout().setEnabled(true);
        }
    }
}
