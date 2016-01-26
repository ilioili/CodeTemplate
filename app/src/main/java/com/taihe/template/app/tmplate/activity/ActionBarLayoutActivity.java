package com.taihe.template.app.tmplate.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.ilioili.appstart.R;
import com.taihe.template.app.base.AppBaseActivity;
import com.taihe.template.app.tmplate.fragment.BannerFragment;
import com.taihe.template.app.tmplate.fragment.PlaceholderFragment;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;

@Layout(R.layout.activity_action_bar_layout)
public class ActionBarLayoutActivity extends AppBaseActivity {


    @Id(R.id.toolbar)
    private Toolbar toolbar;
    @Id(R.id.viewPager)
    private ViewPager viewPager;
    @Id(R.id.tabLayout)
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new PlaceholderFragment();
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "Page"+position;
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        getSupportFragmentManager().beginTransaction().add(R.id.banner_container, new BannerFragment()).commit();
    }

}
