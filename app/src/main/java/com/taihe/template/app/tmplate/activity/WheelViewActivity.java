package com.taihe.template.app.tmplate.activity;

import android.os.Bundle;

import com.taihe.template.app.base.AppBaseActivity;
import com.taihe.template.app.tmplate.fragment.RecyclerWheelViewFragment;

public class WheelViewActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new RecyclerWheelViewFragment()).commit();
    }
}
