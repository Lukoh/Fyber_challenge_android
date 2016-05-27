package goforer.com.fyber_challenge_android.model.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import goforer.com.base.model.BaseModel;

/**
 * Created by lukohnam on 16. 5. 24..
 */
public class Offers extends BaseModel {
    @SerializedName("title")
    private String mTitle;
    @SerializedName("offer_id")
    private long mOfferId;
    @SerializedName("teaser")
    private String mTeaser;
    @SerializedName("required_actions")
    private String mRequiredActions;
    @SerializedName("link")
    private String mLink;
    @SerializedName("offer_types")
    private ArrayList<OfferTypes> mOfferTypes;
    @SerializedName("thumbnail")
    private Thumbnail mThumbnail;
    @SerializedName("payout")
    private int mPayout;
    @SerializedName("time_to_payout")
    private TimeToPayout mTimeToPayout;

    public String getTitle() {
        return mTitle;
    }

    public long getOfferId() {
        return mOfferId;
    }

    public String getTeaser() {
        return mTeaser;
    }

    public String getRequiredActions() {
        return mRequiredActions;
    }

    public String getLink() {
        return mLink;
    }

    public ArrayList<OfferTypes> getOfferTypes() {
        return mOfferTypes;
    }

    public Thumbnail getThumbnail() {
        return mThumbnail;
    }

    public int getPayout() {
        return mPayout;
    }

    public TimeToPayout getTimeToPayout() {
        return mTimeToPayout;
    }

    public static class OfferTypes {
        @SerializedName("offer_type_id")
        private int mOfferTypeId;
        @SerializedName("readable")
        private String mReadable;

        public int getOfferTypeId() {
            return mOfferTypeId;
        }

        public String getReadable() {
            return mReadable;
        }
    }

    public static class Thumbnail {
        @SerializedName("lowres")
        private String mLowres;
        @SerializedName("hires")
        private String mHires;

        public String getLowres() {
            return mLowres;
        }

        public String getHires() {
            return mHires;
        }
    }

    public static class TimeToPayout {
        @SerializedName("amount")
        private long mAmount;
        @SerializedName("readable")
        private String mReadable;

        public long getAmount() {
            return mAmount;
        }

        public String getReadable() {
            return mReadable;
        }
    }
}
