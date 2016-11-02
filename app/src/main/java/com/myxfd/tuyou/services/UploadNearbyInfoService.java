package com.myxfd.tuyou.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearchResult;
import com.amap.api.services.nearby.UploadInfo;
import com.amap.api.services.nearby.UploadInfoCallback;
import com.myxfd.tuyou.model.TuYouUser;
import com.myxfd.tuyou.utils.MapUtil;

import cn.bmob.v3.BmobUser;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UploadNearbyInfoService extends IntentService {


    public UploadNearbyInfoService(String name) {
        super(name);
    }


    /**
     * 子线程中执行
     *
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        NearbySearch search = NearbySearch.getInstance(getApplicationContext());
        search.startUploadNearbyInfoAuto(new UploadInfoCallback() {
            //设置自动上传数据和上传的间隔时间
            @Override
            public UploadInfo OnUploadInfoCallback() {
                UploadInfo ret = new UploadInfo();
                //得到当前的用户信息, 并上传
                BmobUser bmobUser = BmobUser.getCurrentUser();
                if (bmobUser != null) {
                    //上传位置信息
                    LatLonPoint point = MapUtil.getCurrentUserLatLonPoint();
                    ret.setPoint(point);
                    //上传用户id信息
                    ret.setUserID(bmobUser.getObjectId());
                }
                return ret;
            }
        }, 1000);


    }


}
