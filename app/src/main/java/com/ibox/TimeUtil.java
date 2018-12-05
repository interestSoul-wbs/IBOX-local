package com.ibox;

/**
 * Created by lenovo on 2018/6/22.
 */

public class TimeUtil {
    public static String getFormatHMS(long time){
        time=time/1000;//总秒数
        //time=time/1000;//总秒数
        int h=(int) (time/3600);//秒
        int s= (int) (time%60);//秒
        int m= (int) ((time-3600*h)/60);//分
        return String.format("%02d:%02d:%02d",h,m,s);
    }
}
