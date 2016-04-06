package com.taihe.template.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.taihe.template.app.base.AppBaseActivity;
import com.taihe.template.base.injection.Click;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;
import com.taihe.template.base.util.ToastUtil;

@Layout(R.layout.activity_offical_ripple_animation_demo)
public class OfficalRippleAnimationDemo extends AppBaseActivity {

    @Id(R.id.animateView)
    private View vAnimatedView;

    @Click(R.id.btn_collapse)
    private void collapse(View v) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ToastUtil.showShortToast("版本太低，没效果");
        } else {
// previously visible view

// get the center for the clipping circle
            int cx = vAnimatedView.getWidth() / 2;
            int cy = vAnimatedView.getHeight() / 2;

// get the initial radius for the clipping circle
            float initialRadius = (float) Math.hypot(cx, cy);

// create the animation (the final radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(vAnimatedView, cx, cy, initialRadius, 0);

// make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    vAnimatedView.setVisibility(View.INVISIBLE);
                }
            });

// start the animation
            anim.start();
        }


    }

    @Click(R.id.btn_expand)
    private void expand(View v) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ToastUtil.showShortToast("版本太低，没效果");
        } else {
            vAnimatedView.setVisibility(View.VISIBLE);

// get the center for the clipping circle
            int cx = vAnimatedView.getWidth() / 2;
            int cy = vAnimatedView.getHeight() / 2;

// get the final radius for the clipping circle
            float finalRadius = (float) Math.hypot(cx, cy);

// create the animator for this view (the start radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(vAnimatedView, cx, cy, 0, finalRadius);

// make the view visible and start the animation
            vAnimatedView.setVisibility(View.VISIBLE);
            anim.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
