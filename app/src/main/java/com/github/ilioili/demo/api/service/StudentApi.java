package com.github.ilioili.demo.api.service;

import com.github.ilioili.demo.api.model.Student;
import com.github.ilioili.demo.base.BaseTask;
import com.taihe.template.base.http.Error;
import com.taihe.template.base.http.HttpCallback;
import com.taihe.template.base.http.Task;
import com.taihe.template.base.http.TaskHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/18.
 */
public class StudentApi {
    /**
     * 获取某个学生的详细信息
     *
     * @param studentId
     * @param callback
     * @return
     */
    public static Task getStudentDetailInfo(String studentId, HttpCallback<Student> callback) {
        return BaseTask.get("http://cloud.bmob.cn/499c41d5c6f65ec1/getStudentById?objectId=" + studentId, callback);
    }

    /**
     * 获取第一名学生的ID
     *
     * @param callback
     * @return
     */
    public static Task getTopStudentId(HttpCallback<String> callback) {
        return BaseTask.get("http://cloud.bmob.cn/499c41d5c6f65ec1/getTopStudentId", callback);
    }

    /**
     * 获取前十名学生的ID
     *
     * @param callback
     * @return
     */
    public static Task getTop10StudentId(HttpCallback<List<String>> callback) {
        return BaseTask.get("http://cloud.bmob.cn/499c41d5c6f65ec1/getTop10StudentIds", callback);
    }

    /**
     * 获取第一名学生的详细信息（假设服务其只是提供了获取第一名ID的接口和根据ID获取学生信息详情的接口）
     *
     * @param callback
     * @return
     */
    public static Task getTopStudentDetailInfo(final HttpCallback<Student> callback) {
        Task t1 = getTopStudentId(new HttpCallback<String>() {
            @Override
            public Object onSucceed(String s) {
                return s;
            }

            @Override
            public void onFail(Error error) {

            }
        });
        t1.append(new TaskHolder<String>() {
            @Override
            public Task getTask(String id) {
                return getStudentDetailInfo(id, callback);
            }
        });
        return t1;
    }

    public static Task getTop10StudentDetailInfos(final HttpCallback<ArrayList<Student>> callback) {
        return getTop10StudentId(new HttpCallback<List<String>>() {
            @Override
            public Object onSucceed(final List<String> strings) {
                return strings;
            }

            @Override
            public void onFail(Error error) {
                callback.onFail(error);
            }
        }).append(new TaskHolder<List<String>>() {
            @Override
            public Task getTask(List<String> strings) {
                final ArrayList<Student> students = new ArrayList<Student>();
                HttpCallback<Student> c = new HttpCallback<Student>() {
                    @Override
                    public Object onSucceed(Student student) {
                        students.add(student);
                        return null;
                    }

                    @Override
                    public void onFail(Error error) {
                        callback.onFail(error);
                    }
                };
                Task task = getStudentDetailInfo(strings.get(0), c);
                for (int i = 1; i < strings.size() - 1; i++) {
                    task.append(getStudentDetailInfo(strings.get(i), c));
                }
                task.append(getStudentDetailInfo(strings.get(strings.size() - 1), new HttpCallback<Student>() {
                    @Override
                    public Object onSucceed(Student student) {
                        students.add(student);
                        callback.onSucceed(students);
                        return null;
                    }

                    @Override
                    public void onFail(Error error) {
                        callback.onFail(error);
                    }
                }));
                return task;
            }
        });
    }

}
