package com.goforer.fyber_challenge_android;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.goforer.fyber_challenge_android.utility.CommonUtils;

/**
 * Created by lukohnam on 16. 5. 24..
 */
public class Concatenate_Test extends TestCase {
    private static final String IP = "109.235.143.113";
    private static final String LOCALE = "DE";
    private static final String UID = "spiderman";
    private static final String GAID = "c53305827e98418cb1131a19353ed211";

    private static final String RESULT_CONCATENATE_STRING =
            "appid=2070&device_id=c53305827e98418cb1131a19353ed211&ip=109.235.143.113&locale=DE&offer_types=112&page=1&timestamp=1432456744&uid=spiderman&1c915e3b5d42d05136185030892fbb846c278927";

    private static final long TIME_STAMP = 1432456744;

    private static final int APP_ID = 2070;
    private static final int OFFER_TYPES = 112;

    @Before
    public String makeConcatenateString() {
        StringBuilder tmp;
        tmp = new StringBuilder();
        tmp.append("appid=");
        tmp.append(APP_ID).append("&");
        tmp.append("device_id=");
        tmp.append(GAID).append("&");
        tmp.append("ip=");
        tmp.append(IP).append("&");
        tmp.append("locale=");
        tmp.append(LOCALE).append("&");
        tmp.append("offer_types=");
        tmp.append(OFFER_TYPES).append("&");
        tmp.append("page=");
        tmp.append(1).append("&");
        tmp.append("timestamp=");
        tmp.append(TIME_STAMP).append("&");
        tmp.append("uid=");
        tmp.append(UID).append("&");
        tmp.append(CommonUtils.API_KEY);

        return tmp.toString();
    }

    @Test
    public void test_makeConcatenateString() {
        assertEquals(RESULT_CONCATENATE_STRING, makeConcatenateString());
    }
}
