package com.github.ilioili.demo.ui.frame;

import android.os.Bundle;

import com.github.ilioili.demo.base.AppBaseActivity;
import com.taihe.template.base.util.StatusBarUtil;

public class ScrollableTabActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBarUtil.setImmersedWindow(getWindow());
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new ScrollableTabFragment()).commit();
    }

}
