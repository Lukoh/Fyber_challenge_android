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

package com.goforer.fyber_challenge_android.web;

import android.content.Context;

import com.goforer.base.model.event.ResponseEvent;
import com.goforer.fyber_challenge_android.utility.CommonUtils;
import com.goforer.fyber_challenge_android.web.communicator.RequestClient;
import com.goforer.fyber_challenge_android.web.communicator.ResponseClient;

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
 * ResponseClient: Successful response body type.
 * </p>
 *
 */
public enum Intermediary {
    INSTANCE;

    public void getProfile(Context context, String id, String hashKey,  ResponseEvent event) {
        Call<ResponseClient> call = RequestClient.INSTANCE.getRequestMethod(context)
                .getProfile(id, hashKey);
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

    public void postLikeComment(Context context, long offerId, long commenterId,
                                long commentId, ResponseEvent event) {
        Call<ResponseClient> call = RequestClient.INSTANCE.getRequestMethod(context)
                .postLikeComment(CommonUtils.getProfile().getId(), offerId, commenterId, commentId);
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
