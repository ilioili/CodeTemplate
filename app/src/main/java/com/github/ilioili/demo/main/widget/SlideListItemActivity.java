package com.github.ilioili.demo.main.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.github.ilioili.demo.base.AppBaseActivity;

public class SlideListItemActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new SlideListItemFragment()).commit();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, SlideListItemActivity.class);
    }
}
