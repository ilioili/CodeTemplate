package com.taihe.template.app.tmplate.fragment;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ilioili.appstart.R;
import com.taihe.template.base.BaseFragment;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;
import com.taihe.template.base.util.ExHandler;
import com.taihe.template.base.util.TestPicUrls;
import com.taihe.template.base.widget.Indicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.taihe.template.base.util.NullUtil.ept;

/**
 * Created by Administrator on 2016/1/12.
 */
@Layout(R.layout.fragment_banner)
public class BannerFragment extends BaseFragment {

    public static final int TAG_KEY = 3 << 24;
    private final long UPDATE_ITEM_INTERVAL = 3000;
    @Id(R.id.indicator)
    private Indicator indicator;
    @Id(R.id.banner_viewPager)
    private ViewPager viewPager;
    private List<String> list;
    private Handler handler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            postScrollToNextPage();
        }
    };
    private View.OnClickListener onPagerItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = (String) v.getTag(TAG_KEY);
            Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void initView(View rootView) {
        handler = new ExHandler();
        list = new ArrayList<>();
        viewPager.setOffscreenPageLimit(list.size() > 3 ? 1 : 2);//页面3个及以内少时候全部显示，多了预加载1页
        list = Arrays.asList(TestPicUrls.URLS).subList(0, 5);
        viewPager.setAdapter(new CirclePagerAdapter(list));
        viewPager.setCurrentItem(list.size() * 100);
        viewPager.setOnTouchListener(new View.OnTouchListener() {//手指操作ViewPager时候暂停滑动，离开时开启滑动
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        cancelPostScrollTask();
                        break;
                    case MotionEvent.ACTION_UP:
                        postScrollToNextPage();
                        break;
                }
                return false;
            }
        });
        indicator.setUpWithViewPager(viewPager);
        indicator.setCount(list.size());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {//Fragment不可见时候停止滑动，可见时开始滑动
            postScrollToNextPage();
        } else {
            cancelPostScrollTask();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        postScrollToNextPage();//Fragment可见时开始滑动
    }

    private void postScrollToNextPage() {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(runnable, UPDATE_ITEM_INTERVAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelPostScrollTask();//Fragment不可见时候停止滑动
    }

    private void cancelPostScrollTask() {
        handler.removeCallbacksAndMessages(null);
    }

    class CirclePagerAdapter extends PagerAdapter {
        private List dataList;
        private LinkedList<View> viewCache;

        public CirclePagerAdapter(List list) {
            dataList = list;
            viewCache = new LinkedList<>();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            viewCache.add((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = viewCache.poll();
            if (null == view) {
                view = LayoutInflater.from(container.getContext()).inflate(R.layout.viewpager_item_banner, container, false);
                view.setClickable(true);
                view.setOnClickListener(onPagerItemClickListener);
            }
            String picUrl = (String) dataList.get(position % dataList.size());
            Glide.with(container.getContext()).load(picUrl).into((ImageView) view);
            container.addView(view);
            view.setTag(TAG_KEY, picUrl);
            return view;
        }

        @Override
        public int getCount() {
            if (ept(dataList)) {
                return 0;
            } else {
                return Integer.MAX_VALUE;
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }
}
