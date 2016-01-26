package com.taihe.template.base.injection;

/**
 * Created by ilioili on 2015/4/28.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ilioili on 2015/1/12.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Layout {
    int value();
}
