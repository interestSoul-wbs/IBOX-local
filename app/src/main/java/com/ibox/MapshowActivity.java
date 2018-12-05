package com.ibox;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.RouteNode;
import com.baidu.mapapi.search.core.RouteStep;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.ibox.overlayutil.DrivingRouteOverlay;
import com.ibox.overlayutil.WalkingRouteOverlay;

import java.util.List;

public class MapshowActivity extends AppCompatActivity  {

    private MapView mapView;
    private BaiduMap baiduMap;
    public LocationClient mLocationClient;
    private boolean isfirstlocate = true;
    public LocationManager locationManager;
    public String provider;
    public RoutePlanSearch  mSearch;
    public DrivingRouteOverlay  drivingRouteOverlay;
    public DrivingRouteLine   route;
    public Marker marker;
    public InfoWindow mInfoWindow;
    public List<RouteNode>   rn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map_show);
        mLocationClient=new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new Mylocationlistener());
        mapView = (MapView) findViewById(R.id.bmapview);
        baiduMap = mapView.getMap();
        mSearch = RoutePlanSearch.newInstance();

        OnGetRoutePlanResultListener listener=new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult result) {
                if (result == null ) {
                    Toast.makeText(MapshowActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                }
                if(result.error != SearchResult.ERRORNO.NO_ERROR)
                {
                    Toast.makeText(MapshowActivity.this,result.toString(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MapshowActivity.this,MapshowActivity.class);
                    startActivity(intent);
                }
            /*if (result.error ==   SearchResult.ERRORNO) {
                //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                //result.getSuggestAddrInfo()
                Toast.makeText(MapshowActivity.this,"地点有歧义", Toast.LENGTH_SHORT).show();
                return;
            }*/

                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    route = result.getRouteLines().get(0);
                    DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(baiduMap);
                    baiduMap.setOnMarkerClickListener(overlay);
                    overlay.setData(result.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        };

        PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京","北京西站");
        //镇江市
       // PlanNode stNode=PlanNode.withLocation(new LatLng(32.2,119.5));
        PlanNode miNode=PlanNode.withCityNameAndPlaceName("济南","济南站");
        PlanNode stNode = PlanNode.withCityNameAndPlaceName("南京","南京南站");
        //常熟市
       // PlanNode enNode=PlanNode.withLocation(new LatLng(31.65,120.75));
       // PlanNode  miNode=PlanNode.withCityNameAndPlaceName("常州","常州北站");
        //
        LatLng  position=new LatLng(37.45,116.25);
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.track);
        OverlayOptions  overlayOptions=new MarkerOptions().position(position).icon(bitmap);
        marker=(Marker)baiduMap.addOverlay(overlayOptions);
        mSearch.setOnGetRoutePlanResultListener(listener);
        mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
/*        rn=route.getWayPoints();
        for(RouteNode  rns:rn)
        {
           // rns.getLocation();
          //  LatLng  position=new LatLng(rns.getLocation());
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.arrow2);
            OverlayOptions  overlayOptions=new MarkerOptions().position(rns.getLocation()).icon(bitmap);
            marker=(Marker)baiduMap.addOverlay(overlayOptions);
        }*/
       // mSearch.drivingSearch((new DrivingRoutePlanOption()).from(miNode).to(enNode));
          baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener()
         {
             @Override
             public boolean onMarkerClick(Marker marker) {
                 TextView location = new TextView(getApplicationContext());
                 location.setBackgroundResource(R.drawable.popup);
                 location.setPadding(30, 20, 30, 50);
                 //marker.getPosition() ;
                 location.setText("当前位置:德州附近");
                 location.setTextColor(Color.BLACK);
                 final LatLng ll = marker.getPosition();
                 Point p = baiduMap.getProjection().toScreenLocation(ll);
                 p.y -= 47;
                 LatLng llInfo = baiduMap.getProjection().fromScreenLocation(p);
                 // 为弹出的InfoWindow添加点击事件
                // mInfoWindow=new InfoWindow()
                 mInfoWindow = new InfoWindow(location,llInfo, 1);
                // mInfoWindow=new InfoWindow()
                 baiduMap.showInfoWindow(mInfoWindow);
                 return true;
             }
         });
       // baiduMap.setMyLocationEnabled(true);
        //myRoute();
       // requestLocation();
        //BaiduMapNavigation baiduMapNavigation=new BaiduMapNavigation();
        //PlanNode start =PlanNode.withLocation();
        //PlanNode   end=PlanNode.withLocation();

    }

   /* public void myRoute()
    {
        // 天安门坐标
        double mLat1 = 39.915291;//纬度
        double mLon1 = 116.403857;//经度
        // 百度大厦坐标
        double mLat2 = 40.056858;
        double mLon2 = 116.308194;
        //LatLng loc_start = new LatLng(mLat1,mLon1);
        //LatLng loc_end = new LatLng(mLat2, mLon2);
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(new MyRoutePlanListener());
        PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京","北京南站");
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("南京","南京南站");
        mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
    }*/

   /* public class MyRoutePlanListener implements OnGetRoutePlanResultListener
    {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult result) {

        }
        @Override
        public void onGetTransitRouteResult(TransitRouteResult result) {

        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult result) {
            if (result == null ) {
                Toast.makeText(MapshowActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            }
            if(result.error != SearchResult.ERRORNO.NO_ERROR)
            {
                Toast.makeText(MapshowActivity.this,result.toString(), Toast.LENGTH_SHORT).show();
            }
            /*if (result.error ==   SearchResult.ERRORNO) {
                //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                //result.getSuggestAddrInfo()
                Toast.makeText(MapshowActivity.this,"地点有歧义", Toast.LENGTH_SHORT).show();
                return;
            }

            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                 route = result.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(baiduMap);
                baiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
            }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    }*/
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {
        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }
        @Override
        public int getLineColor() {
            //红色的路径
            return Color.RED;
        }

       @Override
       public BitmapDescriptor getStartMarker() {

           return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
       }

       @Override
       public BitmapDescriptor getTerminalMarker() {
           return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
       }
   }

        public void requestLocation()
    {
        initLocation();
        mLocationClient.start();
    }
    public void initLocation()
    {
        LocationClientOption lco=new LocationClientOption();
        lco.setScanSpan(5000);
        //需要详细信息时调用
        lco.setIsNeedAddress(true);
        //强制使用GPS
        lco.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        //LocationClientOption.LocationMode.Device_Sensors
        //LocationClientOption.LocationMode.Battery_Saving  使用网络模式
        //LocationClientOption.LocationMode.Hight_Accuracy
        mLocationClient.setLocOption(lco);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        //onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();


    }
    private  void navigateTo(BDLocation location)
    {
        if(isfirstlocate)
        {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            System.out.println();
            System.out.println(location.getLatitude()+""+location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isfirstlocate=false;
        }
        //显示个人位置图标
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.latitude(location.getLatitude());
        builder.longitude(location.getLongitude());
        MyLocationData data = builder.build();
        baiduMap.setMyLocationData(data);
    }

    public class Mylocationlistener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if(bdLocation.getLocType()==BDLocation.TypeGpsLocation||bdLocation.getLocType()==BDLocation.TypeNetWorkLocation)
            {
                navigateTo(bdLocation);
                //Road();
            }
            //ToNavicate();

            /* StringBuilder sb=new StringBuilder();
            sb.append("纬度：").append(bdLocation.getLatitude()).append("\n");
            sb.append("经度：").append(bdLocation.getLongitude()).append("\n");
            sb.append("省份：").append(bdLocation.getProvince()).append("\n");
            sb.append("市：").append(bdLocation.getCity()).append("\n");
            sb.append("区：").append(bdLocation.getDistrict()).append("\n");
            sb.append("街道：").append(bdLocation.getStreet()).append("\n");
            sb.append("定位方式：");
            if(bdLocation.getLocType()==BDLocation.TypeGpsLocation)
            {
                sb.append("GPS");

            }else
            {
                sb.append("网络方式");
            }
          textView.setText(sb);*/
        }
    }
}
