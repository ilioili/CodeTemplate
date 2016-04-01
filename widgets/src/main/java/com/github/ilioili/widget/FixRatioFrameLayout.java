package com.github.ilioili.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;



/**
 * Created by Administrator on 2015/12/14.
 */
public class FixRatioFrameLayout extends FrameLayout {

    private boolean baseOnWith = true;
    private float ratio = 1;

    public FixRatioFrameLayout(Context context) {
        super(context);
    }

    public FixRatioFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixRatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Ratio);
        ratio = typedArray.getFloat(R.styleable.Ratio_ratio, 1f);
        baseOnWith = typedArray.getBoolean(R.styleable.Ratio_baseOnWidth, true);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if(baseOnWith){
            height = (int) (width*ratio);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }else{
            width = (int) (height*ratio);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
