package com.ibox;

/**
 * Created by lenovo on 2018/4/11.
 */

public class Express {
    private int sendID;
    private int receiveID;
    private String  orderNumber;//订单号
    private int boxID;
    private double  temperature;//温度
    private double humidity;//湿度
    private double  accelation;//加速度
    private  double  lat;//纬度
    private  double  lng;//经度
    private   String things;//物品

    public Express(int sendID, int receiveID, String orderNumber, int boxID, double temperature, double humidity, double accelation, double lat, double lng, String things) {
        this.sendID = sendID;
        this.receiveID = receiveID;
        this.orderNumber = orderNumber;
        this.boxID = boxID;
        this.temperature = temperature;
        this.humidity = humidity;
        this.accelation = accelation;
        this.lat = lat;//纬度
        this.lng = lng;//经度
        this.things = things;
    }
    public Express()
    {

    }
    public Express(String s,int image)
    {
        this.things=s;
        this.boxID=image;
    }

    public int getSendID() {
        return sendID;
    }

    public void setSendID(int sendID) {
        this.sendID = sendID;
    }

    public int getReceiveID() {
        return receiveID;
    }

    public void setReceiveID(int receiveID) {
        this.receiveID = receiveID;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getBoxID() {
        return boxID;
    }

    public void setBoxID(int boxID) {
        this.boxID = boxID;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getAccelation() {
        return accelation;
    }

    public void setAccelation(double accelation) {
        this.accelation = accelation;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getThings() {
        return things;
    }

    public void setThings(String things) {
        this.things = things;
    }
}
