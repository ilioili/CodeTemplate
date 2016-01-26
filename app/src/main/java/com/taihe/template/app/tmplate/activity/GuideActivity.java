package com.taihe.template.app.tmplate.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ilioili.appstart.R;
import com.taihe.template.app.base.AppBaseActivity;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;
import com.taihe.template.base.widget.Indicator;

@Layout(R.layout.activity_guide)
public class GuideActivity extends AppBaseActivity {

    private int[] resIds = new int[]{ R.drawable.guide1, R.drawable.guide2, R.drawable.guide3, R.drawable.guide4};
    @Id(R.id.viewPager)
    private ViewPager viewPager;
    @Id(R.id.indicator)
    private Indicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
                Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//通知栏透明
            //            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//下面三个虚拟按键透明
        }
        super.onCreate(savedInstanceState);
        viewPager.setOffscreenPageLimit(resIds.length);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return resIds.length;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(GuideActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageResource(resIds[position]);
                container.addView(imageView);
                if(position==resIds.length-1){
                    imageView.setClickable(true);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }
                return imageView;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return object.equals(view);
            }
        });
        indicator.setUpWithViewPager(viewPager);
        indicator.setCount(resIds.length);
    }

}
