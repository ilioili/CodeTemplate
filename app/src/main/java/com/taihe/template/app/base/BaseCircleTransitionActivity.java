package com.taihe.template.app.base;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.github.ilioili.widget.CircleAnimationFrame;
import com.taihe.template.base.BaseActivity;
import com.taihe.template.base.util.LogUtil;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/3/3.
 */
public class BaseCircleTransitionActivity extends BaseActivity {

    public static final String UP_X = "startX";
    public static final String UP_Y = "startY";
    private final int ANIMATION_DURATION = 500;
    protected boolean applyCircleTransition = true;
    private CircleAnimationFrame circleAnimationFrame;
    private int startX;
    private int startY;
    private boolean isAnimatingToFinish;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (applyCircleTransition) {
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                startX = (int) ev.getRawX();
                startY = (int) ev.getRawY();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void finish() {
        if (applyCircleTransition) {
            if (isAnimatingToFinish) {
                return;
            } else {
                isAnimatingToFinish = true;
            }
            try {//把Activity变成透明，让之前的一个Activity重绘
                Class[] t = Activity.class.getDeclaredClasses();
                Class translucentConversionListenerClazz = null;
                Class[] method = t;
                int len = t.length;

                for (int i = 0; i < len; ++i) {
                    Class clazz = method[i];
                    if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                        translucentConversionListenerClazz = clazz;
                        break;
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Method m = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz, ActivityOptions.class);
                    m.setAccessible(true);
                    m.invoke(BaseCircleTransitionActivity.this, new Object[]{null, null});
                } else {
                    Method var8 = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz);
                    var8.setAccessible(true);
                    var8.invoke(BaseCircleTransitionActivity.this, new Object[]{null});
                }
            } catch (Throwable e) {
                LogUtil.e(e);
            }
            int upX = getIntent().getIntExtra(UP_X, getResources().getDisplayMetrics().widthPixels / 2);
            int upY = getIntent().getIntExtra(UP_Y, getResources().getDisplayMetrics().heightPixels);
            circleAnimationFrame.collapse(true, upX, upY, ANIMATION_DURATION, false, new CircleAnimationFrame.CompleteListener() {
                @Override
                public void onComplete() {
                    BaseCircleTransitionActivity.super.finish();
                }
            });
        } else {
            super.finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (applyCircleTransition) {
            circleAnimationFrame = new CircleAnimationFrame(this);
            ViewGroup root = (ViewGroup) getWindow().getDecorView();
            View child = root.getChildAt(0);
            child.setBackgroundColor(Color.WHITE);
            root.removeView(child);
            root.addView(circleAnimationFrame);
            circleAnimationFrame.addView(child);
            int upX = getIntent().getIntExtra(UP_X, getResources().getDisplayMetrics().widthPixels / 2);
            int upY = getIntent().getIntExtra(UP_Y, getResources().getDisplayMetrics().heightPixels);
            circleAnimationFrame.expand(true, upX, upY, ANIMATION_DURATION, Color.TRANSPARENT, new CircleAnimationFrame.CompleteListener() {
                @Override
                public void onComplete() {//把Activity变成不透明，之前的一个Activity可以被释放
                    try {
                        Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
                        method.setAccessible(true);
                        method.invoke(BaseCircleTransitionActivity.this, new Object[]{null});
                    } catch (Exception e) {
                        LogUtil.e(e);
                    }
                }
            });
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if (applyCircleTransition) {
            intent.putExtra(UP_X, startX);
            intent.putExtra(UP_Y, startY);
        }
        super.startActivity(intent);
    }
}
