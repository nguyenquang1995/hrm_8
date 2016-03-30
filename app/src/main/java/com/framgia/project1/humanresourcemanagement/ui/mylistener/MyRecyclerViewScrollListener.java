package com.framgia.project1.humanresourcemanagement.ui.mylistener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by hacks_000 on 3/25/2016.
 */
public abstract class MyRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager mLinearLayoutManager;
    private int mLastVisibleItemPosition;

    public MyRecyclerViewScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        mLastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
    }

    public boolean isAbleLoadMore(int endPosition) {
        return (mLastVisibleItemPosition == endPosition - 1);
    }
}
