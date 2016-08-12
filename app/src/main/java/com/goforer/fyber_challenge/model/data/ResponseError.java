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

public class ResponseError extends BaseModel implements Parcelable {
    @SerializedName("code")
    private String mCode;
    @SerializedName("message")
    private String mMessage;

    public String getErrorCode() {
        return mCode;
    }

    public String getErrorMessage() {
        return mMessage;
    }

    protected ResponseError(Parcel in) {
        mCode = in.readString();
        mMessage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCode);
        dest.writeString(mMessage);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ResponseError> CREATOR =
            new Parcelable.Creator<ResponseError>() {
        @Override
        public ResponseError createFromParcel(Parcel in) {
            return new ResponseError(in);
        }

        @Override
        public ResponseError[] newArray(int size) {
            return new ResponseError[size];
        }
    };
}
