package com.github.ilioili.demo.ui.canvas;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

import com.github.ilioili.widget.CircleAnimationFrame;
import com.github.ilioili.demo.R;
import com.github.ilioili.demo.base.AppBaseFragment;
import com.taihe.template.base.injection.Click;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;

/**
 * Created by Administrator on 2016/3/2.
 */
@Layout(R.layout.corner_layout)
public class CornerAnimationFragment extends AppBaseFragment {
    private int duration;
    @Id(R.id.corner)
    private CircleAnimationFrame animationViewGroup;

    @Click(R.id.collapse_to_point_in_window)
    private void collapseToPointInWindow(View v) {
        animationViewGroup.collapse(true, 0, 0, duration, true);
    }

    @Click(R.id.collapse_to_center)
    private void collpase(View v) {
        animationViewGroup.collapse(CircleAnimationFrame.Gravity.Center, duration, true);
    }

    @Click(R.id.collapse_to_right_bottom)
    private void collpaseToRightBottom(View v) {
        animationViewGroup.collapse(CircleAnimationFrame.Gravity.RightBottom, duration, false);
    }

    @Click(R.id.expand_from_center)
    private void expand(View v) {
        animationViewGroup.expand(CircleAnimationFrame.Gravity.Center, duration, Color.BLUE);
    }

    @Click(R.id.expand_from_left_top)
    private void expandFromLeftTop(View v) {
        animationViewGroup.setInterpolator(new BounceInterpolator(),  new LinearInterpolator());
        animationViewGroup.expand(CircleAnimationFrame.Gravity.LeftTop, duration);
    }

    @Click(R.id.expand_from_point_in_window)
    private void expandFromPointInWindow(View v) {
        animationViewGroup.expand(true, 0, 0, duration);
    }

    @Click(R.id.expand_from_point_in_window_with_forground_color)
    private void expandWithForgroundColorCover(View v) {
        animationViewGroup.expand(CircleAnimationFrame.Gravity.Center, duration, Color.RED);
    }

    @Override
    protected void init(View rootView) {
        super.init(rootView);
        duration = getResources().getInteger(android.R.integer.config_longAnimTime);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
