package com.goforer.fyber_challenge;

import com.goforer.fyber_challenge.utility.CommonUtils;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class SHA1_HashKey_Test extends TestCase {
    private static final String IP = "109.235.143.113";
    private static final String LOCALE = "DE";
    private static final String UID = "spiderman";
    private static final String GAID = "c53305827e98418cb1131a19353ed211";

    private static final String SHA1_HASH_KEY = "d2a7c77b365c68805e72d6e8fb23279b63fa5708";

    private static final long TIME_STAMP = 1432456744;

    private static final int APP_ID = 2070;
    private static final int OFFER_TYPES = 112;

    @Before
    private String getHashKey(String advertisingId, long timestamp, int pageNum)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        StringBuilder tmp;
        tmp = new StringBuilder();
        tmp.append("appid=");
        tmp.append(APP_ID).append("&");
        tmp.append("device_id=");
        tmp.append(advertisingId).append("&");
        tmp.append("ip=");
        tmp.append(IP).append("&");
        tmp.append("locale=");
        tmp.append(LOCALE).append("&");
        tmp.append("offer_types=");
        tmp.append(OFFER_TYPES).append("&");
        tmp.append("page=");
        tmp.append(pageNum).append("&");
        tmp.append("timestamp=");
        tmp.append(timestamp).append("&");
        tmp.append("uid=");
        tmp.append(UID).append("&");
        tmp.append(CommonUtils.API_KEY);

        return CommonUtils.SHA1(tmp.toString());
    }

    @Test
    public void test_SHA1_HashKey() {
        try {
            assertEquals(SHA1_HASH_KEY, getHashKey(GAID, TIME_STAMP, 1));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
