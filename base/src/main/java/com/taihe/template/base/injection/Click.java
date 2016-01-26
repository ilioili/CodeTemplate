package com.taihe.template.base.injection;

/**
 * Created by Administrator on 2016/1/20.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ilioili on 2015/1/12.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Click {
    int[] value();
}

