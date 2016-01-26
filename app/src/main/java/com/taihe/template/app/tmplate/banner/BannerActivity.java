package com.taihe.template.app.tmplate.banner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ilioili.appstart.R;
import com.taihe.template.app.tmplate.fragment.BannerFragment;
import com.taihe.template.base.injection.Layout;

@Layout(R.layout.activity_banner)
public class BannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        getSupportFragmentManager().beginTransaction().add(R.id.banner_container, new BannerFragment()).commit();
    }

}
