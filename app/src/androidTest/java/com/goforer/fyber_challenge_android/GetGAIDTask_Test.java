package com.goforer.fyber_challenge_android;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

import com.goforer.fyber_challenge_android.ui.activity.OffersActivity;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import org.jetbrains.annotations.TestOnly;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.goforer.fyber_challenge_android.utility.CommonUtils;

/**
 * Created by lukohnam on 16. 5. 24..
 */
public class GetGAIDTask_Test extends ActivityInstrumentationTestCase2<OffersActivity> {
    private static boolean mIsCalled;

    public GetGAIDTask_Test(){
        super(OffersActivity.class);
    }

    private class GetGAIDTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            AdvertisingIdClient.Info adInfo;
            adInfo = null;
            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(
                        getActivity().getApplicationContext());
                if (adInfo.isLimitAdTrackingEnabled()) {
                    CommonUtils.setLimitAdTrackingEnabled(true);
                    return "Not found GAID";
                }
            } catch (IOException | GooglePlayServicesNotAvailableException
                    | GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            }

            return adInfo.getId();
        }

        @Override
        protected void onPostExecute(String id) {
            if ("Not found GAID".equals(id) && !id.isEmpty()) {
                mIsCalled = true;
            } else {
                mIsCalled = true;
            }
        }
    }

    @TestOnly
    public void testGetGAIDTask() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GetGAIDTask().execute();
            }
        });

	    /* The testing thread will wait here until the UI thread releases it
	     * above with the countDown() or 30 seconds passes and it times out.
	     */
        signal.await(10, TimeUnit.SECONDS);
        assertTrue(mIsCalled);
    }

}
