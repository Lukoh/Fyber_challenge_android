package goforer.com.fyber_challenge_android;

import android.content.Context;
import android.content.res.Resources;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import goforer.com.fyber_challenge_android.utility.ExceptionHandler;

/**
 * Created by lukohnam on 16. 5. 24..
 */
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
