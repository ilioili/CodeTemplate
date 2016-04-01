package com.github.ilioili.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2016/2/25.
 */
public class SuperLayout extends FrameLayout{
    int gridWidth;
    int gridHeight;
    int column = 2;
    int row = 2;
    float scale = 1;
    public SuperLayout(Context context) {
        super(context);
    }

    public SuperLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SuperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SuperLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        gridWidth = MeasureSpec.getSize(widthMeasureSpec);
        gridHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(gridWidth*column, gridHeight*row);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    public void animateTo(int column, int row, float scale, Gravity gravity){
        scrollTo(column*gridWidth, row*gridHeight);
    }
}
