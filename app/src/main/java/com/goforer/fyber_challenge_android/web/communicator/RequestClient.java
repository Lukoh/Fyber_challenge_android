package com.goforer.fyber_challenge_android.web.communicator;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.goforer.fyber_challenge_android.model.action.FinishAction;
import com.goforer.base.model.event.ResponseEvent;
import com.goforer.fyber_challenge_android.utility.CommonUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.http.Query;
import retrofit2.http.GET;

/**
 * Created by lukohnam on 16. 5. 24..
 */
public enum RequestClient {
    INSTANCE;

    private Context mContext;
    private RequestMethod mRequestor;

    private String mRawResponseBody;

    public RequestMethod getRequestMethod(Context context) {
        mContext = context;

        if (mRequestor == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    // Customize the request
                    Request request = original.newBuilder()
                            .header("Accept", "application/json")
                            .method(original.method(), original.body())
                            .build();

                    Response response = chain.proceed(request);

                    mRawResponseBody = response.body().string();

                    // Customize or return the response
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
    static public class RequestCallback implements Callback<ResponseClient> {
        private static final String SIGNATURE_HEADER = "X-Sponsorpay-Response-Signature";

        private ResponseEvent mEvent;

        public RequestCallback(ResponseEvent event) {
            mEvent = event;
        }

        @Override
        public void onResponse(Call<ResponseClient> call,
                               retrofit2.Response<ResponseClient> response) {
            if (!response.isSuccessful()) {
                try {
                    System.out.println(response.errorBody().string());
                } catch (IOException e) {
                    // do nothing
                }

                return;
            }

            if (isResponseError(response.body().getCode())) {
                showErrorMessage(response);
            } else if (mEvent != null) {
                String headerSignature = response.headers().get(SIGNATURE_HEADER);
                String hashkey = null;
                try {
                    hashkey = getResponseSignature(RequestClient.INSTANCE.getBody());
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                if (headerSignature.equals(hashkey)) {
                    mEvent.setResponseClient(response.body());
                    mEvent.getResponseClient().setStatus(ResponseClient.SUCCESSFUL);
                    mEvent.parseInResponse();
                } else {
                    mEvent.setResponseClient(new ResponseClient());
                    mEvent.getResponseClient().setStatus(
                            ResponseClient.RESPONSE_SIGNATURE_NOT_MATCH);
                }

                EventBus.getDefault().post(mEvent);
            }
        }

        @Override
        public void onFailure(Call<ResponseClient> call, Throwable t) {
            boolean isDeviceEnabled = true;

            if (!RequestClient.INSTANCE.isOnline()) {
                isDeviceEnabled = false;
            }

            if (mEvent != null) {
                mEvent.setResponseClient(new ResponseClient());
                if (!isDeviceEnabled) {
                    mEvent.getResponseClient().setStatus(ResponseClient.NETWORK_ERROR);
                } else {
                    mEvent.getResponseClient().setStatus(ResponseClient.GENERAL_ERROR);
                }

                EventBus.getDefault().post(mEvent);
            }
        }

        private void showErrorMessage(retrofit2.Response<ResponseClient> response) {
            FinishAction action = new FinishAction();
            action.setCode(response.body().getCode());
            action.setMessage(response.message());
            EventBus.getDefault().post(action);
        }

        private boolean isResponseError(String code) {
            switch(code) {
                case ResponseClient.ERROR_INVALID_PAGE:
                case ResponseClient.ERROR_INVALID_APPID:
                case ResponseClient.ERROR_INVALID_UID:
                case ResponseClient.ERROR_INVALID_DEVICE_ID:
                case ResponseClient.ERROR_INVALID_IP:
                case ResponseClient.ERROR_INVALID_TIMESTAMP:
                case ResponseClient.ERROR_INVALID_LOCALE:
                case ResponseClient.ERROR_INVALID_ANDROID_ID:
                case ResponseClient.ERROR_INVALID_CATEGORY:
                case ResponseClient.ERROR_INVALID_HASHKEY:
                case ResponseClient.NOT_FOUND:
                case ResponseClient.ERROR_INTERNAL_SERVER:
                case ResponseClient.BAD_GATEWAY:
                    return true;
                case ResponseClient.OK:
                case ResponseClient.NO_CONTENT:
                    return false;
                default:
                    return false;
            }
        }

        private String getResponseSignature(String body)
                throws UnsupportedEncodingException, NoSuchAlgorithmException {
            String value = body + CommonUtils.API_KEY;

            return CommonUtils.SHA1(value);
        }
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        }else {
            if (connectivityManager != null) {
                @SuppressWarnings("deprecation")
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public interface RequestMethod {
        @GET("offers.json")
        Call<ResponseClient> getOffers(
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

    }
}
