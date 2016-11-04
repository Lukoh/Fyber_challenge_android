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

package com.goforer.fyber_challenge.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.goforer.base.model.BaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Profile  extends BaseModel implements Parcelable {
    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("picture_url")
    private String mPictureURL;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("virtual_currency")
    private String mVirtualCurrenccy;
    @SerializedName("country")
    private String mCountry;
    @SerializedName("language")
    private String mLaunguage;
    @SerializedName("points")
    private long mPoints;
    @SerializedName("bookmarks")
    private List<Offers> mBookmarks;
    @SerializedName("ads_subscription")
    private List<Offers> mSubscriptions;

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getPictureURL() {
        return mPictureURL;
    }

    public String getVirtualCurrenccy() {
        return mVirtualCurrenccy;
    }

    public String getCountry() {
        return mCountry;
    }

    public String getLanguage() {
        return mLaunguage;
    }

    public String getEmail() {
        return mEmail;
    }

    public long getPoints() {
        return mPoints;
    }

    public List<Offers> getBookmarks() {
        return mBookmarks;
    }

    public List<Offers> getSubscriptions() {
        return mSubscriptions;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setPictureUrl(String url) {
        mPictureURL = url;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public void setVirtualCurrenccy(String currenccy) {
        mVirtualCurrenccy = currenccy;
    }

    public void setLaunguage(String launguage) {
        mLaunguage = launguage;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public void setPoints(long points) {
        mPoints = points;
    }

    public void setBookmarks(List<Offers> bookmarks) {
        mBookmarks = bookmarks;
    }

    public void setSubscriptions(List<Offers> subscriptions) {
        mSubscriptions = subscriptions;
    }

    protected Profile(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mPictureURL = in.readString();
        mEmail = in.readString();
        mVirtualCurrenccy = in.readString();
        mCountry = in.readString();
        mLaunguage = in.readString();
        mPoints = in.readLong();
        mBookmarks = new ArrayList<>();
        in.readTypedList(mBookmarks, Offers.CREATOR);
        mSubscriptions = new ArrayList<>();
        in.readTypedList(mSubscriptions, Offers.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mPictureURL);
        dest.writeString(mEmail);
        dest.writeString(mVirtualCurrenccy);
        dest.writeString(mCountry);
        dest.writeString(mLaunguage);
        dest.writeLong(mPoints);
        dest.writeTypedList(mBookmarks);
        dest.writeTypedList(mSubscriptions);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

}
