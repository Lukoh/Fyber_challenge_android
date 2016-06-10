/*
 * Copyright (C) 016 Lukoh Nam, goForer
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

package com.goforer.fyber_challenge_android.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.goforer.base.model.BaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public final class Offers extends BaseModel implements Parcelable {
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
    private List<OfferTypes> mOfferTypes;
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

    public List<OfferTypes> getOfferTypes() {
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


    protected Offers(Parcel in) {
        mTitle = in.readString();
        mOfferId = in.readLong();
        mTeaser = in.readString();
        mRequiredActions = in.readString();
        mLink = in.readString();
        mOfferTypes = new ArrayList<>();
        in.readTypedList(mOfferTypes, OfferTypes.CREATOR);
        mThumbnail = in.readParcelable(Thumbnail.class.getClassLoader());
        mPayout = in.readInt();
        mTimeToPayout = in.readParcelable(TimeToPayout.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeLong(mOfferId);
        dest.writeString(mTeaser);
        dest.writeString(mRequiredActions);
        dest.writeString(mLink);
        dest.writeTypedList(mOfferTypes);
        dest.writeParcelable(mThumbnail, flags);
        dest.writeInt(mPayout);
        dest.writeParcelable(mTimeToPayout, flags);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Offers> CREATOR = new Parcelable.Creator<Offers>() {
        @Override
        public Offers createFromParcel(Parcel in) {
            return new Offers(in);
        }

        @Override
        public Offers[] newArray(int size) {
            return new Offers[size];
        }
    };

    public final static class OfferTypes implements Parcelable {
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

        protected OfferTypes(Parcel in) {
            mOfferTypeId = in.readInt();
            mReadable = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(mOfferTypeId);
            dest.writeString(mReadable);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<OfferTypes> CREATOR = new Parcelable.Creator<OfferTypes>() {
            @Override
            public OfferTypes createFromParcel(Parcel in) {
                return new OfferTypes(in);
            }

            @Override
            public OfferTypes[] newArray(int size) {
                return new OfferTypes[size];
            }
        };
    }

    public final static class Thumbnail implements Parcelable {
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

        protected Thumbnail(Parcel in) {
            mLowres = in.readString();
            mHires = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mLowres);
            dest.writeString(mHires);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Thumbnail> CREATOR = new Parcelable.Creator<Thumbnail>() {
            @Override
            public Thumbnail createFromParcel(Parcel in) {
                return new Thumbnail(in);
            }

            @Override
            public Thumbnail[] newArray(int size) {
                return new Thumbnail[size];
            }
        };
    }

    public final static class TimeToPayout implements Parcelable {
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

        protected TimeToPayout(Parcel in) {
            mAmount = in.readLong();
            mReadable = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(mAmount);
            dest.writeString(mReadable);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<TimeToPayout> CREATOR = new Parcelable.Creator<TimeToPayout>() {
            @Override
            public TimeToPayout createFromParcel(Parcel in) {
                return new TimeToPayout(in);
            }

            @Override
            public TimeToPayout[] newArray(int size) {
                return new TimeToPayout[size];
            }
        };
    }
}
