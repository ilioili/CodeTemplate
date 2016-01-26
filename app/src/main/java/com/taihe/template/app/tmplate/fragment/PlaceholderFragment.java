package com.taihe.template.app.tmplate.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = new FrameLayout(getContext());
        final RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            Random random = new Random();
            int count = random.nextInt(35) + 1;

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
        rootView.addView(recyclerView);
        return rootView;
    }

    class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
