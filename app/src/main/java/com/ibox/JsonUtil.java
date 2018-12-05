package com.ibox;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lenovo on 2018/4/25.
 */

public class JsonUtil {
    public static Express parseJsonToExpress(String json) throws JSONException
    {
        JSONObject  jsonobject =new JSONObject(json);
        Express express=new Express();
        express.setSendID(jsonobject.getInt("sendID"));
        express.setReceiveID(jsonobject.getInt("receiveID"));
        express.setOrderNumber(jsonobject.getString("ordernumber"));
        express.setBoxID(jsonobject.getInt("boxID"));
        express.setTemperature((float)jsonobject.getDouble("temperature"));
        express.setHumidity(jsonobject.getDouble("humidity"));
        express.setAccelation((float)jsonobject.getDouble("accelation"));
        express.setLat(jsonobject.getDouble("lat"));
        express.setLng(jsonobject.getDouble("lng"));
        express.setThings(jsonobject.getString("things"));
        return express;

    }
    public static JSONObject ExpressToJson(Express express) throws JSONException
    {
        JSONObject jsonobject =new JSONObject();
        jsonobject.put("sendID",express.getSendID());
        jsonobject.put("receiveID",express.getReceiveID());
        jsonobject.put("ordernumber",express.getOrderNumber());
        jsonobject.put("boxID",express.getBoxID());
        jsonobject.put("temperature",express.getTemperature());
        jsonobject.put("humidity",express.getHumidity());
        jsonobject.put("accelation",express.getAccelation());
        jsonobject.put("lat",express.getLat());
        jsonobject.put("lng",express.getLng());
        jsonobject.put("things",express.getThings());

        return jsonobject;
    }
    public  static User parseJsonToUser(String json) throws JSONException
    {
        JSONObject jsonobject=new JSONObject(json);
        User user=new User();
        user.setID(jsonobject.getInt("ID"));
        user.setUsername(jsonobject.getString("username"));
        user.setPassword(jsonobject.getString("password"));
        user.setTelenumber(jsonobject.getString("telenumber"));
        user.setAddress(jsonobject.getString("address"));
        user.setIP(jsonobject.getString("IP"));
        user.setPort(jsonobject.getString("port"));
        return user;
    }
    public static JSONObject  UserToJson(User  user) throws JSONException
    {
        JSONObject  jsonobject =new JSONObject();
        jsonobject.put("ID", user.getID());
        jsonobject.put("username",user.getUsername());
        jsonobject.put("password",user.getPassword());
        jsonobject.put("telenumber",user.getTelenumber());
        jsonobject.put("address",user.getAddress());
        jsonobject.put("IP",user.getIP());
        jsonobject.put("port", user.getPort());

        return jsonobject;
    }
}
