package com.taihe.template.app.tmplate.fragment;

import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.taihe.template.app.tmplate.selectfile.FileListFragment;
import com.ilioili.appstart.R;
import com.taihe.template.base.BaseFragment;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;

/**
 * Created by Administrator on 2016/1/13.
 */
@Layout(R.layout.fragment_scrollable_tab)
public class ScrollableTabFragment extends BaseFragment {
    @Id(R.id.viewPager)
    private ViewPager viewPager;
    @Id(R.id.tabLayout)
    private TabLayout tabLayout;

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public void startUpdate(ViewGroup container) {
                viewPager.requestLayout();//解决HeadTabPagerWrappper 的 BUG 导致无法自动刷新问题
            }

            @Override
            public Fragment getItem(int position) {
                if(position==0) return FileListFragment.newInstance(Environment.getExternalStorageDirectory().getPath());
                return PlaceholderFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return 10;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "Tab" + position;
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }
}
