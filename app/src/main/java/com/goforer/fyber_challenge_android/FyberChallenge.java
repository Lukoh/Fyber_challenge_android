/*
 * Copyright (C) 2016 Lukoh Nam, goForer
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

package com.goforer.fyber_challenge_android;

import android.content.Context;
import android.content.res.Resources;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.goforer.fyber_challenge_android.utility.ExceptionHandler;

public class FyberChallenge extends MultiDexApplication {
    private static final String TAG = "FyberChallenge";

    public static Context mContext;
    public static Resources mResources;


    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        mResources = getResources();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.UncaughtExceptionHandler exceptionHandler =
                        Thread.getDefaultUncaughtExceptionHandler();
                try {
                    exceptionHandler = new ExceptionHandler(FyberChallenge.this, exceptionHandler,
                            new ExceptionHandler.OnFindCrashLogListener() {

                        @Override
                        public void onFindCrashLog(String log) {
                            Log.e("onFindCrashLog", log);
                        }

                        @Override
                        public void onCaughtCrash(Throwable throwable) {
                            Log.e(TAG, "Ooooooops Crashed!!");
                        }
                    });

                    Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
                } catch( NullPointerException e ) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public static void closeApplication() {
        System.exit(0);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(FyberChallenge.this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}
