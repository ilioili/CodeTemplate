package com.taihe.template.app.tmplate.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.taihe.template.app.base.AppBaseFragment;
import com.taihe.template.base.util.TestPicUrls;
import com.taihe.template.base.widget.FixRatioImageView;

/**
 * Created by Administrator on 2016/1/9.
 */
public class BannerListFragment extends AppBaseFragment {

    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_ITEM = 1;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == recyclerView) {
            recyclerView = new RecyclerView(getContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new RecyclerView.Adapter<Holder>() {
                @Override
                public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
//                    switch (viewType){
//                        case VIEW_TYPE_HEADER:
//                            return
//                            break;
//                        case VIEW_TYPE_ITEM:
//                            break;
//                    }
                    FixRatioImageView imageView = new FixRatioImageView(getContext());
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    imageView.setRatio(0.75f, true);
                    return new Holder(imageView);
                }

                @Override
                public void onBindViewHolder(Holder holder, int position) {
//                    Picasso.with(getContext())
//                            .load(TestPicUrls.URLS[position])
//                            .resize(800, 600)
//                            .placeholder(R.drawable.ic_launcher)
//                            .error(R.drawable.ic_launcher)
//                            .into(holder.imageView);
                }

                @Override
                public int getItemCount() {
                    return TestPicUrls.URLS.length+1;
                }

                @Override
                public int getItemViewType(int position) {
                    if(position==0){
                        return VIEW_TYPE_HEADER;
                    }else{
                        return VIEW_TYPE_ITEM;
                    }
                }
            });
        }
        return recyclerView;
    }


    class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }
    }

}
