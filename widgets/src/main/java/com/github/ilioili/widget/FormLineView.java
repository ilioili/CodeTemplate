package com.github.ilioili.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/3/4.
 */
public class FormLineView extends View {
    private int leftScrollBoder;
    private int rightScrollBoder;
    private int width;
    private int height;
    private int interval;
    private boolean isFingerUp;
    private Paint paint;
    private Paint dashPaint;
    private Path path;
    //    private String titles[] = new String[]{"1月", "2月", "3月", "4月"};
//    private int[] data = new int[]{0, 0, 300,0};
    private String titles[] = new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
    private float[] data = new float[]{0, 100, 300.55f, 200, 500.7f, 0, 50, 800, 500, 200, 400, 100, 0, 100, 300, 200, 500, 0, 50, 800, 500, 200, 400, 100};
    private int[] tops;

    private int lastSelectIndex = -1;
    private Scroller scroller;
    private GestureDetector gestureDetector;
    private OnActionListener actionListener;
    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            int index = (int) ((e.getX() + getScrollX() + interval / 2) / interval);
            if (index < 0) {
                index = 0;
            } else if (index > data.length) {
                index = data.length - 1;
            }
            if (null != actionListener) {
                actionListener.onItemClick(index);
            }
            scrollToIndex(index);
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            isFingerUp = false;
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            scrollBy((int) distanceX, 0);
            if (getScrollX() < leftScrollBoder) {
                scrollTo(leftScrollBoder, 0);
            } else if (getScrollX() > rightScrollBoder) {
                scrollTo(rightScrollBoder, 0);
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            scroller.fling(getScrollX(), 0, (int) -velocityX, 0, leftScrollBoder, rightScrollBoder, 0, 0);
            postInvalidate();
            return true;
        }
    };
    private int firstVisiableIndex;
    private int lastVisiableIndex;

    public FormLineView(Context context) {
        this(context, null);
    }

    public FormLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, gestureListener);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(dp2px(Config.TEXT_SIZE));
        paint.setTextAlign(Paint.Align.CENTER);
        path = new Path();
        dashPaint = new Paint();
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setColor(Color.BLACK);
        int dash = dp2px(5);
        PathEffect effects = new DashPathEffect(new float[]{dash, dash, dash, dash}, 1);
        dashPaint.setPathEffect(effects);
        scroller = new Scroller(context, new DecelerateInterpolator());
    }

    private void scrollToIndex(int index) {
        scroller.startScroll(getScrollX(), 0, interval * (index - Config.FIX_COLUMN / 2) - getScrollX(), 0, 250);
        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean handled = gestureDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            isFingerUp = true;
            if (!handled)
                updateSelectedIndex();
        }
        return true;
    }

    private void updateSelectedIndex() {
        if (isFingerUp) {
            int nowSelectedIndex = getSelectedIndex();
            if (lastSelectIndex == -1) {
                lastSelectIndex = nowSelectedIndex;
            } else if (lastSelectIndex != nowSelectedIndex) {
                lastSelectIndex = nowSelectedIndex;
                if (null != actionListener) {
                    actionListener.onItemSelected(lastSelectIndex);
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        computeVisiableIndex();
        canvas.clipRect(getScrollX(), 0, getScrollX() + width, height);
        drawTitles(canvas);//绘制底部标签
        drawPathShade(canvas);//绘制曲线围成的区域
        drawPoint(canvas);//绘制所有的数据点
        drawSelectData(canvas);//当前聚焦的数据
        drawCenterLine(canvas);//绘制中线
    }

    private void drawCenterLine(Canvas canvas) {
        int yPos = (height - dp2px(Config.BOTTOM_TITLE_SPACE + Config.TOP_DATA_TEXT_SPACE)) / 2 + Config.TOP_DATA_TEXT_SPACE;
        path.reset();
        path.moveTo(-width / 2, yPos);
        path.lineTo(interval * data.length + width / 2, yPos);
        canvas.drawPath(path, dashPaint);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), 0);
        } else {
            updateSelectedIndex();
        }
    }

    private int getCenterIndex() {//TODO
        return 0;
    }

    private void drawSelectData(Canvas canvas) {
        int index = getSelectedIndex();
        int xPos = index * interval;
        canvas.drawLine(xPos, height - dp2px(Config.BOTTOM_TITLE_SPACE), xPos, tops[index], paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(xPos, tops[index], dp2px(Config.DOT_RADIUS) * 2, paint);
        paint.setColor(Config.DOT_COLOR);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        canvas.drawCircle(xPos, tops[index], dp2px(Config.DOT_RADIUS) * 2, paint);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(index * interval, tops[index], dp2px(Config.DOT_RADIUS), paint);
        canvas.drawText(data[index] + "", xPos, tops[index] - dp2px(Config.TOP_DATA_TEXT_SPACE) / 3, paint);
    }

    private int getSelectedIndex() {
        int index = (width / 2 + getScrollX() + interval / 2) / interval;
        if (index > data.length - 1) {
            index = data.length - 1;
        }
        return index;
    }

    private void drawPoint(Canvas canvas) {
        paint.setColor(Config.DOT_COLOR);
        paint.setStyle(Paint.Style.FILL);
        for (int i = firstVisiableIndex; i < lastVisiableIndex; i++) {
            int startX = i * interval;
            canvas.drawLine(startX, height - dp2px(Config.BOTTOM_TITLE_SPACE), startX, tops[i], dashPaint);
            if (i + 1 < lastVisiableIndex)
                canvas.drawLine(startX, tops[i], (i + 1) * interval, tops[i + 1], paint);
            else
                canvas.drawLine(startX, tops[i], startX, height - dp2px(Config.BOTTOM_TITLE_SPACE), paint);
            canvas.drawCircle(startX, tops[i], dp2px(Config.DOT_RADIUS), paint);
        }
    }

    private void drawPathShade(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        path.reset();
        path.moveTo(firstVisiableIndex * interval, height - dp2px(Config.BOTTOM_TITLE_SPACE));
        for (int i = firstVisiableIndex; i < lastVisiableIndex; i++) {
            path.lineTo(i * interval, tops[i]);
        }
        path.lineTo(lastVisiableIndex * interval - interval, height - dp2px(Config.BOTTOM_TITLE_SPACE));
        paint.setColor(Config.SHADE_COLOR);
        canvas.drawPath(path, paint);
    }

    private void computeTopPosition() {
        float max = getMax(data);
        tops = new int[data.length];
        int maxHeight = height - dp2px(Config.TOP_DATA_TEXT_SPACE + Config.BOTTOM_TITLE_SPACE);
        for (int i = 0; i < tops.length; i++) {
            tops[i] = (int) (height - maxHeight * data[i] / max - dp2px(Config.BOTTOM_TITLE_SPACE));
        }
    }

    private int dp2px(int size) {
        return (int) (size * getResources().getDisplayMetrics().density);
    }

    private float getMax(float[] nums) {
        float max = nums[0];
        for (float n : nums) {
            max = max > n ? max : n;
        }
        return max;
    }

    private void drawTitles(Canvas canvas) {
        paint.setColor(Color.BLACK);
        for (int i = firstVisiableIndex; i < lastVisiableIndex; i++) {
            canvas.drawText(titles[i], i * interval, height - Config.BOTTOM_TITLE_SPACE / 2, paint);
        }
        int index = getSelectedIndex();
        paint.setColor(Config.TEXT_COLOR);
        canvas.drawText(titles[index], index * interval, height - Config.BOTTOM_TITLE_SPACE / 2, paint);
    }

    private void computeVisiableIndex() {
        firstVisiableIndex = getScrollX() / interval;
        firstVisiableIndex = firstVisiableIndex < 0 ? 0 : firstVisiableIndex;
        lastVisiableIndex = firstVisiableIndex + Config.FIX_COLUMN + 2;
        lastVisiableIndex = lastVisiableIndex > titles.length ? titles.length : lastVisiableIndex;
    }

    public void setData(String[] titles, final float[] data) {
        this.titles = titles;
        this.data = data;
        leftScrollBoder = -width / 2;
        rightScrollBoder = (data.length - 1) * interval - width / 2;
        post(new Runnable() {
            @Override
            public void run() {
                scrollToIndex(data.length - 1);
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        interval = width / Config.FIX_COLUMN;
        leftScrollBoder = -width / 2;
        rightScrollBoder = interval * (data.length - 1) - width / 2;
        computeTopPosition();
    }

    public void setActionListener(OnActionListener clickListener) {
        this.actionListener = clickListener;
    }

    public interface OnActionListener {
        void onItemClick(int index);

        void onItemSelected(int index);
    }

    static class Config {
        public static final int TEXT_COLOR = Color.parseColor("#0086D1");
        public static final int TEXT_SIZE = 15;
        public static final int BOTTOM_TITLE_SPACE = 30;
        public static final int TOP_DATA_TEXT_SPACE = 30;
        public static final int SHADE_COLOR = Color.parseColor("#220086D1");
        public static final int DOT_COLOR = TEXT_COLOR;
        public static final int DOT_RADIUS = 3;
        public static final int FIX_COLUMN = 6;
    }

}
