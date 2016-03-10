package com.taihe.template.app.ui.viewtest;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.ilioili.appstart.R;
import com.taihe.template.app.widget.HorizontalWheelView;
import com.taihe.template.app.widget.VerticalWheelView;
import com.taihe.template.base.BaseFragment;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;
import com.taihe.template.base.util.ToastUtil;

/**
 * Created by Administrator on 2016/1/25.
 */
@Layout(R.layout.fragment_horizontal_wheel_view)
public class WheelViewFragment extends BaseFragment {
    @Id(R.id.horizontal_wheel_view)
    private HorizontalWheelView horizontalWheelView;
    @Id(R.id.vertical_wheel_view)
    private VerticalWheelView verticalWheelView;
    @Override
    protected void init(View rootView) {
        super.init(rootView);
        horizontalWheelView.setAdapter(new HorizontalWheelView.Adapter() {
            @Override
            public void setView(View convertView, int index) {
                TextView tv = (TextView) convertView;
                tv.setText(""+index);
            }

            @Override
            public int getDataCount() {
                return 20;
            }
        });
        horizontalWheelView.setOnScrollListener(new HorizontalWheelView.OnScrollListener() {
            @Override
            public void onScroll(View v, int indexOffset, float percentage) {
                v.setScaleX(1-percentage*0.8f);
                v.setScaleY(1-percentage*0.8f);
                v.setAlpha(1-percentage*0.8f);
            }
        });
        horizontalWheelView.setOnItemSelectListener(new HorizontalWheelView.OnItemSelectListener() {
            @Override
            public void onItemSelected(int index) {
                ToastUtil.showShortToast(""+index);
            }
        });

        verticalWheelView.setOnScrollListener(new VerticalWheelView.OnScrollListener() {
            @Override
            public void onScroll(View v, int indexOffset, float percentage) {
                TextView tv = (TextView) v;
                if(indexOffset==0){
                    tv.setTextColor(Color.RED);
                }else{
                    tv.setTextColor(Color.BLACK);
                }
                v.setAlpha(1-percentage*0.8f);
            }
        });

        verticalWheelView.setOnItemSelectListener(new VerticalWheelView.OnItemSelectListener() {
            @Override
            public void onItemSelected(int index) {
                ToastUtil.showShortToast("Tab"+index);
            }
        });
    }
}
