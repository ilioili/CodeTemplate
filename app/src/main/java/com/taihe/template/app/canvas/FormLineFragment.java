package com.taihe.template.app.canvas;

import android.view.View;

import com.ilioili.appstart.R;
import com.taihe.template.app.base.AppBaseFragment;
import com.taihe.template.app.widget.FormLineView;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;
import com.taihe.template.base.util.ToastUtil;

/**
 * Created by Administrator on 2016/3/4.
 */
@Layout(R.layout.fragment_formline)
public class FormLineFragment extends AppBaseFragment {
    @Id(R.id.charView)
    private FormLineView chatView;

    private String titles[] = new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
    private float[] data = new float[]{0, 100, 300.55f, 200, 500.7f, 0, 50, 800, 500, 200, 400, 100};


    @Override
    protected void initView(View rootView) {
        chatView.setData(titles, data);
        chatView.setActionListener(new FormLineView.OnActionListener() {
            @Override
            public void onItemClick(int index) {
                ToastUtil.showShortToast("Click:"+index);
            }

            @Override
            public void onItemSelected(int index) {
                ToastUtil.showShortToast("Select:"+index);
            }
        });
    }
}
