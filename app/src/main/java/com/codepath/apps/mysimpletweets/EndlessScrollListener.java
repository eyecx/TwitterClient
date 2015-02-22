package com.codepath.apps.mysimpletweets;

import android.widget.AbsListView;

/**
 * Created by edmundye on 2/12/15.
 */
public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private boolean loading = true;

    public EndlessScrollListener(){};

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
            currentPage++;
        }

        if (!loading && (firstVisibleItem + visibleItemCount >= totalItemCount - visibleThreshold)) {
            loading = true;
            onLoadMore(currentPage + 1, totalItemCount);
        }
    }

    public abstract void onLoadMore(int page, int totalItemCount);

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}
}
