package com.goforer.fyber_challenge;

import android.test.ActivityInstrumentationTestCase2;

import com.goforer.fyber_challenge.model.event.OffersDataEvent;
import com.goforer.fyber_challenge.ui.activity.OffersActivity;
import com.goforer.fyber_challenge.utility.CommonUtils;
import com.goforer.fyber_challenge.web.communicator.RequestClient;
import com.goforer.fyber_challenge.model.data.ResponseOffer;

import org.jetbrains.annotations.TestOnly;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class Request_Test extends ActivityInstrumentationTestCase2<OffersActivity> {
    private static final String IP = "109.235.143.113";
    private static final String LOCALE = "DE";
    private static final String UID = "spiderman";
    private static final String GAID = "c53305827e98418cb1131a19353ed211";

    private static final int APP_ID = 2070;
    private static final int OFFER_TYPES = 112;

    private static boolean mIsSucceeded;

    public Request_Test(){
        super(OffersActivity.class);
    }

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

    @TestOnly
    public void testGetResponse() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);

        long timestamp = System.currentTimeMillis() / 1000L;

        OffersDataEvent event = new OffersDataEvent(true);
        String hashKey = null;
        try {
            hashKey = getHashKey(GAID, timestamp, 1);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Call<ResponseOffer> call = RequestClient.INSTANCE.getRequestMethod()
                .getOffers(APP_ID, GAID, IP, LOCALE, OFFER_TYPES, 1, timestamp, UID, hashKey);
        call.enqueue(new RequestClient.OfferCallback(event, getActivity().getApplicationContext()) {
            @Override
            public void onResponse(Call<ResponseOffer> call, Response<ResponseOffer> response) {
                mIsSucceeded = true;
            }

            @Override
            public void onFailure(Call<ResponseOffer> call, Throwable t) {
                super.onFailure(call, t);
                mIsSucceeded = false;
            }

        });

	    /* The testing thread will wait here until the UI thread releases it
	     * above with the countDown() or 30 seconds passes and it times out.
	     */
        signal.await(10, TimeUnit.SECONDS);
        assertTrue(mIsSucceeded);
    }
}
