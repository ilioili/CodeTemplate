package com.taihe.template.app.ui.canvas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.ilioili.widget.CircleAnimationFrame;
import com.ilioili.appstart.R;
import com.taihe.template.app.base.AppBaseFragment;
import com.taihe.template.base.injection.Click;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;

/**
 * Created by Administrator on 2016/3/2.
 */
@Layout(R.layout.corner_layout)
public class CornerAnimationFragment extends AppBaseFragment {
    @Id(R.id.corner)
    private CircleAnimationFrame animationViewGroup;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Click(R.id.expand_from_center)
    private void expand(View v) {
        animationViewGroup.expand(CircleAnimationFrame.Gravity.Center, 1500);
    }

    @Click(R.id.collapse_to_center)
    private void collpase(View v) {
        animationViewGroup.collpase(CircleAnimationFrame.Gravity.Center, 1500);
    }

    @Click(R.id.expand_from_left_top)
    private void expandFromLeftTop(View v) {
        animationViewGroup.expand(CircleAnimationFrame.Gravity.LeftTop, 500);
    }

    @Click(R.id.collapse_to_right_bottom)
    private void collpaseToRightBottom(View v) {
        animationViewGroup.collpase(CircleAnimationFrame.Gravity.RightBottom, 500);
    }

    @Click(R.id.expand_from_point_in_window)
    private void expandFromPointInWindow(View v) {
        animationViewGroup.expand(0, 0, 5000);
    }
    @Click(R.id.collapse_to_point_in_window)
    private void collapseToPointInWindow(View v) {
        animationViewGroup.collpase(0, 0, 5000);
    }
}
