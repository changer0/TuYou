package com.myxfd.tuyou.fragments;


import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.nearby.NearbyInfo;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearchFunctionType;
import com.amap.api.services.nearby.NearbySearchResult;
import com.amap.api.services.nearby.UploadInfo;
import com.myxfd.tuyou.R;
import com.myxfd.tuyou.adapters.DividerItemDecoration;
import com.myxfd.tuyou.adapters.MapFriendsAdapter;
import com.myxfd.tuyou.model.TuYouUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment implements AMap.OnInfoWindowClickListener, NearbySearch.NearbyListener, AMapLocationListener, View.OnTouchListener {

    private static final String TAG = "MapFragment";
    private MapView mapView;
    private AMap aMap;
    public AMapLocationClient mLocationClient; //声明AMapLocationClient类对象
    private AMapLocationClientOption mLocationOption;//配置信息
    private NearbySearch nearbySearch;
    private Marker myMarker;   //我的地图标志
    private List<Marker> otherMarkers;// 关注的人的地图标志
    private List<TuYouUser> otherUsers;
    private boolean isDrag;//是否是拖拽状态
    private RecyclerView recyclerView; //显示附近人的列表
    private MapFriendsAdapter mAdapter;

    public MapFragment() {
    }

    @Override
    public String getFragmentTitle() {
        return "地图";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_map, container, false);
        otherMarkers = new ArrayList<>();
        otherUsers = new Vector<>();

        mapView = (MapView) itemView.findViewById(R.id.map_view);//获取地图引用
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mapView.onCreate(savedInstanceState);
        mapView.setOnTouchListener(this); //设置触摸监听
        nearbySearch = NearbySearch.getInstance(getApplicationContext());  //初始化附近实例
        aMap = mapView.getMap(); //给aMap赋值
        aMap.setOnInfoWindowClickListener(this);//信息窗体, 点击回调
        startLocation(); //开启定位
        UiSettings settings = aMap.getUiSettings(); //得到高德UI设置项
        settings.setCompassEnabled(true);//指南针
        settings.setMyLocationButtonEnabled(true);//定位控件

        initRecycle(itemView);//初始化RecycleView
        return itemView;
    }

    // ------------------------------------------------
    //初始化RecycleView
    private void initRecycle(View itemView) {
        RecyclerView recyclerView = (RecyclerView) itemView.findViewById(R.id.map_recycle);
        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置添加动画
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        // TODO: 2016/11/3 添加实体类
        if (otherUsers != null) {
            mAdapter = new MapFriendsAdapter(getContext(), otherUsers);
            recyclerView.setAdapter(mAdapter);
        }

    }

    //定位方法
    private void startLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
