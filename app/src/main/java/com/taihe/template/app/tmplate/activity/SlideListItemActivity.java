package com.taihe.template.app.tmplate.activity;

import android.os.Bundle;

import com.taihe.template.app.base.AppBaseActivity;

public class SlideListItemActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new SlideListItemFragment()).commit();
    }
}
