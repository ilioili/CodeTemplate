package com.taihe.template.base.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.taihe.template.base.R;

/**
 * Created by Administrator on 2016/2/16.
 */
public class RelativeContainer extends ViewGroup {
    private boolean baseOnWidth;
    private float ratio;

    public RelativeContainer(Context context) {
        super(context);
    }

    public RelativeContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelativeContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RelativeContainer);
        baseOnWidth = a.getBoolean(R.styleable.RelativeContainer_base_on_width, true);
        ratio = a.getFloat(R.styleable.RelativeContainer_w_h_ratio, 0);
        a.recycle();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RelativeContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int unitLength = baseOnWidth ? getMeasuredWidth() : getMeasuredHeight();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            int left = (int) (layoutParams.relativeLeft * unitLength);
            int top = (int) (layoutParams.relativeTop * unitLength);
            int right = left + child.getWidth();
            int bottom = top + child.getHeight();
            child.layout(left, top, right, bottom);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (baseOnWidth) {
            height = (int) (width * ratio);
        } else {
            width = (int) (height * ratio);
        }
        setMeasuredDimension(width, height);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            int unitLength = baseOnWidth ? width : height;
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (unitLength * layoutParams.relativeWidth), MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (unitLength * layoutParams.relativeHeight), MeasureSpec.EXACTLY);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new RelativeContainer.LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public float relativeWidth;
        public float relativeHeight;
        public float relativeTop;
        public float relativeLeft;

        /**
         * {@inheritDoc}
         */
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.RelativeContainer);
            relativeWidth = a.getFloat(R.styleable.RelativeContainer_relative_width, 0);
            relativeHeight = a.getFloat(R.styleable.RelativeContainer_relative_height, 0);
            relativeLeft = a.getFloat(R.styleable.RelativeContainer_relative_left, 0);
            relativeTop = a.getFloat(R.styleable.RelativeContainer_relative_top, 0);
            a.recycle();
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(int width, int height, int relativeWidth, int relativeHeight, int relativeLeft, int relativeTop) {
            super(width, height);
            this.relativeLeft = relativeLeft;
            this.relativeHeight = relativeHeight;
            this.relativeTop = relativeTop;
            this.relativeWidth = relativeWidth;
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }
    }
}
