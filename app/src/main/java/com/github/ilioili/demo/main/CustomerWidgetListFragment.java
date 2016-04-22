package com.github.ilioili.demo.main;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ilioili.demo.R;
import com.github.ilioili.demo.main.canvas.MeterialCircleActivity;
import com.github.ilioili.demo.main.widget.FormLayoutActivity;
import com.github.ilioili.demo.main.widget.SimpleGridLayoutActivity;
import com.github.ilioili.demo.main.widget.SlideListItemActivity;
import com.github.ilioili.demo.provider.ActivityLabel;
import com.github.ilioili.widget.CircleAnimationFrame;
import com.github.ilioili.widget.FormLayout;
import com.github.ilioili.widget.SimpleGridLayout;
import com.github.ilioili.widget.SlideListItemWrapper;
import com.taihe.template.base.BaseFragment;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.InjectionUtil;
import com.taihe.template.base.injection.Layout;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/10.
 */
@Layout(R.layout.fragment_frame_list)
public class CustomerWidgetListFragment extends BaseFragment implements View.OnClickListener{
    private ArrayList<DataItem> dataList = new ArrayList<>();
    @Id(R.id.recyclerView)
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter = new RecyclerView.Adapter<Holder>() {
        @Override
        public int getItemCount() {
            return dataList.size();
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            DataItem dataItem = dataList.get(position);
            holder.tvName.setText(dataItem.activityName);
            holder.tvClass.setText(dataItem.widgetName);
            holder.tvDescription.setText(dataItem.desc);
            holder.itemView.setTag(dataItem.intent);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.adapter_activity_item, parent, false);
            view.setOnClickListener(CustomerWidgetListFragment.this);
            return new Holder(view);
        }
    };

    @Override
    protected void init(View rootView) {
        initData();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }



    @Override
    public void onClick(View v) {
        Intent it = (Intent) v.getTag();
        startActivity(it);
    }

    class Holder extends RecyclerView.ViewHolder {
        @Id(R.id.activity_name)
        TextView tvName;
        @Id(R.id.class_name)
        TextView tvClass;
        @Id(R.id.description)
        TextView tvDescription;

        public Holder(View itemView) {
            super(itemView);
            InjectionUtil.loadView(itemView, this);
        }
    }

    class DataItem{
        Intent intent;
        String activityName;
        String widgetName;
        String desc;
        public DataItem(Class activity, Class customView, Intent intent, String desc){
            activityName = activity.getName();
            widgetName = customView.getName();
            this.intent = intent;
            this.desc = desc;
        }

    }

    private void initData() {
        DataItem dataItem1 = new DataItem(MeterialCircleActivity.class, CircleAnimationFrame.class, MeterialCircleActivity.newIntent(getContext(), 0, 0), "Meterial环形转场特效");
        dataList.add(dataItem1);
        DataItem dataItem2 = new DataItem(SimpleGridLayoutActivity.class, SimpleGridLayout.class, SimpleGridLayoutActivity.newIntent(getContext()), "简单宫格布局");
        dataList.add(dataItem2);
        DataItem dataItem3 = new DataItem(SlideListItemActivity.class, SlideListItemWrapper.class, SlideListItemActivity.newIntent(getContext()), "侧滑删除控件");
        dataList.add(dataItem3);
        DataItem dataItem4 = new DataItem(FormLayoutActivity.class, FormLayout.class, FormLayoutActivity.newIntent(getContext()), "相对比例绝对宫格布局");
        dataList.add(dataItem4);
    }
}
