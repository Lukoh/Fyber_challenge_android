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

import com.goforer.base.model.ListModel;
import com.goforer.base.model.data.ResponseBase;
import com.goforer.base.ui.decoration.GapItemDecoration;
import com.goforer.base.ui.fragment.RecyclerFragment;
import com.goforer.base.ui.helper.RecyclerItemTouchHelperCallback;
import com.goforer.fyber_challenge.FyberChallenge;
import com.goforer.fyber_challenge.R;
import com.goforer.fyber_challenge.model.action.MoveItemAction;
import com.goforer.fyber_challenge.model.data.Gallery;
import com.goforer.fyber_challenge.model.data.ResponseOffer;
import com.goforer.fyber_challenge.model.event.OffersGalleryEvent;
import com.goforer.fyber_challenge.ui.adapter.OffersGalleryAdapter;
import com.goforer.fyber_challenge.utility.CommonUtils;
import com.goforer.fyber_challenge.web.Intermediary;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class OffersGalleryFragment extends RecyclerFragment<Gallery> {
    private static final String TAG = "EateryGalleryFragment";

    private static final int SPAN_COUNT = 3;
    private static final int SPAN_NUMBER_ONE = 1;

    private static final String IP = "109.235.143.113";
    private static final String LOCALE = "DE";
    private static final String UID = "spiderman";

    private static final int APP_ID = 2070;
    private static final int OFFER_TYPES = 112;

    private OffersGalleryAdapter mAdapter;

    private int mTotalPageNum;

    private boolean mIsImageBrowserClosed = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offers_gallery, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setItemHasFixedSize(true);
        if (!mIsImageBrowserClosed) {
            refresh(true);
        }

        mIsImageBrowserClosed = false;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected RecyclerView.Adapter createAdapter() {
        return mAdapter = new OffersGalleryAdapter(getListItems(), R.layout.grid_gallery_item, true);
    }

    @Override
    protected ItemTouchHelper.Callback createItemTouchHelper() {
        return new RecyclerItemTouchHelperCallback(mContext, mAdapter);
    }

    @Override
    protected boolean isItemDecorationVisible() {
        return true;
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
    protected RecyclerView.LayoutManager createLayoutManager() {
        super.setOnProcessListener(new RecyclerFragment.OnProcessListener() {
            @Override
            public void onCompleted(int result) {
                Log.i(TAG, "onCompleted");

                if (result == OnProcessListener.RESULT_ERROR) {
                    CommonUtils.showToastMessage(mContext, getString(R.string.toast_process_error), Toast.LENGTH_SHORT);
                    FyberChallenge.closeApplication();
                }
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
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.i(TAG, "onScrolled");
            }

            @Override
            public void onError(String message) {
                CommonUtils.showToastMessage(mContext, message, Toast.LENGTH_SHORT);
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, SPAN_COUNT,
                GridLayoutManager.VERTICAL, false);
        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (position % 6) {
                    case 5:
                        return 3;
                    case 3:
                        return 2;
                    default:
                        return 1;
                }
            }
        };

        spanSizeLookup.setSpanIndexCacheEnabled(true);
        gridLayoutManager.setSpanSizeLookup(spanSizeLookup);
        return gridLayoutManager;
    }

    @Override
    protected RecyclerView.ItemDecoration createItemDecoration() {
        return new GapItemDecoration(GapItemDecoration.VERTICAL_LIST,
                getResources().getDimensionPixelSize(R.dimen.grid_item_spacing)) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                       RecyclerView.State state) {
                outRect.set(mGap, mGap, mGap, mGap);
            }
        };
    }

    @Override
    protected List<Gallery> parseItems(ResponseBase responseBase) {
        return new ListModel<>(Gallery.class).fromJson(((ResponseOffer)responseBase).getOffers());
    }

    @Override
    protected boolean isLastPage(int pageNum) {
        return (mTotalPageNum == pageNum) && (mTotalPageNum > 1);

    }

    public OffersGalleryAdapter getAdapter() {
        return mAdapter;
    }

    private void requestOfferList(boolean isNew) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        OffersGalleryEvent event = new OffersGalleryEvent(isNew);
        String advertisingId = CommonUtils.getGoogleAID();
        long timestamp = System.currentTimeMillis() / 1000L;

        String hashKey = getHashKey(advertisingId, timestamp, getCurrentPageNumber());
        hashKey = hashKey.toLowerCase();

        Intermediary.INSTANCE.getOffers(mContext.getApplicationContext(), APP_ID, advertisingId,
                IP, LOCALE, OFFER_TYPES, getCurrentPageNumber(), timestamp, UID, hashKey, event);
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
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(OffersGalleryEvent event) {
        switch(event.getResponseClient().getStatus()) {
            case ResponseOffer.GENERAL_ERROR:
                CommonUtils.showToastMessage(mContext, getString(R.string.toast_server_error_phrase), Toast.LENGTH_SHORT);
                break;
            case ResponseOffer.NETWORK_ERROR:
                CommonUtils.showToastMessage(mContext, getString(R.string.toast_disconnect_phrase), Toast.LENGTH_SHORT);
                break;
            case ResponseOffer.RESPONSE_SIGNATURE_NOT_MATCH:
                CommonUtils.showToastMessage(mContext, getString(R.string.toast_response_signature_mismatch_phrase), Toast.LENGTH_SHORT);
                break;
            case ResponseOffer.SUCCESSFUL:
                if (event.getResponseClient().getCount() == 0) {
                    CommonUtils.showToastMessage(mContext, getString(R.string.toast_no_data), Toast.LENGTH_SHORT);
                    return;
                }

                if (getCurrentPageNumber() == 1) {
                    mTotalPageNum = event.getResponseClient().getPages();
                }

                handleEvent(event);

                break;
        }
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
