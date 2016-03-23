package com.github.ilioili.widget.ripple;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;


/**
 * Created by Administrator on 2016/3/15.
 */
public class RippleRelativeLayout extends RelativeLayout {
    RippleHelper rippleHelper = new RippleHelper(this);
    public RippleRelativeLayout(Context context) {
        super(context);
    }

    public RippleRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RippleRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RippleRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void draw(Canvas canvas) {
        rippleHelper.drawRipple(canvas);
        super.draw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        rippleHelper.getDownPosition(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setPressed(boolean pressed) {
        rippleHelper.onPressed(pressed);
        super.setPressed(pressed);
    }
}
