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

package com.goforer.base.model.data;

import com.google.gson.annotations.SerializedName;

public class ResponseBase {
    // HTTP Code : 200
    public static final String OK = "OK";
    // HTTP Code : 200
    public static final String NO_CONTENT = "NO_CONTENT";
    // HTTP Code : 400
    public static final String ERROR_INVALID_PAGE = "ERROR_INVALID_PAGE";
    // HTTP Code : 400
    public static final String ERROR_INVALID_APPID = "ERROR_INVALID_APPID";
    // HTTP Code : 400
    public static final String ERROR_INVALID_UID = "ERROR_INVALID_UID";
    // HTTP Code : 400
    public static final String ERROR_INVALID_DEVICE_ID = "ERROR_INVALID_DEVICE_ID";
    // HTTP Code : 400
    public static final String ERROR_INVALID_IP = "ERROR_INVALID_IP";
    // HTTP Code : 400
    public static final String ERROR_INVALID_TIMESTAMP = "ERROR_INVALID_TIMESTAMP";
    // HTTP Code : 400
    public static final String ERROR_INVALID_LOCALE = "ERROR_INVALID_LOCALE";
    // HTTP Code : 400
    public static final String ERROR_INVALID_ANDROID_ID = "ERROR_INVALID_ANDROID_ID";
    // HTTP Code : 400
    public static final String ERROR_INVALID_CATEGORY = "ERROR_INVALID_CATEGORY";
    // HTTP Code : 401
    public static final String ERROR_INVALID_HASHKEY = "ERROR_INVALID_HASHKEY";
    // HTTP Code : 404
    public static final String NOT_FOUND = "NOT_FOUND";
    // HTTP Code : 500
    public static final String ERROR_INTERNAL_SERVER = "ERROR_INTERNAL_SERVER_ERROR";
    // HTTP Code : 502
    public static final String BAD_GATEWAY = "BAD_GATEWAY";

    public static final int GENERAL_ERROR = -1;
    public static final int RESPONSE_SIGNATURE_NOT_MATCH = -2;
    public static final int NETWORK_ERROR = -3;
    public static final int SUCCESSFUL = 1;

    @SerializedName("code")
    private String mCode;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("count")
    private int mCount;

    private int mStatus;

    public String getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    public int getCount() {
        return mCount;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public boolean isSuccessful(){
        if (OK.equals(mCode) || NO_CONTENT.equals(mCode)) {
            return true;
        } else {
            return false;
        }
    }
}
