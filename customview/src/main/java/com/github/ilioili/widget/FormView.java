package com.github.ilioili.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;



/**
 * Created by ilioili on 2015/4/1.
 */
public class FormView extends ViewGroup implements Observer {
    private LinearLayout columnDefineLayout;
    private Adapter adapter;
    private View[][] cells;
    private int[] cellsLeftPositions;
    private ArrayList<View> firstLineViewList;
    private LinkedList<View>[] viewRecycler;
    private int firstLineHeight;
    private int firstLineWidth;
    private int minAdjustDistance = 10;

    private int columnNum;
    private int lineNum = 1;
    private int itemHeightSpec;
    private int[] itemWidthSpecs;
    private SparseArray<Boolean> lineSelectState = new SparseArray<>(100);

    private int itemHeight;
    private Rect bound = new Rect();
    private Scroller scroller;
    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                if (Math.abs(velocityX) < 1000) {
                    return false;
                } else {
                    scroller.fling(offsetX, offsetY, (int) -velocityX, 0, 0, bound.right, 0, bound.bottom);
                }

            } else {
                if (Math.abs(velocityY) < 1000) {
                    return false;
                } else {
                    scroller.fling(offsetX, offsetY, 0, (int) -velocityY, 0, bound.right, 0, bound.bottom);
                }
            }
            postInvalidate();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            modifyOffset((int) (offsetX + distanceX), (int) (offsetY + distanceY));
            resoveChild();
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            scroller.forceFinished(true);
            return true;
        }


        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (null != onCellClickListener) {
                int x = (int) e.getX() + offsetX;
                int y = (int) e.getY() + offsetY;
                int line = 0;
                if (y > firstLineHeight) {
                    line = (y - firstLineHeight) / itemHeight + 1;
                }
                int column = 0;
                for (int c = 0; c < columnNum - 1; c++) {
                    if (x >= cellsLeftPositions[c] && x < cellsLeftPositions[c + 1]) {
                        column = c;
                        break;
                    }
                }
                if (column == 0 && x != 0) {
                    column = columnNum - 1;
                }
                if (y < cells[0][0].getBottom()) {
                    line = 0;
                }
                if (x < cells[0][0].getRight()) {
                    column = 0;
                }
                if (line < lineNum && column < columnNum) {
                    onCellClickListener.onCellClick(line, column, cells[line][column]);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }


    };
    private int firstVisibleLine;
    private int lastVisibleLine;
    private int firstVisibleColumn;
    private int lastVisibleColumn;


    public View getCell(int line, int colum) {
        return cells[line][colum];
    }

    //修改偏移量
    private void modifyOffset(int nextX, int nextY) {
        int toBound = 0;
        if (bound.right > getMeasuredWidth()) {
            if (nextX < bound.left) {
                toBound++;
                offsetX = 0;
            } else if (nextX > bound.right - getMeasuredWidth()) {
                offsetX = bound.right - getMeasuredWidth();
                toBound++;
            } else {
                offsetX = nextX;
            }
        }

        if (bound.bottom > getMeasuredHeight()) {
            if (nextY < bound.top) {
                toBound++;
                offsetY = bound.top;
            } else if (nextY > bound.bottom - getMeasuredHeight()) {
                if (null != onScrollToBottomListener && offsetY != bound.bottom - getMeasuredHeight()) {
                    onScrollToBottomListener.onScrollToBottom();
                }
                offsetY = bound.bottom - getMeasuredHeight();
                toBound++;
            } else {
                offsetY = nextY;
            }
        }

        if (toBound == 2) {//两个边界都达到
            scroller.forceFinished(true);
        }
    }

    private GestureDetector gestureDetector;

    private int offsetX;
    private int offsetY;


    public FormView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FormView);
        itemHeight = (int) array.getDimension( R.styleable.FormView_item_height, 200); //提供默认值，放置未指定
        minAdjustDistance = (int) array.getDimension( R.styleable.FormView_min_adjust_distance, Integer.MAX_VALUE);
        gestureDetector = new GestureDetector(context, gestureListener);
        scroller = new Scroller(context, new DecelerateInterpolator());

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    private Rect rect = new Rect();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dispatchDownToTouchedChild(event);
                break;
            case MotionEvent.ACTION_UP:
                dispatchUpToTouchedChild(event);
                adjustPosition();
                break;
        }
        return gestureDetector.onTouchEvent(event);
    }

    private void dispatchDownToTouchedChild(MotionEvent event) {
        event.offsetLocation(offsetX, offsetY);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).getHitRect(rect);
            if (rect.contains((int) (event.getX()), (int) (event.getY()))) {
                event.offsetLocation(getChildAt(i).getLeft(), getChildAt(i).getTop());
                getChildAt(i).dispatchTouchEvent(event);
                event.offsetLocation(-getChildAt(i).getLeft(), -getChildAt(i).getTop());
            }
        }
        event.offsetLocation(-offsetX, -offsetY);
    }

    private void dispatchUpToTouchedChild(MotionEvent event) {
        event.offsetLocation(offsetX, offsetY);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).getHitRect(rect);
            event.offsetLocation(getChildAt(i).getLeft(), getChildAt(i).getTop());
            getChildAt(i).dispatchTouchEvent(event);
            event.offsetLocation(-getChildAt(i).getLeft(), -getChildAt(i).getTop());
        }
        event.offsetLocation(-offsetX, -offsetY);
    }

    public void selectLine(boolean select, int line) {
        for (int colum = 0; colum < columnNum; colum++) {
            if (null != cells[line][colum]) {
                cells[line][colum].setSelected(select);
            }
        }
        lineSelectState.put(line, select);
    }

    public void clearSelectState() {
        for (int line = 1; line < lineNum; line++) {
            lineSelectState.put(line, false);
            for (int colum = 0; colum < columnNum; colum++) {
                if (null != cells[line][colum]) {
                    cells[line][colum].setSelected(false);
                }
            }
        }
    }

    private void adjustPosition() {
        if (scroller.isFinished()) {
            int index = firstVisibleColumn;
            int dx1 = cellsLeftPositions[index + 1] - firstLineViewList.get(0).getMeasuredWidth() - offsetX;
            int dx2 = cellsLeftPositions[index + 2] - firstLineViewList.get(0).getMeasuredWidth() - offsetX;
            int dx = Math.abs(dx1) < Math.abs(dx2) ? dx1 : dx2;
            if (Math.abs(dx) > minAdjustDistance) {
                dx = 0;
            }
//                int dy = -offsetY;
            int dy1 = -((offsetY - firstLineHeight) % itemHeight) + (itemHeight - firstLineHeight);
            int dy2 = itemHeight + dy1;
            int dy = Math.abs(dy1) < Math.abs(dy2) ? dy1 : dy2;
            if (Math.abs(dy) > minAdjustDistance) {
                dy = 0;
            }
            scroller.startScroll(offsetX, offsetY, dx, dy, 200);
            postInvalidate();
        }
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        adapter.addObserver(this);
    }

    public FormView(Context context) {
        this(context, null);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        resoveChild();
    }

    private void resoveChild() {
        if (isDataReady()) {
            computCellVisiblity();
            layoutChildren();
        }
    }

    private View getCellView(int line, int column) {
        View v = viewRecycler[column].pollFirst();
        boolean needMeasure = v == null;
        v = adapter.getView(line, column, v);
        if (needMeasure) {
            v.measure(itemWidthSpecs[column], itemHeightSpec);
        }
        v.setSelected(lineSelectState.get(line, false));
        return v;
    }

    private void layoutChildren() {
        scrollTo(offsetX, offsetY);
        //左上角
        View v = cells[0][0];
        v.layout(offsetX, offsetY, offsetX + v.getMeasuredWidth(), offsetY + v.getMeasuredHeight());
        //标题行（除了左上角）
        for (int column = firstVisibleColumn == 0 ? 1 : firstVisibleColumn; column <= lastVisibleColumn; column++) {
            v = cells[0][column];
            v.layout(cellsLeftPositions[column], offsetY, cellsLeftPositions[column] + v.getMeasuredWidth(), offsetY + v.getMeasuredHeight());
        }
        int offset = firstLineHeight - itemHeight;
        //第一列（除了左上角）
        for (int line = firstVisibleLine == 0 ? 1 : firstVisibleLine; line <= lastVisibleLine; line++) {
            v = cells[line][0];
            v.layout(offsetX, itemHeight * line + offset, offsetX + v.getMeasuredWidth(), itemHeight * line + itemHeight + offset);
        }
        //其他（普通的表格内容）
        for (int line = firstVisibleLine == 0 ? 1 : firstVisibleLine; line <= lastVisibleLine; line++) {
            for (int column = firstVisibleColumn == 0 ? 1 : firstVisibleColumn; column <= lastVisibleColumn; column++) {
                v = cells[line][column];
                v.layout(cellsLeftPositions[column], itemHeight * line + offset, cellsLeftPositions[column] + v.getMeasuredWidth(), itemHeight * line + itemHeight + offset);
            }
        }

    }

    private boolean isDataReady() {
        return lineNum > 0 && cells != null;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        columnDefineLayout = (LinearLayout) getChildAt(0);
        if (null != columnDefineLayout) {
            setColumnParams();
        }
    }


    public void setColumnDefineLayout(LinearLayout linearLayout) {
        columnDefineLayout = linearLayout;
        setColumnParams();
    }

    private void setColumnParams() {
        int unknownMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        columnDefineLayout.measure(unknownMeasureSpec, MeasureSpec.makeMeasureSpec(columnDefineLayout.getLayoutParams().height, MeasureSpec.EXACTLY));

        columnNum = columnDefineLayout.getChildCount();
        firstLineHeight = columnDefineLayout.getMeasuredHeight();
        firstLineWidth = columnDefineLayout.getMeasuredWidth();
        firstLineViewList = new ArrayList(columnDefineLayout.getChildCount());
        cellsLeftPositions = new int[columnNum];
        itemWidthSpecs = new int[columnNum];
        viewRecycler = new LinkedList[columnNum];

        for (int i = 0; i < columnNum; i++) {
            View child = columnDefineLayout.getChildAt(i);
            itemWidthSpecs[i] = MeasureSpec.makeMeasureSpec(child.getMeasuredWidth(), MeasureSpec.EXACTLY);
            firstLineViewList.add(child);
            if (i == 0) {
                cellsLeftPositions[0] = 0;
            } else {
                cellsLeftPositions[i] = cellsLeftPositions[i - 1] + columnDefineLayout.getChildAt(i - 1).getMeasuredWidth();
            }
            viewRecycler[i] = new LinkedList<>();
        }

        itemHeightSpec = MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY);
        columnDefineLayout.removeAllViews();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }


    private void computCellVisiblity() {
        if (offsetY > firstLineHeight) {
            firstVisibleLine = (offsetY - firstLineHeight) / itemHeight + 1;
        }
        lastVisibleLine = (offsetY + getMeasuredHeight() - firstLineHeight) / itemHeight + 1;
        if (lastVisibleLine > lineNum - 1) {
            lastVisibleLine = lineNum - 1;
        }
        int rightBound = offsetX + (getMeasuredWidth() > firstLineWidth ? firstLineWidth : getMeasuredWidth());
        firstVisibleColumn = 0;
        lastVisibleColumn = columnNum - 1;
        for (int i = 1; i < columnNum; i++) {
            View view = firstLineViewList.get(i);
            if (cellsLeftPositions[i] + view.getMeasuredWidth() >= offsetX && cellsLeftPositions[i] <= offsetX) {
                firstVisibleColumn = i;
            }
            if (cellsLeftPositions[i] <= rightBound && view.getMeasuredWidth() + cellsLeftPositions[i] >= rightBound) {
                lastVisibleColumn = i;
            }
        }


        for (int line = 1; line < lineNum; line++) {
            for (int column = 0; column < columnNum; column++) {
                View v = cells[line][column];
                if (line >= firstVisibleLine && line <= lastVisibleLine && ((column >= firstVisibleColumn && column <= lastVisibleColumn) || column == 0)) {
                    //可见
                    if (null == v) {
                        v = cells[line][column] = getCellView(line, column);
                        addView(v, 0);
                    }
                } else {
                    //不可见
                    if (null != v && !(column == 0 && line >= firstVisibleLine && line <= lastVisibleLine)) {
                        viewRecycler[column].addLast(v);
                        cells[line][column] = null;
                        removeView(v);
                    }
                }
            }
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        lineNum = adapter.getLineNum() + 1;
        lineSelectState = new SparseArray<>(lineNum);
        bound.set(0, 0, firstLineWidth, firstLineHeight + itemHeight * adapter.getLineNum());
        cells = new View[lineNum][columnNum];

        removeAllViews();
        for (int column = 0; column < columnNum; column++) {
            View cell = firstLineViewList.get(column);
            cells[0][column] = cell;
            addView(cell, 0);
        }
//        offsetY = 0;
//        offsetX = 0;
        modifyOffset(offsetX, offsetY);
        requestLayout();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            modifyOffset(scroller.getCurrX(), scroller.getCurrY());
            resoveChild();
            postInvalidate();
        }
    }

    public int getColumnNum() {
        return columnNum;
    }

    /**
     * Created by ilioili on 2015/4/1.
     */
    public abstract static class Adapter extends Observable {
        public abstract View getView(int lineNum, int columnNum, View v);

        public abstract int getLineNum();

        @Override
        public void notifyObservers() {
            setChanged();
            super.notifyObservers();
        }
    }

    public interface OnCellClickListener {
        void onCellClick(int line, int column, View vCell);
    }

    public interface OnScrollToBottomListener {
        void onScrollToBottom();
    }

    private OnCellClickListener onCellClickListener;

    private OnScrollToBottomListener onScrollToBottomListener;

    public void setOnCellClickListener(OnCellClickListener onCellClickListener) {
        this.onCellClickListener = onCellClickListener;
    }

    public void setOnScrollToBottomListener(OnScrollToBottomListener onScrollToBottomListener) {
        this.onScrollToBottomListener = onScrollToBottomListener;
    }
}
