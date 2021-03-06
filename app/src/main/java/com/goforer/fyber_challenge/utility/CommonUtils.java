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

package com.goforer.fyber_challenge.utility;

import android.content.Context;
import android.widget.Toast;

import com.goforer.fyber_challenge.model.data.Profile;
import com.goforer.fyber_challenge.web.communicator.RequestClient;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommonUtils {
    public static final String API_KEY = "1c915e3b5d42d05136185030892fbb846c278927";

    public static final String TRANSITION_NAME_FOR_IMAGE = "Image ";
    public static final String TRANSITION_NAME_FOR_TITLE = "Title ";

    private static String mGoogleAID;
    private static boolean mIsLimitAdTrackingEnabled;
    private static Profile mProfile;

    public static String SHA1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static void setGoogleAID(String googleAID) {
        mGoogleAID = googleAID;
    }

    public static void setProfile(Profile profile) {
        mProfile = profile;
    }

    public static void setLimitAdTrackingEnabled(boolean isLimitAdTrackingEnabled) {
        mIsLimitAdTrackingEnabled = isLimitAdTrackingEnabled;
    }

    public static String getGoogleAID() {
        return mGoogleAID;
    }

    public static Profile getProfile() {
        return mProfile;
    }

    public static boolean getLimitAdTrackingEnabled() {
        return mIsLimitAdTrackingEnabled;
    }

    public static String getCurrentDateTime() {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(c.getTime());
    }

    public static void showToastMessage(Context context, String text, int duration) {
        Toast.makeText(context, text, duration).show();
    }

    public static String getHashKey(String advertisingId, long timestamp, int pageNum)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        StringBuilder tmp;
        tmp = new StringBuilder();
        tmp.append("appid=");
        tmp.append(RequestClient.APP_ID).append("&");
        tmp.append("device_id=");
        tmp.append(advertisingId).append("&");
        tmp.append("ip=");
        tmp.append(RequestClient.IP).append("&");
        tmp.append("locale=");
        tmp.append(RequestClient.LOCALE).append("&");
        tmp.append("offer_types=");
        tmp.append(RequestClient.OFFER_TYPES).append("&");
        tmp.append("page=");
        tmp.append(pageNum).append("&");
        tmp.append("timestamp=");
        tmp.append(timestamp).append("&");
        tmp.append("uid=");
        tmp.append(RequestClient.UID).append("&");
        tmp.append(CommonUtils.API_KEY);

        return CommonUtils.SHA1(tmp.toString());
    }
}
