package com.taihe.template.app.mvp;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.taihe.template.app.R;
import com.taihe.template.app.base.AppBaseActivity;
import com.taihe.template.app.mvp.model.StudentProfileModel;
import com.taihe.template.app.mvp.presenter.StudentProfilePresenter;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;

/**
 * Created by Administrator on 2016/1/27.
 */
@Layout(R.layout.activity_mvp_demo)
public class MvpDemoActivity extends AppBaseActivity implements StudentProfilePresenter.IView {

    @Id(R.id.tv_name)
    private TextView tvName;
    @Id(R.id.tv_sex)
    private TextView tvSex;
    @Id(R.id.tv_age)
    private TextView tvAge;
    @Id(R.id.iv_icon)
    private ImageView ivIcon;
    private StudentProfilePresenter spp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spp = new StudentProfilePresenter(this, new StudentProfileModel());
    }

    @Override
    public void set(String name, final int age, final int sex, String iconUrl) {
        tvName.setText(name);
        fadeOut(tvName);
        postDelay(new Runnable() {
            @Override
            public void run() {
                tvAge.setText("" + age);
                fadeOut(tvAge);
            }
        }, 250);
        postDelay(new Runnable() {
            @Override
            public void run() {
                tvSex.setText(sex == 1 ? "男" : "女");
                fadeOut(tvSex);
            }
        }, 500);
        Glide.with(this).load(iconUrl).into(ivIcon);
    }

    private void fadeOut(View v){
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        v.startAnimation(alphaAnimation);
    }
}
