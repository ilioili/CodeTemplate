package com.taihe.template.app.ui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taihe.template.app.R;
import com.taihe.template.app.provider.ActivityLabel;
import com.taihe.template.app.ui.frame.HttpActivity;
import com.taihe.template.base.BaseFragment;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.InjectionUtil;
import com.taihe.template.base.injection.Layout;

/**
 * Created by Administrator on 2016/3/10.
 */
@Layout(R.layout.fragment_frame_list)
public class ThirdPartLearnFragment extends BaseFragment {
    private Class[] activities = new Class[]{HttpActivity.class};
    @Id(R.id.recyclerView)
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter = new RecyclerView.Adapter<Holder>() {
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.adapter_activity_item, parent, false);
            view.setOnClickListener(ThirdPartLearnFragment.this);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Class clazz = activities[position];
            holder.tvName.setText(clazz.getSimpleName());
            holder.tvClass.setText(clazz.getSimpleName());
            holder.tvDescription.setText(ActivityLabel.getLabel(getContext(), clazz));
            holder.itemView.setTag(clazz);
        }

        @Override
        public int getItemCount() {
            return activities.length;
        }
    };
    @Override
    public void onClick(View v) {
        Class clazz = (Class) v.getTag();
        Intent it = new Intent(getActivity(), clazz);
        startActivity(it);
    }
    @Override
    protected void init(View rootView) {
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
