package com.taihe.template.app.ui.frame;

import android.os.Bundle;

import com.taihe.template.app.base.AppBaseActivity;
import com.taihe.template.base.util.StatusBarUtil;

public class ScrollableTabActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBarUtil.setImmersedWindow(getWindow());
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new ScrollableTabFragment()).commit();
    }

}
