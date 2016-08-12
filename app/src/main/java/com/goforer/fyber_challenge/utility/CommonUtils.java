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

package com.goforer.fyber_challenge.utility;

import com.goforer.fyber_challenge.model.data.Profile;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommonUtils {
    public static final String API_KEY = "1c915e3b5d42d05136185030892fbb846c278927";

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
}
