package com.github.ilioili.demo.main.widget;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ilioili.demo.R;
import com.taihe.template.base.BaseFragment;
import com.github.ilioili.widget.SlideListItemWrapper;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/21.
 */
public class SlideListItemFragment extends BaseFragment {
    private SlideListItemWrapper curSlideListItem;
    private ArrayList<String> dataList;
    private void mockData(){
        dataList = new ArrayList<>();
        for(int i=0; i<15; i++){
            dataList.add("左滑拉出菜单:item"+i);
        }
    }
    RecyclerView.Adapter<Holder> adapter = new RecyclerView.Adapter<Holder>() {
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            SlideListItemWrapper slideListItemWrapper = (SlideListItemWrapper) getActivity().getLayoutInflater().inflate(R.layout.slide_list_item, parent, false);
            slideListItemWrapper.setSlideListener(new SlideListItemWrapper.ViewSlideListener() {
                @Override
                public void onViewSliding(SlideListItemWrapper v) {
                    if (null != curSlideListItem) {
                        curSlideListItem.closeMenu();
                    }
                    curSlideListItem = v;
                }
            });
            return new Holder(slideListItemWrapper);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            String tag = dataList.get(position);
            holder.contentView.setText(tag);
            holder.menuView.setTag(tag);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mockData();
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    if (null != curSlideListItem) {
                        curSlideListItem.closeMenu();
                        curSlideListItem.closeMenu();
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        return recyclerView;
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView contentView;
        View menuView;

        public Holder(View itemView) {
            super(itemView);
            contentView = (TextView) itemView.findViewById(R.id.content);
            menuView = itemView.findViewById(R.id.menu);
            menuView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != curSlideListItem) {
                        curSlideListItem.closeMenu();
                        curSlideListItem = null;
                        int index = dataList.indexOf(v.getTag());
                        dataList.remove(index);
                        adapter.notifyItemRemoved(index);

                    }
                }
            });
        }
    }
}
