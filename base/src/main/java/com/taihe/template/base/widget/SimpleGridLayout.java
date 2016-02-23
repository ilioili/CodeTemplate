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
    private float rowHeight;
    private int childHeight;
    private int childWidth;

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
        rowHeight = typedArray.getDimension(R.styleable.SimpleGridLayout_grid_row_height, 1);
        typedArray.recycle();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SimpleGridLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        childWidth = getChildSize(width);
        childHeight = (int) (rowHeight > 1 ? rowHeight : rowHeight * childWidth);
        width = getPaddingLeft() + getPaddingRight() + colum * (childWidth + dividerWidth) - dividerWidth;
        int height = getPaddingTop() + getPaddingBottom() + ((getChildCount() - 1) / colum + 1) * (childHeight + dividerWidth) - dividerWidth;
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    private int getChildSize(int width) {
        return  ((width - (colum - 1) * dividerWidth) - getPaddingLeft() - getPaddingRight()) / colum;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int left = (getPaddingLeft() + i % colum * childWidth + i % colum * dividerWidth);
            int top = (getPaddingTop() + i / colum * childHeight + i / colum * dividerWidth);
            child.layout(left, top, (left + childWidth), (top + childHeight));
        }
    }
}
