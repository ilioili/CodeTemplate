package com.taihe.template.app.tmplate.selectfile;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ilioili.appstart.R;
import com.taihe.template.base.BaseFragment;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Created by Administrator on 2016/1/13.
 */
public class FileListFragment extends BaseFragment {
    private static final String BUNDLE_KEY_PATH = "dir_path";
    private RecyclerView recyclerView;
    private File[] files;
    private Action action = Action.SELECT_DIR_AND_FILE;
    private HashSet selectFiles;
    private OnFileItemOptionListener onFileItemOptionListener;
    private int limit;
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (null != onFileItemOptionListener) {
                if (action != Action.VIEW && selectFiles.size() == limit) {
                    Snackbar snackbar = Snackbar.make(recyclerView, "最多选择" + limit + "个", Snackbar.LENGTH_LONG);
                    snackbar.setAction("取消已选", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectFiles.clear();
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    });
                    snackbar.show();
                } else {
                    onFileItemOptionListener.onFileItemClick((File) v.getTag());
                }
            }
        }

    };
    private View.OnClickListener onCheckListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (contains(v.getTag())) {
                selectFiles.remove(v.getTag());
            } else {
                if (selectFiles.size() < limit) {
                    selectFiles.add(v.getTag());
                } else {
                    Snackbar snackbar = Snackbar.make(recyclerView, "最多选择" + limit + "个", Snackbar.LENGTH_LONG);
                    snackbar.setAction("取消已选", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectFiles.clear();
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    });
                    snackbar.show();
                }
            }
            recyclerView.getAdapter().notifyItemChanged(v.getId());
        }
    };
    private boolean showHinden;

    public static FileListFragment newInstance(String dirPath) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_PATH, dirPath);
        FileListFragment fileListFragment = new FileListFragment();
        fileListFragment.setArguments(bundle);
        return fileListFragment;
    }

    private boolean contains(Object file) {
        if (null == selectFiles) {
            return false;
        } else {
            return selectFiles.contains(file);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public File[] getFiles() {
        String dirPath = getArguments().getString(BUNDLE_KEY_PATH);
        File dir = new File(dirPath);
        if (action == Action.SELECT_DIR) {
            return dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (showHinden) {
                        return f.isDirectory();
                    } else {
                        return f.getName().charAt(0) != '.' && f.isDirectory();
                    }
                }
            });
        } else {
            if (showHinden)
                return dir.listFiles();
            else
                return dir.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().charAt(0) != '.';
                    }
                });
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        if (null == recyclerView) {
            files = getFiles();
            sortFile();
            recyclerView = new RecyclerView(getContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new RecyclerView.Adapter<Holder>() {
                @Override
                public int getItemCount() {
                    return files.length;
                }

                @Override
                public void onBindViewHolder(Holder holder, int position) {
                    File file = files[position];
                    holder.tvFileName.setText(file.getName());
                    holder.itemView.setTag(file);
                    if (file.isDirectory()) {
                        if (file.list().length == 0)
                            holder.tvFileName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_folder_open_black_empty_24dp, 0, 0, 0);
                        else
                            holder.tvFileName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_folder_black_24dp, 0, 0, 0);
                    } else {
                        holder.tvFileName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file_black_24dp, 0, 0, 0);
                    }
                    if (action == Action.VIEW) {
                        holder.vCheck.setVisibility(View.GONE);
                    } else {
                        if (action == Action.SELECT_DIR) {
                            if (file.isDirectory()) {
                                holder.vCheck.setVisibility(View.VISIBLE);
                            } else {
                                holder.vCheck.setVisibility(View.GONE);
                            }
                        } else if (action == Action.SELECT_FILE) {
                            if (file.isFile()) {
                                holder.vCheck.setVisibility(View.VISIBLE);
                            } else {
                                holder.vCheck.setVisibility(View.GONE);
                            }
                        } else {
                            holder.vCheck.setVisibility(View.VISIBLE);
                        }
                        holder.vCheck.setId(position);
                        holder.vCheck.setTag(file);
                        holder.itemView.setSelected(contains(file));
                    }

                }

                @Override
                public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.adapter_file_item, parent, false));
                }
            });
        }
        return recyclerView;
    }

    public void setActionMode(Action action, int limit) {
        this.limit = limit;
        this.action = action;
    }

    public void setOnFileItemClickListener(OnFileItemOptionListener onFileItemClickListener) {
        this.onFileItemOptionListener = onFileItemClickListener;
    }

    public void setSelectFileSet(HashSet fileSet) {
        this.selectFiles = fileSet;
    }

    public void setShowHinden(boolean showHinden) {
        this.showHinden = showHinden;
    }

    public void sortFile() {
        Arrays.sort(files, 0, files.length, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                switch (action) {
                    case VIEW:
                        return 0;
                    case SELECT_DIR_AND_FILE:
                        return lhs.getName().compareTo(rhs.getName());
                    case SELECT_DIR:
                        if (lhs.isDirectory() && rhs.isDirectory() || lhs.isFile() && rhs.isFile()) {
                            return lhs.getName().compareTo(rhs.getName());
                        } else if (lhs.isDirectory()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    case SELECT_FILE:
                        if (lhs.isDirectory() && rhs.isDirectory() || lhs.isFile() && rhs.isFile()) {
                            return lhs.getName().compareTo(rhs.getName());
                        } else if (lhs.isDirectory()) {
                            return 1;
                        } else {
                            return -1;
                        }
                }
                return 0;
            }
        });
    }

    public enum Action {
        SELECT_FILE, SELECT_DIR, SELECT_DIR_AND_FILE, VIEW
    }

    public interface OnFileItemOptionListener {
        void onFileItemClick(File file);
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView tvFileName;
        View vCheck;


        public Holder(View itemView) {
            super(itemView);
            ViewGroup viewGroup = (ViewGroup) itemView;
            tvFileName = (TextView) viewGroup.getChildAt(0);
            vCheck = viewGroup.getChildAt(1);
            itemView.setOnClickListener(onItemClickListener);
            vCheck.setOnClickListener(onCheckListener);
        }
    }
}
