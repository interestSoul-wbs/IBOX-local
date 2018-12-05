package com.ibox;

/**
 * Created by lenovo on 2018/4/13.
 */

public class Box {
    private int id;
    private String type;
    private String  IP;
    private  String port;
    private double  weight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Box(int id, String type, String IP, String port, double weight) {
        this.id = id;

        this.type = type;
        this.IP = IP;
        this.port = port;
        this.weight = weight;
    }
}
