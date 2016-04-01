package com.github.ilioili.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Administrator on 2015/12/9.
 */
public class HeadTabPagerWrapper extends ViewGroup {
    private View headerView;//顶部的View
    private View tabView;//中间的View，滑动一定时候固定在顶部
    private ViewPager viewPager;
    private int offsetY;
    private int offsetBase;
    private float startX;
    private float startY;
    private boolean hasSwipeRefreshLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AdjustAnimator adjustAnimator = new AdjustAnimator();

    public HeadTabPagerWrapper(Context context) {
        this(context, null);
    }

    public HeadTabPagerWrapper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeadTabPagerWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        headerView = getChildAt(0);
        tabView = getChildAt(1);
        viewPager = (ViewPager) getChildAt(2);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean isHeadEat = false;
        boolean isTabEat = false;
        boolean isViewPagerEat = false;
        boolean isFrameEat = false;
        try {
            if (headerView.getBottom() >= event.getY()) {
                MotionEvent headEvent = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(),
                        event.getX(), event.getY() - headerView.getTranslationY(), event.getMetaState());
                isHeadEat = headerView.dispatchTouchEvent(headEvent);
            } else if (tabView.getBottom() >= event.getY()) {
                MotionEvent tabEvent = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(),
                        event.getX(), event.getY() - tabView.getTop() - tabView.getTranslationY(), event.getMetaState());
                isTabEat = tabView.dispatchTouchEvent(tabEvent);
            } else {
                MotionEvent pageEvent = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(),
                        event.getX(), event.getY() - viewPager.getTop() - viewPager.getTranslationY(), event.getMetaState());
                isViewPagerEat = viewPager.dispatchTouchEvent(pageEvent);
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (hasSwipeRefreshLayout) {//解决SwipeRefreshLayout和HeadTabpagerWrapper的滑动冲突
                        swipeRefreshLayout.setEnabled(true);
                    }
                    adjustAnimator.get().cancel();
                    startX = event.getX();
                    startY = event.getY();
                    offsetBase = offsetY;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int dx = (int) Math.abs(event.getX() - startX);
                    int dy = (int) Math.abs(event.getY() - startY);
                    if (dy > dx) {//竖直滑动意图明显
                        if (dy > ViewConfiguration.get(getContext()).getScaledTouchSlop() && canFrameScroll((int) (event.getY() - startY))) {//HeadTabPagerWrapper可以继续滑动
                            offsetY = (int) (offsetBase + event.getY() - startY);
                            if (-offsetY > headerView.getMeasuredHeight()) {
                                offsetY = -headerView.getMeasuredHeight();
                            } else if (offsetY > 0) {
                                offsetY = 0;
                            }
                            requestDisallowInterceptTouchEvent(true);
                            layoutChildren();
                        }
                    } else {
                        if (dy > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                            startX = event.getX();
                            startY = event.getY();
                        }
                        if (hasSwipeRefreshLayout && dx > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {//解决SwipeRefreshLayout和ViewPager的滑动冲突
                            swipeRefreshLayout.setEnabled(false);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (offsetY != 0 && offsetY != -headerView.getMeasuredHeight()) {
                        int target = -headerView.getMeasuredHeight();
                        if (-offsetY < headerView.getMeasuredHeight() / 2) {
                            target = 0;
                        }
                        adjustAnimator.get().setIntValues(offsetY, target);
                        adjustAnimator.get().start();
                    }
                    if (hasSwipeRefreshLayout) {//解决SwipeRefreshLayout和HeadTabpagerWrapper的滑动冲突
                        swipeRefreshLayout.setEnabled(true);
                    }
                    break;
            }
            isFrameEat = (headerView.getBottom() > 0);//HeadView可见，可以继续滑动
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isHeadEat || isTabEat || isViewPagerEat || isFrameEat;
    }

    private boolean canFrameScroll(int dy) {
        if (offsetY == 0 && dy > 0) {
            return false;
        } else if (offsetY == -headerView.getMeasuredHeight() && dy < 0) {
            return false;
        }
        return true;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //清除Measure缓存，防止ViewPager等ChildView不刷新
        headerView.forceLayout();
        tabView.forceLayout();
        viewPager.forceLayout();

        measureChild(headerView, widthMeasureSpec, MeasureSpec.makeMeasureSpec(headerView.getLayoutParams().height, MeasureSpec.EXACTLY));
        measureChild(tabView, widthMeasureSpec, MeasureSpec.makeMeasureSpec(tabView.getLayoutParams().height, MeasureSpec.EXACTLY));
        measureChild(viewPager, widthMeasureSpec, MeasureSpec.makeMeasureSpec(height - tabView.getMeasuredHeight(), MeasureSpec.EXACTLY));
        setMeasuredDimension(width, height);
        ViewParent viewParent = getParent();
        while (true) {
            if (viewParent instanceof SwipeRefreshLayout) {
                hasSwipeRefreshLayout = true;
                swipeRefreshLayout = (SwipeRefreshLayout) viewParent;
                break;
            }
            viewParent = viewParent.getParent();
            if (null == viewParent)
                break;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChildren();
    }

    private void layoutChildren() {
        headerView.layout(0, offsetY, headerView.getMeasuredWidth(), headerView.getMeasuredHeight() + offsetY);
        tabView.layout(0, headerView.getBottom(), tabView.getMeasuredWidth(), tabView.getMeasuredHeight() + headerView.getBottom());
        viewPager.layout(0, tabView.getBottom(), getMeasuredWidth(), getMeasuredHeight() + tabView.getBottom());
    }

    private class AdjustAnimator {
        private ValueAnimator valueAnimator;

        public ValueAnimator get() {
            if (null == valueAnimator) {
                valueAnimator = new ValueAnimator();
                valueAnimator.setInterpolator(new DecelerateInterpolator());
                valueAnimator.setDuration(200);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        offsetY = (int) animation.getAnimatedValue();
                        layoutChildren();
                    }
                });
            }
            return valueAnimator;
        }
    }
}
