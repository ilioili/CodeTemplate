package com.taihe.template.app.tmplate.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ilioili.appstart.R;
import com.taihe.template.base.BaseFragment;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;
import com.taihe.template.base.util.ToastUtil;
import com.taihe.template.base.widget.RecyclerWheelView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/26.
 */
@Layout(R.layout.fragment_recycler_wheel_view)
public class RecyclerWheelViewFragment extends BaseFragment {
    @Id(R.id.recyclerView1)
    private RecyclerWheelView recyclerWheelView1;
    @Id(R.id.recyclerView2)
    private RecyclerWheelView recyclerWheelView2;
    @Id(R.id.recyclerView3)
    private RecyclerWheelView recyclerWheelView3;
    @Id(R.id.recyclerView4)
    private RecyclerWheelView recyclerWheelView4;
    @Id(R.id.recyclerView5)
    private RecyclerWheelView recyclerWheelView5;

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        final ArrayList<String> list = new ArrayList<>();
        list.add("Tab0");
        list.add("Tab1");
        list.add("Tab2");
        list.add("Tab3");
        list.add("Tab4");
        list.add("Tab5");
        list.add("Tab6");
        list.add("Tab7");
        list.add("Tab8");
        list.add("Tab9");
        RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                Button itemView = new Button(getContext());
                return new RecyclerView.ViewHolder(itemView) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                Button button = (Button) holder.itemView;
                button.setText("Tab"+position);
            }

            @Override
            public int getItemCount() {
                return list.size();
            }
        };
        RecyclerWheelView.OnItemSelectedListener onItemSelectedListener =  new RecyclerWheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position, View view) {
                ToastUtil.showShortToast(position);
            }
        };
        recyclerWheelView1.setAdapter(2, adapter,onItemSelectedListener);
        recyclerWheelView2.setAdapter(3, adapter,onItemSelectedListener);
        recyclerWheelView3.setAdapter(4, adapter,onItemSelectedListener);
        recyclerWheelView4.setAdapter(5, adapter,onItemSelectedListener);
        recyclerWheelView5.setAdapter(6, adapter,onItemSelectedListener);
    }
}
