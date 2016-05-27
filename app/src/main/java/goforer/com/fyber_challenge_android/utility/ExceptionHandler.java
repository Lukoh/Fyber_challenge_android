package goforer.com.fyber_challenge_android.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import goforer.com.fyber_challenge_android.BuildConfig;

/**
 * Created by lukohnam on 16. 5. 24..
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "ExceptionHandler";

    private static final String CRASH_LOG_DIRECTORY = "CrashLog";
    private static final String CRASH_LOG_EXTENSION = ".crashlog";

    private Context mContext;
    private String mCrashLogPath;
    private OnFindCrashLogListener mCrashLogListener;
    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler;

    public ExceptionHandler(@NonNull Context context, @NonNull Thread.UncaughtExceptionHandler handler, OnFindCrashLogListener listener) throws NullPointerException {
        mContext = context;

        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }

        mCrashLogPath = cacheDir.getAbsolutePath() + File.separator + CRASH_LOG_DIRECTORY + File.separator + context.getPackageName() + CRASH_LOG_EXTENSION;
        mUncaughtExceptionHandler = handler;
        mCrashLogListener = listener;
        checkCrashLog();
    }

    private void checkCrashLog() {
        File crashLogFile = new File(mCrashLogPath);
        if(crashLogFile.canRead() && crashLogFile.isFile()) {
            try {
                byte[] buffer = new byte[1024];
                byte[] read = new byte[(int)crashLogFile.length()];
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(crashLogFile));

                int readSize, offset = 0;
                while((readSize = inputStream.read(buffer)) != -1) {
                    System.arraycopy(buffer, 0, read, offset, readSize);
                    offset += readSize;
                }

                inputStream.close();

                String errorLog = new String(read);

                if (mCrashLogListener != null) mCrashLogListener.onFindCrashLog(errorLog);

                Log.d("checkCrashLog", errorLog);

            } catch(Exception e) {
                e.printStackTrace();
            }

            File renameFile = new File(mCrashLogPath.replace(CRASH_LOG_EXTENSION, "."
                    + toTimeString(crashLogFile.lastModified()) + CRASH_LOG_EXTENSION));

            if (crashLogFile.renameTo(renameFile)) {
                Log.d(TAG, "renamed");
            } else {
                Log.d(TAG, "Not renamed");
            }
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            File crashLogFile = new File(mCrashLogPath);
            File crashLogDirectory = crashLogFile.getParentFile();

            if (crashLogDirectory != null && (crashLogDirectory.canRead() ||
                    crashLogDirectory.isDirectory() || crashLogDirectory.mkdirs())) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter pw = new PrintWriter(stringWriter);
                ex.printStackTrace(pw);

                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(crashLogFile));
                bufferedWriter.write("Model : " + android.os.Build.MODEL + "\n");
                bufferedWriter.write("Android " + android.os.Build.VERSION.RELEASE + " "
                        + android.os.Build.VERSION.SDK_INT + "\n");
                bufferedWriter.write("Version Name : " + BuildConfig.VERSION_NAME + "\n");
                bufferedWriter.write("Version Code : " + BuildConfig.VERSION_CODE + "\n");
                bufferedWriter.write("Network : " + getNetworkStatus(mContext) + "\n");
                bufferedWriter.write("Date : " + toTimeString(crashLogFile.lastModified()) + "\n");
                bufferedWriter.write(stringWriter.toString());
                bufferedWriter.flush();
                bufferedWriter.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        if (mCrashLogListener != null) mCrashLogListener.onCaughtCrash(ex);
        if (mUncaughtExceptionHandler != null) {
            mUncaughtExceptionHandler.uncaughtException(thread, ex);
        }
    }

    private String toTimeString(long datetime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
        Date date = new Date(datetime);
        return format.format(date);
    }

    public static String toTimeStringNomore(long datetime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(datetime);
        return format.format(date);
    }

    private String getNetworkStatus(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return "WIFI";

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return "MOBILE";
        }

        return "NOT_CONNECTED";
    }

    public interface OnFindCrashLogListener {
        void onFindCrashLog(String log);
        void onCaughtCrash(Throwable throwable);
    }
}

