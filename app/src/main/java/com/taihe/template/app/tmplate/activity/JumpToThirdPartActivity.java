package com.taihe.template.app.tmplate.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.ilioili.appstart.R;
import com.taihe.template.app.base.AppBaseActivity;
import com.taihe.template.base.injection.Click;
import com.taihe.template.base.injection.Layout;

@Layout(R.layout.activity_jump_to_third_part)
public class JumpToThirdPartActivity extends AppBaseActivity {

    @Click(R.id.toQQ)
    private void toQq(View v){
        String url="mqqwpa://im/chat?chat_type=crm&uin=938039033";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    @Click(R.id.toAlipay)
    private void toAlipay(View v){
        Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007&qrcode=https://qr.alipay.com/apn5aqr0qhfzu5y862");
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
