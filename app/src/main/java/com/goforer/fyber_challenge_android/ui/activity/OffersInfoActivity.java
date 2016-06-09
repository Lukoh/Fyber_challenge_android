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

package com.goforer.fyber_challenge_android.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.goforer.base.ui.activity.BaseActivity;
import com.goforer.base.ui.view.SquircleImageView;
import com.goforer.fyber_challenge_android.R;
import com.goforer.fyber_challenge_android.model.data.Offers;
import com.goforer.fyber_challenge_android.utility.ActivityCaller;

import butterknife.BindView;
import butterknife.OnClick;

public class OffersInfoActivity extends BaseActivity {
    private static final String TAG = "OffersInfoActivity";

    private Offers mOffers;

    @BindView(R.id.iv_hires)
    SquircleImageView mHiresView;
    @BindView(R.id.iv_lowres)
    SquircleImageView mLowresView;
    @BindView(R.id.tv_offer_id)
    TextView mOfferIdView;
    @BindView(R.id.tv_teaser)
    TextView mTeaserView;
    @BindView(R.id.tv_payout)
    TextView mPayoutView;
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
    @BindView(R.id.comment_bar)
    View mCommentBar;
    @BindView(R.id.comment_holder)
    View mCommentHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String infoString = getIntent().getStringExtra(Offers.class.getName());
        if (!TextUtils.isEmpty(infoString)) {
            mOffers = Offers.gson().fromJson(infoString, Offers.class);
        }

        super.onCreate(savedInstanceState);

        if (mOffers != null) {
            fillView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_offers_info);
    }

    @Override
    protected void setViews() {
        super.setViews();
        mCommentBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setActionBar() {
        super.setActionBar();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_USE_LOGO);
            //actionBar.setHomeAsUpIndicator(R.drawable.bar_back_mtrl_alpha_90);
            actionBar.setTitle(mOffers.getTitle());
            actionBar.setElevation(0);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void setEffectIn() {
        Log.i(TAG, "setEffectIn");

        overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.scale_down_exit);
    }

    @Override
    protected void setEffectOut() {
        Log.i(TAG, "setEffectOut");

        overridePendingTransition(R.anim.scale_up_enter, R.anim.slide_out_to_bottom);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
    }

    private void fillView() {
        setThumbnail(mOffers.getThumbnail().getHires(), mOffers.getThumbnail().getLowres());

        mOfferIdView.setText(String.valueOf(mOffers.getOfferId()));
        mTeaserView.setText(mOffers.getTeaser());
        mPayoutView.setText(String.valueOf(mOffers.getPayout()));
        mLinkView.setText(mOffers.getLink());
        mOfferTypeIdView.setText(String.valueOf(mOffers.getOfferTypes().get(0).getOfferTypeId()));
        mOfferTypeReadableView.setText(mOffers.getOfferTypes().get(0).getReadable());
        mOfferTypeId2View.setText(String.valueOf(mOffers.getOfferTypes().get(1).getOfferTypeId()));
        mOfferTypeReadable2View.setText(mOffers.getOfferTypes().get(1).getReadable());
        mAmountView.setText(String.valueOf(mOffers.getTimeToPayout().getAmount()));
        mReadableView.setText(mOffers.getTimeToPayout().getReadable());
    }

    private void setThumbnail(String hiresUrl, String lowresUrl) {
        mHiresView.setImage(hiresUrl);
        mLowresView.setImage(lowresUrl);
    }

    @SuppressWarnings("")
    @OnClick(R.id.tv_link)
    void onGoToLink() {
        ActivityCaller.INSTANCE.callLink(this, mOffers.getLink());
    }
}
