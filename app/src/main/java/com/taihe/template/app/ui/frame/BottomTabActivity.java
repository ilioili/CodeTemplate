package com.taihe.template.app.ui.frame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.taihe.template.app.R;
import com.taihe.template.app.base.AppBaseActivity;
import com.taihe.template.app.ui.third.PicGridFragment;
import com.taihe.template.app.ui.third.PicListFragment;
import com.taihe.template.app.ui.third.PicWaterfallFragment;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;

import java.util.ArrayList;
import java.util.List;

/**
 * <img src = "../../{@docRoot}/ui/demo.png" width=540 height=960/>
 */
@Layout(R.layout.activity_main)
public class BottomTabActivity extends AppBaseActivity {

    @Id(R.id.tab_group)
    private ViewGroup vgTabContainer;
    @Id(R.id.viewPager)
    private ViewPager viewPager;

    private List<Fragment> fragmentList;

    public static Intent newIntent(Context context) {
        return new Intent(context, BottomTabActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View selectedItem = vgTabContainer.getChildAt(0);
        selectedItem.setSelected(true);
        vgTabContainer.setTag(selectedItem);
        fragmentList = new ArrayList<>();
        fragmentList.add(new BannerTabFragment());
        fragmentList.add(new ScrollableTabFragment());
        fragmentList.add(new PicListFragment());
        fragmentList.add(new PicGridFragment());
        fragmentList.add(new PicWaterfallFragment());

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                View previousSelectedView = (View) vgTabContainer.getTag();
                previousSelectedView.setSelected(false);
                View currentSelectedView = vgTabContainer.getChildAt(position);
                currentSelectedView.setSelected(true);
                vgTabContainer.setTag(currentSelectedView);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getParent() == vgTabContainer) {
            int index = vgTabContainer.indexOfChild(view);
            viewPager.setCurrentItem(index, true);
        }
    }
}

