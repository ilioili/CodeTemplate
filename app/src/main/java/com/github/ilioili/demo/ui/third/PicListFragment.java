package com.github.ilioili.demo.ui.third;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.ilioili.widget.CircleAnimationFrame;
import com.github.ilioili.demo.R;
import com.github.ilioili.demo.base.AppBaseFragment;
import com.taihe.template.base.util.TestPicUrls;
import com.github.ilioili.widget.FixRatioImageView;

/**
 * Created by Administrator on 2016/1/9.
 */
public class PicListFragment extends AppBaseFragment {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == recyclerView) {
            recyclerView = new RecyclerView(getContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new RecyclerView.Adapter<Holder>() {
                @Override
                public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                    CircleAnimationFrame circleAnimationFrame = new CircleAnimationFrame(getContext());
                    FixRatioImageView imageView = new FixRatioImageView(getContext());
                    circleAnimationFrame.addView(imageView);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    circleAnimationFrame.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    imageView.setRatio(0.75f, true);
                    return new Holder(circleAnimationFrame);
                }

                @Override
                public void onBindViewHolder(final Holder holder, int position) {
                    Glide.with(getContext())
                            .load(TestPicUrls.URLS[position])
                            .placeholder(R.drawable.ic_launcher)
                            .error(R.drawable.ic_launcher)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    holder.circleAnimationFrame.expand(CircleAnimationFrame.Gravity.Center, 2500);
                                    return false;
                                }
                            }).into(holder.imageView);
                }

                @Override
                public int getItemCount() {
                    return TestPicUrls.URLS.length;
                }
            });
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.set(5, 5, 5, 5);
                }
            });
        }

        return recyclerView;
    }

    class Holder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private CircleAnimationFrame circleAnimationFrame;

        public Holder(View itemView) {
            super(itemView);
            circleAnimationFrame = (CircleAnimationFrame) itemView;
            imageView = (ImageView) circleAnimationFrame.getChildAt(0);
        }
    }
}
