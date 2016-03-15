package com.github.ilioili.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2016/3/2.
 */
public class CircleFrame extends FrameLayout implements ValueAnimator.AnimatorUpdateListener {
    private Path path;
    private Gravity gravity;
    private int radius = 0;
    private int maxRadius = 0;
    private ValueAnimator valueAnimator;

    public CircleFrame(Context context, AttributeSet attrs) {
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
                if(radius==0) {
                    setVisibility(View.GONE);
                }else if(radius==maxRadius){
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

    public void expand(final Gravity gravity, final int duration) {
        setVisibility(View.VISIBLE);
        this.gravity = gravity;
        post(new Runnable() {
            @Override
            public void run() {
                int w = getMeasuredHeight();
                int h = getMeasuredHeight();
                switch (gravity) {
                    case Center:
                        maxRadius = (int) (Math.sqrt(w * w / 4 + h * h / 4) + 2);
                        break;
                    case LeftTop:
                    case LeftBottom:
                    case RightBottom:
                    case RightTop:
                        maxRadius = (int) (Math.sqrt(w * w + h * h) + 1);
                        break;
                }
                valueAnimator.setIntValues(0, maxRadius);
                valueAnimator.setDuration(duration);
                valueAnimator.start();
            }
        });
    }

    public void collpase(Gravity gravity, int duration) {
        this.gravity = gravity;
        valueAnimator.setIntValues(radius, 0);
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }


    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        radius = (int) animation.getAnimatedValue();
        path.reset();
        switch (gravity) {
            case Center:
                path.addCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, radius, Path.Direction.CCW);
                break;
            case LeftTop:
                path.addCircle(0, 0, radius, Path.Direction.CCW);
                break;
            case LeftBottom:
                path.addCircle(0, getMeasuredHeight(), radius, Path.Direction.CCW);
                break;
            case RightBottom:
                path.addCircle(getMeasuredWidth(), getMeasuredHeight(), radius, Path.Direction.CCW);
                break;
            case RightTop:
                path.addCircle(getMeasuredWidth(), 0, radius, Path.Direction.CCW);
                break;
        }
        invalidate();
    }


    public enum Gravity {
        LeftTop, LeftBottom, RightTop, RightBottom, Center
    }

}
