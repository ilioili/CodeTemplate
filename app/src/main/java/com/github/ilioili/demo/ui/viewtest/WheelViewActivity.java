package com.github.ilioili.demo.ui.viewtest;

import android.os.Bundle;

import com.github.ilioili.demo.base.AppBaseActivity;

public class WheelViewActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new RecyclerWheelViewFragment()).commit();
    }
}
