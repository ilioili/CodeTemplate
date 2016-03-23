package com.github.ilioili.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import org.jetbrains.annotations.Nullable;

/**
 * Created by Administrator on 2016/3/2.
 */
public class CircleAnimationFrame extends FrameLayout implements ValueAnimator.AnimatorUpdateListener {
    private Path path;
    private int centerX;
    private int centerY;
    private int radius = 0;
    private int maxRadius = 0;
    private int forgroundColor = Color.TRANSPARENT;
    private boolean isExpand;
    private ValueAnimator valueAnimator;
    private CompleteListener completeListener;
    private boolean holdPosition;
    private Interpolator expandInterpolator = new LinearInterpolator();
    private Interpolator collapseInterpolator = new LinearInterpolator();

    public CircleAnimationFrame(Context context) {
        this(context, null);
    }

    public CircleAnimationFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        path = new Path();
        valueAnimator = new ValueAnimator();
        valueAnimator.addUpdateListener(this);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (radius == 0) {
                    setVisibility(holdPosition ? View.INVISIBLE : View.GONE);
                }
                if (null != completeListener) {
                    completeListener.onComplete();
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
        });
    }

    /**
     * @param gravity
     * @param duration
     * @param holdPosition true: invisiable false:gone
     */
    public void collapse(Gravity gravity, int duration, boolean holdPosition) {
        collapse(gravity, duration, holdPosition, null);
    }

    public void collapse(Gravity gravity, int duration, boolean holdPosition, CompleteListener listener) {
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        switch (gravity) {
            case Center:
                collapse(false, w / 2, h / 2, duration, holdPosition, listener);
                break;
            case LeftTop:
                collapse(false, 0, 0, duration, holdPosition, listener);
                break;
            case LeftBottom:
                collapse(false, 0, h, duration, holdPosition, listener);
                break;
            case RightTop:
                collapse(false, w, 0, duration, holdPosition, listener);
                break;
            case RightBottom:
                collapse(false, w, h, duration, holdPosition, listener);
                break;
        }
    }

    public void collapse(boolean isLocationInWindow, final int locationX, final int locationY, final int duration, boolean holdPosition) {
        collapse(isLocationInWindow, locationX, locationY, duration, holdPosition, null);
    }

    public void collapse(boolean isLocationInWindow, final int locationX, final int locationY, final int duration, boolean holdPosition, CompleteListener listener) {
        if (isExpand) {
            isExpand = false;
        } else {
            return;
        }
        this.completeListener = listener;
        this.holdPosition = holdPosition;
        computCircleInfo(isLocationInWindow, locationX, locationY);

        valueAnimator.setInterpolator(collapseInterpolator);
        valueAnimator.setIntValues(radius, 0);
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    private void computCircleInfo(boolean isLocationInWindow, int xInWindow, int yInWindow) {
        if (isLocationInWindow) {
            int[] loc = new int[2];
            getLocationInWindow(loc);
            centerX = xInWindow - loc[0];
            centerY = yInWindow - loc[1];
        } else {
            centerX = xInWindow;
            centerY = yInWindow;
        }
        int dx1 = Math.abs(centerX);
        int dx2 = Math.abs(centerX - getMeasuredWidth());
        int dx = dx1 > dx2 ? dx1 : dx2;
        int dy1 = Math.abs(centerY);
        int dy2 = Math.abs(centerY - getMeasuredHeight());
        int dy = dy1 > dy2 ? dy1 : dy2;
        maxRadius = (int) (Math.sqrt(dx * dx + dy * dy) + 2);
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
        if (forgroundColor != Color.TRANSPARENT && (valueAnimator.isRunning() || valueAnimator.isStarted())) {
            int alpha = (int) ((Color.alpha(forgroundColor)) * (isExpand ? (1 - valueAnimator.getAnimatedFraction()) : valueAnimator.getAnimatedFraction()));
            int curColor = (forgroundColor & 0x00ffffff) | (alpha << 24);
            canvas.drawColor(curColor);
        }
    }

    public void expand() {
        expand(Gravity.Center);
    }

    public void expand(final Gravity gravity) {
        expand(gravity, getResources().getInteger(android.R.integer.config_longAnimTime));
    }

    public void expand(final Gravity gravity, final int duration) {
        expand(gravity, duration, null);
    }

    public void expand(final Gravity gravity, final int duration, CompleteListener listener) {
        expand(gravity, duration, Color.TRANSPARENT, listener);
    }

    public void expand(final Gravity gravity, final int duration, final int forgroundColor) {
        expand(gravity, duration, forgroundColor, null);
    }

    public void expand(final Gravity gravity, final int duration, final int forgroundColor, final CompleteListener listener) {
        setVisibility(View.VISIBLE);
        post(new Runnable() {
            @Override
            public void run() {
                int w = getMeasuredWidth();
                int h = getMeasuredHeight();
                switch (gravity) {
                    case Center:
                        expand(false, w / 2, h / 2, duration, forgroundColor, listener);
                        break;
                    case LeftTop:
                        expand(false, 0, 0, duration, forgroundColor, listener);
                        break;
                    case LeftBottom:
                        expand(false, 0, h, duration, forgroundColor, listener);
                        break;
                    case RightTop:
                        expand(false, w, 0, duration, forgroundColor, listener);
                        break;
                    case RightBottom:
                        expand(false, w, h, duration, forgroundColor, listener);
                        break;

                }
            }
        });

    }

    public void expand(final boolean isLocationInWindow, final int locationX, final int locationY) {
        expand(isLocationInWindow, locationX, locationY, getResources().getInteger(android.R.integer.config_longAnimTime));
    }

    public void expand(final boolean isLocationInWindow, final int locationX, final int locationY, final int duration) {
        expand(isLocationInWindow, locationX, locationY, duration, Color.TRANSPARENT);
    }

    public void expand(boolean isLocationInWindown, final int locationX, final int locationY, final int duration, final int foreGroundColor) {
        expand(isLocationInWindown, locationX, locationY, duration, foreGroundColor, null);
    }

    public void expand(final boolean isLocationInWindow, final int locationX, final int locationY, final int duration, final int foreGroundColor, CompleteListener listener) {
        if (isExpand) {
            return;
        } else {
            isExpand = true;
        }
        this.completeListener = listener;
        this.forgroundColor = foreGroundColor;
        setVisibility(View.VISIBLE);
        post(new Runnable() {
            @Override
            public void run() {
                computCircleInfo(isLocationInWindow, locationX, locationY);
                valueAnimator.setInterpolator(expandInterpolator);
                valueAnimator.setIntValues(radius, maxRadius);
                valueAnimator.setDuration(duration);
                valueAnimator.start();
            }
        });
    }

    /**
     * @return true: 表示已展开（或在展开的动画中）false:表示已关闭（或在关闭的动画中）
     */
    public boolean isExpand() {
        return isExpand;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        radius = (int) animation.getAnimatedValue();
        path.reset();
        path.addCircle(centerX, centerY, radius, Path.Direction.CCW);
        invalidate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (valueAnimator.isRunning()) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxRadius = (int) Math.sqrt(w * w + h * h) + 1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (valueAnimator.isRunning()) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void setInterpolator(@Nullable Interpolator expandInterpolator, @Nullable Interpolator collapseInterpolator) {
        if (null != expandInterpolator) {
            this.expandInterpolator = expandInterpolator;
        }
        if (null != collapseInterpolator) {
            this.collapseInterpolator = collapseInterpolator;
        }
    }


    public enum Gravity {
        LeftTop, LeftBottom, RightTop, RightBottom, Center
    }

    public interface CompleteListener {
        void onComplete();
    }
}
