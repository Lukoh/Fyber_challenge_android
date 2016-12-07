package com.goforer.fyber_challenge.model.data.sort;

import com.goforer.fyber_challenge.model.data.Offers;

import java.util.Comparator;

public class OffersComparator implements Comparator<Offers> {
    private int mType;
    private int mFlag;

    public OffersComparator(int type, int flag) {
        mType = type;
        mFlag = flag;
    }

    @Override
    public int compare(Offers offers1, Offers offers2) {
        switch (mType) {
            case Offers.SORT_TITLE_TYPE:
                if (mFlag == Offers.SORT_DESCENDING_FLAG) {
                    return offers2.getTitle().compareTo(offers1.getTitle());
                } else {
                    return offers1.getTitle().compareTo(offers2.getTitle());
                }
            case Offers.SORT_PAYOUT_TYPE:
                if (mFlag == Offers.SORT_DESCENDING_FLAG) {
                    return offers2.getPayout() - offers1.getPayout();
                } else {
                    return offers1.getPayout() - offers2.getPayout();
                }
            case Offers.SORT_BOOKMARK_AMOUNT_TYPE:
                if (mFlag == Offers.SORT_DESCENDING_FLAG) {
                    return offers2.getBookmarkedCount() - offers1.getBookmarkedCount();
                } else {
                    return offers1.getBookmarkedCount() - offers2.getBookmarkedCount();
                }
            case Offers.SORT_SUBSCRIPTION_AMOUNT_TYPE:
                if (mFlag == Offers.SORT_DESCENDING_FLAG) {
                    return offers2.getSubscribedCount() - offers1.getSubscribedCount();
                } else {
                    return offers1.getSubscribedCount() - offers2.getSubscribedCount();
                }
            default:
                return Offers.NO_SORT;
        }
    }
}
