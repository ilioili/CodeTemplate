package com.github.ilioili.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/3/15.
 */
public class RippleContainer extends ViewGroup {

    RippleHelper rippleHelper = new RippleHelper(this);


    public RippleContainer(Context context) {
        this(context, null);
    }

    public RippleContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RippleContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        rippleHelper.drawRipple(canvas);
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        getChildAt(0).measure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getChildAt(0).getMeasuredWidth(), getChildAt(0).getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        getChildAt(0).layout(l, t, r, b);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        rippleHelper.injectRipple(ev);
        return super.dispatchTouchEvent(ev);
    }




}
