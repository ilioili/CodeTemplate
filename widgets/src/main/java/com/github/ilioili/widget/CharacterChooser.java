package com.github.ilioili.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;



public class CharacterChooser extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int colorDefault;
    private int colorSelected;
    private OnCharacterSelectedListener listener;
    private char curChar = 0;

    public CharacterChooser(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Indicator, defStyle, 0);
        colorDefault = typedArray.getColor(R.styleable.CharacterChooser_default_color, Color.BLACK);
        colorSelected = typedArray.getColor(R.styleable.CharacterChooser_default_color, Color.RED);

        int dimensionPixelSize = typedArray.getInt(R.styleable.CharacterChooser_size, 16);
        this.paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dimensionPixelSize, context.getResources().getDisplayMetrics()));
        typedArray.recycle();

        this.paint.setTextAlign(Align.CENTER);
    }

    public CharacterChooser(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CharacterChooser(Context context) {
        this(context, null);
    }

    public void setOnCharacterSelectedListener(OnCharacterSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                checkNewCharTrigered(event);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                checkNewCharTrigered(event);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public void setSelectedChar(char c) {
        if (c < 'a') c = 'a' - 1;
        if (c > 'z') c = 'z' + 1;
        curChar = c;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float height = (getMeasuredHeight() - getPaddingTop() - getPaddingBottom()) / 28f;
        int x = getPaddingLeft() + (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / 2;
        paint.setColor(curChar == 'a' - 1 ? colorSelected : colorDefault);
        canvas.drawText("★", x, getPaddingTop() + paint.getTextSize(), paint);
        for (int i = 0; i < 26; i++) {
            paint.setColor(curChar == i + 'a' ? colorSelected : colorDefault);
            canvas.drawText((char) (i + 'a') + "", x, getPaddingTop() + height * (i + 1) + paint.getTextSize(), paint);
        }
        paint.setColor(curChar == 'z' + 1 ? colorSelected : colorDefault);
        canvas.drawText("#", x, getPaddingTop() + height * 27 + paint.getTextSize(), paint);
    }

    private void checkNewCharTrigered(MotionEvent event) {
        char c = (char) ((event.getY() - getPaddingTop()) / ((getMeasuredHeight() - getPaddingTop() - getPaddingBottom()) / 28) + 'a' - 1);
        if (curChar != c) {
            curChar = c;
            if (c == 'a' - 1) {
                listener.onImportSelected();
            } else if (c == 'z' + 1) {
                listener.onUnknownSelected();
            } else {
                listener.onCharacterSelected(curChar);
            }
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public interface OnCharacterSelectedListener {
        void onCharacterSelected(char c);

        void onUnknownSelected();

        void onImportSelected();
    }

}
