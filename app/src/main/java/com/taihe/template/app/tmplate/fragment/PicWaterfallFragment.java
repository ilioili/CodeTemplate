package com.taihe.template.app.tmplate.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.taihe.template.app.base.AppBaseFragment;
import com.ilioili.appstart.R;
import com.squareup.picasso.Picasso;
import com.taihe.template.base.util.TestPicUrls;
import com.taihe.template.base.widget.FixRatioImageView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2016/1/9.
 */
public class PicWaterfallFragment extends AppBaseFragment {

    private RecyclerView recyclerView;
    private Random random = new Random(System.currentTimeMillis());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == recyclerView) {
            recyclerView = new RecyclerView(getContext());
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            final ArrayList<Float> ratioList = new ArrayList<>();
            for(int i=0; i<TestPicUrls.URLS.length; i++){
                ratioList.add(random.nextFloat()+0.5f);
            }
            recyclerView.setAdapter(new RecyclerView.Adapter<Holder>() {
                @Override
                public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                    FixRatioImageView imageView = new FixRatioImageView(getContext());
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
                    return new Holder(imageView);
                }

                @Override
                public void onBindViewHolder(Holder holder, int position) {
                    holder.imageView.setRatio(ratioList.get(position), true);
                    Picasso.with(getContext())
                            .load(TestPicUrls.URLS[position])
                            .resize(500, (int) (500 * holder.imageView.getRatio()))
                            .centerCrop()
//                            .placeholder(R.drawable.ic_launcher)
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
        private FixRatioImageView imageView;

        public Holder(View itemView) {
            super(itemView);
            imageView = (FixRatioImageView) itemView;
        }
    }
}
