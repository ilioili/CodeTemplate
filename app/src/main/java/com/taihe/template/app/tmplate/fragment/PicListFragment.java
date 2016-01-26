package com.taihe.template.app.tmplate.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;

import com.taihe.template.app.base.AppBaseFragment;
import com.ilioili.appstart.R;
import com.squareup.picasso.Picasso;
import com.taihe.template.base.util.TestPicUrls;
import com.taihe.template.base.widget.FixRatioImageView;

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
                    FixRatioImageView imageView = new FixRatioImageView(getContext());
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    imageView.setRatio(0.75f, true);
                    return new Holder(imageView);
                }

                @Override
                public void onBindViewHolder(Holder holder, int position) {
                    Picasso.with(getContext())
                            .load(TestPicUrls.URLS[position])
                            .resize(800, 600)
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

        Gallery gallery;
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
