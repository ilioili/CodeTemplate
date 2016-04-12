package com.github.ilioili.demo.ui.frame;

import android.os.Bundle;

import com.github.ilioili.demo.R;
import com.github.ilioili.demo.base.AppBaseActivity;
import com.taihe.template.base.injection.Layout;

@Layout(R.layout.activity_banner)
public class BannerActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        getSupportFragmentManager().beginTransaction().add(R.id.banner_container, new BannerFragment()).commit();
    }

}
