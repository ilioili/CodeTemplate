package com.github.ilioili.demo.common.loadmore;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/1/19.
 */
public abstract class LoadMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private RecyclerView recyclerView;
    private View loadView;
    private boolean canLoadMore = true;

    public LoadMoreAdapter(RecyclerView view, RecyclerView.Adapter delegatedAdapter) {
        recyclerView = view;
        adapter = recyclerView.getAdapter();
        recyclerView.setAdapter(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && canLoadMore) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager.findLastVisibleItemPosition() == adapter.getItemCount()) {//最后一条可见
                        if (canLoadMore) {
                            loadView.setVisibility(View.VISIBLE);
                            onLoadMore(loadView);
                        }
                    }
                }
            }
        });
        LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
//        loadView = layoutInflater.inflate(R.layout.loadmore, recyclerView, false);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 100) {
            return new LoadHolder(loadView);
        } else {
            return adapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == adapter.getItemCount()) {
            return 100;
        } else {
            return adapter.getItemViewType(position);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < adapter.getItemCount()) {
            adapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return adapter.getItemCount() + 1;
    }

    public void setCanLoadMore(boolean value) {
        canLoadMore = value;
        loadView.setVisibility(value ? View.VISIBLE : View.GONE);
    }

    public abstract void onLoadMore(View loadingView);

    public static class LoadHolder extends RecyclerView.ViewHolder {

        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

}
