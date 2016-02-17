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
 * Created by Administrator on 2016/1/27.
 */
public class SimpleGridLayout extends ViewGroup {
    private int colum;
    private int dividerWidth;

    public SimpleGridLayout(Context context) {
        this(context, null);
    }

    public SimpleGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleGridLayout, defStyleAttr, 0);
        colum = typedArray.getInteger(R.styleable.SimpleGridLayout_grid_colum, 3);
        dividerWidth = (int) typedArray.getDimension(R.styleable.SimpleGridLayout_grid_divider_width, 0);
        typedArray.recycle();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SimpleGridLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int size = getChildSize(width);
        int height = ((getChildCount() - 1) / colum + 1) * (size+dividerWidth)+dividerWidth;
        width = size*colum+dividerWidth*colum+dividerWidth;
        int childMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).measure(childMeasureSpec, childMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    private int getChildSize(int width) {
        return (width - (colum + 1) * dividerWidth) / colum;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int size = getChildSize(getWidth());
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int left = i % colum * size + (i % colum + 1) * dividerWidth;
            int top = i / colum * size + (i / colum + 1) * dividerWidth;
            child.layout(left, top, left + size, top + size);
        }
    }
}
