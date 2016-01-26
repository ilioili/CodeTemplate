package com.taihe.template.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.taihe.template.base.R;


/**
 * Created by Administrator on 2015/12/14.
 */
public class FixRatioImageView extends ImageView {

    private boolean baseOnWith = true;
    private boolean matchDrawableRatio = false;
    private float ratio = 1;

    public FixRatioImageView(Context context) {
        super(context);
    }

    public FixRatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixRatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Ratio);
        ratio = typedArray.getFloat(R.styleable.Ratio_ratio, 1f);
        baseOnWith = typedArray.getBoolean(R.styleable.Ratio_baseOnWidth, true);
        matchDrawableRatio = typedArray.getBoolean(R.styleable.Ratio_matchDrawableRatio, false);
        typedArray.recycle();
        Drawable drawable = getDrawable();
        setRatioByDrawableRatio(drawable);
    }

    /**
     *
     * @param ratio
     * @param baseOnWith
     */
    public void setRatio(float ratio, boolean baseOnWith){
        this.ratio = ratio;
        this.baseOnWith = baseOnWith;
    }

    public float getRatio(){
        return ratio;
    }

    public void setRatioBaseOnDrawableRatio(boolean baseOnWith){
        matchDrawableRatio = true;
        this.baseOnWith = baseOnWith;
    }

    private void setRatioByDrawableRatio(Drawable drawable) {
        if (matchDrawableRatio) {
            if (null != drawable && matchDrawableRatio) {
                if (baseOnWith) {
                    ratio = drawable.getIntrinsicHeight() * 1f / drawable.getIntrinsicWidth();
                } else {
                    ratio = drawable.getIntrinsicWidth() * 1f / drawable.getIntrinsicHeight();
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (baseOnWith) {
            height = (int) (width * ratio);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        } else {
            width = (int) (height * ratio);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        setRatioByDrawableRatio(drawable);
    }
}
