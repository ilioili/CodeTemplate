package com.taihe.template.app.ui.viewtest;

import android.os.Bundle;

import com.taihe.template.app.base.AppBaseActivity;

public class WheelViewActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new RecyclerWheelViewFragment()).commit();
    }
}
