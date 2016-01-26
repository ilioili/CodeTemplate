package com.taihe.template.app.tmplate.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.taihe.template.app.tmplate.fragment.WheelViewFragment;
import com.taihe.template.base.BaseActivity;

/**
 * Created by Administrator on 2016/1/25.
 */
public class FragmentTestActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrollView scrollView = new ScrollView(this);
        LinearLayout linearLayout = new LinearLayout(this);
        scrollView.addView(linearLayout);
        addFragment(linearLayout, new WheelViewFragment());
        setContentView(scrollView);
    }

    public void addFragment(LinearLayout linearLayout, final Fragment fragment){
        Button button = new Button(this);
        button.setText(fragment.getClass().getName());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                ((ViewGroup)findViewById(android.R.id.content)).removeAllViews();
                fm.beginTransaction().replace(android.R.id.content, fragment).commit();
            }
        });
        linearLayout.addView(button);
    }
}
