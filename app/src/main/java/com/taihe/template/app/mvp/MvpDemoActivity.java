package com.taihe.template.app.mvp;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilioili.appstart.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
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
        Picasso.with(this).load(iconUrl).into(ivIcon, new Callback() {
            @Override
            public void onSuccess() {
                AnimationSet animationSet = new AnimationSet(false);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                ScaleAnimation scaleAnimation = new ScaleAnimation(3, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_PARENT, 1);
                animationSet.setDuration(1500);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(scaleAnimation);
                animationSet.setFillAfter(true);
                ivIcon.startAnimation(animationSet);
            }

            @Override
            public void onError() {

            }
        });
    }

    private void fadeOut(View v){
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        v.startAnimation(alphaAnimation);
    }
}
