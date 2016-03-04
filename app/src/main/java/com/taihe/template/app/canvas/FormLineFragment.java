package com.taihe.template.app.canvas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taihe.template.app.widget.FormLineView;

/**
 * Created by Administrator on 2016/3/4.
 */
public class FormLineFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return new FormLineView(getContext());
    }
}
