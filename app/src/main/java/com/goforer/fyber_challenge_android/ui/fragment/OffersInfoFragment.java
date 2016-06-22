package com.goforer.fyber_challenge_android.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goforer.base.ui.fragment.BaseFragment;
import com.goforer.base.ui.view.SquircleImageView;
import com.goforer.fyber_challenge_android.R;
import com.goforer.fyber_challenge_android.model.data.Offers;
import com.goforer.fyber_challenge_android.utility.ActivityCaller;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OffersInfoFragment extends BaseFragment {
    private List<Offers> mItems;

    private int mItemPosition;

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
    @BindView(R.id.comment_bar)
    View mCommentBar;
    @BindView(R.id.comment_holder)
    View mCommentHolder;

    /**
     * Create a new instance of OffersInfoFragment
     */
    static public OffersInfoFragment newInstance(List<Offers> offersItems, int position) {
        OffersInfoFragment fragment = new OffersInfoFragment();
        fragment.mItems = offersItems;
        fragment.mItemPosition = position;

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
        setThumbnail(mItems.get(mItemPosition).getThumbnail().getHires(),
                mItems.get(mItemPosition).getThumbnail().getLowres());
        mTeaserView.setText(mItems.get(mItemPosition).getTeaser());
        mLinkView.setText(mItems.get(mItemPosition).getLink());
        mOfferTypeIdView.setText(String.valueOf(
                mItems.get(mItemPosition).getOfferTypes().get(0).getOfferTypeId()));
        mOfferTypeReadableView.setText(mItems.get(mItemPosition).getOfferTypes().get(0).getReadable());
        if (mItems.get(mItemPosition).getOfferTypes().size() > 1)  {
            mOfferTypeId2View.setText(String.valueOf(
                    mItems.get(mItemPosition).getOfferTypes().get(1).getOfferTypeId()));
            mOfferTypeReadable2View.setText(
                    mItems.get(mItemPosition).getOfferTypes().get(1).getReadable());
        }

        mAmountView.setText(String.valueOf(mItems.get(mItemPosition).getTimeToPayout().getAmount()));
        mReadableView.setText(mItems.get(mItemPosition).getTimeToPayout().getReadable());
    }

    private void setThumbnail(String hiresUrl, String lowresUrl) {
        mHiresView.setImage(hiresUrl);
    }

    @SuppressWarnings("")
    @OnClick(R.id.tv_link)
    void onGoToLink() {
        ActivityCaller.INSTANCE.callLink(mContext, mItems.get(mItemPosition).getLink());
    }

}
