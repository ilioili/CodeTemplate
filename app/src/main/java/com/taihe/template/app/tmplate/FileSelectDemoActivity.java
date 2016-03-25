package com.taihe.template.app.tmplate;

import android.content.Intent;
import android.os.Environment;
import android.view.View;

import com.ilioili.appstart.R;
import com.taihe.template.app.base.AppBaseActivity;
import com.taihe.template.app.tmplate.selectfile.FileSelectActivity;
import com.taihe.template.base.injection.Click;
import com.taihe.template.base.injection.Layout;

import java.io.File;

@Layout(R.layout.activity_file_select_demo)
public class FileSelectDemoActivity extends AppBaseActivity {

    public static final int REQUEST_CODE_SELECT_SINGLE_FILE = 001;
    public File dir = Environment.getExternalStorageDirectory();

    @Click(R.id.selec_multiple_dir)
    private void selectMultipleDir(View v) {
        Intent intent = FileSelectActivity.newIntent(this, FileSelectActivity.Action.SELECT_DIR, 1, dir, true);
        startActivityForResult(intent, REQUEST_CODE_SELECT_SINGLE_FILE);
    }

    @Click(R.id.selec_multiple_file_or_dir)
    private void selectMultipleDirOrFile(View v) {
        Intent intent = FileSelectActivity.newIntent(this, FileSelectActivity.Action.SELECT_DIR_OR_FILE, 3, dir, true);
        startActivityForResult(intent, REQUEST_CODE_SELECT_SINGLE_FILE);
    }

    @Click(R.id.selec_multiple_file)
    private void selectMultipleFile(View v) {
        Intent intent = FileSelectActivity.newIntent(this, FileSelectActivity.Action.SELECT_FILE, 9, dir, true);
        startActivityForResult(intent, REQUEST_CODE_SELECT_SINGLE_FILE);
    }

    @Click(R.id.selec_single_dir)
    private void selectSingleDir(View v) {
        Intent intent = FileSelectActivity.newIntent(this, FileSelectActivity.Action.SELECT_DIR, 1, dir, true);
        startActivityForResult(intent, REQUEST_CODE_SELECT_SINGLE_FILE);
    }

    @Click(R.id.selec_single_file_or_dir)
    private void selectSingleDirOrFile(View v) {
        Intent intent = FileSelectActivity.newIntent(this, FileSelectActivity.Action.SELECT_DIR_OR_FILE, 1, dir, true);
        startActivityForResult(intent, REQUEST_CODE_SELECT_SINGLE_FILE);
    }

    @Click(R.id.selec_single_file)
    private void selectSingleFile(View v) {
        Intent intent = FileSelectActivity.newIntent(this, FileSelectActivity.Action.SELECT_FILE, 1, dir, true);
        startActivityForResult(intent, REQUEST_CODE_SELECT_SINGLE_FILE);
    }

    @Click(R.id.view_file)
    private void viewFile(View v) {
        Intent intent = FileSelectActivity.newIntent(this, FileSelectActivity.Action.VIEW, 1, dir, true);
        startActivityForResult(intent, REQUEST_CODE_SELECT_SINGLE_FILE);
    }

    @Click(R.id.view_file_with_hidden)
    private void viewFileWithoutHidden(View v) {
        Intent intent = FileSelectActivity.newIntent(this, FileSelectActivity.Action.VIEW, 1, dir, false);
        startActivityForResult(intent, REQUEST_CODE_SELECT_SINGLE_FILE);
    }
}
