package com.ibox;

/**
 * Created by lenovo on 2018/4/13.
 */

public class User {
    private  int ID;
    private String telenumber;
    private String  username;
    private String password;
    private String  address;
    private String  IP; //用户登录设备的ip地址

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

    private String  port;//用户接收信息的端口

    public User()
    {

    }
    public User(int ID, String  telenumber, String username, String password, String address) {
       // this.ID = ID;
        this.telenumber = telenumber;
        this.username = username;
        this.password = password;
        this.address = address;
    }

    public int getID() {
        return ID;
    }

    public void setID(int userID) {
        this.ID = userID;
    }

    public String getTelenumber() {
        return telenumber;
    }

    public void setTelenumber(String telenumber) {
        this.telenumber = telenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}


