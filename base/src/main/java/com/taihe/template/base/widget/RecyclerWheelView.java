package com.taihe.template.base.widget;

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
public class RecyclerWheelView extends RecyclerView {

    public RecyclerWheelView(Context context) {
        super(context);
    }

    public RecyclerWheelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerWheelView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
    }

    /**
     * @param visiableItemCount      一个屏幕的宽度要显示View的个数
     * @param adapter
     * @param onItemSelectedListener
     */
    public void setAdapter(final int visiableItemCount, final Adapter adapter, final OnItemSelectedListener onItemSelectedListener) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
                child.measure(View.MeasureSpec.makeMeasureSpec(getWidth() / visiableItemCount, View.MeasureSpec.EXACTLY), ViewGroup.LayoutParams.MATCH_PARENT);
            }
        };
        final int offset = visiableItemCount / 2;
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(layoutManager);
        setAdapter(new Adapter() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewHolder holder = null;
                if (viewType == 1) {
                    View itemView = new View(getContext());
                    itemView.setVisibility(View.INVISIBLE);
                    holder = new ViewHolder(itemView) {
                        @Override
                        public String toString() {
                            return super.toString();
                        }
                    };
                } else {
                    holder = adapter.onCreateViewHolder(parent, viewType);
                    holder.itemView.setClickable(true);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            smoothScrollBy((v.getLeft() + v.getRight() - getWidth()) / 2, 0);
                        }
                    });
                }
                return holder;
            }

            @Override
            public int getItemViewType(int position) {
                if (position < offset || position > adapter.getItemCount() + offset - 1) {
                    return 1;
                } else {
                    return 2;
                }
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                if (getItemViewType(position) == 1) {

                } else {
                    adapter.onBindViewHolder(holder, position - offset);
                }
            }

            @Override
            public int getItemCount() {
                return adapter.getItemCount() + offset * 2;
            }
        });
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == SCROLL_STATE_IDLE) {
                    View centerView = getCerterView(offset);
                    if (Math.abs(centerView.getLeft() + centerView.getRight() - getWidth()) < 2) {//允许有一个像素的偏差
                        onItemSelectedListener.onItemSelected(layoutManager.getPosition(centerView) - offset, centerView);
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
            }
        });
    }

    private View getCerterView(int offset) {
        View view = getChildAt(0);
        int minDistance = Math.abs(view.getLeft() + view.getRight() - getWidth());
        for (int i = 1; i < getChildCount(); i++) {
            int abs = Math.abs(getChildAt(i).getLeft() + getChildAt(i).getRight() - getWidth());
            if (minDistance == abs) {
                if (getLayoutManager().getPosition(view) < offset) {
                    return getChildAt(i);
                } else {
                    return getChildAt(i - 1);
                }
            }
            if (minDistance > abs) {
                view = getChildAt(i);
                minDistance = Math.abs(getChildAt(i).getLeft() + getChildAt(i).getRight() - getWidth());
            } else {
                break;
            }
        }
        return view;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position, View view);
    }
}
