package com.taihe.template.app.tmplate.activity;

import android.os.Bundle;

import com.taihe.template.app.base.AppBaseActivity;
import com.taihe.template.app.ui.canvas.FormLineFragment;

/**
 * Created by Administrator on 2016/1/25.
 */
public class FragmentTestActivity extends AppBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new FormLineFragment()).commit();
    }

}
