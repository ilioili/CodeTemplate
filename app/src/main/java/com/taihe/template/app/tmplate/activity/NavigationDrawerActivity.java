package com.taihe.template.app.tmplate.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.taihe.template.app.R;
import com.taihe.template.app.MeterialListFragment;
import com.taihe.template.app.ui.CanvasDemoListFragment;
import com.taihe.template.app.FrameListFragment;
import com.taihe.template.app.ui.PlayGroundFragment;
import com.taihe.template.app.ui.ThirdPartLearnFragment;
import com.taihe.template.app.base.AppBaseActivity;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;
import com.taihe.template.base.util.ToastUtil;

@Layout(R.layout.activity_navigation_drawer)
public class NavigationDrawerActivity extends AppBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Id(R.id.toolbar)
    private Toolbar toolbar;
    @Id(R.id.drawer_layout)
    private DrawerLayout drawer;
    @Id(R.id.nav_view)
    private NavigationView navigationView;

    private Fragment curFragment;
    private FrameListFragment frameListFragment = new FrameListFragment();
    private CanvasDemoListFragment canvasDemoListFragment = new CanvasDemoListFragment();
    private ThirdPartLearnFragment thirdPartLearnFragment = new ThirdPartLearnFragment();
    private PlayGroundFragment playGroundFragment = new PlayGroundFragment();
    private MeterialListFragment meterialFragment = new MeterialListFragment();

    public static Intent newIntent(Context conext, Action action) {
        Intent it = new Intent(conext, NavigationDrawerActivity.class);
        it.setAction(action.toString());
        switch (action) {
            case EXIT_APP:
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case EXIT_TO_TOP:
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
        }
        return it;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        if (Action.EXIT_APP.toString().equals(intent.getAction())) {
            finish();
        } else if (Action.EXIT_TO_TOP.toString().equals(intent.getAction())) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, playGroundFragment, PlayGroundFragment.class.getName()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_frame) {
            toolbar.setTitle("FrameDemos");
            switchToFrame();
        } else if (id == R.id.nav_canvas) {
            toolbar.setTitle("CanvasDemos");
            switchToCanvasLearnFragment();
        } else if (id == R.id.nav_third_part) {
            toolbar.setTitle("ThirdPartyDemos");
            switchToThirdParty();
        } else if (id == R.id.nav_playground) {
            toolbar.setTitle("AllDemos");
            switchToPlayground();
        } else if (id == R.id.nav_meterial) {
            toolbar.setTitle("AsDemos");
            switchToMeterial();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_alipay) {
            jumpToAlipay();
        } else if (id == R.id.nav_qq) {
            jumpToQq();
        } else if (id == R.id.nav_qq_group) {
            jumpToQqGroup();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void switchToMeterial() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, meterialFragment).commit();
    }

    private void jumpToQqGroup() {
        try {
//            String url="mqqwpa://im/chat?chat_type=internal&uin=284821731";
//            String url = "mqqapi://card/show_pslcard?src_type=internal&uin=4008260035&card_type=group";
            String url="mqqwpa://im/chat?chat_type=crm&uin=938039033";//企业客服号
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            ToastUtil.showShortToast("QQ都没安装？");
        }
    }

    private void jumpToQq() {
        try {
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=136351754";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            ToastUtil.showShortToast("QQ都没安装？");
        }
    }

    private void jumpToAlipay() {
        try {
            Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007&qrcode=https://qr.alipay.com/apn5aqr0qhfzu5y862");
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (Exception e) {
            ToastUtil.showShortToast("支付宝都没安装？");
        }
    }

    private void switchToPlayground() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, playGroundFragment).commit();
    }

    private void switchToFrame() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frameListFragment).commit();
    }

    private void switchToThirdParty() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, thirdPartLearnFragment).commit();
    }
    private void switchToCanvasLearnFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, canvasDemoListFragment).commit();
    }

    public enum Action {
        EXIT_APP, EXIT_TO_TOP;
    }
}
