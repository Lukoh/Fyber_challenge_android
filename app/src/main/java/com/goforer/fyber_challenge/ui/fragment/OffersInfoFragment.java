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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goforer.base.ui.fragment.BaseFragment;
import com.goforer.base.ui.view.SquircleImageView;
import com.goforer.fyber_challenge.R;
import com.goforer.fyber_challenge.model.data.Offers;
import com.goforer.fyber_challenge.utility.ActivityCaller;

import butterknife.BindView;
import butterknife.OnClick;

public class OffersInfoFragment extends BaseFragment {
    private Offers mOffers;

    @BindView(R.id.iv_hires)
    SquircleImageView mHiresView;
    @BindView(R.id.tv_teaser)
    TextView mTeaserView;
    @BindView(R.id.tv_link)
    TextView mLinkView;
    @BindView(R.id.tv_offer_type_id)
    TextView mOfferTypeIdView;
    @BindView(R.id.tv_offer_type_readable)
    TextView mOfferTypeReadableView;
    @BindView(R.id.tv_offer_type_id2)
    TextView mOfferTypeId2View;
    @BindView(R.id.tv_offer_type_readable2)
    TextView mOfferTypeReadable2View;
    @BindView(R.id.tv_amount)
    TextView mAmountView;
    @BindView(R.id.tv_readable)
    TextView mReadableView;

    /**
     * Create a new instance of OffersInfoFragment
     */
    public static OffersInfoFragment newInstance(Offers offers) {
        OffersInfoFragment fragment = new OffersInfoFragment();
        fragment.mOffers = offers;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offers_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fillView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void fillView() {
        setThumbnail(mOffers.getThumbnail().getHires());
        mTeaserView.setText(mOffers.getTeaser());
        mLinkView.setText(mOffers.getLink());
        mOfferTypeIdView.setText(String.valueOf(
                mOffers.getOfferTypes().get(0).getOfferTypeId()));
        mOfferTypeReadableView.setText(mOffers.getOfferTypes().get(0).getReadable());
        if (mOffers.getOfferTypes().size() > 1)  {
            mOfferTypeId2View.setText(String.valueOf(
                    mOffers.getOfferTypes().get(1).getOfferTypeId()));
            mOfferTypeReadable2View.setText(
                    mOffers.getOfferTypes().get(1).getReadable());
        }

        mAmountView.setText(String.valueOf(mOffers.getTimeToPayout().getAmount()));
        mReadableView.setText(mOffers.getTimeToPayout().getReadable());
    }

    private void setThumbnail(String hiresUrl) {
        mHiresView.setImage(hiresUrl);
    }

    @SuppressWarnings("")
    @OnClick(R.id.tv_link)
    void onGoToLink() {
        ActivityCaller.INSTANCE.callLink(mContext.getApplicationContext(), mOffers.getLink());
    }
}