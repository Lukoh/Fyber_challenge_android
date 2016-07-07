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

package com.goforer.fyber_challenge_android.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.goforer.base.model.BaseModel;
import com.google.gson.annotations.SerializedName;

public class Gallery extends BaseModel implements Parcelable {
    @SerializedName("title")
    private String mTitle;
    @SerializedName("thumbnail")
    private Thumbnail mThumbnail;
    @SerializedName("gallery_count")
    private int mGalleryCount;

    public String getTitle() {
        return mTitle;
    }

    public Thumbnail getThumbnail() {
        return mThumbnail;
    }

    public int getGalleryCount() {
        return mGalleryCount;
    }

    public void setGalleryCount(int count) {
        mGalleryCount = count;
    }

    protected Gallery(Parcel in) {
        mTitle = in.readString();
        mThumbnail = in.readParcelable(Thumbnail.class.getClassLoader());
        mGalleryCount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeParcelable(mThumbnail, flags);
        dest.writeInt(mGalleryCount);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Gallery> CREATOR = new Parcelable.Creator<Gallery>() {
        @Override
        public Gallery createFromParcel(Parcel in) {
            return new Gallery(in);
        }

        @Override
        public Gallery[] newArray(int size) {
            return new Gallery[size];
        }
    };

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
}
