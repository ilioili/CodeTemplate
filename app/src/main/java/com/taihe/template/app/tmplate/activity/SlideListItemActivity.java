package com.taihe.template.app.tmplate.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SlideListItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new SlideListItemFragment()).commit();
    }
}
