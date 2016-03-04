package com.taihe.template.app.widget;

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
    private Paint paint;
    private Path path;
    private String titles[] = new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "9月", "10月", "11月", "12月","1月", "2月", "3月", "4月", "5月", "6月", "7月", "9月", "10月", "11月", "12月"};
    private int[] data = new int[]{0, 100, 300, 200, 500, 0,50, 800, 500, 200, 400, 100,0, 100, 300, 200, 500, 0,50, 800, 500, 200, 400, 100};
    private int[] tops;

    private Scroller scroller;
    private GestureDetector gestureDetector;
    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            scrollBy((int) distanceX, 0);
            if(getScrollX()<leftScrollBoder){
                scrollTo(leftScrollBoder, 0);
            }else if(getScrollX()>rightScrollBoder){
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

    public FormLineView(Context context) {
        this(context, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean handled = gestureDetector.onTouchEvent(event);
        if(!handled && event.getAction()==MotionEvent.ACTION_UP){
            moveToIdlePosition();
        }
        return true;
    }

    private void moveToIdlePosition() {
        //TODO 滑动到合适的位置
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
        scroller = new Scroller(context, new DecelerateInterpolator());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawTitles(canvas);//绘制底部标签
        drawPathShade(canvas);//绘制曲线围成的区域
        drawPoint(canvas);//绘制所有的数据点
        drawSelectData(canvas);//当前聚焦的数据
        drawCenterLine(canvas);//绘制中线
    }

    private void drawCenterLine(Canvas canvas) {
        PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);
        paint.setPathEffect(effects);
        int yPos = (height-dp2px(Config.BOTTOM_TITLE_SPACE+Config.TOP_DATA_TEXT_SPACE))/2+Config.TOP_DATA_TEXT_SPACE;
        canvas.drawLine(0, yPos, interval*data.length, yPos, paint);
        paint.setPathEffect(null);
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(), 0);
        }
    }

    private int getCenterIndex(){//TODO
        return 0;
    }

    private void drawSelectData(Canvas canvas) {
        int index = (width/2+getScrollX()+interval/2)/interval;
        int xPos = index * interval;
        canvas.drawLine(xPos,height-dp2px(Config.BOTTOM_TITLE_SPACE), xPos,tops[index], paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(xPos, tops[index], dp2px(Config.DOT_RADIUS)*4, paint);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        canvas.drawCircle(xPos, tops[index], dp2px(Config.DOT_RADIUS)*3, paint);

        paint.setColor(Config.DOT_COLOR);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(index * interval, tops[index], dp2px(Config.DOT_RADIUS), paint);
    }

    private void drawPoint(Canvas canvas) {
        paint.setColor(Config.DOT_COLOR);
        paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < data.length; i++) {
            canvas.drawCircle(i * interval, tops[i], dp2px(Config.DOT_RADIUS), paint);
            if (i + 1 < data.length)
                canvas.drawLine(i * interval, tops[i], (i + 1) * interval, tops[i + 1], paint);
        }
    }

    private void drawPathShade(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        path.reset();
        path.moveTo(0, height - dp2px(Config.BOTTOM_TITLE_SPACE));

        for (int i = 0; i < data.length; i++) {
            path.lineTo(i * interval, tops[i]);
        }
        path.lineTo(data.length*interval-interval, height-dp2px(Config.BOTTOM_TITLE_SPACE));
        paint.setColor(Config.SHADE_COLOR);
        canvas.drawPath(path, paint);
    }

    private void computeTopPosition() {
        int max = getMax(data);
        tops = new int[data.length];
        int maxHeight = height - dp2px(Config.TOP_DATA_TEXT_SPACE + Config.BOTTOM_TITLE_SPACE);
        for (int i = 0; i < tops.length; i++) {
            tops[i] = height - maxHeight * data[i] / max - dp2px(Config.BOTTOM_TITLE_SPACE);
        }
    }

    private int dp2px(int size) {
        return (int) (size * getResources().getDisplayMetrics().density);
    }


    private int getMax(int[] nums) {
        int max = nums[0];
        for (int n : nums) {
            max = max > n ? max : n;
        }
        return max;
    }


    private void drawTitles(Canvas canvas) {
        paint.setColor(Config.TEXT_COLOR);
        for (int i = 0; i < titles.length; i++) {
            canvas.drawText(titles[i], i * interval, height - Config.BOTTOM_TITLE_SPACE / 2, paint);
        }
    }

    public void setData(String[] titles, int[] data) {
        this.titles = titles;
        this.data = data;
        leftScrollBoder = -width/2;
        rightScrollBoder = (data.length-1)*interval-width/2;
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
        leftScrollBoder = -width/2;
        rightScrollBoder = interval*(data.length-1)-width/2;
        computeTopPosition();
    }

    static class Config {
        public static final int TEXT_COLOR = Color.parseColor("#005555");
        public static final int TEXT_SIZE = 15;
        public static final int BOTTOM_TITLE_SPACE = 30;
        public static final int TOP_DATA_TEXT_SPACE = 30;
        public static final int SHADE_COLOR = Color.parseColor("#22005555");
        public static final int DOT_COLOR = TEXT_COLOR;
        public static final int DOT_RADIUS = 2;
        public static final int FIX_COLUMN = 6;
    }
    public void setOnItemClickListener(OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }
    private OnItemClickListener clickListener;
    public interface OnItemClickListener {
        void onItemClick(int index);
    }

}
