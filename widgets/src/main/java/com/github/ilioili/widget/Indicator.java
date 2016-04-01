package com.github.ilioili.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by Administrator on 2016/1/14.
 */
public class Indicator extends View {
    private float intervalPx;
    private int drawableWidth;
    private int drawableHeight;
    private int count;
    private int selectIndex;
    private Drawable drawableDefault;
    private Drawable drawableSelect;

    public Indicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Indicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Indicator, defStyleAttr, 0);
        intervalPx = typedArray.getDimension(R.styleable.Indicator_interval, 100);
        drawableWidth = (int) typedArray.getDimension(R.styleable.Indicator_drawable_width, 100);
        drawableHeight = (int) typedArray.getDimension(R.styleable.Indicator_drawable_height, 100);
        drawableSelect = typedArray.getDrawable(R.styleable.Indicator_drawable_selected);
        drawableDefault = typedArray.getDrawable(R.styleable.Indicator_drawable_default);
        drawableDefault.setBounds(0, 0, drawableWidth, drawableHeight);
        drawableSelect.setBounds(0, 0, drawableWidth, drawableHeight);
        typedArray.recycle();
    }

    public void setUpWithViewPager(final ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (count == 0) {
                    selectIndex = 0;
                } else {
                    selectIndex = position % count;
                }
                postInvalidate();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(isInEditMode()){
            count = 5;
            selectIndex = 2;
        }
        if (count == 0) {
            setMeasuredDimension(0, 0);
        } else {
            int width = (int) (count * drawableWidth + (count - 1) * intervalPx)+getPaddingLeft()+getPaddingRight();
            int height = drawableHeight + getPaddingBottom() + getPaddingTop();
            setMeasuredDimension(width, height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(getPaddingLeft(), getPaddingTop());
        for (int i = 0; i < count; i++) {
            if (selectIndex == i) {
                drawableSelect.draw(canvas);
            } else {
                drawableDefault.draw(canvas);
            }
            canvas.translate(drawableWidth+intervalPx, 0);
        }
    }

    public void setCount(int value){
        count = value;
    }
}
