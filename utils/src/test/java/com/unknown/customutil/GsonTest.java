package com.unknown.customutil;

import com.google.gson.Gson;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by hantuo on 16/4/12.
 */
public class GsonTest {

    @Test
    public void testGson(){
        Integer i = new Gson().fromJson("1", Integer.class);
        assertEquals(i, new Integer(1));
        String str = new Gson().fromJson("Hello", String.class);
        assertEquals(str, "Hello");
    }
}
