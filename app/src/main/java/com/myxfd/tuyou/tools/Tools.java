package com.myxfd.tuyou.tools;

import java.util.Random;

/**
 * Created by admin on 2016/11/1.
 */

public class Tools {
    public static String getRandomTuYouName(){

        Random rm = new Random();

        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, 8);

        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);

        return "TuYou"+fixLenthString.substring(1, 9);
    }
}
