package goforer.com.fyber_challenge_android.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


import butterknife.BindView;
import goforer.com.fyber_challenge_android.R;
import goforer.com.fyber_challenge_android.model.data.Offers;
import goforer.com.base.ui.activity.BaseActivity;
import goforer.com.base.ui.adapter.BaseListAdapter;
import goforer.com.base.ui.adapter.BaseViewHolder;
import goforer.com.base.ui.adapter.DefaultViewHolder;
import goforer.com.base.ui.view.SquircleImageView;

/**
 * Created by lukohnam on 16. 5. 24..
 */
public class OfferListAdapter extends BaseListAdapter<Offers> {
    private static BaseActivity mActivity;

    private static final String PAY_OUT = "PayOut : ";

    public OfferListAdapter(BaseActivity activity, List<Offers> items, int layoutResId) {
        super(items, layoutResId);

        mActivity = activity;
    }

    @Override
    public int getItemCount() {
        int count  = super.getItemCount();

        if (isReachedToLast() && count >= 0) {
            count++;
        } else if (count > 1) {
            count++;
        }

        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (isReachedToLast() && position == getItemCount() - 1) {
            return VIEW_TYPE_FOOTER;
        }

        return VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view;
        switch (type) {
            case VIEW_TYPE_FOOTER:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_last_item,
                        viewGroup, false);
                return new DefaultViewHolder(view);
            default:
                return super.onCreateViewHolder(viewGroup, type);
        }
    }

    @Override
    protected RecyclerView.ViewHolder createViewHolder(View view, int type) {
        return new OfferListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)){
            case VIEW_TYPE_FOOTER:
                return;
            default:
                super.onBindViewHolder(viewHolder, position);
        }
    }

    static class OfferListViewHolder extends BaseViewHolder<Offers> {
        private Offers mOffers;

        @BindView(R.id.iv_hires)
        SquircleImageView mHiresView;
        @BindView(R.id.tv_title)
        TextView mTitleView;
        @BindView(R.id.tv_teaser)
        TextView mTeaserView;
        @BindView(R.id.tv_payout)
        TextView mPayoutView;

        public OfferListViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindItem(@NonNull final Offers offers) {
            mOffers = offers;

            mHiresView.setImage(mOffers.getThumbnail().getHires());
            mTitleView.setText(mOffers.getTitle());
            mTeaserView.setText(mOffers.getTeaser());
            mPayoutView.setText(PAY_OUT + String.valueOf(mOffers.getPayout()).toString());
        }
    }
}
