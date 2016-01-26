package com.taihe.template.app.tmplate.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.taihe.template.app.tmplate.fragment.ScrollableTabFragment;

public class ScrollableTabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null!=getSupportActionBar()){
            getSupportActionBar().hide();
        }
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new ScrollableTabFragment()).commit();
    }

}
