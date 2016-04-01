package com.github.ilioili.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/1/26.
 */
public class CircularRecyclerWheelView extends RecyclerView {

    public CircularRecyclerWheelView(Context context) {
        super(context);
    }

    public CircularRecyclerWheelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularRecyclerWheelView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
    }

    /**
     * @param horizontal             是否水平滑动
     * @param visiableItemCount      一个屏幕的宽度要显示View的个数
     * @param adapter
     * @param onItemSelectedListener
     */
    public void setAdapter(final boolean horizontal, final int visiableItemCount, final Adapter adapter, final OnScrollListener onItemSelectedListener) {
        setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
                if (horizontal) {
                    child.measure(View.MeasureSpec.makeMeasureSpec(getWidth() / visiableItemCount, View.MeasureSpec.EXACTLY), ViewGroup.LayoutParams.MATCH_PARENT);
                }else {
                    child.measure(widthUsed, View.MeasureSpec.makeMeasureSpec(getHeight() / visiableItemCount, View.MeasureSpec.EXACTLY));
                }
            }
        };
        layoutManager.setOrientation(horizontal?LinearLayoutManager.HORIZONTAL:LinearLayoutManager.VERTICAL);
        setLayoutManager(layoutManager);
        setAdapter(new Adapter() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewHolder holder = adapter.onCreateViewHolder(parent, viewType);
                holder.itemView.setClickable(true);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        smoothScrollBy((v.getLeft() + v.getRight() - getWidth()) / 2, 0);
                    }
                });
                return holder;
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                adapter.onBindViewHolder(holder, position % adapter.getItemCount());
            }

            @Override
            public int getItemCount() {
                return Integer.MAX_VALUE;
            }
        });
        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View centerView = getCerterView();
                    if (Math.abs(centerView.getLeft() + centerView.getRight() - getWidth()) < 2) {//允许有一个像素的偏差
                        onItemSelectedListener.onScrollStop(layoutManager.getPosition(centerView) % adapter.getItemCount(), centerView);
                    } else {
                        smoothScrollBy((centerView.getRight() + centerView.getLeft() - getWidth()) / 2, 0);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dx == 0 && dy == 0) {
                    smoothScrollBy(1, 0);
                }
                if (null != onItemSelectedListener) {
                    for (int i = 0; i < getChildCount(); i++) {
                        View child = getChildAt(i);
                        onItemSelectedListener.onScrolling(child, (child.getLeft() + child.getRight() - getWidth()) / 2, getWidth() / 2);
                    }
                }

            }
        });
        layoutManager.scrollToPosition(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % adapter.getItemCount());
    }

    private View getCerterView() {
        View view = getChildAt(0);
        int minDistance = Math.abs(view.getLeft() + view.getRight() - getWidth());
        for (int i = 1; i < getChildCount(); i++) {
            int abs = Math.abs(getChildAt(i).getLeft() + getChildAt(i).getRight() - getWidth());
            if (minDistance > abs) {
                view = getChildAt(i);
                minDistance = Math.abs(getChildAt(i).getLeft() + getChildAt(i).getRight() - getWidth());
            } else {
                break;
            }
        }
        return view;
    }

    public interface OnScrollListener {
        void onScrollStop(int position, View view);

        void onScrolling(View view, float offset, float maxOffset);
    }
}
