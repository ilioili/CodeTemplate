package com.github.ilioili.demo.main.frame.mvp.model;

import com.github.ilioili.demo.main.frame.http.model.Student;
import com.github.ilioili.demo.main.frame.http.api.StudentApi;
import com.github.ilioili.demo.main.frame.mvp.base.IBasePresenter;
import com.github.ilioili.demo.main.frame.mvp.presenter.StudentProfilePresenter;
import com.taihe.template.base.http.Error;
import com.taihe.template.base.http.HttpCallback;
import com.taihe.template.base.http.Task;
import com.taihe.template.base.http.TaskHolder;

/**
 * Created by Administrator on 2016/1/27.
 */
public class StudentProfileModel implements StudentProfilePresenter.IStudentModel {
    private Student student;
    @Override
    public String getName() {
        return student.getName();
    }

    @Override
    public int getAge() {
        return student.getAge();
    }

    @Override
    public int getSex() {
        return student.getSex();
    }

    @Override
    public String getIconUrl() {
        return student.getIconUrl();
    }

    @Override
    public void getData(final IBasePresenter callback) {
        Task task = StudentApi.getTopStudentId(new HttpCallback<String>() {
            @Override
            public Object onSucceed(String s) {
                return s;
            }

            @Override
            public void onFail(Error error) {
                callback.onModelFail();
            }
        });
        task.append(new TaskHolder() {
            @Override
            public Task getTask(Object o) {
                return StudentApi.getStudentDetailInfo(o.toString(), new HttpCallback<Student>() {
                    @Override
                    public Object onSucceed(Student s) {
                        student = s;
                        callback.onModelReady();
                        return null;
                    }

                    @Override
                    public void onFail(Error error) {
                        callback.onModelFail();
                    }
                });
            }
        });
        task.execute();
    }
}
