package com.taihe.template.base.util;

import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.Map;

public final class NullUtil {
    /**
     * @return isEmpty or isNull
     */
    public static boolean ept(@Nullable String str) {
        return null == str || str.length() == 0;
    }

    /**
     * @return isEmpty or isNull
     */
    public static boolean ept(@Nullable Collection c) {
        return null == c || c.isEmpty();
    }

    /**
     * @return isEmpty or isNull
     */
    public static final boolean ept(@Nullable Map m) {
        return null == m || m.isEmpty();
    }

    /**
     * @return 两个都为null，返回true<p>obj1为null时，obj2不为null返回false <p>其他情况等同obj1.equals(obj2)
     */
    public static final boolean eqs(@Nullable Object obj1, @Nullable Object obj2) {
        return (obj1 == obj2) || (obj1 != null && obj1.equals(obj2));
    }

}
