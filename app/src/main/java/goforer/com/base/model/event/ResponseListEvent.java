package goforer.com.base.model.event;

/**
 * Created by lukohnam on 16. 5. 24..
 */
public class ResponseListEvent extends ResponseEvent {
    private boolean mIsNew = true;

    public ResponseListEvent() {
        this(false);
    }

    public ResponseListEvent(boolean isNew) {
        mIsNew = isNew;
    }

    public boolean isNew() {
        return mIsNew;
    }
}
