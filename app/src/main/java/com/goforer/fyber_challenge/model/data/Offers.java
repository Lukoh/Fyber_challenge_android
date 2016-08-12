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

package com.goforer.fyber_challenge.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.goforer.base.model.BaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Offers extends BaseModel implements Parcelable {
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
    @SerializedName("bookmarked")
    private boolean mBookmarked;
    @SerializedName("subscribed")
    private boolean mSubscribed;
    @SerializedName("bookmarked_count")
    private int mBookmarkedCount;
    @SerializedName("subscribed_count")
    private int mSubscribedCount;
    @SerializedName("gallery_count")
    private int mGalleryCount;
    @SerializedName("time_to_payout")
    private TimeToPayout mTimeToPayout;
    @SerializedName("events")
    private List<Event> mEvents;
    @SerializedName("Comments")
    private List<Comment> mComments;

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

    public boolean isBookmarked() {
        return mBookmarked;
    }

    public boolean isSubscribed() {
        return mSubscribed;
    }

    public int getBookmarkedCount() {
        return mBookmarkedCount;
    }

    public int getSubscribedCount() {
        return mSubscribedCount;
    }

    public int getGalleryCount() {
        return mGalleryCount;
    }

    public TimeToPayout getTimeToPayout() {
        return mTimeToPayout;
    }

    public List<Event> getEvents() {
        return mEvents;
    }

    public List<Comment> getComments() {
        return mComments;
    }

    public void setBookmarked(boolean bookmarked) {
        mBookmarked = bookmarked;
    }

    public void setSubscribed(boolean subscribed) {
        mSubscribed = subscribed;
    }

    public void setBookmarkedCount(int count) {
        mBookmarkedCount = count;
    }

    public void setSubscribedCount(int count) {
        mSubscribedCount = count;
    }

    public void setGalleryCount(int count) {
        mGalleryCount = count;
    }

    public void setEvents(List<Event> events) {
        mEvents = events;
    }

    public void setComments(List<Comment> comments) {
        mComments = comments;
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
        mBookmarked = in.readByte() != 0;
        mSubscribed = in.readByte() != 0;
        mBookmarkedCount = in.readInt();
        mSubscribedCount = in.readInt();
        mGalleryCount = in.readInt();
        mTimeToPayout = in.readParcelable(TimeToPayout.class.getClassLoader());
        mEvents = new ArrayList<>();
        in.readTypedList(mEvents, Event.CREATOR);
        mComments = new ArrayList<>();
        in.readTypedList(mComments, Comment.CREATOR);
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
        dest.writeByte((byte) (mBookmarked ? 1 : 0));
        dest.writeByte((byte) (mSubscribed ? 1 : 0));
        dest.writeInt(mBookmarkedCount);
        dest.writeInt(mSubscribedCount);
        dest.writeInt(mGalleryCount);
        dest.writeParcelable(mTimeToPayout, flags);
        dest.writeTypedList(mEvents);
        dest.writeTypedList(mComments);
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
