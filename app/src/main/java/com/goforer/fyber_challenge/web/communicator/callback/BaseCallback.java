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

package com.goforer.fyber_challenge.web.communicator.callback;

import com.goforer.base.model.data.ResponseBase;
import com.goforer.fyber_challenge.model.action.FinishAction;
import com.goforer.fyber_challenge.model.data.ResponseError;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.ResponseBody;

public class BaseCallback {
    private static ResponseError mErrorResponse = null;

    protected boolean isResponseError(ResponseBody errorBody) {
        try {
            if (errorBody != null) {
                mErrorResponse = ResponseError.gson()
                        .fromJson(errorBody.string(), ResponseError.class);
            } else {
                return false;
            }
        } catch (IOException e) {
            // do nothing
        }

        assert (mErrorResponse != null ? mErrorResponse.getErrorCode() : null) != null;
        switch(mErrorResponse.getErrorCode()) {
            case ResponseBase.ERROR_INVALID_PAGE:
            case ResponseBase.ERROR_INVALID_APPID:
            case ResponseBase.ERROR_INVALID_UID:
            case ResponseBase.ERROR_INVALID_DEVICE_ID:
            case ResponseBase.ERROR_INVALID_IP:
            case ResponseBase.ERROR_INVALID_TIMESTAMP:
            case ResponseBase.ERROR_INVALID_LOCALE:
            case ResponseBase.ERROR_INVALID_ANDROID_ID:
            case ResponseBase.ERROR_INVALID_CATEGORY:
            case ResponseBase.ERROR_INVALID_HASHKEY:
            case ResponseBase.NOT_FOUND:
            case ResponseBase.ERROR_INTERNAL_SERVER:
            case ResponseBase.BAD_GATEWAY:
                return true;
            case ResponseBase.OK:
            case ResponseBase.NO_CONTENT:
                return false;
            default:
                return false;
        }
    }

    protected void showErrorMessage(String error) {
        FinishAction action = new FinishAction();

        if (mErrorResponse == null) {
            mErrorResponse= ResponseError.gson().fromJson(error, ResponseError.class);
        }

        action.setCode(mErrorResponse.getErrorCode());
        action.setMessage(mErrorResponse.getErrorMessage());
        EventBus.getDefault().post(action);
    }
}
