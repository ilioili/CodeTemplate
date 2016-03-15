package com.github.ilioili.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Administrator on 2016/3/15.
 */
public class RippleHelper implements ValueAnimator.AnimatorUpdateListener {
    private static final int FAST_DURATION = 250;
    private static final int SLOW_DURATION = 1500;
    private int centerX;
    private int centerY;
    private boolean isRippleFinished;
    private Path path;
    private Paint ripplePaint;
    private Paint bgPaint;
    private int radius = 0;
    private int maxRadius = 0;
    private ValueAnimator valueAnimator;
    private View target;

    RippleHelper(View targetView) {
        target = targetView;
        path = new Path();
        ripplePaint = new Paint();
        ripplePaint.setColor(Color.parseColor("#22000000"));
        bgPaint = new Paint();
        bgPaint.setColor(ripplePaint.getColor());
        valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(this);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                radius = 0;
                isRippleFinished = true;
                target.invalidate();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
        });
    }

    private void computCircleInfo(float x, float y) {
        centerX = (int) x;
        centerY = (int) y;
        int dx1 = centerX;
        int dx2 = target.getMeasuredWidth() - centerX;
        int dx = dx1 > dx2 ? dx1 : dx2;
        int dy1 = centerY;
        int dy2 = target.getMeasuredHeight() - centerY;
        int dy = dy1 > dy2 ? dy1 : dy2;
        maxRadius = (int) (Math.sqrt(dx * dx + dy * dy) * 1.1f);
    }


    public void drawRipple(Canvas canvas) {
        if (radius != 0) {
            target.getBackground();
            canvas.clipRect(0, 0, target.getMeasuredWidth(), target.getMeasuredHeight(), Region.Op.INTERSECT);
            int color = ripplePaint.getColor();
            int alpha = Color.alpha(color);
            color &= 0x00ffffff;
            color |= (alpha*radius/maxRadius)<<24;
            canvas.drawColor(color);
            canvas.drawPath(path, ripplePaint);
        }
    }

    void injectRipple(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            isRippleFinished = false;
            computCircleInfo(ev.getX(), ev.getY());
            radius = 0;
            performRipple(SLOW_DURATION);
        } else if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            if (!isRippleFinished) {
                performRipple(FAST_DURATION);
            }
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        radius = (int) animation.getAnimatedValue();
        path.reset();
        path.addCircle(centerX, centerY, radius, Path.Direction.CCW);
        target.invalidate();
    }

    private void performRipple(final int duration) {
        target.post(new Runnable() {
            @Override
            public void run() {
                int d = duration * (maxRadius - radius) / maxRadius;
                valueAnimator.setIntValues(radius, maxRadius);
                valueAnimator.setDuration(d);
                valueAnimator.start();
            }
        });
    }

}
