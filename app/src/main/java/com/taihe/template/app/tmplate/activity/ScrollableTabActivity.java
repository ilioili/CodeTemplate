package com.taihe.template.app.tmplate.activity;

import android.os.Bundle;

import com.taihe.template.app.base.AppBaseActivity;
import com.taihe.template.app.tmplate.fragment.ScrollableTabFragment;

public class ScrollableTabActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null!=getSupportActionBar()){
            getSupportActionBar().hide();
        }
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new ScrollableTabFragment()).commit();
    }

}
