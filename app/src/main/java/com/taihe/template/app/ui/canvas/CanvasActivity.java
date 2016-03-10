package com.taihe.template.app.ui.canvas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ilioili.appstart.R;
import com.taihe.template.app.base.AppBaseActivity;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;

import java.util.ArrayList;
import java.util.List;

@Layout(R.layout.activity_canvas)
public class CanvasActivity extends AppBaseActivity {

    @Id(R.id.viewPager)
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(viewPager);
        final List<Fragment> list = new ArrayList<>();
        list.add(new PathFragment());
        list.add(new CornerAnimationFragment());
        list.add(new FormLineFragment());
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });

    }
}
