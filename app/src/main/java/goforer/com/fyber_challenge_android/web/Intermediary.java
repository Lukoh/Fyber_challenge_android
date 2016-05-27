package goforer.com.fyber_challenge_android.web;

import android.content.Context;

import java.io.IOException;

import goforer.com.base.model.event.ResponseEvent;
import goforer.com.fyber_challenge_android.web.communicator.RequestClient;
import goforer.com.fyber_challenge_android.web.communicator.ResponseClient;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by lukohnam on 16. 5. 24..
 */

/**
 * The intermediary class for sending a request to Server and returns a response.

 * <p>
 * This App adapts a Retrofit to communicate with Server(Retrofit Version 2).
 * Retrofit provides great way to HTTP calls by using annotations on the declared methods to
 * define how requests are made. (Retrofit is a great HTTP client for Android (and Java))
 *
 * A call that is busy writing its request or reading its response may receive a {@link IOException};
 * this is working as designed.
 *
 * ResponseClient: Successful response body type.
 * </p>
 *
 */
public enum Intermediary {
    INSTANCE;

    public void getOffers(Context context, int appId, String device_id, String ip, String locale,
                          int offer_type, int pageNum, long timestamp, String uid, String hashKey,
                          ResponseEvent event)  {

        Call<ResponseClient> call = RequestClient.INSTANCE.getRequestMethod(context)
                .getOffers(appId, device_id, ip, locale, offer_type, pageNum, timestamp, uid, hashKey);
        call.enqueue(new RequestClient.RequestCallback(event) {
            @Override
            public void onResponse(Call<ResponseClient> call, Response<ResponseClient> response) {
                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<ResponseClient> call, Throwable t) {
                super.onFailure(call, t);
            }

        });
    }
}
