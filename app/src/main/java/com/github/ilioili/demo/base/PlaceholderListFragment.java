package com.github.ilioili.demo.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Administrator on 2016/1/9.
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderListFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String BUNDLE_ITEM_COUNT = "section_number";
    private static final String BUNDLE_WITH_SWIPE_REFRESH_LAYOUT = "section_number";


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderListFragment newInstance(int itemCount, boolean withSwipeRefreshLayout) {
        PlaceholderListFragment fragment = new PlaceholderListFragment();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_ITEM_COUNT, itemCount);
        args.putBoolean(BUNDLE_WITH_SWIPE_REFRESH_LAYOUT, withSwipeRefreshLayout);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            Random random = new Random();
            int count = getArguments().getInt(BUNDLE_ITEM_COUNT, 20);

            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                TextView view = new TextView(getContext());
                view.setGravity(Gravity.CENTER);
                view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150));
                return new Holder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                holder.itemView.getLayoutParams().height = random.nextInt(150) + 150;
                holder.itemView.setBackgroundColor(random.nextInt());
                TextView tv = (TextView) holder.itemView;
                tv.setText("ListItem" + position);
                tv.setTextColor(random.nextInt());
                tv.setTextSize(random.nextInt(20)+10);
            }

            @Override
            public int getItemCount() {
                return count;
            }
        });
        boolean withSwipeRefreshLayout = getArguments().getBoolean(BUNDLE_WITH_SWIPE_REFRESH_LAYOUT);
        if(withSwipeRefreshLayout){
            final SwipeRefreshLayout swipeRefreshLayout = new SwipeRefreshLayout(getContext());
            swipeRefreshLayout.addView(recyclerView);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, 1500);
                }
            });
            return swipeRefreshLayout;
        }else{
            return recyclerView;
        }
    }

    class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
