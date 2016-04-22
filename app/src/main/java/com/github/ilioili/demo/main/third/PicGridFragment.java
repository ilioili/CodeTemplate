package com.github.ilioili.demo.main.third;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.ilioili.demo.base.AppBaseFragment;
import com.github.ilioili.demo.R;
import com.taihe.template.base.util.TestPicUrls;
import com.github.ilioili.widget.FixRatioImageView;

/**
 * Created by Administrator on 2016/1/9.
 */
public class PicGridFragment extends AppBaseFragment {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == recyclerView) {
            recyclerView = new RecyclerView(getContext());
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(new RecyclerView.Adapter<Holder>() {
                @Override
                public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                    FixRatioImageView imageView = new FixRatioImageView(getContext());
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    imageView.setRatio(0.75f, true);
                    return new Holder(imageView);
                }

                @Override
                public void onBindViewHolder(Holder holder, int position) {
                    Glide.with(getContext())
                            .load(TestPicUrls.URLS[position])
                            .placeholder(R.drawable.ic_launcher)
                            .error(R.drawable.ic_launcher)
                            .into(holder.imageView);
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

        public Holder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView;
        }
    }
}
