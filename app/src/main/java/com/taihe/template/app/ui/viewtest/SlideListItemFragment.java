package com.taihe.template.app.ui.viewtest;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilioili.appstart.R;
import com.taihe.template.base.BaseFragment;
import com.taihe.template.base.widget.SlideListItemWrapper;

/**
 * Created by Administrator on 2016/1/21.
 */
public class SlideListItemFragment extends BaseFragment {
    private SlideListItemWrapper curSlideListItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecyclerView.Adapter<Holder>() {
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

            }

            @Override
            public int getItemCount() {
                return 100;
            }
        });
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
        View upLayer;
        View downLayer;

        public Holder(View itemView) {
            super(itemView);
            upLayer = itemView.findViewById(R.id.layer_up);
            downLayer = itemView.findViewById(R.id.layer_down);
            downLayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != curSlideListItem) {
                        curSlideListItem.closeMenu();
                        curSlideListItem = null;
                    }
                }
            });
        }
    }
}
