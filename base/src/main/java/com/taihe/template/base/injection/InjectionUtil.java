package com.taihe.template.base.injection;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ilioili on 2015/12/12.
 */
public class InjectionUtil {
    public static void load(final Activity activity, boolean loadSupperViewField) {
        Class<? extends Activity> aClass = activity.getClass();
        if (aClass.isAnnotationPresent(Layout.class)) {
            activity.setContentView(aClass.getAnnotation(Layout.class).value());
            List<Field> fieldList = new ArrayList<>();
            List<Method> methodList = new ArrayList<>();
            if (loadSupperViewField) {
                Class clazz = aClass;
                while (clazz != Activity.class) {
                    Field[] fields = clazz.getDeclaredFields();
                    fieldList.addAll(Arrays.asList(fields));
                    Method[] methods = clazz.getDeclaredMethods();
                    methodList.addAll(Arrays.asList(methods));
                    clazz = clazz.getSuperclass();
                }
            } else {
                Field[] fields = aClass.getDeclaredFields();
                fieldList = Arrays.asList(fields);
                Method[] methods = aClass.getDeclaredMethods();
                methodList.addAll(Arrays.asList(methods));
            }
            Field f = null;
            try {
                for (Field field : fieldList) {
                    if (field.isAnnotationPresent(Id.class)) {
                        f = field;
                        field.setAccessible(true);
                        field.set(activity, activity.findViewById(field.getAnnotation(Id.class).value()));
                    }
                }
                for (final Method method : methodList) {
                    if (method.isAnnotationPresent(Click.class)) {
                        View.OnClickListener onClickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    method.setAccessible(true);
                                    method.invoke(activity, v);
                                } catch (IllegalAccessException e) {
                                    Log.e(InjectionUtil.class.getName(), Log.getStackTraceString(e));
                                } catch (InvocationTargetException e) {
                                    Log.e(InjectionUtil.class.getName(), Log.getStackTraceString(e));
                                } catch (Exception e) {
                                    Log.e(InjectionUtil.class.getName(), Log.getStackTraceString(e));
                                } catch (Error e) {
                                    Log.e(InjectionUtil.class.getName(), Log.getStackTraceString(e));
                                }
                            }
                        };
                        for (int id : method.getAnnotation(Click.class).value()) {
                            View view = activity.findViewById(id);
                            if (null == view) {
                                throw new NullPointerException();
                            }
                            view.setOnClickListener(onClickListener);
                        }
                    }
                }
            } catch (ClassCastException e) {
                Log.e(aClass.getName(), "转型异常:" + f.getName() + "是" + f.getType());
                throw new IllegalStateException();
            } catch (NullPointerException e) {
                Log.e(aClass.getName(), "控件未找到，请检查View声明的类型");
            } catch (Exception e) {
                Log.e(aClass.getName(), Arrays.toString(e.getStackTrace()));
            }
        }
    }

    public static View loadView(LayoutInflater layoutInflater, ViewGroup viewGroup, final Fragment fragment, boolean loadSupperViewField) {
        View rootView = null;
        Class<? extends Fragment> fragmentClass = fragment.getClass();
        if (fragmentClass.isAnnotationPresent(Layout.class)) {
            rootView = layoutInflater.inflate(fragmentClass.getAnnotation(Layout.class).value(), viewGroup, false);
            List<Field> fieldList = new ArrayList<>();
            List<Method> methodList = new ArrayList<>();
            if (!loadSupperViewField) {
                fieldList = Arrays.asList(fragmentClass.getDeclaredFields());
                Method[] methods = fragmentClass.getDeclaredMethods();
                methodList.addAll(Arrays.asList(methods));
            } else {
                Class clazz = fragmentClass;
                while (clazz != Fragment.class) {
                    Field[] fields = clazz.getDeclaredFields();
                    fieldList.addAll(Arrays.asList(fields));
                    Method[] methods = clazz.getDeclaredMethods();
                    methodList.addAll(Arrays.asList(methods));
                    clazz = clazz.getSuperclass();
                }
            }
            Field f = null;
            try {
                for (Field field : fieldList) {
                    if (field.isAnnotationPresent(Id.class)) {
                        f = field;
                        field.setAccessible(true);
                        field.set(fragment, rootView.findViewById(field.getAnnotation(Id.class).value()));
                    }
                }
            } catch (ClassCastException e) {
                Log.e(fragmentClass.getName(), "转型异常:" + f.getName() + "是" + f.getType());
                throw new IllegalStateException();
            } catch (NullPointerException e) {
                Log.e(fragmentClass.getName(), "控件未找到，请检查View声明的类型");
            } catch (Exception e) {
                Log.e(fragmentClass.getName(), Arrays.toString(e.getStackTrace()));
            }
            for (final Method method : methodList) {
                if (method.isAnnotationPresent(Click.class)) {
                    View.OnClickListener onClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                method.setAccessible(true);
                                method.invoke(fragment, v);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    for (int id : method.getAnnotation(Click.class).value()) {
                        View view = rootView.findViewById(id);
                        if (null == view) {
                            throw new NullPointerException();
                        }
                        view.setClickable(true);
                        view.setOnClickListener(onClickListener);
                    }
                }
            }
        }
        return rootView;
    }

    public static void loadView(View contentView, Object viewHolder) {
        Class<? extends Object> aClass = viewHolder.getClass();
        Field f = null;
        try {
            for (Field field : aClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    f = field;
                    field.setAccessible(true);
                    field.set(viewHolder, contentView.findViewById(field.getAnnotation(Id.class).value()));
                }
            }
        } catch (ClassCastException e) {
            Log.e(aClass.getName(), "转型异常:" + f.getName() + "是" + f.getType());
            throw new IllegalStateException();
        } catch (NullPointerException e) {
            Log.e(aClass.getName(), "控件未找到，请检查View声明的类型");
        } catch (Exception e) {
            Log.e(aClass.getName(), Arrays.toString(e.getStackTrace()));
        }
    }
}
