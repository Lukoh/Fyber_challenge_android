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

package com.goforer.fyber_challenge.web;

import android.content.Context;

import com.goforer.base.model.event.ResponseEvent;
import com.goforer.fyber_challenge.model.data.ResponseOffer;
import com.goforer.fyber_challenge.utility.CommonUtils;
import com.goforer.fyber_challenge.web.communicator.RequestClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

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
 * ResponseOffer: Successful response body type.
 * </p>
 *
 */
public enum Intermediary {
    INSTANCE;

    public void getProfile(final Context context, String id, String hashKey,
                           ResponseEvent<ResponseOffer> event) {
        Call<ResponseOffer> call = RequestClient.INSTANCE.getRequestMethod()
                .getProfile(id, hashKey);
        call.enqueue(new RequestClient.OfferCallback(event, context) {
            @Override
            public void onResponse(Call<ResponseOffer> call, Response<ResponseOffer> response) {
                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<ResponseOffer> call, Throwable t) {
                super.onFailure(call, t);
            }

        });
    }

    public void getOffers(final Context context, long appId, String device_id, String ip, String locale,
                          int offer_type, int pageNum, long timestamp, String uid, String hashKey,
                          ResponseEvent<ResponseOffer> event)  {
        Call<ResponseOffer> call = RequestClient.INSTANCE.getRequestMethod()
                .getOffers(appId, device_id, ip, locale, offer_type, pageNum, timestamp, uid, hashKey);
        call.enqueue(new RequestClient.OfferCallback(event, context) {
            @Override
            public void onResponse(Call<ResponseOffer> call, Response<ResponseOffer> response) {
                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<ResponseOffer> call, Throwable t) {
                super.onFailure(call, t);
            }

        });
    }

    public void postLikeComment(Context context, long offerId, long commenterId,
                                long commentId, ResponseEvent<ResponseOffer> event) {
        Call<ResponseOffer> call = RequestClient.INSTANCE.getRequestMethod()
                .postLikeComment(CommonUtils.getProfile().getId(), offerId, commenterId, commentId);
        call.enqueue(new RequestClient.OfferCallback(event, context) {
            @Override
            public void onResponse(Call<ResponseOffer> call, Response<ResponseOffer> response) {
                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<ResponseOffer> call, Throwable t) {
                super.onFailure(call, t);
            }

        });
    }
}
