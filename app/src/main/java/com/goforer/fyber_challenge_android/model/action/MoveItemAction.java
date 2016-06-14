package com.goforer.fyber_challenge_android.model.action;

import android.support.v7.widget.RecyclerView.LayoutManager;

public class MoveItemAction {
    private LayoutManager mLayoutManager;
    private int mPosition;

    public LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setLayoutManager(LayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    public void setPosition(int position) {
        mPosition = position;
    }
}
