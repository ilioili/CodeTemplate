package com.github.ilioili.demo.main.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.ilioili.demo.R;
import com.github.ilioili.demo.base.AppBaseActivity;
import com.github.ilioili.demo.base.PlaceholderListFragment;
import com.github.ilioili.demo.main.frame.ScrollableTabFragment;
import com.github.ilioili.widget.UpDownPageNestedScrollView;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;

@Layout(R.layout.activity_taobao_item_detail_nested_scroll_view_demo)
public class JdGoodsDetailDemoActivity extends AppBaseActivity {

    @Id(R.id.swipeRefreshLayout)
    private SwipeRefreshLayout swipeRefreshLayout;

    @Id(R.id.toolbar)
    private Toolbar toolbar;

    @Id(R.id.jdNestedScrollView)
    private UpDownPageNestedScrollView jdNestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        jdNestedScrollView.setOnPageSelectedListener(new UpDownPageNestedScrollView.OnPageSelectedListener() {
            @Override
            public void onScrollToFirstPage() {
                toolbar.setTitle("宝贝");
            }

            @Override
            public void onScrollToSecondPage() {
                toolbar.setTitle("宝贝详情");
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container_up, PlaceholderListFragment.newInstance(10, false));
        fragmentTransaction.add(R.id.container_down, new ScrollableTabFragment());
        fragmentTransaction.commit();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, JdGoodsDetailDemoActivity.class);
    }
}
