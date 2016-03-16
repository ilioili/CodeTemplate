package com.github.ilioili.widget.ripple;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;


/**
 * Created by Administrator on 2016/3/15.
 */
public class RippleTextView extends TextView {
    RippleHelper rippleHelper = new RippleHelper(this);
    public RippleTextView(Context context) {
        super(context);
    }

    public RippleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RippleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RippleTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void draw(Canvas canvas) {
        rippleHelper.drawRipple(canvas);
        super.draw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        rippleHelper.injectRipple(ev);
        return super.dispatchTouchEvent(ev);
    }


}
