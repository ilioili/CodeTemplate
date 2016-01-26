package com.taihe.template.app.tmplate;

import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ilioili.appstart.R;
import com.taihe.template.app.api.model.Student;
import com.taihe.template.app.api.service.StudentService;
import com.taihe.template.app.base.AppBaseActivity;
import com.taihe.template.base.http.Error;
import com.taihe.template.base.http.HttpCallback;
import com.taihe.template.base.http.Task;
import com.taihe.template.base.http.TaskHolder;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;
import com.taihe.template.base.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

@Layout(R.layout.activity_http)
public class HttpActivity extends AppBaseActivity {

    @Id(R.id.tv_result)
    private TextView tvResult;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bt_req_no_dependency:
                reqGetTop10();
                break;
            case R.id.bt_req_dependency:
                reqGetTopStudent();
                break;
            case R.id.bt_req_json_obj:
                reqGetData();
                break;
        }
    }

    //可用的ID["4LrCDDDH","e9KNAAAf","6GLjNNNf","MS0b1113","tU6T999H","qkmU666k","iNTLSSSU","owlCPPPY","81xvEEEL"]
    private void reqGetTop10() {//无依赖关系的Task拼接队列,应用场景：进入页面前先加载数据，数据来自不同的接口
        showLoading();
        final ArrayList<Student> list = new ArrayList<>();
        Task t1 = StudentService.getStudentDetailInfo("4LrCDDDH", new HttpCallback<Student>() {
            @Override
            public Object onSucceed(Student student) {
                list.add(student);
                tvResult.setText(new Gson().toJson(list));
                return null;
            }

            @Override
            public void onFail(Error error) {
                dismissLoading();
            }
        });
        Task t2 = StudentService.getStudentDetailInfo("e9KNAAAf", new HttpCallback<Student>() {
            @Override
            public Object onSucceed(Student student) {
                list.add(student);
                tvResult.setText(new Gson().toJson(list));
                return null;
            }

            @Override
            public void onFail(Error error) {
                dismissLoading();
            }
        });
        Task t3 = StudentService.getStudentDetailInfo("6GLjNNNf", new HttpCallback<Student>() {
            @Override
            public Object onSucceed(Student student) {
                list.add(student);
                tvResult.setText(new Gson().toJson(list));
                dismissLoading();
                return null;
            }

            @Override
            public void onFail(Error error) {
                dismissLoading();
            }
        });
        Task task = t1.append(t2).append(t3);
        execute(task);
    }

    private void reqGetTopStudent() {
        showLoading();
        Task task = StudentService.getTopStudentId(new HttpCallback<String>() {
            @Override
            public Object onSucceed(String id) {
                return id;
            }

            @Override
            public void onFail(Error error) {
                dismissLoading();
            }
        }).append(new TaskHolder<String>() {

            @Override
            public Task getTask(String s) {
                return StudentService.getStudentDetailInfo(s, new HttpCallback<Student>() {
                    @Override
                    public Object onSucceed(Student student) {
                        tvResult.setText(new Gson().toJson(student));
                        dismissLoading();
                        return null;
                    }

                    @Override
                    public void onFail(Error error) {
                        dismissLoading();
                    }
                });
            }
        });
        execute(task);
    }

    private void reqGetData() {
        showLoading();
        Task task = StudentService.getTop10StudentId(new HttpCallback<List<String>>() {
            @Override
            public Object onSucceed(List<String> ids) {
                tvResult.setText(new Gson().toJson(ids));
                dismissLoading();
                return null;
            }

            @Override
            public void onFail(Error error) {
                ToastUtil.showShortToast(error);
                dismissLoading();
            }
        });
        execute(task);
    }
}
