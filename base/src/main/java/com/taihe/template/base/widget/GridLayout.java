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
public class GridLayout extends ViewGroup {
    private int colum = 3;

    public GridLayout(Context context) {
        this(context, null);
    }

    public GridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyGridLayout, defStyleAttr, 0);
        colum = typedArray.getInteger(R.styleable.MyGridLayout_colum, 3);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GridLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = ((getChildCount() - 1) / colum + 1) * (width / colum);
        setMeasuredDimension(width, height);
        int size = width / colum;
        int childMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).measure(childMeasureSpec, childMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0;
        int top = 0;
        int size = getWidth() / colum;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            left = i % colum * size;
            top = i / colum * size;
            child.layout(left, top, left+size, top+size);
        }
    }


}
