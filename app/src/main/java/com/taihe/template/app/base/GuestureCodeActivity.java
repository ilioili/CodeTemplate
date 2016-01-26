package com.taihe.template.app.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2016/1/8.
 */
public class GuestureCodeActivity extends AppBaseActivity {

    private long latestPauseTime;//应用切换到后台的时间

    private View overlayView;

    private SharedPreferences sharedPreferences;


    protected boolean showGestureCover() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("S_config", MODE_PRIVATE);
        latestPauseTime = sharedPreferences.getLong("time", System.currentTimeMillis());
    }

    private void hideKeyboardIfPossible() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            View v = getCurrentFocus();
            if (null != v && null != v.getWindowToken()) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Math.abs(latestPauseTime - System.currentTimeMillis()) > 3 * 1000) {//用绝对值防止手机时间被调整引起的失效
            if (showGestureCover()) {
                hideKeyboardIfPossible();
                if (null == overlayView) {
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    getWindow().addContentView(overlayView, params);
                }
                overlayView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        latestPauseTime = System.currentTimeMillis();
    }

    @Override
    public void onBackPressed() {
        if (null == overlayView || overlayView.getVisibility() != View.VISIBLE) {
            super.onBackPressed();
        }
    }
}
