package com.github.ilioili.demo.main.frame;

import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.github.ilioili.demo.R;
import com.github.ilioili.demo.base.PlaceholderListFragment;
import com.taihe.template.base.BaseFragment;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;
import com.taihe.template.base.util.TestPicUrls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/1/12.
 */
@Layout(R.layout.fragment_banner_tab)
public class BannerTabFragment extends BaseFragment {
    @Id(R.id.swipeRefreshLayout)
    private SwipeRefreshLayout swipeRefreshLayout;
    @Id(R.id.viewPager)
    private ViewPager viewPager;
    @Id(R.id.tabs)
    private TabLayout tabLayout;

    @Override
    protected void init(View rootView) {
        final List<String> picUrlList = new ArrayList<>(6);
        picUrlList.addAll(0, Arrays.asList(TestPicUrls.URLS).subList(0, 5));

        swipeRefreshLayout.setNestedScrollingEnabled(true);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.BLACK, Color.RED, Color.CYAN);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 5000);
            }
        });
//        swipeRefreshLayout.setEnabled(false);

        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return PlaceholderListFragment.newInstance(position, false);
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "Page" + position;
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        });
        tabLayout.setupWithViewPager(viewPager);

        getChildFragmentManager().beginTransaction().add(R.id.banner_container, new BannerFragment()).commit();
    }
}


