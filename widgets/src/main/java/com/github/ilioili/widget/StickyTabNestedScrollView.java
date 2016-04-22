package com.github.ilioili.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by hantuo on 16/4/20.
 * with 3 childviews scroll in this NestedScrollView. Typically used in the scene that a tabview in the model can state at the top while scrolling up
 */
public class StickyTabNestedScrollView extends NestedScrollView {
    private LinearLayout innerContainer;
    private int maxSrollY;

    public StickyTabNestedScrollView(Context context) {
        this(context, null);
    }

    public StickyTabNestedScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickyTabNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        innerContainer = new LinearLayout(context) {

            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                int width = MeasureSpec.getSize(widthMeasureSpec);
                int height = MeasureSpec.getSize(heightMeasureSpec);
                measureChild(getChildAt(0), widthMeasureSpec, MeasureSpec.makeMeasureSpec(getChildAt(0).getLayoutParams().height, MeasureSpec.EXACTLY));
                measureChild(getChildAt(1), widthMeasureSpec, MeasureSpec.makeMeasureSpec(getChildAt(1).getLayoutParams().height, MeasureSpec.EXACTLY));
                measureChild(getChildAt(2), widthMeasureSpec, MeasureSpec.makeMeasureSpec(height - getChildAt(1).getMeasuredHeight(), MeasureSpec.EXACTLY));
                setMeasuredDimension(width, getChildAt(0).getMeasuredHeight() + getChildAt(1).getMeasuredHeight() + getChildAt(2).getMeasuredHeight());
            }
        };
        innerContainer.setOrientation(LinearLayout.VERTICAL);
        super.addView(innerContainer);
    }


    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        innerContainer.addView(child, params);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public void requestChildFocus(View child, View focused) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        innerContainer.measure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
        maxSrollY = innerContainer.getMeasuredHeight() - height;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0) {//往上滑时候要先于ChildView（例如：RecyclerView）滑动
            int consume = 0;
            if (dy + getScrollY() < 0) {
                consume = -getScrollY();
            } else if (dy + getScrollY() > maxSrollY) {
                consume = maxSrollY - getScrollY();
            } else {
                consume = dy;
            }
            scrollBy(0, consume);
            consumed[1] = consume;
        }
        dispatchNestedPreScroll(dx, dy, consumed, null);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(target, velocityX, velocityY);
    }


}
