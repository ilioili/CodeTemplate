package com.github.ilioili.demo.main.frame.mvp.presenter;

import com.github.ilioili.demo.main.frame.mvp.base.IBaseModel;
import com.github.ilioili.demo.main.frame.mvp.base.IBasePresenter;
import com.github.ilioili.demo.main.frame.mvp.base.IBaseView;

/**
 * Created by Administrator on 2016/1/27.
 */
public class StudentProfilePresenter implements IBasePresenter {
    IView iView;
    IStudentModel iStudentModel;

    public StudentProfilePresenter(IView iView, IStudentModel iModel){
        this.iView = iView;
        this.iStudentModel = iModel;
        iView.showLoading();
        iStudentModel.getData(this);
    }

    @Override
    public void onModelReady() {
        iView.dismissLoading();
        iView.set(iStudentModel.getName(), iStudentModel.getAge(), iStudentModel.getSex(), iStudentModel.getIconUrl());
    }

    @Override
    public void onModelFail() {
        iView.dismissLoading();
    }

    public interface IView extends IBaseView{
        void set(String name, int age, int sex, String iconUrl);
    }

    public interface IStudentModel extends IBaseModel {
        String getName();
        int getAge();
        int getSex();
        String getIconUrl();
    }
}
