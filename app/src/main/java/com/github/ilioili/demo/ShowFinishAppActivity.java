package com.github.ilioili.demo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.ilioili.demo.R;
import com.github.ilioili.demo.base.AppBaseActivity;
import com.github.ilioili.demo.tmplate.activity.NavigationDrawerActivity;

public class ShowFinishAppActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_finish_app);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "长按我退出应用", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
        fab1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                exitApp(ShowFinishAppActivity.this);
                return true;
            }
        });
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "长按我退回到主界面", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(NavigationDrawerActivity.newIntent(ShowFinishAppActivity.this, NavigationDrawerActivity.Action.EXIT_TO_TOP));
                return true;
            }
        });
    }

}
