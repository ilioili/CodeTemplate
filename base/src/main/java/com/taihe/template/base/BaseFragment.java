package com.taihe.template.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taihe.template.base.http.Task;
import com.taihe.template.base.injection.InjectionUtil;
import com.taihe.template.base.util.NullUtil;


import java.util.HashSet;
import java.util.List;

public class BaseFragment extends Fragment implements View.OnClickListener {

    protected View rootView;
    private HashSet<Task> taskHashSet;

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
     * 调用 findViewById(id),不能再onCreateView()中调用
     *
     * @param id
     */
    public final <T extends View> T f(int id) {
        return (T) getActivity().findViewById(id);
    }

    @Override
    public void onClick(View v) {
        List<Fragment> fragmentList = getChildFragmentManager().getFragments();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(null==rootView){
            rootView = InjectionUtil.loadView(inflater, container, this, false);
            rootView.setClickable(true);//防止点击事件穿透
            init(rootView);
        }
        return rootView;
    }

    protected void init(View rootView){

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public BaseActivity getBaseActivity(){
        return (BaseActivity) getActivity();
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
            getActivity().findViewById(id).setVisibility(View.GONE);
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
            getView().findViewById(id).setVisibility(View.INVISIBLE);
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
            getView().findViewById(id).setVisibility(View.VISIBLE);
        }
    }


    protected void execute(Task task) {
        if(null==task){
            return;
        }
        if (null == taskHashSet) {
            taskHashSet = new HashSet<>();
        }
        taskHashSet.add(task);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != taskHashSet) {
            for (Task task : taskHashSet) {
                if (!task.isCanceled()) {
                    task.cancelTask();
                }
            }
        }
        BaseApplication baseApplication = (BaseApplication) getActivity().getApplication();
        baseApplication.getRefWatcher(getActivity()).watch(this);
    }
}
