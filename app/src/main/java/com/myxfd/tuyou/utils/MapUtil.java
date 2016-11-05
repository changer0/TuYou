package com.myxfd.tuyou.utils;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.nearby.UploadInfo;
import com.myxfd.tuyou.model.TuYouUser;

import cn.bmob.v3.BmobUser;

/**
 * Created by Lulu on 2016/11/2.
 */

public class MapUtil {
//    public static LatLonPoint getCurrentUserLatLonPoint() {
//        LatLonPoint ret = null;
//        //得到当前的用户信息, 并上传
//        BmobUser bmobUser = BmobUser.getCurrentUser();
//        if (bmobUser != null) {
//            if (bmobUser instanceof TuYouUser) {
//                TuYouUser user = (TuYouUser) bmobUser;
//                //位置信息
//                Double lat = user.getLat();
//                Double lgt = user.getLgt();
//                if (lat != null && lgt != null) {
//                    ret = new LatLonPoint(lat, lgt);
//                }
//            }
//        }
//        return ret;
//    }

}
