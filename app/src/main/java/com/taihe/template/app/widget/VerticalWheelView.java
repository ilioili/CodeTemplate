package com.taihe.template.app.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

/**
 * Created by ilioili on 2015/9/23.
 */
public class VerticalWheelView extends ViewGroup {
    //直接赋值初始化，不必判空
    private LinkedList<View> childViewList;
    private Adapter adapter = new Adapter() {
        @Override
        public void setView(View convertView, int index) {
            TextView tv = (TextView) convertView;
            tv.setText(index + "");
        }

        @Override
        public int getDataCount() {
            return 5;
        }
    };
    //直接赋值初始化，不必判空
    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public void onItemSelected(int index) {
            Toast.makeText(getContext(), "" + index, Toast.LENGTH_SHORT).show();
        }
    };
    private int currentIndex;//中间显示的那条数据对应的索引
    private int duration = 350;
    private OnScrollListener onScrollListener;
    private MyScroller scroller;
    private boolean isScrollerStarted = false;
    private GestureDetector gestureDetector;
    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            scrollView((int) distanceY);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int endPosition = ((int) (-velocityY)) / 500 * itemHeight;
            int durationScale = (int) (Math.abs(velocityY) / 3000) + 1;
            scroller.startScroll(0, getScrollY(), 0, endPosition - getScrollY(), duration * durationScale);
            postInvalidate();
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            scroller.forceFinished(true);
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            int clickIndex = ((int) e.getY()) / itemHeight - (getChildCount() - 1) / 2 + 1 + currentIndex;
            setCurrentIndex(clickIndex);
            return true;
        }
    };

    public void setOnScrollListener(OnScrollListener onScrollListener){
        this.onScrollListener = onScrollListener;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (null != onScrollListener) {
            int size = childViewList.size();
            for (int i = 0; i < size; i++) {
                View childAt = childViewList.get(i);
                float percentage = Math.abs((childAt.getTop() - getScrollY() + itemHeight / 2 - getMeasuredHeight() / 2f) / (getMeasuredHeight() / 2f));
                onScrollListener.onScroll(childAt, i - childViewList.size()/2, percentage);
            }
        }
        super.dispatchDraw(canvas);
    }

    private void scrollView(int distanceY) {
        if (distanceY > itemHeight) {
            scrollView(itemHeight);
            scrollView(distanceY - itemHeight);
        } else if (distanceY < -itemHeight) {
            scrollView(-itemHeight);
            scrollView(distanceY + itemHeight);
        } else {
            scrollBy(0, distanceY);
            if (getScrollY() >= itemHeight) {
                View view = childViewList.pollFirst();
                childViewList.addLast(view);
                int nextIndex = currentIndex + (getChildCount() + 1) / 2;
                if (nextIndex >= adapter.getDataCount()) {
                    nextIndex -= adapter.getDataCount();
                }
                adapter.setView(view, nextIndex);
                scrollBy(0, -itemHeight);
                layoutChildrenView();
                currentIndex++;
                if (currentIndex >= adapter.getDataCount()) {
                    currentIndex -= adapter.getDataCount();
                }
                onItemSelectListener.onItemSelected(currentIndex);
            } else if (getScrollY() <= -itemHeight) {
                View view = childViewList.pollLast();
                childViewList.addFirst(view);
                int preIndex = currentIndex - (getChildCount() + 1) / 2;
                if (preIndex < 0) {
                    preIndex += adapter.getDataCount();
                }
                adapter.setView(view, preIndex);
                scrollBy(0, itemHeight);
                layoutChildrenView();
                currentIndex--;
                if (currentIndex < 0) {
                    currentIndex += adapter.getDataCount();
                }
            }
        }
    }

    private int itemHeight;

    public VerticalWheelView(Context context) {
        this(context, null);
    }

    public VerticalWheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VerticalWheelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        gestureDetector = new GestureDetector(context, gestureListener);
        gestureDetector.setIsLongpressEnabled(false);
        scroller = new MyScroller(context, new DecelerateInterpolator(5));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        childViewList = new LinkedList();
        for (int i = 0; i < getChildCount(); i++) {
            childViewList.add(getChildAt(i));
        }
        initChildViews();
    }

    private void initChildViews() {
        currentIndex = 0;
        for (int i = 0; i < getChildCount(); i++) {
            int halfCount = (getChildCount() - 1) / 2;
            if (i < halfCount) {
                adapter.setView(childViewList.get(i), adapter.getDataCount() - halfCount + i);
            } else {
                adapter.setView(childViewList.get(i), i - halfCount);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            scrollViewToIdlePosition();
            return true;
        } else {
            return true;
        }
    }

    /**
     * 慢速滑动时抬起手指后，把View滑动到合适的位置
     */
    private void scrollViewToIdlePosition() {
        if (getScrollY() == 0) {
            return;
        } else if (getScrollY() < itemHeight / 2 && getScrollY() > -itemHeight / 2) {
            scroller.startScroll(0, getScrollY(), 0, -getScrollY());
        } else if (getScrollY() > itemHeight / 2) {
            scroller.startScroll(0, getScrollY(), 0, itemHeight - getScrollY());
        } else {
            scroller.startScroll(0, getScrollY(), 0, -itemHeight - getScrollY());
        }
        postInvalidate();
        //        System.out.println("scrollViewToIdlePosition");
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        itemHeight = getMeasuredHeight() / (getChildCount() - 2);
        int childMeasureSpec = MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY);
        measureChildren(widthMeasureSpec, childMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChildrenView();
    }

    public void setCurrentIndex(final int index) {
        post(new Runnable() {
            @Override
            public void run() {
                int n = index - currentIndex;
                scroller.startScroll(0, 0, 0, n * itemHeight, duration);
                postInvalidate();
            }
        });

    }

    private void layoutChildrenView() {
        int top = -itemHeight;
        for (View childView : childViewList) {
            childView.layout(0, top, getMeasuredWidth(), top + itemHeight);
            top += itemHeight;
        }
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        initChildViews();
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    @Override
    public void computeScroll() {
        if (!scroller.isFinished()) {
            scroller.computeScrollOffset();
            //            System.out.println("getDistanceX:" + scroller.getDistanceX());
            if (0 != scroller.getDistanceY()) {
                scrollView(scroller.getDistanceY());
            } else if (getScrollY() == 0) {
                scroller.forceFinished(true);
            }
            postInvalidate();
        } else {
            if (isScrollerStarted) {
                isScrollerStarted = false;
                onItemSelectListener.onItemSelected(currentIndex);
            }
        }
    }


    public static interface Adapter {
        /**
         * 使用方法参照ListAdapter
         *
         * @param convertView 不会为空
         * @param index
         * @return
         */
        void setView(View convertView, int index);

        int getDataCount();
    }

    public interface OnItemSelectListener {
        void onItemSelected(int index);
    }

    public interface OnScrollListener {
        /**
         * @param v
         * @param indexOffset
         * @param percentage  v中间X位置和WheelView中间位置的距离与WheelView宽度一半的比值
         */
        void onScroll(View v, int indexOffset, float percentage);
    }

    class MyScroller extends Scroller {
        private int distanceY;
        private int lastY = getScrollY();

        public int getDistanceY() {
            return distanceY;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            lastY = startY;
            isScrollerStarted = true;
            super.startScroll(startX, startY, dx, dy, duration);
        }

        @Override
        public boolean computeScrollOffset() {
            boolean isScrolling = super.computeScrollOffset();
            distanceY = getCurrY() - lastY;
            lastY = getCurrY();
            return isScrolling;
        }

        public MyScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

    }
}
