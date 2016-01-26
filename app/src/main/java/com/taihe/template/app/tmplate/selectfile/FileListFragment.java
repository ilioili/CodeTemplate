package com.taihe.template.app.tmplate.selectfile;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihe.template.base.BaseFragment;

import java.io.File;

/**
 * Created by Administrator on 2016/1/13.
 */
public class FileListFragment extends BaseFragment {
    private static final String BUNDLE_KEY_PATH = "dir_path";
    private RecyclerView recyclerView;
    private File[] files;
    private View.OnClickListener onItemClickListener;
    private OnFileItemOptionListener onFileItemClickListener;

    public static FileListFragment newInstance(String dirPath) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_PATH, dirPath);
        FileListFragment fileListFragment = new FileListFragment();
        fileListFragment.setArguments(bundle);
        return fileListFragment;
    }

    public void setOnFileItemClickListener(OnFileItemOptionListener listener){
        onFileItemClickListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == recyclerView) {
            files = getFiles();
            onItemClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onFileItemClickListener) {
                        onFileItemClickListener.onFileItemClick((File) v.getTag());
                    }
                }
            };
            recyclerView = new RecyclerView(getContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new RecyclerView.Adapter<Holder>() {
                @Override
                public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return new Holder(new TextView(getContext()));
                }

                @Override
                public void onBindViewHolder(Holder holder, int position) {
                    TextView tv = (TextView) holder.itemView;
                    tv.setText(files[position].getName());
                    holder.itemView.setTag(files[position]);
                }

                @Override
                public int getItemCount() {
                    return files.length;
                }
            });
        }
        return recyclerView;
    }

    public File[] getFiles() {
        String dirPath = getArguments().getString(BUNDLE_KEY_PATH);
        File dir = new File(dirPath);
        return dir.listFiles();
    }

    public interface OnFileItemOptionListener {
        void onFileItemClick(File file);
        void onFileItemSelected(File file);
        void onFileItemRemoveFromSelected(File file);
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView tvFileName;
        TextView updateTime;
        ImageView ivIcon;

        public Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(onItemClickListener);
            itemView.setPadding(20, 20, 20, 20);
        }
    }
}
