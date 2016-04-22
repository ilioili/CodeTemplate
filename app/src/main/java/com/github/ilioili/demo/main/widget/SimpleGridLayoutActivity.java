package com.github.ilioili.demo.main.widget;

import android.content.Context;
import android.content.Intent;

import com.github.ilioili.demo.R;
import com.github.ilioili.demo.base.AppBaseActivity;
import com.github.ilioili.widget.SimpleGridLayout;
import com.taihe.template.base.injection.Layout;

@Layout(R.layout.activity_simple_grid_layout)
public class SimpleGridLayoutActivity extends AppBaseActivity {

    public static Intent newIntent(Context context){
        Intent it = new Intent(context, SimpleGridLayoutActivity.class);
        return it;
    }

}
