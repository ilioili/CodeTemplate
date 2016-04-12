package com.github.ilioili.demo.ui.frame;

import android.graphics.Color;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.github.ilioili.demo.R;
import com.github.ilioili.demo.tmplate.fragment.PlaceholderFragment;
import com.github.ilioili.demo.tmplate.selectfile.FileListFragment;
import com.taihe.template.base.BaseFragment;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;

/**
 * Created by Administrator on 2016/1/13.
 */
@Layout(R.layout.fragment_scrollable_tab)
public class ScrollableTabFragment extends BaseFragment {
    private final static int[] TabColors = new int[]{
            Color.parseColor("#F44336"),
            Color.parseColor("#E91E63"),
            Color.parseColor("#9C27B0"),
            Color.parseColor("#673AB7"),
            Color.parseColor("#3F51B5"),
            Color.parseColor("#2196F3"),
            Color.parseColor("#03A9F4"),
            Color.parseColor("#00BCD4"),
            Color.parseColor("#673AB7"),
            Color.parseColor("#3F51B5")};
    @Id(R.id.viewPager)
    private ViewPager viewPager;
    @Id(R.id.tabLayout)
    private TabLayout tabLayout;

    @Override
    protected void init(View rootView) {
        super.init(rootView);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public void startUpdate(ViewGroup container) {
                viewPager.requestLayout();//解决HeadTabPagerWrappper 的 BUG 导致无法自动刷新问题
            }

            @Override
            public Fragment getItem(int position) {
                if (position == 0)
                    return FileListFragment.newInstance(Environment.getExternalStorageDirectory().getPath());
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
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int startColor = TabColors[position];
                int nextPos = position + 1;
                if(nextPos==TabColors.length){
                    nextPos--;
                }
                int endColor = TabColors[nextPos];
                int curColor = Color.argb(
                        (int) (Color.alpha(startColor) * (1 - positionOffset) + Color.alpha(endColor) * positionOffset),
                        (int)(Color.red(startColor)*(1-positionOffset)+Color.red(endColor)*positionOffset),
                        (int)(Color.green(startColor)*(1-positionOffset)+Color.green(endColor)*positionOffset),
                        (int)(Color.blue(startColor)*(1-positionOffset)+Color.blue(endColor)*positionOffset));
                viewPager.getRootView().setBackgroundColor(curColor);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
