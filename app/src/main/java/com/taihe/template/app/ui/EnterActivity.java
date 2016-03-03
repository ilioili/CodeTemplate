package com.taihe.template.app.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ilioili.appstart.BuildConfig;
import com.ilioili.appstart.R;
import com.taihe.template.app.base.AppBaseActivity;

import java.util.ArrayList;

import static com.taihe.template.base.util.NullUtil.eqs;

/**
 * Activity入口页面
 */
public class EnterActivity extends AppBaseActivity {

    private final View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivityInfo activityInfo = (ActivityInfo) v.getTag();
            try {
                Intent it = new Intent(EnterActivity.this, EnterActivity.class.getClassLoader().loadClass(activityInfo.name));
                startActivity(it);
                overridePendingTransition(0, 0);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    };

    private ArrayList<ActivityInfo> activityInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityInfos = getActivityInfos();
        RecyclerView recyclerView = new RecyclerView(this);
        setContentView(recyclerView);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new RecyclerView.Adapter<Holder>() {
            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(EnterActivity.this).inflate(R.layout.content_main_list_item, parent, false);
                return new Holder(view);
            }

            @Override
            public void onBindViewHolder(Holder holder, int position) {
                ActivityInfo activity = activityInfos.get(position);
                holder.tvActivityClassName.setText(activity.name);
                holder.tvActivityTitle.setText(activity.loadLabel(getPackageManager()));
                holder.itemView.setTag(activity);
            }

            @Override
            public int getItemCount() {
                return activityInfos.size();
            }
        });

    }

    public ArrayList<ActivityInfo> getActivityInfos() {
        try {
            final PackageInfo packageInfo = getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_ACTIVITIES);
            ArrayList<ActivityInfo> activityInfos = new ArrayList<>(packageInfo.activities.length - 1);
            for (ActivityInfo activityInfo : packageInfo.activities) {
                if (!eqs(activityInfo.name, EnterActivity.class.getName())) {
                    activityInfos.add(activityInfo);
                }
            }
            return activityInfos;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    class Holder extends RecyclerView.ViewHolder {

        TextView tvActivityClassName;
        TextView tvActivityTitle;

        public Holder(View itemView) {
            super(itemView);
            tvActivityTitle = (TextView) ((ViewGroup) itemView).getChildAt(0);
            tvActivityClassName = (TextView) ((ViewGroup) itemView).getChildAt(1);
            itemView.setOnClickListener(onItemClickListener);
        }


    }
}
