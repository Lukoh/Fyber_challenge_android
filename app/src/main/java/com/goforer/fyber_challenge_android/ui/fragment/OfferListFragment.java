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

package com.goforer.fyber_challenge_android.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.goforer.base.model.ListModel;
import com.goforer.base.ui.fragment.RecyclerFragment;
import com.goforer.fyber_challenge_android.R;
import com.goforer.fyber_challenge_android.model.action.BookmarkChangeAction;
import com.goforer.fyber_challenge_android.model.action.MoveItemAction;
import com.goforer.fyber_challenge_android.model.action.SubscriptionChangeAction;
import com.goforer.fyber_challenge_android.model.data.Offers;
import com.goforer.fyber_challenge_android.model.event.OffersDataEvent;
import com.goforer.fyber_challenge_android.ui.activity.OffersListActivity;
import com.goforer.fyber_challenge_android.ui.adapter.OfferListAdapter;
import com.goforer.fyber_challenge_android.ui.view.drawer.SlidingDrawer;
import com.goforer.fyber_challenge_android.utility.CommonUtils;
import com.goforer.fyber_challenge_android.web.Intermediary;
import com.goforer.fyber_challenge_android.web.communicator.ResponseClient;
import com.google.gson.JsonElement;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OfferListFragment extends RecyclerFragment<Offers> {
    private static final String TAG = "OfferListFragment";

    private static final String IP = "109.235.143.113";
    private static final String LOCALE = "DE";
    private static final String UID = "spiderman";

    private static final int APP_ID = 2070;
    private static final int OFFER_TYPES = 112;

    private OfferListAdapter mAdapter;
    private SlidingDrawer mSlidingDrawer;

    private int mTotalPageNum;

    @BindView(R.id.fam_menu)
    FloatingActionMenu mMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offers_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMenu.hideMenu(false);
        mTotalPageNum = 1;

        setItemHasFixedSize(true);

        refresh();

        mSlidingDrawer = new SlidingDrawer(getBaseActivity(), SlidingDrawer.DRAWER_PROFILE_TYPE,
                R.id.drawer_container,
                savedInstanceState);
        mSlidingDrawer.setDrawerInfo(((OffersListActivity)mActivity).getProfile());
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

        mSlidingDrawer.setDrawerInfo(((OffersListActivity)mActivity).getProfile());
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
                if (result == OnProcessListener.RESULT_ERROR) {
                    Toast.makeText(mContext, R.string.toast_process_error,
                            Toast.LENGTH_SHORT).show();
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

                mMenu.showMenu(false);
            }

            @Override
            public void onScrolled() {
                Log.i(TAG, "onScrolled");

                mMenu.hideMenu(true);
            }
        });

        return new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    protected RecyclerView.Adapter createAdapter() {
        return mAdapter = new OfferListAdapter(mActivity, mItems, R.layout.list_offer_item, true);
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
    protected List<Offers> parseItems(JsonElement json) {
        return new ListModel<>(Offers.class).fromJson(json);
    }

    @Override
    protected boolean isLastPage(int pageNum) {
        return (mTotalPageNum == pageNum) && (mTotalPageNum > 1);

    }

    private void requestOfferList(boolean isNew) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        OffersDataEvent event = new OffersDataEvent(isNew);
        String advertisingId = CommonUtils.getGoogleAID();
        long timestamp = System.currentTimeMillis() / 1000L;

        String hashKey = getHashKey(advertisingId, timestamp, mCurrentPage);
        hashKey = hashKey.toLowerCase();

        Intermediary.INSTANCE.getOffers(mContext, APP_ID, advertisingId, IP, LOCALE, OFFER_TYPES,
                mCurrentPage, timestamp, UID, hashKey, event);
    }

    @SuppressWarnings("")
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(OffersDataEvent event) {
        switch(event.getResponseClient().getStatus()) {
            case ResponseClient.GENERAL_ERROR:
                showToastMessage(getString(R.string.toast_server_error_phrase));
                break;
            case ResponseClient.NETWORK_ERROR:
                showToastMessage(getString(R.string.toast_disconnect_phrase));
                break;
            case ResponseClient.RESPONSE_SIGNATURE_NOT_MATCH:
                showToastMessage(getString(R.string.toast_response_signature_mismatch_phrase));
                break;
            case ResponseClient.SUCCESSFUL:
                if (event.getResponseClient().getCount() == 0) {
                    showToastMessage(getString(R.string.toast_no_offers));
                    return;
                }

                if (mCurrentPage == 1) {
                    mTotalPageNum = event.getResponseClient().getPages();
                }

                handleEvent(event);

                break;
        }
    }

    @SuppressWarnings("")
    @OnClick(R.id.fam_menu)
    void onMenuToggle() {
        mMenu.toggle(true);
    }

    private void showToastMessage(String phrase) {
        Toast.makeText(mContext, phrase, Toast.LENGTH_SHORT).show();
    }

    private String getHashKey(String advertisingId, long timestamp, int pageNum)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        StringBuilder tmp;
        tmp = new StringBuilder();
        tmp.append("appid=");
        tmp.append(APP_ID).append("&");
        tmp.append("device_id=");
        tmp.append(advertisingId).append("&");
        tmp.append("ip=");
        tmp.append(IP).append("&");
        tmp.append("locale=");
        tmp.append(LOCALE).append("&");
        tmp.append("offer_types=");
        tmp.append(OFFER_TYPES).append("&");
        tmp.append("page=");
        tmp.append(pageNum).append("&");
        tmp.append("timestamp=");
        tmp.append(timestamp).append("&");
        tmp.append("uid=");
        tmp.append(UID).append("&");
        tmp.append(CommonUtils.API_KEY);

        return CommonUtils.SHA1(tmp.toString());
    }

    @SuppressWarnings("")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAction(MoveItemAction action) {
        mAdapter.moveSelectedPosition(getRecyclerView().getLayoutManager(), action.getPosition());
    }

    @SuppressWarnings("")
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onAction(BookmarkChangeAction action) {
        if (!action.isBookmarked()) {
            ((OffersListActivity)mActivity).getProfile().getBookmarks()
                    .remove(action.getPosition());
        }

        mItems.get(action.getPosition()).setBookmarked(action.isBookmarked());
    }

    @SuppressWarnings("")
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onAction(SubscriptionChangeAction action) {
        if (!action.isSubscribed()) {
            ((OffersListActivity)mActivity).getProfile().getSubscriptions()
                    .remove(action.getPosition());
        }

        mItems.get(action.getPosition()).setSubscribed(action.isSubscribed());
    }
}
