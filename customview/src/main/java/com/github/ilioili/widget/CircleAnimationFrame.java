package com.github.ilioili.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2016/3/2.
 */
public class CircleAnimationFrame extends FrameLayout implements ValueAnimator.AnimatorUpdateListener {
    private Path path;
    private int centerX;
    private int centerY;
    private int radius = 0;
    private int maxRadius = 0;
    private ValueAnimator valueAnimator;
    private CompleteListener listener;

    public CircleAnimationFrame(Context context){
        this(context, null);
    }

    public CircleAnimationFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        path = new Path();
        valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(new DecelerateInterpolator(1f));
        valueAnimator.addUpdateListener(this);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (radius == 0) {
                    setVisibility(View.GONE);
                }
                if (null != listener) {
                    listener.onComplete();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (isInEditMode()) {
            path.addCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredHeight() > getMeasuredWidth() ? getMeasuredHeight() / 2 : getMeasuredWidth() / 2, Path.Direction.CCW);
            canvas.clipPath(path);
            canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), Region.Op.INTERSECT);
        } else {
            canvas.clipPath(path);
            canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), Region.Op.INTERSECT);
        }
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxRadius = (int) Math.sqrt(w * w + h * h) + 1;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(valueAnimator.isRunning()){
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(valueAnimator.isRunning()){
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void expand(final Gravity gravity, final int duration) {
        setVisibility(View.VISIBLE);
        post(new Runnable() {
            @Override
            public void run() {
                final int[] loc = new int[2];
                getLocationInWindow(loc);
                int w = getMeasuredWidth();
                int h = getMeasuredHeight();
                switch (gravity) {
                    case Center:
                        computCircleInfo(loc[0] + w / 2, loc[1] + h / 2);
                        break;
                    case LeftTop:
                        computCircleInfo(loc[0], loc[1]);
                        break;
                    case LeftBottom:
                        computCircleInfo(loc[0], loc[1] + h);
                        break;
                    case RightTop:
                        computCircleInfo(loc[0] + w, loc[1]);
                        break;
                    case RightBottom:
                        computCircleInfo(loc[0] + w, loc[1] + h);
                        break;

                }
                valueAnimator.setIntValues(radius, maxRadius);
                valueAnimator.setDuration(duration);
                valueAnimator.start();

            }
        });
    }

    public void expand(final Gravity gravity, final int duration, CompleteListener listener) {
        expand(gravity, duration);
        this.listener = listener;
    }

    public void expand(final int xInWindow, final int yInWindow, final int duration) {
        setVisibility(View.VISIBLE);
        post(new Runnable() {
            @Override
            public void run() {
                computCircleInfo(xInWindow, yInWindow);
                valueAnimator.setIntValues(radius, maxRadius);
                valueAnimator.setDuration(duration);
                valueAnimator.start();
            }
        });

    }

    public void expand(final int xInWindow, final int yInWindow, final int duration, CompleteListener listener) {
        expand(xInWindow, yInWindow, duration);
        this.listener = listener;
    }

    private void computCircleInfo(int xInWindow, int yInWindow) {
        int[] loc = new int[2];
        getLocationInWindow(loc);
        centerX = xInWindow - loc[0];
        centerY = yInWindow - loc[1];
        int dx1 = Math.abs(centerX - 0);
        int dx2 = Math.abs(centerX - getMeasuredWidth());
        int dx = dx1 > dx2 ? dx1 : dx2;
        int dy1 = Math.abs(centerY - 0);
        int dy2 = Math.abs(centerY - getMeasuredHeight());
        int dy = dy1 > dy2 ? dy1 : dy2;
        maxRadius = (int) (Math.sqrt(dx * dx + dy * dy) + 2);
    }

    public void collpase(Gravity gravity, int duration) {
        final int[] loc = new int[2];
        getLocationInWindow(loc);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        switch (gravity) {
            case Center:
                computCircleInfo(loc[0] + w / 2, loc[1] + h / 2);
                break;
            case LeftTop:
                computCircleInfo(loc[0], loc[1]);
                break;
            case LeftBottom:
                computCircleInfo(loc[0], loc[1] + h);
                break;
            case RightTop:
                computCircleInfo(loc[0] + w, loc[1]);
                break;
            case RightBottom:
                computCircleInfo(loc[0] + w, loc[1] + h);
                break;
        }
        valueAnimator.setIntValues(radius, 0);
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    public void collpase(Gravity gravity, int duration, CompleteListener listener) {
        collpase(gravity, duration);
        this.listener = listener;
    }

    public void collpase(final int xInWindow, final int yInWindow, final int duration) {
        computCircleInfo(xInWindow, yInWindow);
        valueAnimator.setIntValues(radius, 0);
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    public void collpase(final int xInWindow, final int yInWindow, final int duration, CompleteListener listener) {
        collpase(xInWindow, yInWindow, duration);
        this.listener = listener;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        radius = (int) animation.getAnimatedValue();
        path.reset();
        path.addCircle(centerX, centerY, radius, Path.Direction.CCW);
        invalidate();
    }


    public enum Gravity {
        LeftTop, LeftBottom, RightTop, RightBottom, Center
    }

    public interface CompleteListener {
        void onComplete();
    }
}
