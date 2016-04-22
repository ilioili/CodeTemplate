package com.github.ilioili.demo.androidapitest;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.ilioili.demo.R;
import com.github.ilioili.demo.main.frame.ScrollableTabFragment;
import com.github.ilioili.demo.main.third.PicListFragment;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container_up, new PicListFragment());
        fragmentTransaction.add(R.id.container_down, new ScrollableTabFragment());
        fragmentTransaction.commit();
    }

}
