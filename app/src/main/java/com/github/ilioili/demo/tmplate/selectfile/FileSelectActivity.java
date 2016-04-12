package com.github.ilioili.demo.tmplate.selectfile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ilioili.demo.R;
import com.github.ilioili.demo.base.AppBaseActivity;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

@Layout(R.layout.activity_file_select)
public class FileSelectActivity extends AppBaseActivity implements FileListFragment.OnFileItemOptionListener {
    private static final String INTENT_KEY_DIR = "INTENT_KEY_DIR";
    private static final String INTENT_KEY_LIMIT = "INTENT_KEY_LIMIT";
    private static final String INTENT_KEY_SHOW_HIDEN_FILE = "INTENT_KEY_SHOW_HIDEN_FILE";
    private static final String INTENT_KEY_ACTION = "INTENT_KEY_ACTION";

    @Id(R.id.tabScrollView)
    private HorizontalScrollView horizontalScrollView;
    @Id(R.id.root_dir)
    private View vRootDir;
    @Id(R.id.tab_container)
    private LinearLayout llTabContainer;
    private FileListFragment rootFragment;
    private HashMap<File, View> cacheMap = new HashMap<>();
    private HashSet<File> selectFiles = new HashSet<>();

    /**
     * @param action         {@link Action}
     * @param limit
     * @param showHidenFiles 是否显示隐藏的文件夹
     * @param dir            if null ，Environment.getExternalStorageDirectory() will be set as a default value
     * @return
     */
    public static Intent newIntent(Context context, Action action, int limit, @Nullable File dir, boolean showHidenFiles) {
        if (limit < 1) {
            throw new IllegalArgumentException("limit must > 0");
        }
        if (null == dir) {
            dir = Environment.getExternalStorageDirectory();
        }
        Intent it = new Intent(context, FileSelectActivity.class);
        it.putExtra(INTENT_KEY_LIMIT, limit);
        it.putExtra(INTENT_KEY_ACTION, action.name());
        it.putExtra(INTENT_KEY_DIR, dir.getPath());
        it.putExtra(INTENT_KEY_SHOW_HIDEN_FILE, showHidenFiles);
        return it;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.root_dir:
                llTabContainer.removeAllViews();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, rootFragment).commit();
                break;
        }
    }

    private FileListFragment getFragment(String path){
        FileListFragment fragment = FileListFragment.newInstance(path);
        fragment.setOnFileItemClickListener(this);
        fragment.setSelectFileSet(selectFiles);
        fragment.setShowHinden(getIntent().getBooleanExtra(INTENT_KEY_SHOW_HIDEN_FILE, true));
        String action = getIntent().getStringExtra(INTENT_KEY_ACTION);
        int limit = getIntent().getIntExtra(INTENT_KEY_LIMIT, Integer.MAX_VALUE);
        if(Action.SELECT_DIR.name().equals(action)){
            fragment.setActionMode(FileListFragment.Action.SELECT_DIR, limit);
        }else if(Action.SELECT_FILE.name().equals(action)){
            fragment.setActionMode(FileListFragment.Action.SELECT_FILE, limit);
        }else if(Action.SELECT_DIR_OR_FILE.name().equals(action)){
            fragment.setActionMode(FileListFragment.Action.SELECT_DIR_AND_FILE, limit);
        }else{
            fragment.setActionMode(FileListFragment.Action.VIEW, limit);
        }
        return fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vRootDir.setOnClickListener(this);
        vRootDir.setClickable(true);

        rootFragment = getFragment(getIntent().getStringExtra(INTENT_KEY_DIR));
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, rootFragment).commit();
    }

    @Override
    public void onFileItemClick(File file) {
        if (file.isDirectory()) {
            View tabView = cacheMap.get(file);
            if (null == tabView) {
                tabView = LayoutInflater.from(this).inflate(R.layout.file_select_tab, horizontalScrollView, false);
                TextView tvDir = f(R.id.tv_dir, tabView);
                tvDir.setText(file.getName());
                FileListFragment fileListFragment = getFragment(file.getPath());
                tabView.setTag(fileListFragment);
                cacheMap.put(file, tabView);
                tabView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = llTabContainer.getChildCount() - 1; i > llTabContainer.indexOfChild(v); i--) {
                            llTabContainer.removeViewAt(i);
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, (Fragment) v.getTag()).commit();
                        postScrollToEnd();
                    }
                });
            }
            llTabContainer.addView(tabView);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, (Fragment) tabView.getTag()).commit();
            postScrollToEnd();
        }
    }

    private void postScrollToEnd() {
        postWhenIdle(new Runnable() {
            @Override
            public void run() {
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });
    }

    public enum Action {
        SELECT_DIR,
        SELECT_FILE,
        SELECT_DIR_OR_FILE,
        VIEW
    }
}
