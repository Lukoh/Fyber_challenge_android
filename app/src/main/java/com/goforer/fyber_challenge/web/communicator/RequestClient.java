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

package com.goforer.fyber_challenge.web.communicator;

import android.content.Context;

import com.goforer.base.model.data.ResponseBase;
import com.goforer.base.model.event.ResponseEvent;
import com.goforer.fyber_challenge.model.data.ResponseOffer;
import com.goforer.fyber_challenge.utility.CommonUtils;
import com.goforer.fyber_challenge.utility.ConnectionUtils;
import com.goforer.fyber_challenge.web.communicator.callback.BaseCallback;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public enum RequestClient {
    INSTANCE;

    public static final String IP = "109.235.143.113";
    public static final String LOCALE = "DE";
    public static final String UID = "spiderman";

    public static final long APP_ID = 2070;
    public static final int OFFER_TYPES = 112;


    private static final long READ_TIME_OUT = 5;
    private static final long WRITE_TIME_OUT = 5;
    private static final long CONNECT_TIME_OUT = 5;

    private RequestMethod mRequestor;

    private String mRawResponseBody;

    public RequestMethod getRequestMethod() {
        if (mRequestor == null) {
           final OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                    .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS);

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Connection", "keep-alive")
                            .header("Content-Encoding", "gzip")
                            .method(original.method(), original.body())
                            .build();

                    Response response = chain.proceed(request);

                    mRawResponseBody = response.body().string();

                    return response.newBuilder()
                            .body(ResponseBody.create(response.body().contentType(),
                                    mRawResponseBody)).build();
                }
            });

            OkHttpClient client = httpClient.build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.fyber.com/feed/v1/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            mRequestor = retrofit.create(RequestMethod.class);
        }

        return mRequestor;
    }

    public String getBody() {
        return mRawResponseBody;
    }


    /**
     * Communicates responses from Server or offline requests.
     * One and only one method will be invoked in response to a given request.
     */
    static public class OfferCallback extends BaseCallback implements Callback<ResponseOffer> {
        private static final String SIGNATURE_HEADER = "X-Sponsorpay-Response-Signature";

        private ResponseEvent mEvent;
        private Context mContext;

        protected OfferCallback(ResponseEvent event, Context context) {
            mEvent = event;
            mContext = context;
        }

        @Override
        public void onResponse(Call<ResponseOffer> call,
                               retrofit2.Response<ResponseOffer> response) {
            if (!response.isSuccessful() || isResponseError(response.errorBody())) {
                try {
                    showErrorMessage(response.errorBody().string());
                    System.out.println(response.errorBody().string());
                } catch (IOException e) {
                    // do nothing
                }

                return;
            }

            if (mEvent != null) {
                if (responseValidityCheck(response.headers().get(SIGNATURE_HEADER))) {
                    mEvent.setResponseClient(response.body());
                    mEvent.getResponseClient().setStatus(ResponseBase.SUCCESSFUL);
                    mEvent.parseInResponse();
                } else {
                    mEvent.setResponseClient(new ResponseOffer());
                    mEvent.getResponseClient().setStatus(
                            ResponseBase.RESPONSE_SIGNATURE_NOT_MATCH);
                }

                EventBus.getDefault().post(mEvent);
            }
        }

        @Override
        public void onFailure(Call<ResponseOffer> call, Throwable t) {
            boolean isDeviceEnabled = true;

            if (!ConnectionUtils.INSTANCE.isOnline(mContext)) {
                isDeviceEnabled = false;
            }

            if (mEvent != null) {
                mEvent.setResponseClient(new ResponseOffer());
                if (!isDeviceEnabled) {
                    mEvent.getResponseClient().setStatus(ResponseOffer.NETWORK_ERROR);
                } else {
                    mEvent.getResponseClient().setStatus(ResponseOffer.GENERAL_ERROR);
                }

                EventBus.getDefault().post(mEvent);
            }
        }

        private String getResponseSignature(String body)
                throws UnsupportedEncodingException, NoSuchAlgorithmException {
            String value = body + CommonUtils.API_KEY;

            return CommonUtils.SHA1(value);
        }

        private boolean responseValidityCheck(final String headerSignature) {
            String hashKey = null;
            try {
                hashKey = getResponseSignature(RequestClient.INSTANCE.getBody());
            } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            return headerSignature.equals(hashKey);

        }
    }

    public interface RequestMethod {
        @GET("profile.json")
        Call<ResponseOffer> getProfile(
                @Query("id") String id,
                @Query("hashkey") String hashkey
        );

        @GET("offers.json")
        Call<ResponseOffer> getOffers(
                @Query("appid") long appid,
                @Query("device_id") String google_ad_id,
                @Query("ip") String ip,
                @Query("locale") String locale,
                @Query("offer_types") int offer_types,
                @Query("page") int page,
                @Query("timestamp") long timestamp,
                @Query("uid") String uid,
                @Query("hashkey") String hashkey
        );

        @FormUrlEncoded
        @POST("offers/comment/like")
        Call<ResponseOffer> postLikeComment(
                @Field("uid") String my_id,
                @Field("offer_id") long offer_id,
                @Field("commenter_id") long commenter_id,
                @Field("comment_id") long comment_id
        );
    }
}