//        设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(10000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }

    // --------------------------------------------------
    // 高德地图信息窗体回调监听
    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.hideInfoWindow();//隐藏信息窗体
        Toast.makeText(getContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
    }

    // -----------------------
    // 更新当前用户的位置信息
    private void updateCurrentUserLocation(final LatLonPoint point) {
        //清除 用户信息
//        nearbySearch.clearUserInfoAsyn();
        //单次上传
        UploadInfo uploadInfo = new UploadInfo();
        uploadInfo.setCoordType(NearbySearch.AMAP);
        uploadInfo.setPoint(point);
        //得到当前的用户信息, 并上传
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if (bmobUser != null) {
            uploadInfo.setPoint(point);
            //上传到高德, 用户id信息
            uploadInfo.setUserID(bmobUser.getObjectId());
            nearbySearch.uploadNearbyInfoAsyn(uploadInfo);
            Log.d(TAG, "updateCurrentUserLocation: 上传高德用户信息");

            //上传位置信息到Bmob服务器
            TuYouUser user = new TuYouUser();
            user.setLat(point.getLatitude());
            user.setLgt(point.getLongitude());
            user.update(bmobUser.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.d(TAG, "OnUploadInfoCallback: 更新完成");
                    } else {
                        Log.e(TAG, "OnUploadInfoCallback: 更新失败");
                    }
                }
            });

        }
    }

    // ---------------------------------------------------
    //在地图中添加其他用户的位置
    private boolean addListenter = false;

    private void addOtherUser(LatLonPoint point) {
        //设置搜索条件
        NearbySearch.NearbyQuery query = new NearbySearch.NearbyQuery();

        //设置搜索的中心点
        query.setCenterPoint(point);
        //设置搜索的坐标体系
        query.setCoordType(NearbySearch.AMAP);
        //设置搜索半径
        query.setRadius(1000);
        //设置查询的时间
        query.setTimeRange(5000);
        //设置查询的方式驾车还是距离
        query.setType(NearbySearchFunctionType.DISTANCE_SEARCH);
        //调用异步查询接口
        nearbySearch
                .searchNearbyInfoAsyn(query);
        //添加回调监听
        if (!addListenter) {
            nearbySearch.addNearbyListener(this);
            addListenter = true;
        }
    }

    // ---------------------------------------------------------------------
    //NearbySearch 附近搜索回调接口
    @Override
    public void onUserInfoCleared(int i) {
    }

    @Override
    public void onNearbyInfoSearched(NearbySearchResult nearbySearchResult, int resultCode) {
        //搜索周边附近用户回调处理
        if (resultCode == 1000) {
            if (nearbySearchResult != null
                    && nearbySearchResult.getNearbyInfoList() != null
                    && nearbySearchResult.getNearbyInfoList().size() > 0) {
                List<NearbyInfo> list = nearbySearchResult.getNearbyInfoList();

                //清空Marker
                for (int i = 0; i < otherMarkers.size(); i++) {
                    otherMarkers.get(i).remove();
                    otherMarkers.remove(i);
                }

                //清空RecycleView中的数据
                otherUsers.clear();
                Log.d(TAG, "onNearbyInfoSearched: 附近用户的的个数" + list.size());


                for (NearbyInfo info : list) {
                    final int distance = info.getDistance();
                    Log.d(TAG, "done: 用户的距离: " + distance);

                    //如果得到的附近的人是本人, 忽略
                    String userId = info.getUserID();
                    Log.d(TAG, "onNearbyInfoSearched: 当前用户ID: " + info.getUserID());
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    if (bmobUser != null) {
                        if (userId.equals(bmobUser.getObjectId())) {
                            continue;
                        }
                    }

                    LatLonPoint point = info.getPoint();
                    MarkerOptions options = new MarkerOptions();
                    options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.poi_marker_red))
                            .position(new LatLng(point.getLatitude(), point.getLongitude()))
                            .draggable(false)
                            .title(userId);

                    Log.d(TAG, "onNearbyInfoSearched: 查询到的位置" + info.getPoint());
                    Log.d(TAG, "onNearbyInfoSearched: 查询的的用户ID: " + info.getUserID());


                    Marker marker = aMap.addMarker(options);
                    otherMarkers.add(marker);

                    BmobQuery<TuYouUser> query = new BmobQuery<>();
                    query.getObject(userId, new QueryListener<TuYouUser>() {
                        @Override
                        public void done(TuYouUser user, BmobException e) {
                            if (e == null) {
                                if (user != null) {
                                    user.setDistance(distance);
                                    otherUsers.add(user);
                                    Collections.sort(otherUsers);
                                    mAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Log.d(TAG, "done: e ==> " + e.getMessage());
                            }

                        }
                    });
                }


            } else {
                Toast.makeText(getContext(), "附近暂无 图友", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d(TAG, "onNearbyInfoSearched: 周边搜索出现异常，异常码为：" + resultCode);
        }
    }

    @Override
    public void onNearbyInfoUploaded(int i) {

    }


    // -------------------------------------------------------
    //定位回调
    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location.getErrorCode() == 0) {
            //定位成功
            Log.d(TAG, "onLocationChanged: location => " + location.getLongitude() + " : " + location.getLatitude());

            //将定位信息显示到地图上
            MarkerOptions markerOptions = new MarkerOptions();
            //得到定位的位置
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            //设置标记物
            markerOptions.position(latLng)
                    .title(location.getCity())
                    .snippet(location.getAddress())
                    .draggable(false)//是否拖拽
                    .icon(BitmapDescriptorFactory.fromBitmap(
                            BitmapFactory.decodeResource(getResources(), R.mipmap.poi_marker_pressed)
                    ));

            //添加到地图上
            if (myMarker != null) {
                myMarker.remove();//先清掉
            }
            myMarker = aMap.addMarker(markerOptions);
            //放大到最高级别
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(20);
            aMap.animateCamera(zoom);

            //如果此时不是拖拽状态应该始终保持当前位置是屏幕中心
            if (!isDrag) {
                CameraUpdate changeLatLng = CameraUpdateFactory.changeLatLng(latLng);
                aMap.animateCamera(changeLatLng);
            }

            //更新上传自身的位置信息
            updateCurrentUserLocation(new LatLonPoint(latLng.latitude, latLng.longitude));
            //在地图上添加其他的用户的点
            addOtherUser(new LatLonPoint(latLng.latitude, latLng.longitude));

            Toast.makeText(getContext(), "定位成功", Toast.LENGTH_SHORT).show();

        } else {
            //定位异常
        }
    }

    // --------------------------------------------------------------
    // 用于处理MapView的触摸事件
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean ret = false;
        int action = event.getAction();
        switch (v.getId()) {
            case R.id.map_view:
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        isDrag = true;
                        ret = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        isDrag = false;
                        ret = true;
                        break;
                }

                break;
        }

        return ret;
    }

    // -------------------------------------------------------
    // 生命周期方法
    @Override
    public void onDestroy() {
        //停止定位
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
        //清楚用户信息
        nearbySearch.clearUserInfoAsyn();
        //释放附近资源
        NearbySearch.destroy();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
    }
}
