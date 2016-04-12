package com.github.ilioili.demo.ui.frame;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ilioili.demo.R;
import com.github.ilioili.demo.base.AppBaseActivity;
import com.taihe.template.base.injection.Click;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;
import com.taihe.template.base.util.ToastUtil;


@Layout(R.layout.activity_injection)
public class InjectionActivity extends AppBaseActivity {
    @Id(R.id.toolbar)
    private Toolbar toolbar;
    @Id(R.id.tv_content)
    private TextView tvContent;
    @Id(R.id.iv_icon)
    private ImageView ivIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivIcon.setImageResource(R.drawable.ic_launcher);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_settings){
            ToastUtil.showLongToast("Haha!");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
