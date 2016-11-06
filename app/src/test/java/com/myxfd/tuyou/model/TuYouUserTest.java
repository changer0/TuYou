package com.myxfd.tuyou.model;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Lulu on 2016/11/6.
 */
public class TuYouUserTest {
    @Test
    public void getAge() throws Exception {

    }

    @Test
    public void setAge() throws Exception {
        TuYouUser user = new TuYouUser();
        user.setAge(22);
        int age = user.getAge();
        System.out.println("生日" + user.getBirthday());
    }


}