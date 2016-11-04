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

package com.goforer.fyber_challenge.model.data;

import com.goforer.base.model.data.ResponseBase;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class ResponseOffer extends ResponseBase {
    @SerializedName("pages")
    private int mPages;
    @SerializedName("information")
    private Information mInformation;
    /**
     * This fit in only Fyber server, but The variable's name of JsonElement have to be the data
     * in real project and the datas have to be parsed and put to each data class.
     * Please refer to below.
     *
     * @SerializedName("datas")
     * private JsonElement mDatas
     *
     */
    @SerializedName("offers")
    private JsonElement mOffers;

    public int getPages() {
        return mPages;
    }

    public Information getInformtion() {
        return mInformation;
    }

    public JsonElement getOffers() {
        return mOffers;
    }

    public static class Information {
        @SerializedName("app_name")
        private String mAppName;
        @SerializedName("appid")
        private long mAppId;
        @SerializedName("virtual_currency")
        private String mVirualCurrency;
        @SerializedName("country")
        private String mCountry;
        @SerializedName("language")
        private String mLanguage;
        @SerializedName("support_url")
        private String mSupportUrl;

        public String getAppName() {
            return mAppName;
        }

        public long getAppId() {
            return mAppId;
        }

        public String getVirualCurrency() {
            return mVirualCurrency;
        }

        public String getCountry() {
            return mCountry;
        }

        public String getLanguage() {
            return mLanguage;
        }

        public String getSupportUrl() {
            return mSupportUrl;
        }
    }
}
