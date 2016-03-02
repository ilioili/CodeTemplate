package com.taihe.template.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2016/2/26.
 */
public class FragmentHostActivity extends BaseActivity {
    private static final String FRAGMENT_NAME = "fragment name";
    private static final String FRAGMENT_BUNDLE = "fragment name";

    public static Intent newIntent(Context context, Fragment fragment) {
        Intent it = new Intent(context, FragmentHostActivity.class);
        it.putExtra(FRAGMENT_NAME, fragment.getClass().getName());
        it.putExtra(FRAGMENT_BUNDLE, fragment.getArguments());
        return it;
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        try {
            String fragmentClassName = getIntent().getStringExtra(FRAGMENT_NAME);
            Class fragmentClass = Class.forName(fragmentClassName);
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(getIntent().getBundleExtra(FRAGMENT_BUNDLE));
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment, fragmentClassName).commit();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }
}
