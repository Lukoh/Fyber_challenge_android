package com.goforer.fyber_challenge;

import android.test.ActivityInstrumentationTestCase2;

import com.goforer.fyber_challenge.ui.activity.OffersActivity;
import com.goforer.fyber_challenge.web.communicator.RequestClient;

import org.jetbrains.annotations.TestOnly;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RetrofitCreate_Test extends ActivityInstrumentationTestCase2<OffersActivity> {

    private static boolean mIsCalled;

    public RetrofitCreate_Test() {
        super(OffersActivity.class);
    }

    @TestOnly
    public void testCreateRetrofit() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);

        RequestClient.RequestMethod call =
                RequestClient.INSTANCE.getRequestMethod();

        if (call != null) {
            mIsCalled = true;
        } else {
            mIsCalled = false;
        }

        /* The testing thread will wait here until the UI thread releases it
	     * above with the countDown() or 30 seconds passes and it times out.
	     */
        signal.await(10, TimeUnit.SECONDS);
        assertTrue(mIsCalled);

    }
}
