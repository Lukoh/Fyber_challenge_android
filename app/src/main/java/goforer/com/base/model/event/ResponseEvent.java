package goforer.com.base.model.event;

import goforer.com.fyber_challenge_android.web.communicator.ResponseClient;

/**
 * Created by lukohnam on 16. 5. 24..
 */
public class ResponseEvent {
    protected ResponseClient mResponseClient;
    protected String mTag;

    public boolean isMine(String tag){
        return tag == null || tag.equals(mTag);
    }

    public void parseInResponse() {
    }

    public ResponseClient getResponseClient() { return mResponseClient; }

    public String getTag() { return mTag; }

    public void setResponseClient(ResponseClient responseClient) { mResponseClient = responseClient; }

    public void setTag(String tag) { mTag = tag; }
}
