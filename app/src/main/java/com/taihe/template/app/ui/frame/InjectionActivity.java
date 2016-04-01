package com.taihe.template.app.ui.frame;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihe.template.app.R;
import com.taihe.template.app.base.AppBaseActivity;
import com.taihe.template.base.injection.Click;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;
import com.taihe.template.base.util.ExHandler;
import com.taihe.template.base.util.ToastUtil;


@Layout(R.layout.activity_injection)
public class InjectionActivity extends AppBaseActivity {
    @Id(R.id.tv_content)
    private TextView tvContent;
    @Id(R.id.iv_icon)
    private ImageView ivIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ivIcon.setImageResource(R.drawable.ic_launcher);
        new ExHandler().post(new Runnable() {//演示ExHandler对异常的自动捕捉, 建议以后所有用Handler的地方都使用ExHandler
            @Override
            public void run() {
                Object o = null;
                o.toString();
                ToastUtil.showShortToast("上一行代码崩溃抛异常，本行代码不会执行哦");
            }
        });
    }
    @Click({R.id.show_toast1, R.id.show_toast2})//支持多个控件的点击事件触发同一个方法（注意{R.id.set_text}中id需要大括号包裹）
    private void showToast(TextView v) {
        switch (v.getId()) {
            case R.id.show_toast1:
                ToastUtil.showShortToast("1111");
                break;
            case R.id.show_toast2:
                ToastUtil.showShortToast("2222");
                break;
        }
    }
    @Click(R.id.iv_icon)
    private void showHelloToast(ImageView iv) {
        ToastUtil.showShortToast("Hello guy ! I'm a ImageView");
    }
    @Click(R.id.set_text)//点击事件绑定（注意{R.id.set_text}中id需要大括号包裹）
    private void setText(View v) {
        tvContent.setText("Hello" + System.currentTimeMillis());
    }

    @Click(R.id.crash)
    private void execptionCauthtOutSide(View v) {
        ToastUtil.showShortToast("异常自动捕捉");
        Object o = null;
        o.toString();
    }
}
