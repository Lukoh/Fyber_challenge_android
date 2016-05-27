package goforer.com.fyber_challenge_android.model.action;

/**
 * Created by lukohnam on 16. 5. 24..
 */
public class FinishAction {
    private String mCode;
    private String mMessage = "";

    public String getCode() { return mCode; }

    public String getMessage() { return mMessage; }

    public void setCode(String code) { mCode = code; }

    public void setMessage(String message) { mMessage = message; }
}
