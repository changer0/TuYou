package com.myxfd.tuyou.fragments;


import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.nearby.NearbyInfo;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearchFunctionType;
import com.amap.api.services.nearby.NearbySearchResult;
import com.myxfd.tuyou.R;
import com.myxfd.tuyou.utils.MapUtil;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment implements AMap.OnInfoWindowClickListener, NearbySearch.NearbyListener {

    private static final String TAG = "MapFragment";

    private MapView mapView;

    private Fragment circleFragment, mineFragment, mapFragment, messageFragment;
    private FragmentManager manager;
    private AMap aMap;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //配置信息
    private AMapLocationClientOption mLocationOption;

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
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
                aMap.addMarker(markerOptions);
                //放大到最高级别
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(20);
                aMap.animateCamera(zoom);

                //始终保持当前位置是屏幕中心
                CameraUpdate changeLatLng = CameraUpdateFactory.changeLatLng(latLng);
                aMap.animateCamera(changeLatLng);

                Toast.makeText(getContext(), "定位成功", Toast.LENGTH_SHORT).show();
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e(TAG, "location Error, ErrCode:"
                        + location.getErrorCode() + ", errInfo:"
                        + location.getErrorInfo());
            }


        }
    };

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
        //获取地图引用
        mapView = (MapView) itemView.findViewById(R.id.map_view);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mapView.onCreate(savedInstanceState);
        //给aMap赋值
        aMap = mapView.getMap();
        //信息窗体, 点击回调
        aMap.setOnInfoWindowClickListener(this);

        //开启定位
        startLocation();

        return itemView;
    }

    //定位方法
    private void startLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);


        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
//        设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    // --------------------------------------------------
    // 信息窗体回调监听
    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.hideInfoWindow();//隐藏信息窗体
        Toast.makeText(getContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
    }


    // ---------------------------------------------------
    //在地图中添加其他用户的位置
    private void addOtherUser() {
        //设置搜索条件
        NearbySearch.NearbyQuery query = new NearbySearch.NearbyQuery();

        //设置搜索的中心点
        query.setCenterPoint(MapUtil.getCurrentUserLatLonPoint());
        //设置搜索的坐标体系
        query.setCoordType(NearbySearch.AMAP);
        //设置搜索半径
        query.setRadius(1000);
        //设置查询的时间
        query.setTimeRange(10000);
        //设置查询的方式驾车还是距离
        query.setType(NearbySearchFunctionType.DRIVING_DISTANCE_SEARCH);
        //调用异步查询接口
        NearbySearch.getInstance(getApplicationContext())
                .searchNearbyInfoAsyn(query);
        //添加回调监听
        NearbySearch.getInstance(getApplicationContext()).addNearbyListener(this);
    }

    @Override
    public void onUserInfoCleared(int i) {
    }

    @Override
    public void onNearbyInfoSearched(NearbySearchResult nearbySearchResult, int resultCode) {
        //搜索周边附近用户回调处理
        if(resultCode == 1000){
            if (nearbySearchResult != null
                    && nearbySearchResult.getNearbyInfoList() != null
                    && nearbySearchResult.getNearbyInfoList().size() > 0) {
                NearbyInfo nearbyInfo = nearbySearchResult.getNearbyInfoList().get(0);

                // TODO: 2016/11/2 待完成!!!
                
            } else {
                Log.d(TAG, "onNearbyInfoSearched: 周边搜索结果为空");
            }
        }
        else{
            Log.d(TAG, "onNearbyInfoSearched: 周边搜索出现异常，异常码为：" + resultCode);
        }
    }

    @Override
    public void onNearbyInfoUploaded(int i) {

    }


    // -------------------------------------------------------
    // 生命周期方法
    @Override
    public void onDestroy() {
        //停止定位
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
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
