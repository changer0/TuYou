package com.myxfd.tuyou;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.support.multidex.MultiDex;

import com.amap.api.maps2d.MapsInitializer;
import com.amap.api.services.nearby.NearbySearch;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusBuilder;

import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.sharesdk.framework.ShareSDK;
import easeui.controller.EaseUI;

/**
 * Created by liangyue on 16/10/31.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        Bmob.initialize(this, "45df1c53ec3984c40da6b54e686ed0cc");
        EventBusBuilder builder = EventBus.builder();
        builder.installDefaultEventBus();
        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);

        //ShareSDK的初始化
        ShareSDK.initSDK(this);
        //高德地图初始化
        try {
            MapsInitializer.initialize(getApplicationContext());
            NearbySearch.getInstance(getApplicationContext());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        //初始化
        Context appContext = this;
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(appContext.getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        EaseUI.getInstance().init(appContext, options);

        EMClient.getInstance().init(appContext, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
