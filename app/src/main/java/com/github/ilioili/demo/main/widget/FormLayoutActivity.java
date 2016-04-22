package com.github.ilioili.demo.main.widget;

import android.content.Context;
import android.content.Intent;

import com.github.ilioili.demo.R;
import com.github.ilioili.demo.base.AppBaseActivity;
import com.taihe.template.base.injection.Layout;

/**
 * Created by Administrator on 2016/2/17.
 */
@Layout(R.layout.activity_effencial_viewgroup)
public class FormLayoutActivity extends AppBaseActivity {
    public static Intent newIntent(Context context) {
        return new Intent(context, FormLayoutActivity.class);
    }
}
