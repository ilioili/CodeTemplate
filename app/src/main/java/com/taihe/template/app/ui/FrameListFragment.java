//package com.taihe.template.app.ui;
//
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.ilioili.appstart.R;
//import com.taihe.template.app.base.AppBaseFragment;
//
///**
// * Created by Administrator on 2016/3/8.
// */
//public class FrameListFragment extends AppBaseFragment {
//    private RecyclerView recyclerView;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if(null!=recyclerView){
//            recyclerView = getRecyclerView();
//        }
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }
////
////    public RecyclerView getRecyclerView() {
////        RecyclerView recyclerView = new RecyclerView(getContext());
////        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
////        recyclerView.setAdapter(new RecyclerView.Adapter() {
////            @Override
////            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
////                View view = LayoutInflater.from(EnterActivity.this).inflate(R.layout.content_main_list_item, parent, false);
////                return
////            }
////
////            @Override
////            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
////
////            }
////
////            @Override
////            public int getItemCount() {
////                return 0;
////            }
////        });
////        return recyclerView;
////    }
//}
