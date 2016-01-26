package com.taihe.template.app.tmplate.selectfile;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilioili.appstart.R;
import com.taihe.template.app.base.AppBaseActivity;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;

import java.io.File;
import java.util.HashMap;

@Layout(R.layout.activity_file_select)
public class FileSelectActivity extends AppBaseActivity implements FileListFragment.OnFileItemOptionListener {
    private static final String ACTION_SELECT_SINGLE_FILE = "action_select_single_file";
    private static final String ACTION_SELECT_MULTIPLE_FILE = "action_select_multi_file";
    private static final String ACTION_SELECT_SINGLE_DIR = "action_select_single_dir";
    private static final String ACTION_SELECT_MULTIPLE_DIR = "action_select_multiple_dir";


    @Id(R.id.tabScrollView)
    private HorizontalScrollView horizontalScrollView;
    @Id(R.id.root_dir)
    private View vRootDir;
    @Id(R.id.tab_container)
    private LinearLayout llTabContainer;
    private FileListFragment rootFragment;
    private HashMap<File, View> cacheMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vRootDir.setOnClickListener(this);
        vRootDir.setClickable(true);
        rootFragment = FileListFragment.newInstance(Environment.getExternalStorageDirectory().getPath());
        rootFragment.setOnFileItemClickListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, rootFragment).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.root_dir:
                llTabContainer.removeAllViews();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, rootFragment).commit();
                break;
        }
    }

    @Override
    public void onFileItemClick(File file) {
        if (file.isDirectory()) {
            View tabView = cacheMap.get(file);
            if (null == tabView) {
                tabView = LayoutInflater.from(this).inflate(R.layout.file_select_tab, horizontalScrollView, false);
                TextView tvDir = f(R.id.tv_dir, tabView);
                tvDir.setText(file.getName());
                FileListFragment fileListFragment = FileListFragment.newInstance(file.getPath());
                fileListFragment.setOnFileItemClickListener(this);
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

    @Override
    public void onFileItemSelected(File file) {

    }

    @Override
    public void onFileItemRemoveFromSelected(File file) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void postScrollToEnd() {
        postWhenIdle(new Runnable() {
            @Override
            public void run() {
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });
    }
}
