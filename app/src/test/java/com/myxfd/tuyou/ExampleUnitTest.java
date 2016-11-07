package com.myxfd.tuyou;

import android.util.Log;

import com.myxfd.tuyou.model.TuYouUser;
import com.myxfd.tuyou.ndk.NativeHelper;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testPwd() throws Exception {
        String key = NativeHelper.getPasswrodKey();
        System.out.println(key);
    }
}