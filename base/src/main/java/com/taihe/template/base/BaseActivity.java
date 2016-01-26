package com.taihe.template.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.MessageQueue;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.taihe.template.base.http.Task;
import com.taihe.template.base.http.util.NetworkingUtil;
import com.taihe.template.base.injection.InjectionUtil;
import com.taihe.template.base.util.ExHandler;
import com.taihe.template.base.util.NullUtil;
import com.taihe.template.base.util.ToastUtil;

import java.util.HashSet;
import java.util.List;


/**
 * 支持通过ID进行注解,初始化注解过ID的View
 * <p/>
 * 简化findViewById(...)方法->f(...)
 * <p/>
 * 支持String，Collection， Map的判空和Equals判断（不用先做Null判断了）简化方法eq()->equals(), em()->isEmpty()
 * <p/>
 * 跳转Activity时，屏蔽当前Activity的触屏事件，防止产生重复点击问题
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 刷新页面数据
     */
    public static final int REQUEST_CODE_FRESH_DATA = 888;
    /**
     * 结束当前页面
     */
    public static final int REQUEST_CODE_FINISH = 999;

    private static BaseActivity currentActivity;
    protected Handler handler = new ExHandler();
    private boolean blockTouchEvent;
    private HashSet<Task> taskHashSet;

    public static BaseActivity currentActivity() {
        return currentActivity;
    }

    /**
     * @param block onResume()会自动调用此方法 blockTouchEvent(true)
     */
    final protected void blockTouchEvent(boolean block) {
        blockTouchEvent = block;
    }

    protected void execute(Task task) {
        if (null == task) {
            return;
        }
        if (null == taskHashSet) {
            taskHashSet = new HashSet<>();
        }
        task.execute(this);
        taskHashSet.add(task);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != taskHashSet) {
            for (Task call : taskHashSet) {
                call.cancelTask();
            }
        }
        BaseApplication baseApplication = (BaseApplication) getApplication();
        baseApplication.getRefWatcher(this).watch(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (blockTouchEvent) {
            return false;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    /**
     * 调用(Clazz)v.findViewById(id)
     *
     * @param id
     * @param v
     */
    public final <T extends View> T f(int id, View v) {
        return (T) v.findViewById(id);
    }

    /**
     * 调用 findViewById(id)
     *
     * @param id
     */
    public final <T extends View> T f(int id) {
        return (T) findViewById(id);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if (null != v && null != v.getWindowToken()) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    @Override
    @CallSuper
    public void onClick(View v) {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (!NullUtil.ept(fragmentList)) {
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof View.OnClickListener && fragment.isResumed() && fragment.getUserVisibleHint()) {
                    View rootView = fragment.getView();
                    View tmp = v;
                    while (true) {//判断v是Fragment的View时候调用Fragment的onClick方法
                        if (tmp == rootView) {
                            ((View.OnClickListener) fragment).onClick(v);
                            break;
                        } else if (null == tmp.getParent() || !(tmp.getParent() instanceof View)) {
                            break;
                        } else {
                            tmp = (View) tmp.getParent();
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Window window = getWindow();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//通知栏透明
//            //            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//下面三个虚拟按键透明
//        }
//        window.requestFeature(Window.FEATURE_NO_TITLE);
        if (savedInstanceState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);// remove掉保存的Fragment
        }
        super.onCreate(savedInstanceState);
        InjectionUtil.load(this, true);
        NetworkingUtil.init(getApplicationContext());
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentActivity = this;
        blockTouchEvent(false);
    }

    protected void postDelay(Runnable runnable, long delay) {
        handler.postDelayed(runnable, delay);
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void postWhenIdle(final Runnable runnable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getMainLooper().getQueue().addIdleHandler(new MessageQueue.IdleHandler() {
                @Override
                public boolean queueIdle() {
                    runnable.run();
                    return false;
                }
            });
        }else {
            handler.post(runnable);
        }
    }

    /**
     * setGone
     * 设置View[]为不可见gone
     *
     * @param views
     */
    public void setGone(View... views) {
        for (View v : views) {
            v.setVisibility(View.GONE);
        }
    }

    public void setGone(int... viewIds) {
        for (int id : viewIds) {
            findViewById(id).setVisibility(View.GONE);
        }
    }

    /**
     * setInvisiable
     * 设置View[]为不可见invisiable
     *
     * @param views
     */
    public void setInvisialbe(View... views) {
        for (View v : views) {
            v.setVisibility(View.INVISIBLE);
        }
    }

    public void setInvisialbe(int... viewIds) {
        for (int id : viewIds) {
            findViewById(id).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * setVisiable
     * 设置View[]为可见visiable
     *
     * @param views
     */
    public void setVisiable(View... views) {
        for (View v : views) {
            v.setVisibility(View.VISIBLE);
        }
    }

    public void setVisiable(int... viewIds) {
        for (int id : viewIds) {
            findViewById(id).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 强制弹出软键盘
     *
     * @param view
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    protected void showToast(Object obj) {
        ToastUtil.showShortToast(this, obj);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        blockTouchEvent(true);
    }

}
