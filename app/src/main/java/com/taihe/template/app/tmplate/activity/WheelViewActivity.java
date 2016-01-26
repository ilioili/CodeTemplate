package com.taihe.template.app.tmplate.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.taihe.template.app.tmplate.fragment.WheelViewFragment;

public class WheelViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new WheelViewFragment()).commit();
    }
}
