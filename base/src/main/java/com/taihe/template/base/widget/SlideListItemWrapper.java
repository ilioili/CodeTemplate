package com.taihe.template.base.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * 列表滑动删除
 */
public class SlideListItemWrapper extends FrameLayout {
    final public int DURATION = 250;
    GestureDetector dector;
    private int scrollX;
    private Scroller scroller;
    OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            scroller.forceFinished(true);
            scrollX += distanceX;
            checkBorder();
            scrollTo(scrollX, 0);
            postInvalidate();
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (scrollX != 0 || scrollX != getChildAt(1).getMeasuredWidth()) {
                int interval = velocityX > 0 ? -scrollX : getChildAt(1).getMeasuredWidth() - scrollX;
                scroller.startScroll(scrollX, 0, interval, 0, DURATION);
                postInvalidate();
            }
            return true;
        }

        ;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    };
    private int minFingerDetectorInterval;
    private ViewSlideListener listener;
    private float firstFingerY;
    private float firstFingerX;
    private boolean blockSlide;


    @SuppressLint("NewApi")
    public SlideListItemWrapper(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        dector = new GestureDetector(context, gestureListener);
        scroller = new Scroller(context);
        minFingerDetectorInterval = context.getResources().getDisplayMetrics().widthPixels / 24;
    }

    public SlideListItemWrapper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideListItemWrapper(Context context) {
        this(context, null, 0);
    }

    public void setSlideListener(ViewSlideListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (blockSlide) return false;
        if (!dector.onTouchEvent(event)) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (scrollX != 0 || scrollX != getChildAt(1).getMeasuredWidth()) {
                    int interval = scrollX < getChildAt(1).getMeasuredWidth() / 2 ? -scrollX : getChildAt(1).getMeasuredWidth() - scrollX;
                    scroller.startScroll(scrollX, 0, interval, 0, DURATION);
                    postInvalidate();
                }
            } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                if (scrollX != 0) {
                    scroller.startScroll(scrollX, 0, -scrollX, 0, DURATION);
                    postInvalidate();
                }
            }
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (isInEditMode()) {
            getChildAt(0).layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
            getChildAt(1).setAlpha(0.5f);//XML编辑模式下方便看到遮挡的View
            getChildAt(1).layout(getMeasuredWidth() - getChildAt(1).getMeasuredWidth(), 0, getMeasuredWidth(), getMeasuredHeight());
        } else {
            getChildAt(0).layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
            getChildAt(1).layout(getMeasuredWidth(), 0, getChildAt(1).getMeasuredWidth() + getMeasuredWidth(), getMeasuredHeight());
        }

    }

    private void checkBorder() {
        if (scrollX >= getChildAt(1).getMeasuredWidth()) {
            scroller.forceFinished(true);
            scrollX = getChildAt(1).getMeasuredWidth();
        } else if (scrollX <= 0) {
            scroller.forceFinished(true);
            scrollX = 0;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (blockSlide) {
            return false;
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            firstFingerY = ev.getY();
            firstFingerX = ev.getX();
            dector.onTouchEvent(ev);//让Detector记录Down事件，否则onScroll的最后一次事件是上次的Up事件的位�?
            scroller.forceFinished(true);
        }
        //判断滑动方向为水平方向则拦截事件
        if (ev.getAction() == MotionEvent.ACTION_MOVE
                && Math.abs(ev.getY() - firstFingerY) + Math.abs(ev.getX() - firstFingerX) > minFingerDetectorInterval
                && Math.abs(ev.getY() - firstFingerY) < Math.abs(ev.getX() - firstFingerX)) {
            if (null != listener) {
                listener.onViewSliding(this);
            }
            requestDisallowInterceptTouchEvent(true);
            return true;
        }
        return false;
    }

    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollX = scroller.getCurrX();
            checkBorder();
            scrollTo(scrollX, 0);
            postInvalidate();
        }
    }

    public void closeMenu() {
        if (scrollX != 0) {
            scroller.startScroll(scrollX, 0, -scrollX, 0, DURATION);
            postInvalidate();
        }
    }

    public void openMenu() {
        if (scrollX != getMeasuredWidth()) {
            scroller.startScroll(scrollX, 0, getMeasuredWidth() - scrollX, DURATION);
            postInvalidate();
        }
    }

    public void blockSlide(boolean block) {
        this.blockSlide = block;
    }

    public interface ViewSlideListener {
        void onViewSliding(SlideListItemWrapper v);
    }
}
