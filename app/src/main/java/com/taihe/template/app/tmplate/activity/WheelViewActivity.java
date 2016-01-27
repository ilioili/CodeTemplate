package com.taihe.template.app.tmplate.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.taihe.template.app.tmplate.fragment.RecyclerWheelViewFragment;

public class WheelViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new RecyclerWheelViewFragment()).commit();
    }
}
