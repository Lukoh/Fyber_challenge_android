package goforer.com.fyber_challenge_android.utility;

import android.support.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Created by lukohnam on 16. 5. 25..
 */
public class CommonUtils {
    public static final String API_KEY = "1c915e3b5d42d05136185030892fbb846c278927";

    private static String mGoogleAID;
    private static boolean mIsLimitAdTrackingEnabled;

    public static String SHA1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static void setGoogleAID(String googleAID) {
        mGoogleAID = googleAID;
    }

    public static void setLimitAdTrackingEnabled(boolean isLimitAdTrackingEnabled) {
        mIsLimitAdTrackingEnabled = isLimitAdTrackingEnabled;
    }

    public static String getGoogleAID() {
        return mGoogleAID;
    }

    public static boolean getLimitAdTrackingEnabled() {
        return mIsLimitAdTrackingEnabled;
    }
}
