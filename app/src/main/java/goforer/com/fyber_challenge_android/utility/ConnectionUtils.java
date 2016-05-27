package goforer.com.fyber_challenge_android.utility;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by lukohnam on 16. 5. 24..
 */
public enum ConnectionUtils {
    INSTANCE;

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
