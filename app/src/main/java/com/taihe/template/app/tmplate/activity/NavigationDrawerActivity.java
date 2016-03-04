package com.taihe.template.app.tmplate.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ilioili.appstart.R;
import com.taihe.template.app.base.AppBaseActivity;
import com.taihe.template.app.ui.EnterActivity;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;
import com.taihe.template.base.util.ToastUtil;

@Layout(R.layout.activity_navigation_drawer)
public class NavigationDrawerActivity extends AppBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Id(R.id.toolbar)
    private Toolbar toolbar;
    @Id(R.id.fab)
    private FloatingActionButton fab;
    @Id(R.id.drawer_layout)
    private DrawerLayout drawer;
    @Id(R.id.nav_view)
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(this, EnterActivity.class));
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_alipay) {
            try {
                Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007&qrcode=https://qr.alipay.com/apn5aqr0qhfzu5y862");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } catch (Exception e) {
                ToastUtil.showShortToast("支付宝都没安装？");
            }
        } else if (id == R.id.nav_qq) {
            try {
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=136351754";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } catch (Exception e) {
                ToastUtil.showShortToast("QQ都没安装？");
            }
        } else if (id == R.id.nav_qq_group) {
            try {
//            String url="mqqwpa://im/chat?chat_type=internal&uin=284821731";
                String url = "mqqapi://card/show_pslcard?src_type=internal&uin=284821731&card_type=group";
//            String url="mqqwpa://im/chat?chat_type=crm&uin=284821731";//企业客服号
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } catch (Exception e) {
                ToastUtil.showShortToast("QQ都没安装？");
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
