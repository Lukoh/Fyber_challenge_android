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

public class Comment extends BaseModel implements Parcelable {
    @SerializedName("comment_id")
    private long mCommentId;
    @SerializedName("commenter_idx")
    private long mCommenterId;
    @SerializedName("offer_id")
    private long mOfferId;
    @SerializedName("like_count")
    private long mLikeCount;
    @SerializedName("commenter_picture")
    private Picture mPicture;
    @SerializedName("comment")
    private String mComment;
    @SerializedName("commenter_name")
    private String mCommenterName;
    @SerializedName("date")
    private String mDate;

    public Comment() {
    }

    public long getCommentId() {
        return mCommentId;
    }

    public long getCommenterId() {
        return mCommenterId;
    }

    public long getOfferId() {
        return mOfferId;
    }

    public long getCommentLikeCount() {
        return mLikeCount;
    }

    public Picture getPicture() {
        return mPicture;
    }

    public String getComment() {
        return mComment;
    }

    public String getCommenterName() {
        return mCommenterName;
    }

    public String getDate() {
        return mDate;
    }

    public void setCommentId(long commentId) {
        mCommentId = commentId;
    }

    public void setCommenterId(long commenterId) {
        mCommenterId = commenterId;
    }

    public void setOfferId(long offerId) {
        mOfferId = offerId;
    }

    public void setCommentLikeCount(long count) {
        mLikeCount = count;
    }

    public void setPicture(Picture picture) {
        mPicture = picture;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public void setCommenterName(String commenterName) {
        mCommenterName = commenterName;
    }

    public void setDate(String date) {
        mDate = date;
    }

    protected Comment(Parcel in) {
        mCommentId = in.readLong();
        mCommenterId = in.readLong();
        mOfferId = in.readLong();
        mLikeCount = in.readLong();
        mPicture = in.readParcelable(Picture.class.getClassLoader());
        mComment = in.readString();
        mCommenterName = in.readString();
        mDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mCommentId);
        dest.writeLong(mCommenterId);
        dest.writeLong(mOfferId);
        dest.writeLong(mLikeCount);
        dest.writeParcelable(mPicture, flags);
        dest.writeString(mComment);
        dest.writeString(mCommenterName);
        dest.writeString(mDate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };


    public final static class Picture implements Parcelable {
        @SerializedName("commenter_picture_url")
        private String mCommenterPictureUrl;

        public Picture() {

        }

        public String getCommenterPictureUrl() {
            return mCommenterPictureUrl;
        }

        public void setCommenterPictureUrl(String url) {
            mCommenterPictureUrl = url;
        }

        protected Picture(Parcel in) {
            mCommenterPictureUrl = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mCommenterPictureUrl);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Picture> CREATOR = new Parcelable.Creator<Picture>() {
            @Override
            public Picture createFromParcel(Parcel in) {
                return new Picture(in);
            }

            @Override
            public Picture[] newArray(int size) {
                return new Picture[size];
            }
        };
    }
}
