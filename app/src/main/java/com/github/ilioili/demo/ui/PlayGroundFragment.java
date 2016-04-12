package com.github.ilioili.demo.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ilioili.demo.BuildConfig;
import com.github.ilioili.demo.R;
import com.github.ilioili.demo.provider.ActivityLabel;
import com.taihe.template.base.BaseFragment;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.InjectionUtil;
import com.taihe.template.base.injection.Layout;

/**
 * Created by Administrator on 2016/3/10.
 */
@Layout(R.layout.fragment_frame_list)
public class PlayGroundFragment extends BaseFragment{
    private ActivityInfo[] activityInfos;
    @Id(R.id.recyclerView)
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter = new RecyclerView.Adapter<Holder>() {
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.adapter_activity_item, parent, false);
            view.setOnClickListener(PlayGroundFragment.this);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            String className = activityInfos[position].name;
            holder.tvName.setText(className);
            holder.tvClass.setText(className);
            holder.tvDescription.setText(ActivityLabel.getLabel(getContext(), className));
            holder.itemView.setTag(className);
        }

        @Override
        public int getItemCount() {
            return activityInfos.length;
        }
    };

    public void getActivityInfos() {
        try {
            final PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_ACTIVITIES);
            activityInfos = packageInfo.activities;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            String className = (String) v.getTag();
            Class clazz = Class.forName(className);
            Intent it = new Intent(getActivity(), clazz);
            startActivity(it);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void init(View rootView) {
        getActivityInfos();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
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
}
